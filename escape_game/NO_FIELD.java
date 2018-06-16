package escape_game;

import java.awt.image.ImageObserver;
import java.net.URL;

public class NO_FIELD extends Field {
	
	@Override
	boolean is_character_in(int cx, int cy) {
		return false;
	}
	
	void examine_result(String ex_result) {
	}
	
	public String toString() {
		return "no_field";
	}

	@Override
	void examine_effect(String examine_point, String item) {	
	}

	@Override
	boolean final_text() {
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
	String here(int cx, int cy) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	String return_text() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	void setFlagTrue(String flagString) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
