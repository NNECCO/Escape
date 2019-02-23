package escape_game;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.ArrayList;


public class WesternStyleRoom extends Field {
	private boolean is_get_west_dk_key = false;
	private boolean is_get_cutter = false;
	private boolean is_get_light = false;
	private boolean is_get_magnet = false;
	private boolean is_open_door = false;
	//ドアを調べたかどうか
	private boolean is_checked_door = false;
	//マップ
	Image image_west_room_chair = null;
	Image image_west_room_trashbox = null;
	Image image_west_room_door = null;
	Image image_west_room_bed = null;
	Image image_west_room_desk = null;
	Image image_west_room_window = null;
	Image image_west_room_bookcase = null;
	Image image_west_room_base = null;
	Mainpro mainpro;

	public WesternStyleRoom (Mainpro mainpro) {
		texts = new ArrayList<String>();
		texts.add("ここは、どこなんだ・・・？");
		texts.add("自分以外に誰もいないようだ・・・");
		texts.add("とりあえず、出口を探そう");
		text_index = -1;
		this.mainpro = mainpro;
	}
	public void paint() {
		//テキストのみの表示の場合は、画面を全て黒くする
		if (mainpro.is_show_text = true && mainpro.is_show_buttons == false) {
			mainpro.buffer.setColor(Color.black);
			mainpro.buffer.fillRect(0, 0, mainpro.screen_size_x, mainpro.screen_size_y);
		}

		super.paint(mainpro);
	}

	void examine_effect (String examine_point, String item) {
		//玄関ドアの確認に関わらず、ドア・窓の処理は共通
		if (examine_point.equals("door")) {
			if (is_open_door == false) {
				mainpro.message_add("このドア、鍵がかかってるな");
				if(is_checked_door == false) {
					//ドア確認フラグ取得
					is_checked_door = true;
				}

				if (item.equals("洋室-DKの鍵")) {
					mainpro.message_add("この鍵をつかってみるか...");
					mainpro.message_add("（DKにつながる鍵を使った！）");
					mainpro.message_add("（ガチャッ）");
					mainpro.message_add("開いたみたいだな");
					is_open_door = true;
				}
				else {
				}
			}
			else {
				mainpro.message_add("（既にドアは開いている）");
			}
		}
		else if(examine_point.equals("window")) {
			mainpro.message_add("この窓から出られるかも...");
			mainpro.message_add("......");
			mainpro.message_add("......");
			mainpro.message_add("ダメだ。");
			mainpro.message_add("窓の鍵は見つからないし、窓自体も相当固くて割れそうにない。");
		}
		//洋室-DKのドアを調べていない場合
		else if (is_checked_door == false) {
			mainpro.message_add("まずは、この家から出られるかを確認しよう。");
		}
		else {
			//ゴミ箱のexamine処理は、玄関ドアのチェックに関わらず有効
			if (examine_point.equals("trash")) {
				if (is_get_west_dk_key == false) {
					mainpro.message_add("（ガサゴソ...）");
					mainpro.message_add("ん？");
					mainpro.message_add("鍵が捨ててあるな");

					//ドアが空かないことを確認しているとき
					if(is_checked_door == true) {
						mainpro.bag.add("洋室-DKの鍵");
						mainpro.message_add("（鍵を手に入れた！）");
						is_get_west_dk_key = true;
					}
					//まだドアを確認していないとき
					else if(is_checked_door == false){
						mainpro.message_add("そんなことよりも今はここから出ることが先決だ");
					}
				}
				else {
					mainpro.message_add("もう何もないわー");
				}
			}
			//玄関のドアを調べていない場合は洋室の鍵以外は取得しない。
			else if (mainpro.entrance.getIsCheckDoor() == false) {
				mainpro.message_add("この家を出るための鍵などは見つからない。");
			}
			//玄関のドアを調べている場合は、アイテム取得可
			else {
				if (examine_point.equals("desk") && mainpro.character_direct.equals("right")) { //机を正面から見た場合(側面からはNG)
					if (is_get_cutter == false) {
						mainpro.bag.add("カッター");
						mainpro.message_add("ドラ●モンとかでてきたりしねぇかな");
						mainpro.message_add("あ、カッターがあったわ");
						mainpro.message_add("（カッターを手に入れた！）");
						is_get_cutter = true;
					}
					else {
						mainpro.message_add("ドラ●モンは出てこないみたいだな");
					}
				}
				else if (examine_point.equals("bed")) {
					if (is_get_light == false) {
						mainpro.bag.add("懐中電灯");
						mainpro.message_add("ベッドの下に何かあるかな......");
						mainpro.message_add("残念、懐中電灯しかなかったか");
						mainpro.message_add("（懐中電灯を手に入れた！）");
						is_get_light = true;
					}
					else {
						double random = Math.random();
						mainpro.message_add("（ガサゴソ...）");
						mainpro.message_add("......");
						if (random < 0.5) {
							mainpro.message_add("！！");
							mainpro.message_add("やっぱり、何もないか");
						}
						else {
							mainpro.message_add("！！！！！！！！！！！！！");
							mainpro.message_add("<<<<<<<GAME OVER......>>>>>>");
							mainpro.message_add("It's a joke");
						}
					}
				}
				else if (examine_point.equals("bookshelf") && mainpro.character_direct.equals("front")) {
					if (is_get_magnet == false) {
						mainpro.message_add("（色々な本が並んでいる。）");
						double random = Math.random();
						if (random < 0.2) {
							mainpro.bag.add("可変式棒磁石");
							mainpro.message_add("（本棚の内側に棒磁石がくっついている）");
							mainpro.message_add("強力な磁石だから何かに使えるかもしれないな");
							mainpro.message_add("（棒磁石を手に入れた！）");
							is_get_magnet = true;
						}
						else {
							mainpro.message_add("いろんな本があるけど、");
							mainpro.message_add("使えそうなものは見当たらないな");
							mainpro.message_add("大抵、ここに何かあるのは定石なのに...");
						}
					}
					else {
						mainpro.message_add("流石にもうないだろ");
					}
				}
			}
		}
	}

	String return_text() {
		return texts.get(text_index);
	}

	boolean final_text() {
		if (text_index == texts.size()-1) return true;
		return false;
	}

	boolean is_character_in (int cx,int cy) {
		String here = here(cx,cy);
		if (here.equals("inside")) return true;
		else if (here.equals("door") && is_open_door == true) {
			return true;
		}
		return false;
	}

	String here(int cx,int cy) {
		if      (30 <= cx && cx + mainpro.character_size_x <= 144 && cy <= 49) return "door";
		else if (224 <= cx + mainpro.character_size_x && cx <= 326
				&& cy <= 63) return "bookshelf";
		else if (348 <= cx+mainpro.character_size_x
				&& cx + mainpro.character_size_x <= 378
				&& 79 <= cy + mainpro.character_size_y
				&& cy <= 140) return "chair";
		else if (378 <= cx + mainpro.character_size_x
				&& 59 <= cy + mainpro.character_size_y
				&& cy <= 191) return "desk";
		else if (410 <= cx+mainpro.character_size_x
				&& 199 <= cy+mainpro.character_size_y
				&& cy <= 245) return "trash";
		else if (335 <= cx + mainpro.character_size_x
				&& cx +mainpro.character_size_x <= 459
				&& 279 <= cy + mainpro.character_size_y
				&& cy <= 339) return "bed";
		else if (30 <= cx && cx <= 170 && 290 <= cy) return "window";
		else if (30 <= cx && cx + mainpro.character_size_x <= 467
				 && 50 <= cy && cy + mainpro.character_size_y <= 343) return "inside";
		else return "wall";
	}

	@Override
	void examine_result(String ex_result) {
		//調べた結果起こる現象
		//例:机を調べると机が動く等
	}

	public String toString() {
		return "WesternStyleRoom";
	}

	String hint() {
		if (is_checked_door == false) {
			return "この部屋からでなければ...";
		}
		else if (is_get_west_dk_key == false) {
			return "とりあえず、この部屋から出るための鍵を探さなきゃ";
		}
		else if (is_get_west_dk_key == true && is_open_door == false) {
			return "さっき手に入れた鍵を使って、この部屋から出れないだろうか";
		}
		return "隣の部屋に行こう";
	}

	void showMap(ImageObserver mapr) {
		mainpro.buffer.drawImage(image_west_room_base, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100,mapr);
		mainpro.buffer.drawImage(image_west_room_chair, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100,mapr);
		mainpro.buffer.drawImage(image_west_room_trashbox, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100,mapr);
		mainpro.buffer.drawImage(image_west_room_desk, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100,mapr);
		mainpro.buffer.drawImage(image_west_room_bed, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100,mapr);
		mainpro.buffer.drawImage(image_west_room_bookcase, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100,mapr);
		mainpro.buffer.drawImage(image_west_room_window, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100,mapr);
		if (is_open_door == false) {
			mainpro.buffer.drawImage(image_west_room_door, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100,mapr);
		}
	}

	public void setImages(URL codeBase) {
		image_west_room_chair = mainpro.getImage(codeBase,"../material_data/escape_game/westroom/west_room_chair.png");
		image_west_room_trashbox = mainpro.getImage(codeBase,"../material_data/escape_game/westroom/west_room_trashbox.png");
		image_west_room_door = mainpro.getImage(codeBase,"../material_data/escape_game/westroom/west_room_door.png");
		image_west_room_bed = mainpro.getImage(codeBase,"../material_data/escape_game/westroom/west_room_bed.png");
		image_west_room_desk = mainpro.getImage(codeBase,"../material_data/escape_game/westroom/west_room_desk.png");
		image_west_room_window = mainpro.getImage(codeBase,"../material_data/escape_game/westroom/west_room_window.png");
		image_west_room_bookcase = mainpro.getImage(codeBase,"../material_data/escape_game/westroom/west_room_bookcase.png");
		image_west_room_base = mainpro.getImage(codeBase,"../material_data/escape_game/westroom/west_room_base.png");
	}

	@Override
	void setFlagTrue(String flagString) {
		if (flagString.equals("is_get_west_dk_key")) is_get_west_dk_key = true;
		else if (flagString.equals("is_get_cutter")) is_get_cutter = true;
		else if (flagString.equals("is_get_light")) is_get_light = true;
		else if (flagString.equals("is_get_magnet")) is_get_magnet = true;
		else if (flagString.equals("is_open_door")) is_open_door = true;
		else if (flagString.equals("is_checked_door")) is_checked_door = true;
		else System.out.println("次のフラグ名に対応するフラグはありませんでした:"+flagString);
	}

	public boolean get_is_open_door () {
		return is_open_door;
	}

	public boolean isGetMagnet() {
		return this.is_get_magnet;
	}
}
