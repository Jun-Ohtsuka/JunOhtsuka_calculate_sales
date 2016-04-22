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


class OpenFile extends Exception{
	void Open(String x, HashMap<String, String> y, String z){
		try {
			//支店定義ファイルのデータを読み込む
			//System.out.println(args[0]);//デバッグ用
			File file = new File (x);
			//System.out.println(file);//デバッグ用
			FileReader fileRead = new FileReader(file);
			//System.out.println(fileRead);//デバッグ用
			BufferedReader buffRead = new BufferedReader(fileRead);
			//System.out.println(buffRead);//デバッグ用
			String str;
			String[] list;
			while((str = buffRead.readLine()) != null){
				//","で分割してHashMapに追加
				list = str.split(",");
				//System.out.println(Arrays.asList(list));//デバッグ用
				try {
					//ファイルの中身の確認
					if(list.length != 2){
						throw new InvalidException(z + "定義ファイルのフォーマットが不正です");
					}//if(list.length)~~
					//ファイルのコードフォーマットが適切かどうかの確認
					if(z == "支店"){
						if(list[0].length() != 3){
							throw new InvalidException(z + "定義ファイルのフォーマットが不正です");
						}//if(errorCheck)//支店
					} else
					if(z == "商品"){
						if(list[0].length() != 8){
							throw new InvalidException(z + "定義ファイルのフォーマットが不正です");
						}
					}//if(z ==)
					y.put(list[0],list[1]);
					//System.out.println(y);
				} catch(InvalidException e){
					System.out.println(e);
					buffRead.close();
					fileRead.close();
					return;
				}//try~~catch
			}//while((s = br.readLine()) != null)
			buffRead.close();
			fileRead.close();
		} catch(IOException e){
				System.out.println(z + "定義ファイルが存在しません");
				return;
		}//try~catch
	}//void
}//class


class JoinCode{
	void Join(HashMap<String,String> x, HashMap<String, Integer> y,String z){
		for (int i = 0; i < 99999; i++){
			String key = String.valueOf(i + 1);
			if(z == "商品"){
				while (key.length() < 5){
					key ="0" + key;
				}//while(num.length)
				key ="SFT" + key;
			}else if(z == "支店"){
				while (key.length() < 3){
					key ="0" + key;
				}//while(num.length)
			}//if(z ==)
			if(x.containsKey(key)){
				y.put(key, 0);
			}//if(x.containsKey)~~
			if(y.size() == x.size()){
				return;
			}//if(y,size() ==)~~
		}//for(int i;~~)
	}
}


class CheckCode extends Exception{
	void Check(HashMap<String, String> x, ArrayList<String> y, HashMap<String, Integer> z, HashMap<String, String> l, int m, String n){
		try {
			int counter = 0;
			for (int j = 0; j <= 99999; j++){
				String key = String.valueOf(j + 1);
				int code = -1;
				if(n == "商品"){
					while (key.length() < 5){
						key ="0" + key;
					}//while(key.len)
					key = "SFT" + key;
					code = 1;
				} else if(n == "支店"){
					while (key.length() < 3){
						//System.out.println(f + 1);//デバッグ用
						//System.out.println(x.keySet());
						key ="0" + key;
					}//while(key.len)
					code = 0;
				}//if(n == )~~
				if(j == 99999 && counter <= 0){
					//一つもコードが一緒じゃなかったとき
					throw new InvalidException(l.get(String.valueOf(m + 1)) + "の" + n +"コードが不正です");
				}else if(y.get(code).equals(key)){
					//一致しているので合計に計算
					int getSum = z.get(key);
					int getVal = Integer.parseInt(y.get(2));
					int sum = getSum + getVal;
					z.put(key,sum);
					counter++;
					//合計が10桁以下かどうか
					String strSum = String.valueOf(z.get(key));
					if(strSum.length() > 10){
						throw new InvalidException("合計金額が10桁を超えました");
					}//if(sum)~~
				}//if(j == 99999)else if(y.get(code).equals(key))
				if(z.size() == x.size()){
					return;
				}//if(z.size() ==)~~
			}//for(int j;)~~
		} catch (InvalidException e) {
			System.out.println(e);
			return;
		}//try~~catch
	}//void
}//class


class SortMap{
	void Sort(HashMap<String, Integer> x, HashMap<String,String> y, ArrayList<String> z){
		//Map.Entry のリストを作る
		List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(x.entrySet());
		//Comparator で Map.Entry の値を比較
		Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
			//比較関数
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());//降順
			}
		});
		//outList.add(entries.get(0));
		//並び替えたエントリーマップを1行ごとに連結してリストに格納
		for (Entry<String, Integer> e : entries) {
			//System.out.println(e.getKey() + " = " + e.getValue());//デバッグ用
			String outKey = e.getKey();
			String outName = y.get(outKey);
			String outVal = String.valueOf(e.getValue());
			String out = outKey + "," + outName + "," + outVal;//1行に連結
			z.add(out);
		}//for
	}
}


class OutputFile extends Exception{
	void OutPut(String x, String y, ArrayList<String> z){
		try{
			for(int i = 0; i < 2; i++){
				File file = new File(x + y);
				if(file.exists()){
					FileWriter fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);
					for(int f = 0; f < z.size(); f++){
						bw.write(z.get(f) + "\r\n");
					}//for(int f;)~~
					bw.close();
					System.out.println(y + "ファイルの書き込み完了");
					break;
				}else{
					//ない場合ファイルを作成する
					try{
						file.createNewFile();
						System.out.println(y + "ファイルを作成しました");
					}catch(IOException e){
						System.out.println(e);
						return;
					}//try~~catch
				}//if~~else
			}//for(int i;)~~
		}catch(IOException e){
			System.out.println(e);
			return;
		}//try~~Catch
	}//void
}//class


public class Main {
	public static void main(String[] args) {

		//店舗データを保存するためのHashMap
		HashMap<String, String> branch = new HashMap<String, String>();
		//商品データを保存するためのHashMap
		HashMap<String, String> commodity = new HashMap<String, String>();
		//売上データのファイル名を保存するためのHashMap
		HashMap<String, String> rcdName = new HashMap<String, String>();

		//支店定義ファイルの読み込み
		OpenFile openBran = new OpenFile();
		openBran.Open(args[0] + "\\branch.lst", branch, "支店");
		//System.out.println(branch);//デバッグ用
		//商品定義ファイルの読み込み
		OpenFile openCom = new OpenFile();
		openCom.Open(args[0] + "\\commodity.lst", commodity, "商品");

		//売上ファイルの名前読み込み
		String path = args[0];
		File dir = new File(path);
		String[] files = dir.list();

		//連番かどうかの確認
		try{
			//rcdファイル名の確認
			int f = 1;
			for (int i = 0; i < files.length; i++){
				String fileRcd = files[i];
				//rcdファイルだけを抜き出し処理する
				if(fileRcd.contains("rcd")){
					String key = String.valueOf(f);
					//ファイル名が8桁+.rcdじゃないファイルをはじく処理
					if(files[i].length() == 12){
						rcdName.put(key,files[i]);
						f++;
					}//if(file[i].length)~~
				}//if(fileRcd.contains)~~
			}//for(int i = 0; ~~)
			//System.out.println(rcdName);//デバッグ用
			int rcdcount = rcdName.size();

			//rcdファイルが0だった場合のエラー処理
			if(rcdName.size() ==0){
				throw new InvalidException("予期せぬエラーが発生しました");
			}//if(rudName.size == 0)~~

			//連番確認処理
			for(int i = 1; i <= rcdcount; i++){
				String checkName = String.valueOf(i) + ".rcd" + "$";
				Pattern p = Pattern.compile(checkName);
				String name = rcdName.get(String.valueOf(i));
				Matcher m = p.matcher(name);
				if(m.find() == false){
					throw new InvalidException("売上ファイル名が連番ではありません");
				//} else{System.out.println("マッチ");//デバッグ用
				}//if
			}//for(i = 1; ~~)
		} catch(InvalidException e){
			System.out.println(e);
			return;
		}//try~~catch

		//統計用データ格納作成
		//支店データ統計用HashMap
		HashMap<String, Integer> sumBran = new HashMap<String, Integer>();
		//商品データ統計用HashMap
		HashMap<String, Integer> sumCom = new HashMap<String, Integer>();

		//統計用HashMapに支店コードと合計を結びつける処理
		JoinCode mapCodeBran = new JoinCode();
		mapCodeBran.Join(branch, sumBran,"支店");
		//System.out.println(sumBran);//デバッグ用
		//統計用HashMapに商品コードと合計を結びつける処理
		JoinCode mapCodeCom = new JoinCode();
		mapCodeCom.Join(commodity,sumCom,"商品");
		//System.out.println(sumCom);//デバッグ用

		//売上ファイルのデータ読み込み
		try {
			for(int i = 0; i < rcdName.size(); i++){
				File rcdFile = new File (args[0] + "\\" + rcdName.get(String.valueOf(i + 1)));
				FileReader rcdFileRead = new FileReader(rcdFile);
				BufferedReader rcdBuffRead = new BufferedReader(rcdFileRead);
				String strRcd;
				ArrayList<String> listRcd = new ArrayList<String>();

				//売上ファイルの中身を呼び出し一時格納する処理
				try{
					while((strRcd = rcdBuffRead.readLine()) != null){
						//System.out.println(strRcd);//デバッグ用
						listRcd.add(strRcd);
						//System.out.println("ListRcdのデバッグ");
						//System.out.println(listRcd);//デバッグ用
					}//while((strRcd = brRcd.readLine()) != null)
				} catch(IOException e){
					System.out.println(e);
					return;
				} finally{
					rcdBuffRead.close();
					rcdFileRead.close();
				}//try~~
				//System.out.println(listRcd);//デバッグ用

				//売上ファイルのフォーマット(行数)が適正かどうかの判定
				if(listRcd.size() != 3){
					throw new InvalidException(rcdName.get(String.valueOf(i + 1)) + "のフォーマットが不正です。");
				}//if(strRcd.len)~~

				//支店コードと一致しているかの判定
				CheckCode codeBran = new CheckCode();
				codeBran.Check(branch, listRcd, sumBran, rcdName, i, "支店");
				//商品コードと一致しているかの判定
				CheckCode codeCom = new CheckCode();
				codeCom.Check(commodity, listRcd, sumCom, rcdName, i, "商品");

			}//for(int i;~~)
		} catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		} catch(InvalidException e){
			System.out.println(e);
			return;
		}//try~catch

		//売上データ出力

		//支店合計出力用にマップをソートする
		ArrayList<String> outBranList = new ArrayList<String>();
		SortMap sortBran = new SortMap();
		sortBran.Sort(sumBran, branch, outBranList);
		//System.out.println(outBranList);//デバッグ用

		//商品合計出力用にマップをソートする
		ArrayList<String> outComList = new ArrayList<String>();
		SortMap sortCom = new SortMap();
		sortCom.Sort(sumCom, commodity, outComList);

		//ファイル操作
		//branch.out
		OutputFile outBran = new OutputFile();
		outBran.OutPut(args[0], "\\branch.out", outBranList);
		//commodity.out
		OutputFile outCom = new OutputFile();
		outCom.OutPut(args[0], "\\commodity.out", outComList);

	}//void main
}//class MAIN