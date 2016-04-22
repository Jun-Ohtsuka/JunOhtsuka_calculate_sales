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

class OpenFile extends IOException{
	void Open(String x, HashMap<String, String> y, int z){
		String name = "";
		try {
			//支店定義ファイルのデータを読み込む
			//System.out.println(args[0]);
			File file = new File (x);
			//System.out.println(file);
			FileReader fileRead = new FileReader(file);
			//System.out.println(fileRead);
			BufferedReader buffRead = new BufferedReader(fileRead);
			//System.out.println(buffRead);
			String str;
			String[] list;
			if(z == 0){
				name = "支店";
			}else if(z == 1){
				name = "商品";
			}//if(z == )
			while((str = buffRead.readLine()) != null){
				//","で分割してHashMapに追加
				list = str.split(",");
				//System.out.println(y.length);00
				try {
					if(list.length != 2){
						throw new InvalidException(name + "定義ファイルのフォーマットが不正です");
					}//if
					String errorCheck = list[0];
					if(z == 0){
						if(errorCheck.length() != 3){
							throw new InvalidException(name + "定義ファイルのフォーマットが不正です");
						}//if(errorCheck)//支店
					} else
					if(z == 1){
						if(errorCheck.length() != 8){
							throw new InvalidException(name + "定義ファイルのフォーマットが不正です");
						}
					}//if(z ==)
					for(int i = 0; i < list.length; i++){
						y.put(list[0],list[1]);
						//System.out.println(y);
					}//for
				} catch(InvalidException e){
					System.out.println(e);
					buffRead.close();
					fileRead.close();
					System.exit(1);
				}//try~~catch
			}//while((s = br.readLine()) != null)
			buffRead.close();
			fileRead.close();
		} catch(IOException e){
				System.out.println(name + "定義ファイルが存在しません");
				System.exit(1);
		}//try~catch
	}//void
}//class


class OutputFile extends IOException{
	void OutPut(String x , ArrayList<String> y){
		try{
			for(int i = 0; i < 2; i++){
				File file = new File(x);
				if(file.exists()){
					//System.out.println("ファイルはある");
					FileWriter fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);
					for(int f = 0; f < y.size(); f++){
						bw.write(y.get(f) + "\r\n");
						//System.out.println("書き込み");
					}//for(int f;)~~
					bw.close();
					System.out.println(x + "ファイルの書き込み完了");
					break;
				}else{
					//System.out.println("ファイルはない");
					//ない場合ファイルを作成する
					try{
						file.createNewFile();
						System.out.println(x + "ファイルを作成しました");
					}catch(IOException e){
						System.out.println(e);
						System.exit(1);
					}//try~~catch
				}//if~~else
			}//for(int i;)~~
		}catch(IOException e){
			System.out.println(e);
			System.exit(1);
		}//try~~Catch
	}//void
}//class


class CheckCode extends IOException{
	void Check(HashMap<String, String> x, ArrayList<String> y, HashMap<String, Integer> z, HashMap<String, String> l, int m, int n){
		try {
			int counter = 0;
			for (int f = 0; f <= x.size(); f++){
				String key = String.valueOf(f + 1);
				String codeName = "";
				if(n == 1){
					while (key.length() < 5){
						key ="0" + key;
					}//while(key.len)
					key = "SFT" + key;
					codeName = "商品";
				} else if(n == 0){
					while (key.length() < 3){
						key ="0" + key;
					}//while(key.len)
					codeName = "支店";
				}//if(n == )~~
				String checkCode = y.get(n);
				if(f == x.size() && counter <= 0){
					throw new InvalidException(l.get(String.valueOf(m + 1)) + "の" + codeName +"コードが不正です");
				}else if(checkCode.equals(key)){
					//一致しているので合計に計算
					int getSum = z.get(key);
					int getVal = Integer.parseInt(y.get(2));
					int sum = getSum + getVal;
					z.put(key,sum);
					//System.out.println(sumCom);
					counter++;
					//合計が10桁以下かどうか
					String strSum = String.valueOf(z.get(key));
					if(strSum.length() > 10){
						throw new InvalidException("合計金額が10桁を超えました");
					}//if(sum)~~
				}//if(f <= commodity)else if(checkCode.equals(comKey))
			}//for(int f;)~~
		} catch (InvalidException e) {
			System.out.println(e);
			System.exit(1);
		}//try~~catch
	}//void
}//class


class JoinCode{
	HashMap<String,Integer> Join(HashMap<String,String> x, HashMap<String, Integer> y,int z){
		for (int i = 0; i < x.size(); i++){
			String key = String.valueOf(i + 1);
			if(z == 1){
				while (key.length() < 5){
					key ="0" + key;
				}//while(num.length)
				key ="SFT" + key;
			}else if(z == 0){
				while (key.length() < 3){
					key ="0" + key;
				}//while(num.length)
			}//if(z ==)
			y.put(key, 0);
		}//for(int i;~~)
		return y;
	}
}


class SortMap{
	ArrayList<String> Sort(HashMap<String, Integer> x, HashMap<String,String> y, ArrayList<String> z){
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
			//System.out.println(e.getKey() + " = " + e.getValue());
			String outKey = e.getKey();
			String outName = y.get(outKey);
			String outVal = String.valueOf(e.getValue());
			String out = outKey + "," + outName + "," + outVal;//1行に連結
			z.add(out);
		}//for
		return z;
	}
}

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
		openBran.Open(args[0] + "\\branch.lst", branch, 0);
		//商品定義ファイルの読み込み
		OpenFile openCom = new OpenFile();
		openCom.Open(args[0] + "\\commodity.lst", commodity, 1);

		//売上ファイルの名前読み込み
		String path = args[0];
		File dir = new File(path);
		String[] files = dir.list();
		try{
			//売り上げファイルの拡張子チェック
			int count = 0;
			for(int i = 0; i < 2; i++){
				for (int f = 0; f < files.length; f++){
					String fileRcd = files[f];
					if(fileRcd.contains("rcd") && count == 0){
						count++;
					}//if(fileRcd~~)
				}//for(int f;)
				if(count <= 0){
					throw new InvalidException("拡張子が違います");
				}//if(count <= 0)
			}//for
		} catch(InvalidException e){
			System.out.println(e);
			return;
		}//try~~catch

		//連番かどうかの確認
		try{
			//rcdファイル名の数の確認
			for (int i = 0; i < files.length; i++){
				String fileRcd = files[i];
				if(fileRcd.contains("rcd")){
					String key = String.valueOf(i + 1);
					if(files[i].length() != 12){
						throw new InvalidException("予期せぬエラーが発生しました");
					}
					rcdName.put(key,files[i]);
				}
			}//for(int i = 0; ~~)
			int rcdcount = rcdName.size();

			//rcdファイルが2以上だったら行う処理
			if(rcdcount >= 2){
				for(int i = 1; i <= rcdcount; i++){
					String checkName = String.valueOf(i) + ".rcd" + "$";
					Pattern p = Pattern.compile(checkName);
					String name = rcdName.get(String.valueOf(i));
					Matcher m = p.matcher(name);
					if(m.find() == false){
						throw new InvalidException("売上ファイル名が連番ではありません");
					//} else{System.out.println("マッチ");
					}//if
				}//for(i = 1; ~~)
			}//if
		} catch(InvalidException e){
			System.out.println(e);
			System.exit(1);
		}//try~~catch

		//統計用データ格納作成
		//支店データ統計用HashMap
		HashMap<String, Integer> sumBran = new HashMap<String, Integer>();
		//商品データ統計用HashMap
		HashMap<String, Integer> sumCom = new HashMap<String, Integer>();

		//支店コードと合計を結びつけるHashMap
		JoinCode mapCodeBran = new JoinCode();
		mapCodeBran.Join(branch, sumBran,0);
		//商品コードと合計を結びつけるHashMap
		JoinCode mapCodeCom = new JoinCode();
		mapCodeCom.Join(commodity,sumCom,1);

		//売上ファイルのデータ読み込み
		try {
			for(int i = 0; i < rcdName.size(); i++){
				File rcdFile = new File (args[0] + "\\" + rcdName.get(String.valueOf(i + 1)));
				FileReader rcdFileRead = new FileReader(rcdFile);
				BufferedReader rcdBuffRead = new BufferedReader(rcdFileRead);
				String strRcd;
				ArrayList<String> listRcd = new ArrayList<String>();

				//集計本体
				try{
					while((strRcd = rcdBuffRead.readLine()) != null){
						//System.out.println(strRcd);
						listRcd.add(strRcd);
						//System.out.println(mRcd);
					}//while((strRcd = brRcd.readLine()) != null)

					//売上ファイルの行数が適正かどうかの判定
					if(listRcd.size() > 3 || listRcd.size() < 3){
						String name = rcdName.get(String.valueOf(i + 1)) + "のフォーマットが不正です。";
						throw new InvalidException(name);
					}//if(strRcd.len)~~

					//支店コードと一致しているかの判定
					CheckCode codeBran = new CheckCode();
					codeBran.Check(branch, listRcd, sumBran, rcdName, i, 0);
					//商品コードと一致しているかの判定
					CheckCode codeCom = new CheckCode();
					codeCom.Check(commodity, listRcd, sumCom, rcdName, i, 1);

				} catch(InvalidException e){
					System.out.println(e);
					System.exit(1);
				} finally{
					rcdBuffRead.close();
					rcdFileRead.close();
				}//try~~
			}//for(int i;~~)
		} catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			System.exit(1);
		}//try~catch


		//売上データ出力
		//商品合計出力用にマップをソートする
		ArrayList<String> outComList = new ArrayList<String>();
		SortMap sortCom = new SortMap();
		sortCom.Sort(sumCom, commodity, outComList);
		//支店合計出力用にマップをソートする
		ArrayList<String> outBranList = new ArrayList<String>();
		SortMap sortBran = new SortMap();
		sortBran.Sort(sumBran, branch, outBranList);


		//ファイル操作
		//commodity.out
		OutputFile outCom = new OutputFile();
		outCom.OutPut(args[0] + "\\commodity.out", outComList);
		//branch.out
		OutputFile outBran = new OutputFile();
		outBran.OutPut(args[0] + "\\branch.out", outBranList);

	}//void main
}//class MAIN
