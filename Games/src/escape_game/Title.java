package escape_game;

import java.awt.Color;
import java.util.ArrayList;

public class Title{
	/**
	 * 
	 */
	private ArrayList<String> title_name = new ArrayList<String>();
	private boolean is_first_called = true;
	
	public void paint(Mainpro mainpro) {
		if (is_first_called == true) {
			ArrayList<String> s1 = mainpro.message_separate("気が付いたら", 18);
			ArrayList<String> s2 = mainpro.message_separate("部屋にいました", 18);
			for (int i = 0;i < s1.size();i++) {
				title_name.add(s1.get(i));
			}
			for (int i = 0;i < s2.size();i++) {
				title_name.add(s2.get(i));
			}
			is_first_called = false;
		}
		//タイトル画面を描画
		mainpro.buffer.setFont(mainpro.mb50);
		for (int i = 0;i < title_name.size();i++) {
			mainpro.buffer.setColor(Color.white);
			mainpro.buffer.drawString(title_name.get(i), 30*(i+1), 200+50*i);
		}
		mainpro.buffer.setColor(Color.black);
		mainpro.buffer.setFont(mainpro.mpi20);
		mainpro.buffer.drawString("はじめから[1]", 166,300);
		mainpro.buffer.drawRect(166,280,130,20);
		mainpro.buffer.drawString("つづきから[2]", 166,330);
		mainpro.buffer.drawRect(166,310,130,20);
	}
	
	String click_where (int x,int y) {
		if (166 <= x && x <= 166 + 130 //はじめからが押された
				&& 280 <= y && y <= 280 + 20) {
			return "first";
		}
		else if (166 <= x && x <= 166 + 130 //つづきからが押された
				&& 310 <= y && y <= 310 + 20) {
			return "continue";
		}
		return "no-flags";//フラグがたつクリックでない
	}

}
