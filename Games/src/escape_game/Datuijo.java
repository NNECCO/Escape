package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Datuijo extends Field{

	private Image datuijo;
	private Image datuijo2; //tapなし
	private Image noDoor;
	private Image door1; // 風呂場<-> datuijo
	private Image door2; // datuijo <-> DkTop
	private Image mirror1;
	private Image mirror2; // broken
	private Image card;

	boolean openDoor1; // 風呂場 <-> datuijo
	boolean openDoor2; // datuijo <-> DkTop
	//boolean brokenMirror;
	boolean latm; //鏡を見ている
	boolean setTap;
	boolean fan; //換気扇
	public boolean getFan() { return fan; }

	private Random rnd = new Random(); //お遊び用

	Mainpro mainpro;

	public Datuijo(Mainpro mainpro) {
		this.mainpro = mainpro;
		//this.brokenMirror = false;
		this.latm = false;
		this.setTap = false;
		this.fan = false;
	}

	public void paint(Mainpro mainpro) {
		if(latm) {
			/*if(!brokenMirror) {
				mainpro.buffer.drawImage(mirror1, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
			} else {
				mainpro.buffer.drawImage(mirror2, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
			}*/
			mainpro.buffer.drawImage(mirror1, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
			if(mainpro.message.get(0).equals("nothing")) {
				latm = false;
			}
		}

		super.paint(mainpro);
	}

	@Override
	boolean is_character_in(int cx, int cy) {
		String here = here(cx, cy);
		if(here.equals("inside")) {
			return true;
		} else if(here.equals("Datuijo->DkTop")) {
			return true;
		} else if(here.equals("Datuijo->Bathroom")) {
			return true;
		}
		return false;
	}

	@Override
	String here(int cx, int cy) {
		if(120 <= cx && cx <= 250 && cy < 130) {
			return "senmenjo";
		} else if(240 < cx && cy <= 220) {
			return "sentakuki";
		} else if(cx < 120 && 130 <= cy && cy <= 150) {
			return "fan";
		} else if(350 <= cx && 230 <= cy) {
			return "Datuijo->DkTop";
		} else if(cx < 120 && 200 <= cy && cy <= 250) {
			return "Datuijo->Bathroom";
		} else if(120 <= cx && cx < 350 && 50 <= cy && cy <= 300) {
			return "inside";
		}
		return "wall";
	}

	@Override
	void examine_result(String examine_point) {
		if(examine_point.equals("senmenjo")) {
			//蛇口が付いている場合は鏡を見る
			if(setTap) latm = true;
		} else if(examine_point.equals("fan")) {
			//換気扇のスイッチのON/OFF
			fan = !fan;
		}
	}

	@Override
	void examine_effect(String examine_point, String item) {
		switch (examine_point) {
			case "fan":
				mainpro.message_add("(カチッ)");
				break;
			case "senmenjo":
				//蛇口が付いている場合とそうでない場合で分ける
				if(setTap) { //付いている場合
					//バッグから床下で手に入れた紙を使用した場合
					if(item.equals(IconName.HINT)) {
						mainpro.message_add("(ジャーーーッ)");
						mainpro.message_add("この紙、試しに濡らしてみるか");
						mainpro.message_add("・・・");
						mainpro.message_add("・・・・・・");
						mainpro.message_add("文字が浮かび上がってまいりましたーー!!");
						mainpro.message_add("(謎の紙を手に入れた!)");
						//何かのヒントをバッグから削除
						mainpro.bag.remove(IconName.HINT);
						//ヒントのヒントをバッグに追加
						mainpro.bag.add(IconName.HINT_OF_HINT);
					} else {
						//確率で水飲みイベント
						if(0.2 <= rnd.nextDouble()) {
							examine_result(examine_point);
							mainpro.message_add("(鏡の中に何かのカードがある)");
							mainpro.message_add("(遊〇王カード??)");
							mainpro.message_add("(こんなカードなかったよな?)");
						} else {
							mainpro.message_add("(ジャーーーーッ)");
							mainpro.message_add("水UMAAA!（^o^）");
						}
					}
				} else { //付いていない場合
					//バッグから蛇口を使用した場合
					if("蛇口".equals(item)) {
						mainpro.message_add("台所の蛇口が取りつけられる！");
						mainpro.message_add("(蛇口を取り付けた!)");
						mainpro.bag.remove("蛇口");
						this.setTap = true;
					} else {
						mainpro.message_add("この洗面所、蛇口がない・・・");
						mainpro.message_add("(水分が欲しい（'□'）)");
						if(rnd.nextDouble() <= 0.2 && mainpro.bag.contains("ツナ缶")) {
							mainpro.message_add("冷蔵庫にはまだツナ缶あったよな");
							mainpro.message_add("（　＾ω＾）・・・");
							mainpro.message_add("ツナ缶で水分を補充じゃあああ!!!");
							mainpro.message_add("(ツナ缶を消費しました)");
							mainpro.bag.remove("ツナ缶");
						}
					}
				}
				break;
			case "sentakuki":

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
		//if(!brokenMirror) return "鏡に何か映ってる";
		if(!setTap && !mainpro.bag.contains("蛇口")) return "水が飲みたい（'△'）";
		else if(!setTap && mainpro.bag.contains("蛇口")) return "蛇口を使うとしたら水場ぐらいか・・・";
		else if(setTap && mainpro.bag.contains("何かのヒント")) return "せっかく水が使えるようになったし、何か試してみるか";
		return "ここにはもう何もなさそうだ";
	}

	@Override
	void showMap(ImageObserver field) {
		if(this.setTap) {
			mainpro.buffer.drawImage(datuijo, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		} else {
			mainpro.buffer.drawImage(datuijo2, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		}


	}

	@Override
	void setImages(URL codeBase) {
		this.datuijo = mainpro.getImage(codeBase,"../material_data/escape_game/datuijo/datuijo.png");
		this.datuijo2 = mainpro.getImage(codeBase,"../material_data/escape_game/datuijo/datuijo2.png");
		this.noDoor = mainpro.getImage(codeBase,"../material_data/escape_game/datuijo/noDoor.png");
		this.card = mainpro.getImage(codeBase,"../material_data/escape_game/datuijo/card.png");
		this.door1 = mainpro.getImage(codeBase,"../material_data/escape_game/datuijo/door1.png");
		this.door2 = mainpro.getImage(codeBase,"../material_data/escape_game/datuijo/door2.png");
		this.mirror1 = mainpro.getImage(codeBase,"../material_data/escape_game/datuijo/mirror1.png");
		this.mirror2 = mainpro.getImage(codeBase,"../material_data/escape_game/datuijo/mirror2.png");
	}

	@Override
	public String toString() {
		return "Datuijo";
	}

	@Override
	void setFlagTrue(String flagString) {
		if (flagString.equals("openDoor1")) {
			openDoor1 = true;
		} else if (flagString.equals("openDoor2")) {
			openDoor2 = true;
		} else if (flagString.equals("latm")) {
			latm = true;
		} else if (flagString.equals("setTap")) {
			setTap = true;
		} else if (flagString.equals("fan")) {
			fan = true;
		}
	}

	@Override
	ArrayList<String> getTrueFlags() {
		ArrayList<String> trueFlags = new ArrayList<String>();
		if (openDoor1) {
			trueFlags.add("openDoor1");
		}
		if (openDoor2) {
			trueFlags.add("openDoor2");
		}
		/*if (brokenMirror) {
			trueFlags.add("brokenMirror");
		}*/
		if (latm) {
			trueFlags.add("latm");
		}
		if (setTap) {
			trueFlags.add("setTap");
		}
		if (fan) {
			trueFlags.add("fan");
		}
		return trueFlags;
	}
}
