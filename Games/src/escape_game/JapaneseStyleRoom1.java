package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;

//和室1(下側の方の和室)
public class JapaneseStyleRoom1 extends Field {
	//部屋
	Image image_room;
	//ドア
	Image image_dkDoor;
	Image image_higherClosetDoor;
	Image image_lowerClosetDoor;
	Image image_jSRoom2Door;
	//ヒント
	Image image_batu_hint;
	Image image_maru_hint;

	//ドアの開閉フラグ
	private boolean isHClosetDoorOpened = false;
	private boolean isLClosetDoorOpened = false;
	private boolean isJSRoom2DoorOpened = true;
	//窓確認フラグ
	private boolean isCheckedWindow = false;
	//押入れ確認フラグ
	private boolean isCheckedHigherCloset = false;
	private boolean isCheckedLowerCloset = false;
	//掛け軸確認フラグ
	private boolean isCheckedKakejiku = false;
	//JSR2に移動済みか
	private boolean isAlreadyMoveToJSR2 = false;

	//もの
	private static final String HIGHER_CLOSET = "higher_closet";
	private static final String LOWER_CLOSET = "lower_closet";
	private static final String WINDOW = "window";
	private static final String WALL = "wall";
	private static final String SHELF = "shelf";
	private static final String DOOR_TO_DK = "door_to_dk";
	private static final String DOOR_TO_JSR2 = "door_to_jsr2";
	private static final String INSIDE = "inside";
	private static final String KAKEJIKU = "kakejiku";

	Mainpro mainpro;

	public JapaneseStyleRoom1(Mainpro mainpro) {
		this.mainpro = mainpro;
	}

	@Override
	public void paint(Mainpro mainpro) {
		super.paint(mainpro);
		if (isCheckedKakejiku) {
			mainpro.buffer.drawImage(image_maru_hint, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100,
					mainpro);
			if (mainpro.message.get(0).equals("nothing")) {
				isCheckedKakejiku = false;
			}
		} else if (isCheckedHigherCloset) {
			mainpro.buffer.drawImage(image_batu_hint, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100,
					mainpro);
			if (mainpro.message.get(0).equals("nothing")) {
				isCheckedHigherCloset = false;
			}
		}
	}

	@Override
	boolean is_character_in(int cx, int cy) {
		String here = here(cx, cy);
		if (here.equals(INSIDE) || here.equals(DOOR_TO_DK)) {
			return true;
		} else if (here.equals(DOOR_TO_JSR2) && isJSRoom2DoorOpened) {
			return true;
		}
		return false;
	}

	@Override
	String here(int cx, int cy) {
		if (cx <= 40) {
			if (cy <= 150) {
				return HIGHER_CLOSET;
			} else if (180 <= cy) {
				return LOWER_CLOSET;
			}
			return WALL;
		} else if (330 <= cy) {
			if (50 <= cx && cx <= 60) {
				return WALL;
			} else if (70 <= cx && cx <= 340) {
				return WINDOW;
			}
		} else if (360 <= cx && 190 <= cy) {
			return SHELF;
		} else if (410 <= cx) {
			if (150 <= cy && cy <= 180) {
				return KAKEJIKU;
			}
			return WALL;
		} else if (cy <= 20) {
			if (280 <= cx && cx <= 320) {
				return DOOR_TO_DK;
			} else if (50 <= cx && cx <= 150) {
				return DOOR_TO_JSR2;
			} else {
				return WALL;
			}
		}
		return INSIDE;
	}

	@Override
	void examine_result(String ex_result) {
	}

	@Override
	void examine_effect(String examine_point, String item) {
		if (examine_point.equals(HIGHER_CLOSET)) {
			if (!isCheckedHigherCloset) {
				isCheckedHigherCloset = true;
			}
			mainpro.message_add("何か張られている...");
			mainpro.message_add(" ");
			mainpro.message_add(" ");
			mainpro.message_add("複数の×が書かれている。");
			mainpro.message_add("脱出のヒントになるのだろうか。");
			mainpro.message_add("この絵を見る限り、×の数と赤の矢印のようなものが重要な気がする...");
		} else if (examine_point.equals(LOWER_CLOSET)) {
			if (!isCheckedLowerCloset) {
				isCheckedLowerCloset = true;
			}
			mainpro.message_add("1ルートしか実装できなかったので、何もないよ！！(メタ)");
		} else if (examine_point.equals(KAKEJIKU)) {
			isCheckedKakejiku = true;
			mainpro.message_add("これは掛け軸か。");
			mainpro.message_add("...って、カレンダーやないかい！！");
			mainpro.message_add("なんか○がところどころについてるのが気になるな。");
			mainpro.message_add("にしても、絵うまいな！！！！！");
		} else if (examine_point.equals(WINDOW)) {
			isCheckedWindow = true;
			mainpro.message_add("最初の部屋と同様に窓はかなり頑丈で、開けられなさそうだ。");
		} else if (examine_point.equals(DOOR_TO_DK)) {
			mainpro.message_add("DKへのドアは空いている。");
			mainpro.message_add("このDKとは決してドン○ーコ○グのことではない。");
		} else if (examine_point.equals(DOOR_TO_JSR2)) {
			if (isJSRoom2DoorOpened) {
				mainpro.message_add("前の部屋に行けそうだ。");
			} else {
				mainpro.message_add("なんかあかないと思ったら、なんか引っかかってた！");
				mainpro.message_add("(ピロリロリン！！！)");
				mainpro.message_add("よし、前の部屋に行けるようになったぞ！");
				isJSRoom2DoorOpened = true;
			}
		}
	}

	@Override
	String return_text() {
		return null;
	}

	@Override
	boolean final_text() {
		return false;
	}

	@Override
	String hint() {
		if (isCheckedWindow == false)
			return "ここの窓からは出られるだろうか？";
		return "この部屋にはほとんど物が無い。脱出に関係ありそうなものもなさそうかな？";
	}

	@Override
	void showMap(ImageObserver field) {
		mainpro.buffer.drawImage(image_room, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100, field);
		/*DK<->和室1のドアは常に開放
		mainpro.buffer.drawImage(image_dkDoor, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);*/
		if (isHClosetDoorOpened == false)
			mainpro.buffer.drawImage(image_higherClosetDoor, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100,
					field);
		if (isLClosetDoorOpened == false)
			mainpro.buffer.drawImage(image_lowerClosetDoor, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100,
					field);
		if (isJSRoom2DoorOpened == false)
			mainpro.buffer.drawImage(image_jSRoom2Door, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100,
					field);
	}

	@Override
	void setImages(URL codeBase) {
		image_room = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room1/washitu1.png");
		image_dkDoor = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room1/F4.png");
		image_higherClosetDoor = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room1/F1.png");
		image_lowerClosetDoor = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room1/F2.png");
		image_jSRoom2Door = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room1/F3.png");
		image_maru_hint = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room1/maru_hint.png");
		image_batu_hint = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room1/batu_hint.png");
	}

	@Override
	public String toString() {
		return "JapaneseStyleRoom1";
	}

	@Override
	void setFlagTrue(String flagString) {
		if (flagString.equals("isHClosetDoorOpened")) {
			isHClosetDoorOpened = true;
		} else if (flagString.equals("isLClosetDoorOpened")) {
			isLClosetDoorOpened = true;
		} else if (flagString.equals("isJSRoom2DoorOpened")) {
			isJSRoom2DoorOpened = true;
		} else if (flagString.equals("isCheckedWindow")) {
			isCheckedWindow = true;
		} else if (flagString.equals("isCheckedHigherCloset")) {
			isCheckedHigherCloset = true;
		} else if (flagString.equals("isCheckedLowerCloset")) {
			isCheckedLowerCloset = true;
		} else if (flagString.equals("isCheckedKakejiku")) {
			isCheckedKakejiku = true;
		} else {
			System.out.println("次のフラグ名に対応するフラグはありませんでした:" + flagString);
		}
	}

	//mainproから呼び出す用のフラグ変更メソッド
	void changeDoorStatusToJSR2() {
		//JSR1 -> JSR2 への遷移処理
		if (isAlreadyMoveToJSR2 == false) {
			isJSRoom2DoorOpened = false;
			isAlreadyMoveToJSR2 = true;
		}
	}

	boolean getIsJSRoom2DoorOpened() {
		return isJSRoom2DoorOpened;
	}

}
