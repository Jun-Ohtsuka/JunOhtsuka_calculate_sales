package jp.co.plusize.ohtsuka_jun.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InvalidException extends Exception{
	InvalidException(String message){
		super(message);
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
		try {
			//支店定義ファイルのデータを読み込む
			//System.out.println(args[0]);
			File fileBranch = new File (args[0] + "\\branch.lst");
			//System.out.println(fileBranch);
			FileReader frBranch = new FileReader(fileBranch);
			//System.out.println(frBranch);
			BufferedReader brBranch = new BufferedReader(frBranch);
			//System.out.println(brBranch);
			String strBranch;
			String[] bran;
			while((strBranch = brBranch.readLine()) != null){
				//","で分割してHashMapに追加
				bran = strBranch.split(",");
				//System.out.println(bran.length);
				try {
					if(bran.length < 2 || bran.length > 2){
						throw new InvalidException("支店定義ファイルのフォーマットが不正です");
					}
					for(int i = 0; i < bran.length; i++){
						branch.put(bran[0],bran[1]);
						//System.out.println(branch);
					}//for
				} catch(InvalidException e){
					System.out.println(e);
					brBranch.close();
					frBranch.close();
					return;
				}//try~~catch
			}//while((s = br.readLine()) != null)
			brBranch.close();
			frBranch.close();
		} catch(IOException e){
			System.out.println("支店定義ファイルが存在しません");
			return;
		}//try~catch


		//商品定義ファイルの読み込み
		try {
			//商品定義ファイルのデータを読み込む
			File fileCom = new File (args[0] + "\\commodity.lst");
			FileReader frCom = new FileReader(fileCom);
			BufferedReader brCom = new BufferedReader(frCom);
			String strCom;
			String[] com;
			while((strCom = brCom.readLine()) != null){
				//","で分割してHashMapに追加
				com = strCom.split(",");
				try {
					if(com.length < 2 || com.length > 2){
						throw new InvalidException("商品定義ファイルのフォーマットが不正です");
					}
					for(int i = 0; i < com.length; i++){
						commodity.put(com[0],com[1]);
					}//for(int i;)
				} catch(InvalidException e){
					System.out.println(e);
					brCom.close();
					frCom.close();
					return;
				}//try~~catch
			}//while((strCom = brCom.readLine()) != null)
			brCom.close();
			frCom.close();

		} catch(IOException e){
			System.out.println("商品定義ファイルが存在しません");
			return;
		}//try~catch

		//集計を行う
		String path = args[0];
		File dir = new File(path);
		String[] files = dir.list();
		try{
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
			//rcdファイルの数の確認
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
			return;
		}//try~~catch

		//統計用データ格納作成
		//支店データ統計用HashMap
		HashMap<String, Integer> sumBran = new HashMap<String, Integer>();
		//商品データ統計用HashMap
		HashMap<String, Integer> sumCom = new HashMap<String, Integer>();

		//支店コードと合計を結びつける処理
		for (int i = 1; i <= branch.size(); i++){
			String key = String.valueOf(i);
			while (key.length() < 3){
				key ="0" + key;
			}//while(key.length)
			sumBran.put(key, 0);
		}//for(int i;~~)

		//商品コードと合計を結びつけるHashMap作成
		for (int i = 0; i < commodity.size(); i++){
			String num = String.valueOf(i + 1);
			String key = "SFT";
			while (num.length() < 5){
				num ="0" + num;
			}//while(key.length)
			key = key + num;
			sumCom.put(key, 0);
		}//for(int i;~~)

		//売上データの読み込み
		try {
			for(int i = 0; i < rcdName.size(); i++){
				File fileRcd = new File (args[0] + "\\" + rcdName.get(String.valueOf(i + 1)));
				FileReader frRcd = new FileReader(fileRcd);
				BufferedReader brRcd = new BufferedReader(frRcd);
				String strRcd;
				ArrayList<String> mRcd = new ArrayList<String>();

				//集計本体
				try{
					while((strRcd = brRcd.readLine()) != null){
						//System.out.println(strRcd);
						mRcd.add(strRcd);
						//System.out.println(mRcd);
					}//while((strRcd = brRcd.readLine()) != null)

					//売上ファイルの行数が適正かどうかの判定
					if(mRcd.size() > 3 || mRcd.size() < 3){
						String name = rcdName.get(String.valueOf(i + 1)) + "のフォーマットが不正です。";
						throw new InvalidException(name);
					}//if(strRcd.len)~~

					//支店コードと一致しているかの判定
					int counter = 0;
					for (int f = 0; f <= branch.size(); f++){
						String branKey = String.valueOf(f + 1);
						while (branKey.length() < 3){
							branKey ="0" + branKey;
						}//while(key.len)
						String checkCode = mRcd.get(0);
						if(f == branch.size() && counter <= 0){
							throw new InvalidException(rcdName.get(String.valueOf(i + 1)) + "の支店コードが不正です");
						}else if(checkCode.equals(branKey)){
							//一致しているので合計に計算
							int getBransum = sumBran.get(branKey);
							int getBranval = Integer.parseInt(mRcd.get(2));
							int sum = getBransum + getBranval;
							sumBran.put(branKey,sum);
							System.out.println(sumBran);
							counter++;

							//合計が10桁以下かどうか
							String strSum = String.valueOf(sumBran.get(branKey));
							if(strSum.length() > 9){
								throw new InvalidException("合計金額が10桁を超えました");
							}//if(sum)~~
						}//if(f <= branch)else if(checkCode.equals(key))
					}//for(int f;)~~

					//商品コードと一致しているかの判定
					int comcounter = 0;
					for (int f = 0; f <= commodity.size(); f++){
						String comKey = String.valueOf(f + 1);
						while (comKey.length() < 5){
							comKey ="0" + comKey;
						}//while(key.len)
						comKey = "SFT" + comKey;
						String checkCode = mRcd.get(1);
						if(f == branch.size() && comcounter <= 0){
							throw new InvalidException(rcdName.get(String.valueOf(i + 1)) + "の商品コードが不正です");
						}else if(checkCode.equals(comKey)){
							//一致しているので合計に計算
							int getComsum = sumCom.get(comKey);
							int getComval = Integer.parseInt(mRcd.get(2));
							int sum = getComsum + getComval;

							sumCom.put(comKey,sum);
							System.out.println(sumCom);
							comcounter++;

							//合計が10桁以下かどうか
							String strSum = String.valueOf(sumCom.get(comKey));
							if(strSum.length() > 10){
								throw new InvalidException("合計金額が10桁を超えました");
							}//if(sum)~~
						}//if(f <= commodity)else if(checkCode.equals(comKey))
					}//for(int f;)~~
				} catch(InvalidException e){
					System.out.println(e);
					return;
				} finally{
					brRcd.close();
					frRcd.close();
				}//try~~
			}//for(int i;~~)
		} catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}//try~catch

		/*
		 *
		 * 売上データ出力
		 *
		 * 支店別・商品別
		 * ハッシュマップの降順ソート方法
		 *
		 * ファイルに出力
		 *
		 *
		 *
		 *
		 *
		 */



		//売上データ出力


	}//void main
}//class MAIN
