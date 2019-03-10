package escape_game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SaveDataManager {
	private Mainpro mMainpro;

	// フィールドのID
	public static final int FIELD_ID_WESTERN_ROOM = 1;
	public static final int FIELD_ID_DK = 2;
	public static final int FIELD_ID_DK_TOP = 3;
	public static final int FIELD_ID_JSR1 = 4;
	public static final int FIELD_ID_JSR2 = 5;
	public static final int FIELD_ID_WC = 6;
	public static final int FIELD_ID_ENTRANCE = 7;
	public static final int FIELD_ID_DATUIJO = 8;
	public static final int FILED_ID_BATHROOM = 9;

	public SaveDataManager() {
		this(null);
	}

	public SaveDataManager(Mainpro mainpro) {
		this.mMainpro = mainpro;
	}

	// セーブデータ読み込み
	public boolean readSaveData() {
		if(!isSaveEnabled("read-all")) {
			return false;
		} else {
			BufferedReader saveData = null;
			try {
				saveData = new BufferedReader(new FileReader("../savedata/escape_game/savedata.txt"));
				String data = saveData.readLine();
				while (data != null) {
					if (data.substring(0, 2).equals("//")) { //コメント行
					} else if (data.substring(0, 3).equals("END")) { //セーブファイル終端行
						mMainpro.setConfig();
					} else {
						String[] dataArray = data.split(":"); //データラベル:データ
						String dataLabel = dataArray[0]; //データラベル
						String dataContent = dataArray[1]; //データ
						String[] dataLabels = dataLabel.split("\\("); //データラベルを"("で分解[※データラベル(部屋名)となっているものがあるため]※(等をsplitする際はescape記号必須
						String dataLabel_woLP = null; //woLP = without left Paren
						if (dataLabels != null) {
							dataLabel_woLP = dataLabels[0];
						}
						if (dataLabel.equals("playerX")) {
							mMainpro.player_x = Integer.parseInt(dataContent);
						} else if (dataLabel.equals("playerY")) {
							mMainpro.player_y = Integer.parseInt(dataContent);
						} else if (dataLabel.equals("field")) {
							Field field = mMainpro.getField(dataContent);
							if (field == null) { //field取得失敗
								System.out.println("fieldの取得に失敗しました。");
								return false;
							}
							mMainpro.now_field = field;
							mMainpro.screenMode = dataContent;
							mMainpro.now_field.setImages(mMainpro.getCodeBase());
						} else if (dataLabel.equals("item")) {
							String[] items = dataContent.split(",");
							for (int index = 0; index < items.length; index++) {
								if (items[index].equals("nothing")) {
									break;
								}
								mMainpro.bag.add(items[index]);
							}
						} else if (dataLabel_woLP.equals("flag")) {
							String[] fieldNames = dataLabels[1].split("\\)"); //field名)を")"で分解
							String fieldName = fieldNames[0]; //room名
							if (fieldName.equals("Common")) { //共通のフラグ
								String[] flags = dataContent.split(",");
								for (int index = 0; index < flags.length; index++) {
									if (flags[index].equals("nothing")) {
										break;
									}
									mMainpro.setFlagTrue(flags[index]);
								}
							} else { //各フィールド(部屋)毎のフラグ
								Field field = mMainpro.getField(fieldName);
								String[] flags = dataContent.split(",");
								for (int index = 0; index < flags.length; index++) {
									if (flags[index].equals("nothing")) {
										break;
									}
									field.setFlagTrue(flags[index]);
								}
							}
						} else { //設定されていないラベルがファイルに書かれている場合
							System.out.println("設定されていないラベル名がファイルに記述されています");
							return false;
						}
					}
					data = saveData.readLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					saveData.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	// セーブデータ書き込み
	public boolean writeSaveData() {
		if(!isSaveEnabled("write-all")) {
			return false;
		} else {
			// セーブデータの生成
			System.out.println("data save : savedata creating...");

			ArrayList<String> saveDataList = new ArrayList<String>();

			saveDataList.add("//" + getCurrentDate() + "(playerX,playerY以外はtrueのものをカンマ区切りで記述。)");
			saveDataList.add("playerX:" + mMainpro.player_x);
			saveDataList.add("playerY:" + mMainpro.player_y);
			saveDataList.add("field:" + mMainpro.now_field.toString());
			saveDataList.add("item:" + getBagStr());
			saveDataList.add("flag(Common):" + getCommonTrueFlg());
			saveRoomData(saveDataList);
			saveDataList.add("END");

			// 生成したセーブデータを出力
			for (int index = 0; index < saveDataList.size(); index++) {
				System.out.println("[" + (index + 1) + "行目] :" + saveDataList.get(index));
			}
			// セーブファイルに書き込む
			BufferedWriter saveData = null;
			try {
				saveData = new BufferedWriter(new FileWriter("../savedata/escape_game/savedata.txt"));
				System.out.println("data save : saving...");
				for (int saveListIndex = 0; saveListIndex < saveDataList.size(); saveListIndex++) {
					saveData.write(saveDataList.get(saveListIndex));
					saveData.newLine();
				}
				System.out.println("data save : complete!");
			} catch (Exception e) {
				System.out.println("data save : failed(x_x)");
				e.printStackTrace();
			} finally {
				try {
					saveData.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		 }
		 return true;
	 }

	// セーブ可能判定
	private boolean isSaveEnabled(String prefix) {
		if (mMainpro == null) {
			System.out.println("【" + prefix + "】 error : cannot access saveData caused by null");
			return false;
		}
		return true;
	}

	// 現在時刻取得(フォーマット：年月日...ex)20190306)
	public String getCurrentDate() {
		Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
	}

	private String getBagStr() {
		String bagStr = "";
		for (int bagIndex = 0; bagIndex < mMainpro.bag.size(); bagIndex++) {
			bagStr += mMainpro.bag.get(bagIndex);
			if (bagIndex != mMainpro.bag.size() - 1) {
				bagStr += ",";
			}
		}
		if (bagStr.equals("")) {
			bagStr = "nothing";
		}
		return bagStr;
	}

	private String getCommonTrueFlg() {
		if (mMainpro.is_already_checked_bag) {
			return "is_already_checked_bag";
		}
		return "nothing";
	}

	// 全部屋の値=trueのフラグリストをセーブデータ用のリストに設定する
	private void saveRoomData(ArrayList<String> saveDataList) {
		for (int roomIndex = 0; roomIndex < mMainpro.roomStrList.size(); roomIndex++) {
			// 各部屋の値=trueのフラグリストを取得する
			String roomStr = mMainpro.roomStrList.get(roomIndex);
			Field field = mMainpro.getField(roomStr);
			ArrayList<String> flagList = field.getTrueFlags();

			String roomFlgStr = "";
			if (flagList != null) {
				for (int flagIndex = 0; flagIndex < flagList.size(); flagIndex++) {
					roomFlgStr += flagList.get(flagIndex);
					if (flagIndex != flagList.size() - 1) {
						roomFlgStr += ",";
					}
				}
			}
			if (roomFlgStr.equals("")) {
				roomFlgStr = "nothing";
			}
			saveDataList.add("flag(" + roomStr + "):" + roomFlgStr);

		}
	}
}
