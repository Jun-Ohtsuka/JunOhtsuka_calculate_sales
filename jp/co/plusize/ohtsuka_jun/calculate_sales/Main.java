package jp.co.plusize.ohtsuka_jun.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class InvalidException extends Exception{
	InvalidException(String message){
		super(message);
	}//InvalidException
}//class


class OpenFile extends Exception {
	@SuppressWarnings("finally")
	boolean Open(String argsPass, String filePass, HashMap<String, String> nameMap, HashMap<String, String> codeMap, String fileName) {
		boolean a = true;
		try {
			//支店定義ファイルのデータを読み込む
			BufferedReader buffRead = new BufferedReader(new FileReader(new File (argsPass,filePass)));
			String str;
			String[] list;
			int keyCount = 1;
			try{
				while((str = buffRead.readLine()) != null){
					//","で分割してHashMapに追加
					list = str.split(",");
					//ファイルの中身の確認
					if(list.length != 2){
						System.out.println(fileName + "定義ファイルのフォーマットが不正です");
						a = false;
						return a;
					}//if(list.length)~~
					//ファイルのコードフォーマットが適切かどうかの確認
					if(fileName == "支店"){
						Pattern p = Pattern.compile("^\\d{3}$");
						Matcher m = p.matcher(list[0]);
						if(m.find() == false){
							System.out.println(fileName + "定義ファイルのフォーマットが不正です");
							a = false;
							return a;
						}//if(errorCheck)//支店
					} else
					if(fileName == "商品"){
						Pattern p = Pattern.compile("^\\p{Alnum}{8}$");
						Matcher m = p.matcher(list[0]);
						if(m.find() == false){
							System.out.println(fileName + "定義ファイルのフォーマットが不正です");
							a = false;
							return a;
						}
					}//if(z ==)
					nameMap.put(list[0],list[1]);
					codeMap.put(String.valueOf(keyCount), list[0]);
					keyCount++;
				}//while((s = br.readLine()) != null)
			}finally{
				buffRead.close();
			}
		}catch (IOException e){
			System.out.println(fileName + "定義ファイルが存在しません");
			a = false;
		}finally{
			return a;
		}
	}//boolean Open
}//class


class JoinCode{
	HashMap<String, Long> Join(HashMap<String,String> nameMap, HashMap<String, String> codeMap, String fileName){
		HashMap<String, Long> sumName = new HashMap<>();
		for (int i = 0; i < codeMap.size(); i++){
			String key = codeMap.get(String.valueOf(i + 1));
			if(nameMap.containsKey(key)){
				sumName.put(key, 0L);
			}//if(x.containsKey)~~
			if(sumName.size() == nameMap.size()){
				return sumName;
			}//if(y,size() ==)~~
		}//for(int i;~~)
		return sumName;
	}
}


class CheckCode extends Exception{
	boolean Check(HashMap<String, String> codeMap, ArrayList<String> methodListRcd, HashMap<String, Long> sumName,
			HashMap<Integer, String> methodRcdName, int loopNum, String fileName){

		boolean a = true;
		int code = -1;
		if(fileName == "商品"){
			code = 1;
		} else if(fileName == "支店"){
			code = 0;
		}
		String key = methodListRcd.get(code);
		//コードがあるかどうかの判定
		if(codeMap.containsValue (key) == false){
			System.out.println(methodRcdName.get(loopNum + 1) + "の" + fileName +"コードが不正です");
			a = false;
			return a;
		} else{
			//コードが一致しているので合計に計算
			Long sum = Long.valueOf(methodListRcd.get(2)) + sumName.get(key);
			//合計が10桁以下かどうか
			if(String.valueOf(sum).length() > 10){
				System.out.println("合計金額が10桁を超えました");
				a = false;
				return a;
			}//if(String.valueOf(sum))~~
			sumName.put(key,sum);
		}
		return a;
	}//boolean Check
}//class


class SortMap{
	ArrayList<String> Sort(HashMap<String, Long> sumName, HashMap<String,String> nameMap){
		ArrayList<String> z = new ArrayList<>();
		//Map.Entry のリストを作る
		List<Entry<String, Long>> entries = new ArrayList<Entry<String, Long>>(sumName.entrySet());
		//Comparator で Map.Entry の値を比較
		Collections.sort(entries, new Comparator<Entry<String, Long>>() {
			//比較関数
			@Override
			public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
				return o2.getValue().compareTo(o1.getValue());//降順
			}
		});
		//並び替えたエントリーマップを1行ごとに連結してリストに格納
		for (Entry<String, Long> e : entries) {
			String outPut = e.getKey() + "," + nameMap.get(e.getKey()) + "," + String.valueOf(e.getValue());//1行に連結
			z.add(outPut);
		}//for
		return z;
	}
}


class OutputFile extends Exception{
	boolean OutPut(String argsPass, String filePass, ArrayList<String> outPutList){
		boolean a = true;
		try{
			for(int i = 0; i < 2; i++){
				File file = new File(argsPass, filePass);
				if(file.exists()){
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					for(int f = 0; f < outPutList.size(); f++){
						bw.write(outPutList.get(f));
						if(f+1 != outPutList.size()){
							bw.newLine();
						}
					}//for(int f;)~~
					bw.close();
					//System.out.println(filePass + "ファイルの書き込み完了");//デバッグ用
					break;
				}else{
					//ない場合ファイルを作成する
					try{
						file.createNewFile();
						//System.out.println(filePass  + "ファイルを作成しました");//デバッグ用
					}catch(IOException e){
						System.out.println("予期せぬエラーが発生しました");
						a = false;
						return a;
					}//try~~catch
				}//if~~else
			}//for(int i;)~~
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			a = false;
			return a;
		}//try
		return a;
	}//boolean OutPut
}//class


public class Main {
	public static void main(String[] args) {
		//支店データを保存するためのHashMap
		HashMap<String, String> branch = new HashMap<>();
		//支店コードを保存するためのHashMap
		HashMap<String, String> branchCode = new HashMap<>();
		//商品データを保存するためのHashMap
		HashMap<String, String> commodity = new HashMap<>();
		//商品コードを保存するためのHashMap
		HashMap<String, String> commodityCode = new HashMap<>();
		//売上データのファイル名を保存するためのHashMap
		HashMap<Integer, String> rcdName = new HashMap<>();
		//例外処理を判定する変数
		boolean exception = true;

		//コマンドライン引数に渡された値が1個以外の場合
		if(args.length != 1){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}

		//支店定義ファイルの読み込み
		OpenFile openBran = new OpenFile();
		exception = openBran.Open(args[0] , "branch.lst", branch, branchCode, "支店");
		//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
		if(!exception){
			return;
		}

		//商品定義ファイルの読み込み
		OpenFile openCom = new OpenFile();
		exception = openCom.Open(args[0] , "commodity.lst", commodity, commodityCode, "商品");
		//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
		if(!exception){
			return;
		}

		//売上ファイルの名前読み込み
		String path = args[0];
		File dir = new File(path);
		File[] fileList = dir.listFiles();
		String[] files = dir.list();

		int keyCount = 1;
		//.rcdの名前がついているものだけを抜き出し
		for (int i = 0 ; i < fileList.length ; i++){
			//抜き出したものの名前が一致しているかどうか判定
			String checkName =  "^\\d{8}.rcd$";
			Pattern p = Pattern.compile(checkName);
			String name = files[i];
			Matcher m = p.matcher(name);
			if(m.find()){//一致していたら行う処理
				//抜き出したものがファイルかフォルダーかを判定
				if (fileList[i].isFile()){//ファイルだったときだけrcdNameにぶちこむ
					rcdName.put(keyCount,files[i]);
					keyCount++;
				}//if(fileList[i])
			}//if(m.find())~~
		}//for(int i;)~~

		//rcdファイルが0だった場合のエラー処理
		if(rcdName.size() ==0){
			System.out.println("売上ファイル名が連番になっていません");
			return;
		}

		//連番確認処理
		String[] rcdSpl = rcdName.get(rcdName.size()).split("\\.");
		int max = Integer.valueOf(rcdSpl[0]);
		if(rcdName.size() != max){
			System.out.println("売上ファイル名が連番になっていません");
			return;
		}

		//統計用データ格納作成
		//支店データ統計用HashMap
		HashMap<String, Long> sumBran = new HashMap<>();
		//商品データ統計用HashMap
		HashMap<String, Long> sumCom = new HashMap<>();

		//統計用HashMapに支店コードと合計を結びつける処理
		JoinCode mapCodeBran = new JoinCode();
		sumBran = mapCodeBran.Join(branch, branchCode, "支店");
		//統計用HashMapに商品コードと合計を結びつける処理
		JoinCode mapCodeCom = new JoinCode();
		sumCom = mapCodeCom.Join(commodity, commodityCode, "商品");

		//売上ファイルのデータ読み込み
		try {
			for(int i = 0; i < rcdName.size(); i++){
				BufferedReader rcdBuffRead = new BufferedReader(new FileReader(new File (args[0], rcdName.get(i + 1))));
				String strRcd;
				ArrayList<String> listRcd = new ArrayList<String>();

				//売上ファイルの中身を呼び出し一時格納する処理
				try{
					while((strRcd = rcdBuffRead.readLine()) != null){
						listRcd.add(strRcd);
					}//while((strRcd = brRcd.readLine()) != null)
				} catch(IOException e){
					System.out.println(e);
					return;
				} finally{
					rcdBuffRead.close();
				}//try~~

				//売上ファイルのフォーマット(行数)が適正かどうかの判定
				if(listRcd.size() != 3){
					System.out.println(rcdName.get(i + 1) + "のフォーマットが不正です");
					return;
				}//if(strRcd.len)~~

				//支店コードと一致しているかの判定
				CheckCode codeBran = new CheckCode();
				exception = codeBran.Check(branchCode, listRcd, sumBran, rcdName, i, "支店");
				//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
				if(!exception){
					return;
				}
				//商品コードと一致しているかの判定
				CheckCode codeCom = new CheckCode();
				exception = codeCom.Check(commodityCode, listRcd, sumCom, rcdName, i, "商品");
				//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
				if(!exception){
					return;
				}

			}//for(int i;~~)
		} catch(IOException e){
			System.out.println(e);
			return;
		}//try~catch

		//売上データ出力
		//支店合計出力用にマップをソートする
		ArrayList<String> outBranList = new ArrayList<>();
		SortMap sortBran = new SortMap();
		outBranList = sortBran.Sort(sumBran, branch);

		//商品合計出力用にマップをソートする
		ArrayList<String> outComList = new ArrayList<>();
		SortMap sortCom = new SortMap();
		outComList = sortCom.Sort(sumCom, commodity);

		//ファイル操作
		//branch.out
		OutputFile outBran = new OutputFile();
		exception = outBran.OutPut(args[0], "branch.out", outBranList);
		//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
		if(!exception){
			return;
		}
		//commodity.out
		OutputFile outCom = new OutputFile();
		exception = outCom.OutPut(args[0], "commodity.out", outComList);
		//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
		if(!exception){
			return;
		}

	}//void main
}//class Main