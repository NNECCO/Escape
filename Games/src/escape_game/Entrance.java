package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Entrance extends Field{

	Image gennkann;//扉あり
	Image gennkann2;//扉なし
	Image moyou;
	
	private boolean isCheckDoor; //最序盤、玄関の扉の調査フラグ
	private boolean opened; //玄関が開いているかどうか
	private boolean latd; //玄関の模様を見ている最中かどうか
	
	Mainpro mainpro;	
	
	Entrance(Mainpro mainpro) {
		this.mainpro = mainpro;
		this.isCheckDoor = false;
		this.opened = false;
		this.latd = false;
		//texts = new ArrayList<String>();
		/* 序盤 */
		
	}
	
	public void paint(Mainpro mainpro) {
		//玄関の模様を調べているとき
		if(latd) {
			mainpro.buffer.drawImage(moyou, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mainpro);
			if(mainpro.message.get(0).equals("nothing")) {
				latd = false;
			}
		}
		super.paint(mainpro);
	}
	
	/**
	 * あたり判定の結果を返す
	 */
	boolean is_character_in(int cx, int cy) {
		String here = here(cx, cy);
		if(here.equals("inside") || here.equals("Entrance->DkTop")) {
			return true;
		} else if(here.equals("door") && opened) {
			return true;
		} else {
			return false;			
		}
	}
	
	/**
	 * あたり判定
	 */
	String here(int cx, int cy) {
		if(150 <= cx && cx <= 270 && cy < 80) {
			return "door";
		} else if(150 <= cx && cx <= 310 && 300 <= cy) {
			return "Entrance->DkTop";
		} else if(cx < 130 && cy < 270) { 
			return "getabako";
		} else if(10 <= cx && cx <= 410 && 80 <= cy && cy + mainpro.character_size_y <= 360) {
			return "inside";
		}
		return "wall";
	}

	@Override
	void examine_result(String examine_point) {
		if(examine_point.equals("door")) {
			if(!opened) {
				latd = true;
			}
		}
	}

	@Override
	void examine_effect(String examine_point, String item) {
		if(examine_point.equals("door")) {
			if(!opened && !isCheckDoor) {
				examine_result(examine_point);
				mainpro.message_add("・・・・・開かない");
				mainpro.message_add("この扉の模様・・・何かしないと開かないってわけか");
				mainpro.message_add("この家を調べよう");
				isCheckDoor = true;
			} else {
				examine_result(examine_point);
				mainpro.message_add("この家を調べよう");
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
		if(!this.getIsCheckDoor()) {
			message = "玄関だ!";
		} else if(this.getIsCheckDoor()) {
			message = "扉のあの模様が手掛かりだよな・・・";
		}
		return message;
	}

	@Override
	void showMap(ImageObserver mapr) {
		if(!opened) {
			mainpro.buffer.drawImage(gennkann, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mapr);
		} else {
			mainpro.buffer.drawImage(gennkann2, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, mapr);			
		}

	}

	@Override
	void setImages(URL codeBase) {
		gennkann = mainpro.getImage(codeBase, "../material_data/escape_game/entrance/gennkann.png");		
		gennkann2 = mainpro.getImage(codeBase, "../material_data/escape_game/entrance/gennkann2.png");
		moyou = mainpro.getImage(codeBase, "../material_data/escape_game/entrance/moyou.png");
	}

	@Override
	public String toString() {
		return "Entrance";
	}

	public boolean getIsCheckDoor() {
		return isCheckDoor;
	}

	public void setIsCheckDoor(boolean isCheckDoor) {
		this.isCheckDoor = isCheckDoor;
	}

	@Override
	String return_text() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	void setFlagTrue(String flagString) {
		if (flagString.equals("isCheckDoor")) isCheckDoor = true;
		else if (flagString.equals("opened")) opened = true;
		else if (flagString.equals("latd")) latd = true;
		else System.out.println("次のフラグ名に対応するフラグはありませんでした:"+flagString);
	}

}
