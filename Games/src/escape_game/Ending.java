package escape_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class Ending {
	private Image endCard = null;
	private Mainpro mainpro;
	private int drawScreenX, drawScreenY;
	/**
	 * 文字列の書き出し位置
	 */
	private int X_drawStart, Y_drawStart;
	private int fontSize;
	private String font;
	/**
	 * 文字列と文字列の上下の間隔
	 */
	private int interval;
	/**
	 * エンドカードを表示する閾値(最後の「制作：ゲー研」が上に消えてちょっとしてから)
	 */
	private int threshold_Endcard;
	
	private Sequencer bgmEndroll;
	private Sequence sequence_Endroll;
	private final String path_BgmEndroll = "../sound_data/game_maoudamashii_3_theme13.mid";
	public Sequencer getBgmEndroll() { return bgmEndroll; }
	private Sequencer bgmEndcard;
	private Sequence sequence_Endcard;
	private final String path_BgmEndcard = "../sound_data/game_maoudamashii_9_jingle01.mid";
	public Sequencer getBgmEndcard() { return bgmEndcard; }
	private boolean playEndroll;
	public boolean getPlayEndroll() { return playEndroll; }
	private boolean playEndcard;
	public boolean getPlayEndcard() { return playEndcard; }
	
	private final int speed = 3;
	private final String KIKAKU = "企画：/Ultimate D.T";
	private final String SENARIO = "シナリオ：/Ultimate D.T";
	private final String DESIGN_CHARACTER = "キャラクターデザイン：/Ultimate D.T";
	private final String DESIGN_OBJECT = "オブジェクトデザイン：/Ultimate D.T/R.M";
	private final String BACKGROUND = "背景：/Ultimate D.T/R.M";
	private final String PROGRAM = "プログラム：/Ultimate D.T/NNECCO";
	private final String BGM_TITLE = "タイトルBGM:/〇";
	private final String BGM_ENDING = "エンディングBGM:/〇";
	private final String SOUND_EFFECT = "サウンドエフェクト：/〇";
	private final String SEISAKU = "制作：/ゲー研";
	private final String[] List_str = {KIKAKU, SENARIO, DESIGN_CHARACTER, DESIGN_OBJECT, BACKGROUND,
										PROGRAM, BGM_TITLE, BGM_ENDING, SOUND_EFFECT, SEISAKU};
			
	Ending(Mainpro mainpro, int drawScreenX, int drawScreenY) {
		this.mainpro = mainpro;
		this.drawScreenX = drawScreenX;
		this.drawScreenY = drawScreenY;
	}

	public void init() throws Exception {
		X_drawStart = 125;
		Y_drawStart = drawScreenY+100;
		fontSize = 20;
		font = "MS明朝";
		interval = fontSize*3;
		//間隔*間隔の数+改行の数*フォントサイズ+(Y_drawStart - drawScreenY)+ちょい余裕
		threshold_Endcard = interval * List_str.length + (13+1)*fontSize + 150;
		bgmEndroll = MidiSystem.getSequencer();
		sequence_Endroll = MidiSystem.getSequence(new File(path_BgmEndroll));
		bgmEndroll.setSequence(sequence_Endroll);
		bgmEndroll.open();
		bgmEndcard = MidiSystem.getSequencer();
		sequence_Endcard = MidiSystem.getSequence(new File(path_BgmEndcard));
		bgmEndcard.setSequence(sequence_Endcard);
		bgmEndcard.open();
		playEndroll = false;
		playEndcard = false;
	}
	
	public void paint(Mainpro mainpro) {
		if(Y_drawStart < -threshold_Endcard) {
			if(!playEndcard) {
				mainpro.screenMode = "EndCard";
				playEndroll = false;
				bgmEndroll.stop();
				bgmEndcard.start();
				playEndcard = true;
			}
			mainpro.buffer.drawImage(endCard, 0, 0, drawScreenX, drawScreenY, mainpro);
		}
		else {
			if(!playEndroll) {
				bgmEndroll.start();
				playEndroll = true;
			}
			
			//背景を黒色にする
			mainpro.buffer.fillRect(0, 0, drawScreenX, drawScreenY);
			mainpro.buffer.setFont(new Font(font, Font.PLAIN, fontSize));
			mainpro.buffer.setColor(Color.WHITE);
			
			String str;
			String[] ss;
			//改行の数
			int brNum = 0;
			//描画の基準であるY_drawStartからどれだけ離れた位置で書き始めるかの値
			int brInterval;
			//エンドロールの描画
			for(int i=0; i<List_str.length; i++) {
				str = List_str[i];
				ss = str.split("/");
				brInterval = brNum * fontSize;
				for(int j=0; j<ss.length; j++) {
					mainpro.buffer.drawString(ss[j], X_drawStart, Y_drawStart + i*interval + fontSize*j + brInterval);
						brNum++;
				}
			}
			//文字列を画面の上へ流していく
			Y_drawStart -= speed;
		}
	}

	public void setEndingImage() {
		endCard = mainpro.getImage(mainpro.getCodeBase(), "../material_data/escape_game/other/endcard.png");
	}

}
