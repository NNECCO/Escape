package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.ArrayList;

public class Bathroom extends Field{

	private Image bathroom;
	private Image timer;
	private Image timer_zoom;
	private Image timer_zoom2;
	private Image yuge;
	private boolean latt; //timerを見ている
	private boolean flug_timer_zoom = false;
	private Mainpro mainpro;
	private Datuijo datuijo;
	
	public Bathroom(Mainpro mainpro, Datuijo datuijo) {
		this.mainpro = mainpro;
		this.datuijo = datuijo;
	}
	
	public void paint(Mainpro mainpro) {
		if(latt) {
			if(!flug_timer_zoom) mainpro.buffer.drawImage(timer_zoom, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
			else mainpro.buffer.drawImage(timer_zoom2, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
			if(mainpro.message.get(0).equals("nothing")) {
				latt = false;
				flug_timer_zoom = true;
			}
		}
	}
	
	@Override
	boolean is_character_in(int cx, int cy) {
		String here = here(cx, cy);
		if(here.equals("inside")) {
			return true;
		} else if(here.equals("Bathroom->Datuijo")) {
			return true;
		}
		return false;
	}

	@Override
	String here(int cx, int cy) {
		if(60 <= cx && cx <= 150 && 100 <= cy && cy <= 250) {
			return "inside";
		} else if(150 <= cx && cx <= 170 && 100 <= cy && cy <= 230) {
			return "out-bathtub";
		} else if(cx < 240 && 120 <= cy && cy <= 250) {
			return "bathtub";
		} else if(cx < 240 && 250 < cy ) {
			return "bathtubside";
		} else if(410 < cx && 160 <= cy && cy <= 230) {
			return "Bathroom->Datuijo";
		} else if(cx == 60 && cy <= 115) {
			return "timer";
		} else if(cy < 120 || 290 < cy || 410 < cx) {
			return "wall";
		}
		return "inside";
	}

	@Override
	void examine_result(String ex_result) {
		if(ex_result.equals("timer")) {
			latt = true;
		}
	}

	@Override
	void examine_effect(String examine_point, String item) {
		switch(examine_point) {
			case "bathtub":
				mainpro.player_x = 150;
				mainpro.player_y = 175;
				break;
			case "timer":
				if(!flug_timer_zoom) {
					mainpro.message_add("なにこれ?");
					mainpro.message_add("解像度ひでえww");
					mainpro.message_add("(画像を修正しました!!?)");
				} else {
					mainpro.message_add("なにこれ?");
					mainpro.message_add("真ん中に2・・・何を意味しているんだ?");
				}
				break;
			case "out-bathtub":
				mainpro.player_x = 250;
				mainpro.player_y = 200;
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
		if(datuijo.getFan()) return "風呂場と言えばやはり浴槽!!";
		else return "湯気で何も見えない・・・";
	}

	@Override
	void showMap(ImageObserver field) {
		mainpro.buffer.drawImage(bathroom, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		mainpro.buffer.drawImage(timer, 70, 70, 20, 20, field);
		//脱衣所で換気扇のスイッチをONにしていなければ湯気を表示
		if(!datuijo.getFan()) mainpro.buffer.drawImage(yuge, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
	}

	@Override
	void setImages(URL codeBase) {
		bathroom = mainpro.getImage(codeBase, "../material_data/escape_game/bathroom/base.png");
		timer = mainpro.getImage(codeBase, "../material_data/escape_game/bathroom/timer.png");
		timer_zoom = mainpro.getImage(codeBase, "../material_data/escape_game/bathroom/timer_zoom.png");
		timer_zoom2 = mainpro.getImage(codeBase, "../material_data/escape_game/bathroom/timer_zoom2.png");
		yuge = mainpro.getImage(codeBase, "../material_data/escape_game/bathroom/yuge.png");
	}

	@Override
	public String toString() {
		return "Bathroom";
	}

	@Override
	void setFlagTrue(String flagString) {
		if (flagString.equals("latt")) {
			latt = true;
		} else if (flagString.equals("flug_timer_zoom")) {
			flug_timer_zoom = true;
		}
	}

	@Override
	ArrayList<String> getTrueFlags() {
		ArrayList<String> trueFlags = new ArrayList<String>();
		if (latt) {
			trueFlags.add("latt");
		}
		if (flug_timer_zoom) {
			trueFlags.add("flug_timer_zoom");
		}
		return trueFlags;
	}
}
