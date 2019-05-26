package escape_game;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class InputDialog {
	private int centerX, centerY;
	private int width, height;
	private int startX, startY;
	private int endX, endY;
	private Graphics graphics;
	private Button yesBtn;
	private Button noBtn;
	private Font textFont;

	private Applet applet;

	private KeyListener keyListener;
	private MouseListener mouseListener;

	private static Font mb50 = new Font("MS明朝", Font.BOLD, 50);
	private static Font mb30 = new Font("MS明朝", Font.BOLD, 30);
	private static Font mb20 = new Font("MS明朝", Font.BOLD, 20);
	private static Font mb15 = new Font("MS明朝", Font.BOLD, 15);
	private static Font mb10 = new Font("MS明朝", Font.BOLD, 10);

	private static Font mp20 = new Font("MS明朝", Font.PLAIN, 20);

	private static int screenSizeX = 500;
	private static int screenSizeY = 500;
	private static final int BUTTON_INVISIBLE_OFFSET = 2000;

	private String[] selections = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private int[] selectionIndex = { 0, 0, 0, 0, 0 };
	private String[] answers = null;

	private String title = null;
	private Image titleImg = null;
	private ArrayList<Button> upperBtns = null;
	private ArrayList<Button> lowerBtns = null;

	// 入力ダイアログを表示しているか
	private boolean isInputDialogShown = false;

	// ダイアログ縦分割数(例:構成要素がタイトルとボタンのみ⇒2)
	private int separateNums = 1;

	// 選択肢を含むタイトルか
	private boolean isSelectionShown = false;

	// ボタン押下時にコールされるインタフェース
	private ResultListener resultListener;

	interface ResultListener {
		void ok();
		void cancel();
	}

	/**
	 *
	 * @param applet
	 * @param graphics
	 * @param centerX ダイアログ中央x座標
	 * @param centerY ダイアログ中央y座標
	 * @param width ダイアログ幅
	 * @param height ダイアログ縦
	 * @param title ダイアログのタイトル
	 * @param titleImg ダイアログタイトルの画像
	 * @param answers ダイアログの選択肢回答
	 */
	InputDialog(Applet applet, Graphics graphics, MouseListener mouseListener, KeyListener keyListener,
			int centerX, int centerY, int width, int height, String title, Image titleImg, String[] answers) {
		// フィールドの設定
		this.applet = applet;
		this.graphics = graphics;
		this.mouseListener = mouseListener;
		this.keyListener = keyListener;

		this.width = width;
		this.height = height;
		updateComponentStatus(centerX, centerY);

		this.title = title;
		this.titleImg = titleImg;
		this.answers = answers;

		// ダイアログの分割数の設定
		updateSeparateNums();

		// ボタンの設定
		createSelectBtns();

		this.yesBtn = new Button("決定");
		this.noBtn = new Button("キャンセル");
		applet.add(yesBtn);
		applet.add(noBtn);
		yesBtn.addKeyListener(keyListener);
		yesBtn.addMouseListener(mouseListener);
		noBtn.addKeyListener(keyListener);
		noBtn.addMouseListener(mouseListener);

		applet.add(yesBtn);
		applet.add(noBtn);

		setAllBtnVisibility(false);
	}

	public void updateScreenSize(int x, int y) {
		screenSizeX = x;
		screenSizeY = y;
	}

	public void onKeyPress(KeyEvent e) {
		negativeBtnSelected();
	}

	public void onClick(MouseEvent e) {
		if (e.getSource() == yesBtn) {
			positiveBtnSelected();
		} else if (e.getSource() == noBtn) {
			negativeBtnSelected();
		} else {
			for (int index = 0; index < upperBtns.size(); index++) {
				if (e.getSource() == upperBtns.get(index)) {
					upperBtnSelected(index);
					break;

				} else if (e.getSource() == lowerBtns.get(index)) {
					lowerBtnSelected(index);
					break;
				} else if (index == upperBtns.size() - 1) {
					negativeBtnSelected();
					break;
				}
			}
		}
	}

	private void positiveBtnSelected() {
		isInputDialogShown = false;
		setAllBtnVisibility(false);
		if (resultListener == null) {
			System.out.println("error: not init ResultListener");
			return;
		}
		resultListener.ok();
	}

	private void negativeBtnSelected() {
		isInputDialogShown = false;
		setAllBtnVisibility(false);
		if (resultListener == null) {
			System.out.println("error: not init ResultListener");
			return;
		}
		resultListener.cancel();
	}

	private void upperBtnSelected(int index) {
		selectionIndex[index]++;
		if (selectionIndex[index] > selections.length - 1) {
			selectionIndex[index] = 0;
		}
	}

	private void lowerBtnSelected(int index) {
		selectionIndex[index]--;
		if (selectionIndex[index] < 0) {
			selectionIndex[index] = selections.length - 1;
		}
	}

	// 入力ダイアログを表示
	public void show(Graphics graphics) {
		;
		if (this.graphics == null) {
			this.graphics = graphics;
		}

		if (upperBtns != null && lowerBtns != null && !isInputDialogShown) {
			isSelectionShown = true;
			setAllBtnVisibility(true);

			// ダイアログのボタン表示
			updateAnsterBtnsStatus();
		}
		if (!getIsInputDialogShown()) {
			isInputDialogShown = true;
		}

		// ダイアログの枠を生成
		createDialogField();

		showTitle();
		showTitleImg();
		showSelectionBtns();

		cleanSetup();
	}

	// ダイアログの中心点を更新
	public void updateComponentStatus(int centerX, int centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
		calculateEdgePoint();
	}

	// ダイアログの描画開始点を求めて、更新
	private void calculateEdgePoint() {
		this.startX = centerX - width / 2;
		this.startY = centerY - height / 2;
		this.endX = startX + width;
		this.endY = startY + height;
	}

	// ダイアログのベースを生成する
	private void createDialogField() {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(startX, startY, width, height);

		graphics.setColor(Color.BLACK);
		graphics.drawRect(startX, startY, width, height);
	}

	// ボタンの表示設定を更新
	private void updateAnsterBtnsStatus() {
		int colNums = (isSelectionShown == true ? 2 : 1);
		yesBtn.setLocation(startX, endY - height / separateNums);
		yesBtn.setSize(width / colNums, height / separateNums);
		yesBtn.setFont(getTextFont());

		if (isSelectionShown == true) {
			noBtn.setLocation(startX + width / 2, endY - height / separateNums);
			noBtn.setSize(width / 2, height / separateNums);
			noBtn.setFont(getTextFont());
		}

	}

	public boolean getIsInputDialogShown() {
		return isInputDialogShown;
	}

	private Font getTextFont() {
		if (textFont != null) {
			return textFont;
		}
		int forCompSize = height / separateNums;

		if (forCompSize > 200) {
			return mb50;
		} else if (forCompSize > 120) {
			return mb30;
		} else if (forCompSize > 80) {
			return mb20;
		} else if (forCompSize > 60) {
			return mb15;
		}

		return mb10;
	}

	public void setTextFont(Font textFont) {
		this.textFont = textFont;
	}

	// 描画情報の初期化
	void cleanSetup() {
		graphics.setFont(mp20);
		graphics.setColor(Color.black);
	}

	// タイトル表示
	private void showTitle() {
		if (title != null) {
			graphics.setFont(getTextFont());
			graphics.drawString(title, startX, startY + 20);
		}
	}

	// 画像表示
	private void showTitleImg() {
		if (titleImg != null) {
			int titleStartY = startY;
			if (title != null) {
				titleStartY += height / separateNums;
			}
			graphics.drawImage(titleImg, startX, titleStartY, width, height / separateNums, applet);
		}
	}

	// 選択ボタン表示
	private void showSelectionBtns() {
		if (upperBtns != null && lowerBtns != null) {
			int widthSeparateNums = answers.length;
			for (int selectBtnIndex = 0; selectBtnIndex < answers.length; selectBtnIndex++) {
				// 上ボタン
				Button upperBtn = upperBtns.get(selectBtnIndex);
				upperBtn.setLocation(startX + width / widthSeparateNums * (selectBtnIndex),
						startY + (height / separateNums));
				upperBtn.setSize(width / widthSeparateNums, height / separateNums / 3);
				upperBtn.setLabel("Up");

				// 下ボタン
				Button lowerBtn = lowerBtns.get(selectBtnIndex);
				lowerBtn.setLocation(startX + width / widthSeparateNums * (selectBtnIndex),
						startY + (height / separateNums) + (height / separateNums) / 3 * 2);
				lowerBtn.setSize(width / widthSeparateNums, height / separateNums / 3);
				lowerBtn.setLabel("Down");

				// 選択テキスト
				graphics.setFont(mb15);
				graphics.drawString(selections[selectionIndex[selectBtnIndex]] , startX + width / widthSeparateNums * (selectBtnIndex) + (width / widthSeparateNums)/2,
						startY + (height / separateNums) + (height / separateNums) / 3 * 2 );
			}
		}
	}

	// 選択ボタンの生成
	private void createSelectBtns() {
		if (applet != null && answers != null) {
			if (upperBtns == null) {
				upperBtns = new ArrayList<Button>();
			} else {
				upperBtns.clear();
			}

			if (lowerBtns == null) {
				lowerBtns = new ArrayList<Button>();
			} else {
				lowerBtns.clear();
			}
			for (int selectBtnIndex = 0; selectBtnIndex < answers.length; selectBtnIndex++) {
				upperBtns.add(new Button());
				Button upperBtn = upperBtns.get(selectBtnIndex);
				upperBtn.addKeyListener(keyListener);
				upperBtn.addMouseListener(mouseListener);
				applet.add(upperBtn);

				lowerBtns.add(new Button());
				Button lowerBtn = lowerBtns.get(selectBtnIndex);
				lowerBtn.addKeyListener(keyListener);
				lowerBtn.addMouseListener(mouseListener);
				applet.add(lowerBtn);
			}
		}
	}

	// ダイアログの縦分割数を更新
	private void updateSeparateNums() {
		separateNums = 1;
		if (title != null) {
			separateNums++;
		}
		if (titleImg != null) {
			separateNums++;
		}
		if (answers != null) {
			separateNums++;
		}
	}

	public void setResultListener(ResultListener resultListener) {
		this.resultListener = resultListener;
	}

	public void setSelections(String[] selections) {
		this.selections = selections;
	}

	public void setTitle(String title) {
		this.title = title;
		updateSeparateNums();
	}

	public void setTitleImg(Image titleImg) {
		this.titleImg = titleImg;
		updateSeparateNums();
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
		updateSeparateNums();
		createSelectBtns();
	}

	private void setAllBtnVisibility(boolean isVisible) {
		if (isVisible) {
			yesBtn.setVisible(isVisible);
			noBtn.setVisible(isVisible);
			if (applet != null && answers != null) {
				for (int selectBtnIndex = 0; selectBtnIndex < answers.length; selectBtnIndex++) {
					if (upperBtns != null) {
						upperBtns.get(selectBtnIndex).setVisible(isVisible);
					}
					if (lowerBtns != null) {
						lowerBtns.get(selectBtnIndex).setVisible(isVisible);
					}
				}
			}
		} else {
			yesBtn.setLocation(screenSizeX + BUTTON_INVISIBLE_OFFSET, screenSizeY + BUTTON_INVISIBLE_OFFSET);
			noBtn.setLocation(screenSizeX + BUTTON_INVISIBLE_OFFSET, screenSizeY + BUTTON_INVISIBLE_OFFSET);
			for (int selectBtnIndex = 0; selectBtnIndex < answers.length; selectBtnIndex++) {
				if (upperBtns != null) {
					Button upperBtn = upperBtns.get(selectBtnIndex);
					upperBtn.setLocation(screenSizeX + BUTTON_INVISIBLE_OFFSET, screenSizeY + BUTTON_INVISIBLE_OFFSET);
				}
				if (lowerBtns != null) {
					Button lowerBtn = lowerBtns.get(selectBtnIndex);
					lowerBtn.setLocation(screenSizeX + BUTTON_INVISIBLE_OFFSET, screenSizeY + BUTTON_INVISIBLE_OFFSET);
				}
			}
		}
	}

	// ダイアログを非表示にする
	public void clearDialog() {
		setAllBtnVisibility(false);
	}

	public void resetSelectionIndex(int selectionIndexSize) {
		this.selectionIndex = new int[selectionIndexSize];
		for (int index = 0; index < selectionIndex.length; index++) {
			selectionIndex[index] = 0;
		}
	}

	public int[] getSelectionIndex() {
		return selectionIndex;
	}

	public String[] getSelections() {
		return selections;
	}

	public String[] getAnswers() {
		return answers;
	}

}
