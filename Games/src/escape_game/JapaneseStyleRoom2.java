package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;

//和室1(下側の方の和室)
public class JapaneseStyleRoom2 extends Field {
	//部屋
	private Image image_room;
	//ドア
	private Image image_jsr1Door;
	private Image image_dkDoor;
	//アイテム
	private Image image_tutu;
	//ヒント
	private Image image_logo;
	private Image image_logoHint;

	//ドアの開閉フラグ
	private boolean isDKDoorOpened = false;
	private boolean isJSR1DoorOpened = false;

	//ドアチェックフラグ
	private boolean isCheckedDKDoor = false;
	private boolean isCheckedJSR1Door = false;

	//アイテム取得フラグ
	private boolean isGetTutu = false;

	//ヒント確認フラグ
	private boolean isCheckedLogoHint;
	private boolean isLogoHintLooking;

	//もの
	private static final String INSIDE = "inside";
	private static final String WALL = "wall";
	private static final String TABLE = "table";
	private static final String LOGO = "logo";
	//畳(当たり判定結果はINSIDEと同じ)
	private static final String TATAMI_LEFT_TOP = "leftTop";
	private static final String TATAMI_LEFT_MIDDLE = "leftMiddle";
	private static final String TATAMI_LEFT_BOTTOM = "leftBottom";
	private static final String TATAMI_CENTER = "center";
	private static final String TATAMI_RIGHT_TOP = "rightTop";
	private static final String TATAMI_RIGHT_BOTTOM = "rightBottom";

	//部屋名
	private static final String DOOR_TO_DK = "door_to_dk";
	private static final String DOOR_TO_JSR1 = "door_to_jsr1";

	//DKドアへの謎回答
	private static final String[] CORRECT_TATAMI_LIST = { TATAMI_LEFT_TOP, TATAMI_LEFT_MIDDLE, TATAMI_LEFT_BOTTOM,
			TATAMI_CENTER, TATAMI_RIGHT_TOP, TATAMI_RIGHT_BOTTOM };
	private int targetAnsTatamiIndex = 0;
	private boolean isCorrectAns = false;

	private Mainpro mainpro;

	public JapaneseStyleRoom2(Mainpro mainpro) {
		this.mainpro = mainpro;
	}

	@Override
	public void paint(Mainpro mainpro) {
		super.paint(mainpro);
		if (isLogoHintLooking) {
			mainpro.buffer.drawImage(image_logoHint, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100,
					mainpro);
			if (mainpro.message.get(0).equals("nothing")) {
				isLogoHintLooking = false;
			}
		}
	}

	@Override
	boolean is_character_in(int cx, int cy) {
		String here = here(cx, cy);
		if (here.equals(INSIDE)) {
			return true;
		} else if (here.equals(TATAMI_LEFT_TOP) || here.equals(TATAMI_LEFT_MIDDLE) || here.equals(TATAMI_LEFT_BOTTOM) ||
				here.equals(TATAMI_CENTER) || here.equals(TATAMI_RIGHT_TOP) || here.equals(TATAMI_RIGHT_BOTTOM)) {
			if (!isCorrectAns) {
				updateTatamiStatus(here);
			}
			return true;
		} else if (here.equals(DOOR_TO_JSR1) && isJSR1DoorOpened) {
			return true;
		} else if (here.equals(DOOR_TO_DK) && isDKDoorOpened) {
			return true;
		}
		return false;
	}

	@Override
	String here(int cx, int cy) {
		if (360 <= cx) {
			if (40 <= cy && cy <= 170) {
				return DOOR_TO_DK;
			} else if (230 <= cy && cy <= 270) {
				return LOGO;
			} else {
				return WALL;
			}
		} else if (cy <= 30 || cx <= 70) {
			return WALL;
		} else if (320 <= cy) {
			if (250 <= cx) {
				return DOOR_TO_JSR1;
			}
			return WALL;
		} else if (90 <= cy && cy <= 230 && 130 <= cx && cx <= 310) {
			return TABLE;
		}
		//畳判定
		//キャラが上or下向き
		if (mainpro.character_direct.equals("front") || mainpro.character_direct.equals("back")) {
			if (cy <= 80 && cx <= 240) {
				return TATAMI_LEFT_TOP;
			} else if (120 <= cy && cy <= 240 && cx <= 140) {
				return TATAMI_LEFT_MIDDLE;
			} else if (280 <= cy && cx <= 240) {
				return TATAMI_LEFT_BOTTOM;
			} else if (200 <= cx && cx <= 240 && 120 <= cy && cy <= 250) {
				return TATAMI_CENTER;
			} else if (cy <= 160 && 300 <= cx) {
				return TATAMI_RIGHT_TOP;
			} else if (200 <= cy && 300 <= cx) {
				return TATAMI_RIGHT_BOTTOM;
			}
		} //キャラが左or右向き
		else if (mainpro.character_direct.equals("left") || mainpro.character_direct.equals("right")) {
			if (cy <= 60 && cx <= 260) {
				return TATAMI_LEFT_TOP;
			} else if (120 <= cy && cy <= 220 && cx <= 160) {
				return TATAMI_LEFT_MIDDLE;
			} else if (270 <= cy && cx <= 260) {
				return TATAMI_LEFT_BOTTOM;
			} else if (190 <= cx && cx <= 270 && 100 <= cy && cy <= 240) {
				return TATAMI_CENTER;
			} else if (cy <= 150 && 300 <= cx) {
				return TATAMI_RIGHT_TOP;
			} else if (180 <= cy && 300 <= cx) {
				return TATAMI_RIGHT_BOTTOM;
			}
		}
		return INSIDE;
	}

	@Override
	void examine_result(String ex_result) {
	}

	@Override
	void examine_effect(String examine_point, String item) {
		if (examine_point.equals(DOOR_TO_DK)) {
			if (isCorrectAns && !isDKDoorOpened) {
				isDKDoorOpened = true;
				mainpro.message_add("あ、開くじゃん！！");
				mainpro.message_add("って、隣の部屋はDKだったのか。新しい部屋じゃなかったなぁ。");
			} else if (!isCheckedDKDoor && !isDKDoorOpened) {
				isCheckedDKDoor = true;
				mainpro.message_add("このドアは開けられるかな...");
				mainpro.message_add("...");
				mainpro.message_add("...");
				mainpro.message_add("だめだ、どう頑張っても開きそうにない。");
			} else {
				if (isDKDoorOpened) {
					mainpro.message_add("このドアは開いている。");
				} else {
					mainpro.message_add("どうやったらこのドアを開けられるだろうか...");
					mainpro.message_add("この部屋を色々と調べてみるか。");
				}
			}
		} else if (examine_point.equals(DOOR_TO_JSR1)) {
			if (!isCheckedJSR1Door && !isJSR1DoorOpened) {
				isCheckedJSR1Door = true;
				mainpro.message_add("あれ、このドア開かないやん。");
				mainpro.message_add("なんでや！！！！！");
			} else {
				if (isJSR1DoorOpened) {
					mainpro.message_add("このドアは開いている。");
				} else {
					mainpro.message_add("本当に開かんし...");
					mainpro.message_add("OWATA＼(＾o＾)／");
				}
			}
		} else if (examine_point.equals(TABLE)) {
			if (!isGetTutu) {
				isGetTutu = true;
				mainpro.message_add("机の上になんかあるな。");
				mainpro.message_add("(つつを手に入れた！)");
				mainpro.bag.add("筒");
			} else {
				mainpro.message_add("もう机には何もないようだ。");
			}
		} else if (examine_point.equals(LOGO)) {
			isLogoHintLooking = true;
			if (!isCheckedLogoHint) {
				isCheckedLogoHint = true;
				mainpro.message_add("何か壁に貼られているな。");
				mainpro.message_add("なんだこれ、赤い矢印がつながっているように見えるが。");
				mainpro.message_add("この部屋から出る方法と関係があるのだろうか。");
			} else {
				if (!isCorrectAns) {
					mainpro.message_add("この壁紙の意味は一体...");
					mainpro.message_add("矢印の頂点が6つ...");
					mainpro.message_add("この部屋に6つの何かがある？");
				} else {
					mainpro.message_add("畳の踏む順番だったのか");
				}
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
		if (!isCheckedDKDoor) {
			return "あそこに見えるドアは開くだろうか";
		} else if (!isCheckedJSR1Door && !isJSR1DoorOpened) {
			return "一旦さっきの和室に戻ってみよう";
		} else if (!isGetTutu || !isCheckedLogoHint) {
			return "この部屋に何かないか探してみよう";
		} else if (!isCorrectAns) {
			return "壁に貼ってある紙の内容が、この部屋から出る方法に関係あるのかな...";
		} else if (!isDKDoorOpened) {
			return "何か変わったものはないか調べてみよう。";
		}
		return "この部屋にはもう目ぼしいものはない気がする。";
	}

	@Override
	void showMap(ImageObserver field) {
		mainpro.buffer.drawImage(image_room, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100, field);
		mainpro.buffer.drawImage(image_logo, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100, field);
		if (isDKDoorOpened == false)
			mainpro.buffer.drawImage(image_dkDoor, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100, field);
		if (isJSR1DoorOpened == false)
			mainpro.buffer.drawImage(image_jsr1Door, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100, field);
		if (isGetTutu == false)
			mainpro.buffer.drawImage(image_tutu, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y - 100, field);
	}

	@Override
	void setImages(URL codeBase) {
		image_room = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room2/washitsu2_1.png");
		image_dkDoor = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room2/F1.png");
		image_jsr1Door = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room2/F2.png");
		image_tutu = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room2/tsu.png");
		image_logo = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room2/logo.png");
		image_logoHint = mainpro.getImage(codeBase, "../material_data/escape_game/japanese_room2/logo_hint.png");
	}

	@Override
	public String toString() {
		return "JapaneseStyleRoom2";
	}

	@Override
	void setFlagTrue(String flagString) {
		if (flagString.equals("isDKDoorOpened")) {
			isDKDoorOpened = true;
		} else if (flagString.equals("isJSR1DoorOpened")) {
			isJSR1DoorOpened = true;
		} else if (flagString.equals("isCheckedDKDoor")) {
			isCheckedDKDoor = true;
		} else if (flagString.equals("isCheckedJSR1Door")) {
			isCheckedJSR1Door = true;
		} else if (flagString.equals("isGetTutu")) {
			isGetTutu = true;
		} else if (flagString.equals("isCheckedLogoHint")) {
			isCheckedLogoHint = true;
		} else if (flagString.equals("isLogoHintLooking")) {
			isLogoHintLooking = true;
		} else if (flagString.equals("isCorrectAns")) {
			isCorrectAns = true;
		} else {
			System.out.println("次のフラグ名に対応するフラグはありませんでした:" + flagString);
		}
	}

	static String getDoorToDK() {
		return DOOR_TO_DK;
	}

	static String getDoorToJSR1() {
		return DOOR_TO_JSR1;
	}

	//畳状況更新
	private void updateTatamiStatus(String stepedTatami) {
		if (stepedTatami.equals(CORRECT_TATAMI_LIST[targetAnsTatamiIndex])) {
			targetAnsTatamiIndex++;
			mainpro.message_add("(カチッ)");
			mainpro.message_add("何かが押されたようだ");
			//畳の踏む順番が正しい
			if (targetAnsTatamiIndex == CORRECT_TATAMI_LIST.length) {
				isCorrectAns = true;
				mainpro.message_add("(ピロリロリン♪↑)");
				mainpro.message_add("なんだ、なんか音が鳴ったぞ");
				mainpro.message_add("改めて今まで開かなかったドアを調べてみよう。");
			}
		} else {
			boolean stepedTatamiResetFlg = true;
			for (int i = 0; i < targetAnsTatamiIndex; i++) {
				if (CORRECT_TATAMI_LIST[i].equals(stepedTatami)) {
					stepedTatamiResetFlg = false;
				}
			}
			if (stepedTatamiResetFlg && targetAnsTatamiIndex != 0) {
				targetAnsTatamiIndex = 0;
				mainpro.message_add("(テロテロン↓)");
				mainpro.message_add("なにか、残念な感じの音が鳴ったな");
			}
		}
	}

	void updateJSR1DoorFlag(boolean isJSR1DoorOpened) {
		this.isJSR1DoorOpened = isJSR1DoorOpened;
	}
}
