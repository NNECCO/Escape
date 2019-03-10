package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;

public class WC extends Field{

	//マップ
	private Image image_wc_benki = null;
	private Image image_wc_door = null;
	private Image image_wc_hint_triangle = null;
	private Image image_wc_base = null;
	private Image image_wc_window = null;
	private Image image_wc_mat = null;
	private Image imagae_wc_senmendai = null;

	private Mainpro mainpro;

	//ドアの開閉フラグ
	private boolean isDkDoorOpen = true;

	//チェックフラグ
	private boolean isCheckedWindow = false;
	private boolean isCheckedSenmendai = false;

	//アイテム取得フラグ
	private boolean isGotTriangleHint = false;
	private boolean isGotDatuijoKey = false;

	//もの
	public static final String DOOR_TO_DK = "door_to_dk";
	public static final String MAT = "mat";
	public static final String TOILET = "toilet";
	public static final String WINDOW = "window";
	public static final String TRIANGLE_HINT = "triangle_hint";
	public static final String SENMENDAI = "senmendai";
	public static final String DATUIJO_KEY = "datuijo_key";
	public static final String TOILET_HANDLE = "toilet_handle";
	//場所
	public static final String WALL = "wall";
	public static final String INSIDE = "inside";

	public WC(Mainpro mainpro) {
		this.mainpro = mainpro;
	}

	@Override
	boolean is_character_in(int cx, int cy) {
		// TODO 自動生成されたメソッド・スタブ

		String here = here(cx,cy);
		if (here.equals(INSIDE)) return true;
		else if (here.equals(DOOR_TO_DK) && isDkDoorOpen == true) {
			return true;
		}
		return false;
	}

	@Override
	String here(int cx, int cy) {
		if (cx <= 30) {
			return WALL;
		} else if (320 <= cy) {
			if (cx <= 60) {
				return DOOR_TO_DK;
			} else {
				return WALL;
			}
		} else if (cy <= 120) {
			if (cx <= 170) {
				return WINDOW;
			} else {
				return WALL;
			}
		} else if (250 <= cx && cy <= 150) {
			return SENMENDAI;
		} else if (170 <= cx && 170 <= cy && cy <= 280) {
			return TOILET;
		} else if (290 <= cy && 250 <= cx) {
			return TOILET_HANDLE;
		}
		return "inside";
	}

	@Override
	void examine_result(String ex_result) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	void examine_effect(String examine_point, String item) {
		switch(examine_point) {
			case TOILET:
				if (!isGotTriangleHint) {
					mainpro.message_add("便器の蓋の上に何かがおかれている。");
					mainpro.message_add("紙だ、何か書かれているみたいだ。");
					mainpro.message_add("この家から出るヒントになるかもしれないし、持っていこう。");
					mainpro.message_add("(ヒントらしきものが書かれた紙を手に入れた！)");
					mainpro.bag.add(IconName.TRIANGLE_HINT);
					isGotTriangleHint = true;
				} else {
					mainpro.message_add("便器にはもう何もなさそうだ('_')");
				}
				break;
			case TOILET_HANDLE:
				if (!isGotDatuijoKey) {
					mainpro.message_add("意味はないが水を流すか...");
					mainpro.message_add("...");
					mainpro.message_add("...");
					mainpro.message_add("...");
					mainpro.message_add("あ、レバーの後ろに何かあるぞ！！");
					mainpro.message_add("これは、まぎれもなくカギだ！！！！");
					mainpro.message_add("(鍵を手に入れた！)");
					mainpro.bag.add(IconName.DK_DATUIJO_KEY);
					isGotDatuijoKey = true;
				} else {
					mainpro.message_add("(ジャーーー)");
					mainpro.message_add("...");
					mainpro.message_add("もう何もないな・・・");
				}
				break;
			case DOOR_TO_DK:
				if (!isDkDoorOpen) {
					mainpro.message_add("なんでドアが閉まってるねん！！！バグや！");
					mainpro.message_add("<<<<<バグです>>>>>");
				} else {
					mainpro.message_add("DKへとつながるドアは開いている。");
				}
				break;
			case WINDOW:
				if (!isCheckedWindow) {
					mainpro.message_add("このドアは...まあ開くわけないよなぁ...");
					isCheckedWindow = true;
				} else {
					mainpro.message_add("安定の頑丈さだぜ");
				}
				break;
			case SENMENDAI:
				if (!isCheckedSenmendai) {
					mainpro.message_add("洗面台だ、特筆すべきことは何もない。");
					isCheckedSenmendai = true;
				} else {
					mainpro.message_add("お湯が出ないタイプの洗面台だな。");
					mainpro.message_add("冬はつらいね(小並感)");
				}
				break;
			default:
				break;
		}
	}

	@Override
	String return_text() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	boolean final_text() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	String hint() {
		if (!isGotTriangleHint) {
			return "明らかに怪しいものがあるな、調べてみよう";
		} else if (!isGotDatuijoKey) {
			return "あと一つくらいはこの部屋にありそうだね";
		} else if (!isCheckedSenmendai || !isCheckedWindow) {
			return "一応他に何かないか調べとくかぁ";
		}
		return "いつでもなぜかトイレは落ち着くよなぁ...";
	}

	@Override
	void showMap(ImageObserver field) {
		// TODO 自動生成されたメソッド・スタブ
		mainpro.buffer.drawImage(image_wc_base, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		mainpro.buffer.drawImage(image_wc_mat, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		mainpro.buffer.drawImage(imagae_wc_senmendai, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		mainpro.buffer.drawImage(image_wc_benki, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		if (!isGotTriangleHint) {
			mainpro.buffer.drawImage(image_wc_hint_triangle, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		}
		mainpro.buffer.drawImage(image_wc_window, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		if (!isDkDoorOpen) {
			mainpro.buffer.drawImage(image_wc_door, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		}
	}

	@Override
	void setImages(URL codeBase) {
		// TODO 自動生成されたメソッド・スタブ
		image_wc_benki         = mainpro.getImage(codeBase,"../material_data/escape_game/wc/benki.png");
		image_wc_door          = mainpro.getImage(codeBase,"../material_data/escape_game/wc/door.png");
		image_wc_hint_triangle = mainpro.getImage(codeBase, "../material_data/escape_game/wc/hint_triangle.png");
		image_wc_base          = mainpro.getImage(codeBase, "../material_data/escape_game/wc/wc_base.png");
		image_wc_window        = mainpro.getImage(codeBase, "../material_data/escape_game/wc/window.png");
		image_wc_mat           = mainpro.getImage(codeBase, "../material_data/escape_game/wc/mat.png");
		imagae_wc_senmendai    = mainpro.getImage(codeBase, "../material_data/escape_game/wc/senmendai.png");
	}

	@Override
	public String toString() {
		return "WC";
	}

	@Override
	void setFlagTrue(String flagString) {
		if (flagString.equals("isDkDoorOpen")) {
			isDkDoorOpen = true;
		} else if (flagString.equals("isCheckedWindow")) {
			isCheckedWindow = true;
		} else if (flagString.equals("isCheckedSenmendai")) {
			isCheckedSenmendai = true;
		} else if (flagString.equals("isGotTriangleHint")) {
			isGotTriangleHint = true;
		} else if (flagString.equals("isGotDatuijoKey")) {
			isGotDatuijoKey = true;
		}

	}

}
