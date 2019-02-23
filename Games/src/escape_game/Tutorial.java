package escape_game;

import java.awt.Color;
import java.util.ArrayList;

public class Tutorial{
	
	private ArrayList<String> texts = new ArrayList<String>();
	public int text_index = -1;
	
	public Tutorial() {
		texts.add("...zz......zzZ...");
		texts.add("...zz......はっ!");
		texts.add("...ここは？");
	}
	
	public void paint(Mainpro mainpro) {
		draw_textbox(mainpro);
	}
	
	public void draw_textbox(Mainpro mainpro) {
		//テキストボックスの表示
		mainpro.buffer.setColor(Color.black);
		mainpro.buffer.fillRect(0, 0, mainpro.screen_size_x, mainpro.screen_size_y);
		mainpro.buffer.setColor(Color.white);
		mainpro.buffer.fillRect(10, mainpro.screen_size_y-90, mainpro.screen_size_x-20, 80);
		mainpro.clean_setup();
		//テキストボックス
		if (mainpro.is_next == true) {
			mainpro.is_next = false;
		}
		mainpro.buffer.drawString(mainpro.text, 20, mainpro.screen_size_y-50);
	}
	String return_text() {//テキストインデックス(text_index)に対応するテキストを返す
		return texts.get(text_index);
	}
	
	boolean final_text() {//その画面に置ける最後のテキストだった場合、trueを返す
		if (text_index == texts.size()-1) return true; 
		return false;
	}
	
}
