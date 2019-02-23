package escape_game;

import java.awt.Color;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.ArrayList;

public abstract class Field  {
	int text_index = 0;
	protected ArrayList<String> texts;
	
	public void paint(Mainpro mainpro) {
		if (mainpro.is_show_text == true) { //テキストの表示
			mainpro.buffer.setColor(Color.black);
			mainpro.buffer.drawRect(10, mainpro.screen_size_y-90, mainpro.screen_size_x-20, 80);
			mainpro.buffer.setColor(Color.white);
			mainpro.buffer.fillRect(10, mainpro.screen_size_y-90, mainpro.screen_size_x-20, 80);
			mainpro.clean_setup();
			mainpro.buffer.drawString(mainpro.text, 20, mainpro.screen_size_y-50);
		}
	}
	
	/**
	 * あたり判定の結果を返す
	 * @param cx
	 * @param cy
	 * @return 
	 */
	abstract boolean is_character_in (int cx,int cy);
	
	/**
	 * あたり判定
	 * @param cx
	 * @param cy
	 * @return
	 */
	abstract String here(int cx,int cy);
	
	/**
	 * 調べた結果起こる現象
	 * 例:机を調べると机が動く等
	 * @param ex_result
	 */
	abstract void examine_result (String ex_result);
	
	/**
	 * 調べた場所に何かある
	 * @param examine_point
	 */
	abstract void examine_effect(String examine_point, String item);
	
	/**
	 * テキストインデックス(text_index)に対応するテキストを返す	
	 * @return
	 */
	abstract String return_text();
	
	/**
	 * その画面に置ける最後のテキストだった場合、trueを返す
	 * @return
	 */
	abstract boolean final_text();
	
	/**
	 * 進行のヒントを示す
	 * @return
	 */
	abstract String hint();
	
	/**
	 * マップのimageを描画する
	 * @param field
	 */
	abstract void showMap(ImageObserver field);
	
	/**
	 * マップのimageの変数に画像を格納する
	 * @param codeBase
	 */
	abstract void setImages(URL codeBase);
	
	/**
	 * 部屋名を返す
	 */
	public abstract String toString();
	
	/**
	 * flagの値をtrueにする
	 */
	abstract void setFlagTrue(String flagString);
}
