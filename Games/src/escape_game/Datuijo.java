package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;
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
	boolean brokenMirror;
	boolean latm; //鏡を見ている
	boolean setTap;
	
	private Random rnd = new Random(); //お遊び用
	
	Mainpro mainpro;
	
	public Datuijo(Mainpro mainpro) {
		this.mainpro = mainpro;
		this.brokenMirror = false;
		this.latm = false;
		this.setTap = false;
	}
	
	public void paint(Mainpro mainpro) {
		if(latm) {
			if(!brokenMirror) {
				mainpro.buffer.drawImage(mirror1, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
			} else {
				mainpro.buffer.drawImage(mirror2, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
			}
			
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
		if(190 <= cx && cx <= 210 && cy < 130) {
			return "tap";
		} else if(150 <= cx && cx <= 250 && cy < 130) {
			return "mirror";
		} else if(120 <= cx && cx <= 250 && cy < 130) {
			return "senmenjo";
		} else if(260 < cx && cy <= 220) {
			return "sentakuki";
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
		if(examine_point.equals("mirror")) {
			if(!brokenMirror) {
				latm = true;
			}
		}	
	}

	@Override
	void examine_effect(String examine_point, String item) {
		switch (examine_point) {
			case "mirror":
				if(!brokenMirror) {
					examine_result(examine_point);
					mainpro.message_add("鏡の中に何かのカードがある");
				} else {
					examine_result(examine_point);
					mainpro.message_add("割れた鏡だ");
				}
				break;
			case "tap":
				if(!this.setTap) {
					if(mainpro.bag.contains("蛇口")) {
						mainpro.message_add("台所の蛇口が取りつけられる！");
						mainpro.message_add("(蛇口を取り付けた!)");
						this.setTap = true;	
					} else {
						mainpro.message_add("この洗面所、蛇口がない・・・");
						mainpro.message_add("(水分が欲しい（'□'）)");
						if(rnd.nextDouble() <= 0.4 && mainpro.bag.contains("ツナ缶")) {
							mainpro.message_add("冷蔵庫にはまだツナ缶あったよな");
							mainpro.message_add("（　＾ω＾）・・・");
							mainpro.message_add("ツナ缶で水分を補充じゃあああ!!!");
							mainpro.message_add("(ツナ缶を消費しました)");
							mainpro.bag.remove("ツナ缶");
						}
					}
				} else {
					mainpro.message_add("(ジャーーーッ)");
					if(item.equals("何かのヒント")) {
						mainpro.message_add("この紙、試しに濡らしてみるか");
						mainpro.message_add("・・・文字が浮かび上がってまいりましたーー!!");
						mainpro.message_add("(ヒント1を手に入れた!)");
						
					} else {
						mainpro.message_add("水はいつでも飲めるな（^o^）");	
					}
				}
				break;
			case "ヒント1":
				
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
		if(!brokenMirror) return "鏡に何か映ってる";
		if(!setTap && mainpro.bag.contains("蛇口")) return "蛇口を使うとしたら水場ぐらいか・・・"; 
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
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
