package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.ArrayList;

public class Dk extends Field {

	//アイテム管理フラグ
	private boolean tunacan = false;
	private boolean tap = false;
	private boolean key_yukasita = false; //和室2のフラグを使う
	private boolean key_wc_10yen = false;
	private boolean hint = false;
	private boolean key_jsr1 = false;
	
	//ドアの開閉フラグ
	private boolean door_wasitu1 = false;
	private boolean door_wasitu2 = false;
	
	//調査フラグ
	private boolean jsrFlug = false; //冷蔵庫わきの鍵取得のためのフラグ
	
	//マップ
	Image img_dk_floor = null;
	Image img_dk_fusuma = null;
	Image img_dk_table = null;
	Image img_dk_door_wasitu1 = null;
	Image img_dk_door_westroom = null;
	Image img_dk_tap = null;
	
	Mainpro mainpro;
	
	Dk(Mainpro mainpro) {
		this.mainpro = mainpro;
		texts = new ArrayList<String>();
		/* 序盤 */
		texts.add("どこかの家か？玄関は・・・");
		/* 探索 */
		//texts.add("");
	}
	
	public void paint(Mainpro mainpro) {
		super.paint(mainpro);
	}
	
	boolean is_character_in(int cx, int cy) {
		String here = here(cx, cy);
		if(here.equals("inside")) {
			return true;
		} else if(here.equals("Dk->WestRoom") && mainpro.westernStyleRoom.get_is_open_door()) {
			return true;
		} else if(here.equals("Dk->japaneseStyleRoom1") && this.isDoor_wasitu1()) {
			return true;
		} else if(here.equals("Dk->japaneseStyleRoom2") && this.isDoor_wasitu2()) {
			return true;
		}
		return false;
	}
	
	String here(int cx, int cy) {
		if(190 <= cx && cx <= 210 && 300 < cy) {
			return "Dk->WestRoom";
		} else if(cx < 60 && 300 < cy) {
			return "Dk->japaneseStyleRoom1";
		} else if(cx < 30 && 40 <= cy && cy <= 110) {
			return "Dk->japaneseStyleRoom2";
		} else if(140 < cx && cx < 270 && 10 < cy && cy <= 70) {
			return "chairTop";
		} else if(110+70 < cx+mainpro.character_size_x && cx < 290 && 70 < cy && cy < 210) {
			return "table";
		} else if(140 < cx && cx < 270 && 210 <= cy && cy < 260) {
			return "chairBottom";
		} else if(340 < cx && 40 <= cy && cy <= 60) {
			return "sink";
		} else if(340 < cx && cy < 240) {
			return "daidokoro";
		} else if(340 < cx && 240 <= cy) {
			return "reizouko";
		}
		else if(30 <= cx && cx <= 410 && cy <= 300) {
			return "inside";
		}
		return "wall";
	}

	@Override
	void examine_result(String ex_result) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	void examine_effect(String examine_point, String item) {
		if(mainpro.entrance.getIsCheckDoor()) {
			if(examine_point.equals("reizouko") && !tunacan) {
				mainpro.message_add("なにかあるかな?");
				mainpro.message_add("・・・ツナ缶がある。");
				mainpro.message_add("後で腹が減ったときにでも食べるか");
				mainpro.message_add("(ツナ缶を手に入れた!)");
				tunacan = true;
				mainpro.bag.add("ツナ缶");
			} else if(examine_point.equals("reizouko") && tunacan && !jsrFlug) {
				mainpro.message_add("もう何もない");
				jsrFlug = true;
			} else if(examine_point.equals("reizouko") && tunacan && jsrFlug && !mainpro.westernStyleRoom.isGetMagnet()) {
				mainpro.message_add("ん!?隙間の奥に鍵がある");
				mainpro.message_add("・・・・・手が届かない( ・ ω ・ )");
			} else if(examine_point.equals("reizouko") && tunacan && jsrFlug && mainpro.westernStyleRoom.isGetMagnet() && !this.key_jsr1) {
				mainpro.message_add("磁石でくっ付ければ・・・っしゃ!取れた!!!");
				mainpro.message_add("(和室1への鍵を手に入れた!)");
				setKey_jsr1(true);
				mainpro.bag.add("DK-和室1の鍵");
			} else if(examine_point.equals("reizouko") && tunacan && jsrFlug && mainpro.westernStyleRoom.isGetMagnet() && this.key_jsr1) {
				mainpro.message_add("ここにはもう何もない");
			} else if(examine_point.equals("sink") && !tap) {
				mainpro.message_add("水は出るのか？");
				mainpro.message_add("・・・!?");
				mainpro.message_add("蛇口が取れた!!!");
				mainpro.message_add("(蛇口を手に入れた!)");
				tap = true;
				mainpro.bag.add("蛇口");
			} else if(examine_point.equals("sink") && tap) {
				mainpro.message_add("蛇口が外れた水道だ");
				mainpro.message_add("水を出せないから意味がない");
			} else if(examine_point.equals("table") && !key_yukasita && item.equals(IconName.YUKASITA_KEY) && !key_wc_10yen && !hint) {
				mainpro.message_add("よいしょっと・・・");
				mainpro.message_add("10円玉とー、なんだこれ？");
				mainpro.message_add("(10円玉を手に入れた!)");
				mainpro.message_add("(ヒント1を手に入れた!)");
				key_wc_10yen = true;
				hint = true;
				key_yukasita = true;
				mainpro.bag.add("十円玉");
				mainpro.bag.add("何かのヒント");
			} else if(examine_point.equals("table") && key_yukasita && key_wc_10yen) {
				mainpro.message_add("もう何もない");
			} else if(examine_point.equals("table") && !key_yukasita) {
				mainpro.message_add("床下収納がある!");
				mainpro.message_add("けど鍵が掛かっているのか。開かない");
			}
			
			if(examine_point.equals("Dk->japaneseStyleRoom1") && item.equals("DK-和室1の鍵") && !this.isDoor_wasitu1()) {
				this.setDoor_wasitu1(true);
				mainpro.message_add("(ガチャリとドアが開いた)");
			} else if(examine_point.equals("Dk->japaneseStyleRoom1") && item.equals("DK-和室1の鍵") && this.isDoor_wasitu1()) {
				mainpro.message_add("(もう開いてる)");
			}
			
		}
	}

	boolean final_text() {
		if(text_index == texts.size()-1) {
			return true;
		}
		return false;
	}

	public String toString() {
		return "Dk";
	}
	
	@Override
	String hint() {
		if(!mainpro.entrance.getIsCheckDoor()) {
			return "玄関を探そう";
		} else if(mainpro.entrance.getIsCheckDoor()) {
			return "部屋の物を見てまわろう";
		}
		return "玄関を開けるためのヒントを探そう";
	}

	@Override
	void showMap(ImageObserver mapr) {
		mainpro.buffer.drawImage(img_dk_floor, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mapr);
		mainpro.buffer.drawImage(img_dk_tap, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
		mainpro.buffer.drawImage(img_dk_fusuma, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
		mainpro.buffer.drawImage(img_dk_table, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
		mainpro.buffer.drawImage(img_dk_door_wasitu1, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
		mainpro.buffer.drawImage(img_dk_door_westroom, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
	}
	
	void setImages(URL codeBase) {
		img_dk_floor = mainpro.getImage(codeBase, "../material_data/escape_game/dk/dk_base_bottom.png");
		img_dk_tap = mainpro.getImage(codeBase, "../material_data/escape_game/dk/dk_tap.png");
		img_dk_fusuma = mainpro.getImage(codeBase, "../material_data/escape_game/dk/dk_fusuma.png");
		img_dk_table = mainpro.getImage(codeBase, "../material_data/escape_game/dk/dk_table.png");
		img_dk_door_wasitu1 = mainpro.getImage(codeBase, "../material_data/escape_game/dk/dk_wasitu_door.png");
		img_dk_door_westroom = mainpro.getImage(codeBase, "../material_data/escape_game/dk/dk_westroom_door.png");
	}

	@Override
	String return_text() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	void setFlagTrue(String flagString) {
		if (flagString.equals("tunacan")) tunacan = true;
		else if (flagString.equals("tap")) tap = true;
		//else if (flagString.equals("key_wasitu1_magnet")) key_wasitu1_magnet = true;
		else if (flagString.equals("key_yukasita")) key_yukasita = true;
		else if (flagString.equals("key_wc_10yen")) key_wc_10yen = true;
		else if (flagString.equals("key_jsr1")) key_jsr1 = true;
		else if (flagString.equals("door_wasitu1")) setDoor_wasitu1(true);
		else if (flagString.equals("door_wasitu2")) setDoor_wasitu2(true);
		else System.out.println("次のフラグ名に対応するフラグはありませんでした:"+flagString);
	}

	public boolean isDoor_wasitu1() {
		return door_wasitu1;
	}

	public void setDoor_wasitu1(boolean door_wasitu1) {
		this.door_wasitu1 = door_wasitu1;
	}

	public boolean isDoor_wasitu2() {
		return door_wasitu2;
	}

	public void setDoor_wasitu2(boolean door_wasitu2) {
		this.door_wasitu2 = door_wasitu2;
	}

	public boolean isKey_jsr1() {
		return key_jsr1;
	}

	public void setKey_jsr1(boolean key_jsr1) {
		this.key_jsr1 = key_jsr1;
	}
}

class DkTop extends Field{

	Image dk_base_top;
	Image dk_door_dressing_room;
	Image dk_door_wc;
	
	Mainpro mainpro;

	private boolean isWCDoorOpen = false;
	private boolean isDatuijoDoorOpen = false;

	DkTop(Mainpro mainpro) {
		this.mainpro = mainpro;
	}
	
	boolean is_character_in(int cx, int cy) {
		String here = here(cx, cy);
		if(here.equals("inside") || (here.equals("DkTop->datuijo") && isDatuijoDoorOpen)
			|| here.equals("DkTop->Entrance") || (here.equals("DkTop->W.C") && isWCDoorOpen)) {
			return true;
		}
		return false;
	}
	
	String here(int cx, int cy) {
		if(cx < 130 && 180 <= cy && cy <= 220) {
			return "DkTop->datuijo";
		} else if(160 <= cx && cx <= 240 && cy < 100) {
			return "DkTop->Entrance";
		} else if(340 < cx && cx <= 370 && cy < 100) {
			return "DkTop->W.C";
		} else if(340 < cx && 190 < cy) {
			return "renji";
		} else if(cx < 130 && cy < 320) {
			return "datuijo";
		} else if(30 <= cx && cx <= 410 && 100 <= cy) {
			return "inside";
		}
		return "wall";
	}

	@Override
	void examine_result(String ex_result) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	void examine_effect(String examine_point, String item) {
		if(mainpro.entrance.getIsCheckDoor()) {
			if(examine_point.equals("renji")) {
				mainpro.message_add("何もない");
			} else if (examine_point.equals("DkTop->W.C")) {
				if (isWCDoorOpen) {
					mainpro.message_add("ドアは開いている");
				} else if (!isWCDoorOpen && item.equals(IconName.JUENDAMA)) {
					mainpro.message_add("家のトイレの鍵は簡単に開くんだよな");
					mainpro.message_add("よっしゃ、開いたぜ！");
					isWCDoorOpen = true;
				} else {
					mainpro.message_add("このドアは鍵がかかっているようだ");
					mainpro.message_add("しかし鍵穴はほかのドアのと比べると簡素なものだな");
				}
			} else if (examine_point.equals("DkTop->datuijo")) {
				if (isDatuijoDoorOpen) {
					mainpro.message_add("ドアは開いている");
				} else if (!isDatuijoDoorOpen && item.equals(IconName.DK_DATUIJO_KEY)) {
					mainpro.message_add("この鍵でここのドアは開かないかなぁ...");
					mainpro.message_add("開くじゃん、やったぜ！！！");
					isDatuijoDoorOpen = true;
				} else {
					mainpro.message_add("恒例のごとく、鍵がかかっている");
				}
			}
		} 
	}

	@Override
	boolean final_text() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	String hint() {
		String message="玄関を開けるためのヒントを探そう";
		if(!mainpro.entrance.getIsCheckDoor()) {
			message = "玄関から出られればいいけど・・・";
		} else if(mainpro.entrance.getIsCheckDoor()) {
			message = "家の中を調べよう";
		}
		return message;
	}

	@Override
	void showMap(ImageObserver mapr) {
		mainpro.buffer.drawImage(dk_base_top, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mapr);
		if (!isDatuijoDoorOpen) {
			mainpro.buffer.drawImage(dk_door_dressing_room, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
		}
		if (!isWCDoorOpen) {
			mainpro.buffer.drawImage(dk_door_wc, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
		}
	}

	@Override
	void setImages(URL codeBase) {
		dk_base_top = mainpro.getImage(codeBase, "../material_data/escape_game/dk/dk_base_top.png");
		dk_door_dressing_room = mainpro.getImage(codeBase, "../material_data/escape_game/dk/dk_dressing_room_door.png");
		dk_door_wc = mainpro.getImage(codeBase, "../material_data/escape_game/dk/dk_toilet_door.png");
		
	}

	@Override
	public String toString() {
		return "DkTop";
	}

	@Override
	String return_text() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	void setFlagTrue(String flagString) {
		System.out.println("次のフラグ名に対応するフラグはありませんでした:"+flagString);
	}
	
}
