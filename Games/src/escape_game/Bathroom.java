package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Bathroom extends Field{

	Image bathroom1;
	Image bathroom2;
	Image bathroom3;
	Image timer;
	
	boolean latt; //timerを見ている
	
	Mainpro mainpro;
	
	public Bathroom(Mainpro mainpro) {
		this.mainpro = mainpro;
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
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	void examine_result(String ex_result) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	void examine_effect(String examine_point, String item) {
		// TODO 自動生成されたメソッド・スタブ
		
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
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	void showMap(ImageObserver field) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	void setImages(URL codeBase) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	void setFlagTrue(String flagString) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
