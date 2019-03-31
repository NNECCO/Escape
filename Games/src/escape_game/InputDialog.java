package escape_game;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

public class InputDialog {
	private int mCenterX, mCenterY;
	private int mWidth, mHeight;
	private int mStartX, mStartY;
	private int mEndX, mEndY;
	private Graphics mGraphics;
	private Button mYesBtn;
	private Button mNoBtn;
	private Font mTextFont;

	private Applet mApplet;

	private static Font mb50 = new Font("MS明朝", Font.BOLD, 50);
	private static Font mb30 = new Font("MS明朝", Font.BOLD, 30);
	private static Font mb20 = new Font("MS明朝", Font.BOLD, 20);
	private static Font mb15 = new Font("MS明朝", Font.BOLD, 15);
	private static Font mb10 = new Font("MS明朝", Font.BOLD, 10);

	private static Font mp20 = new Font("MS明朝", Font.PLAIN, 20);

	private String[] mSelections = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	private String[] mAnswers = null;

	private String mTitle = null;
	private Image mTitleImg = null;
	private ArrayList<JComboBox<String>> mSelectBtns = null;

	// 入力ダイアログを表示しているか
	private boolean mIsInputDialogShown = false;

	// ダイアログ縦分割数(例:構成要素がタイトルとボタンのみ⇒2)
	private int mSeparateNums = 1;

	// 選択肢を含むタイトルか
	private boolean mIsSelectionShown = false;

	// ボタン押下時にコールされるインタフェース
	private ResultListener mResultListener;

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
	InputDialog(Applet applet, Graphics graphics, int centerX, int centerY, int width, int height,
					String title, Image titleImg, String[] answers) {
		// フィールドの設定
		this.mApplet = applet;
		this.mGraphics = graphics;
		this.mWidth = width;
		this.mHeight = height;
		updateComponentStatus(centerX, centerY);

		this.mTitle = title;
		this.mTitleImg = titleImg;
		this.mAnswers = answers;

		// ダイアログの分割数の設定
		updateSeparateNums();

		// ボタンの設定
		createJComboBoxBtns();

		this.mYesBtn = new Button("決定");
		this.mNoBtn = new Button("キャンセル");
		mApplet.add(mYesBtn);
		mApplet.add(mNoBtn);

		// mYesBtn.addKeyListener(keyListener);
		// mNoBtn.addKeyListener(keyListener);

		mYesBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mIsInputDialogShown = false;
				setAllBtnVisibility(false);
				setAllBtnIsEnable(false);
				if (mResultListener == null) {
					System.out.println("error: not init ResultListener");
					return;
				}
				mResultListener.ok();
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		mNoBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mIsInputDialogShown = false;
				setAllBtnVisibility(false);
				setAllBtnIsEnable(false);
				if (mResultListener == null) {
					System.out.println("error: not init ResultListener");
					return;
				}
				mResultListener.cancel();
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		setAllBtnVisibility(false);
		setAllBtnIsEnable(false);
	}

	// 入力ダイアログを表示
	public void show(Graphics graphics) {;
		if (this.mGraphics == null) {
			this.mGraphics = graphics;
		}

		if (mSelectBtns != null && !mIsInputDialogShown) {
			mIsSelectionShown = true;
			setAllBtnVisibility(true);
			setAllBtnIsEnable(true);

			// ダイアログのボタン表示
			updateAnsterBtnsStatus();
		}
		if (!getIsInputDialogShown()) {
			mIsInputDialogShown = true;
		}


		// ダイアログの枠を生成
		createDialogField();


		showTitle();
		showTitleImg();
		showSelectionBtns();
		// showAnswerBtns();

		cleanSetup();
	}

	// ダイアログの中心点を更新
	public void updateComponentStatus(int centerX, int centerY) {
		mCenterX = centerX;
		mCenterY = centerY;
		calculateEdgePoint();
	}

	// ダイアログの描画開始点を求めて、更新
	private void calculateEdgePoint() {
		mStartX = mCenterX - mWidth / 2;
		mStartY = mCenterY - mHeight / 2;
		mEndX = mStartX + mWidth;
		mEndY = mStartY + mHeight;
	}

	// ダイアログのベースを生成する
	private void createDialogField() {
		mGraphics.setColor(Color.WHITE);
		mGraphics.fillRect(mStartX, mStartY, mWidth, mHeight);

		mGraphics.setColor(Color.BLACK);
		mGraphics.drawRect(mStartX, mStartY, mWidth, mHeight);
	}

	// ボタンの表示設定を更新
	private void updateAnsterBtnsStatus() {
		int colNums = (mIsSelectionShown == true ? 2 : 1);
		mYesBtn.setLocation(mStartX, mEndY - mHeight/mSeparateNums);
		mYesBtn.setSize(mWidth/colNums, mHeight/mSeparateNums);
		mYesBtn.setFont(getTextFont());

		if (mIsSelectionShown == true) {
			mNoBtn.setLocation(mStartX + mWidth/2, mEndY - mHeight/mSeparateNums);
			mNoBtn.setSize(mWidth/2, mHeight/mSeparateNums);
			mNoBtn.setFont(getTextFont());
		}

	}

	// 決定ボタンとキャンセルボタンを表示
	private void showAnswerBtns() {
		mYesBtn.setEnabled(true);

		if (mIsSelectionShown == true) {
			mNoBtn.setEnabled(true);
		}
	}

	public boolean getIsInputDialogShown() {
		return mIsInputDialogShown;
	}


	private Font getTextFont() {
		if (mTextFont != null) {
			return mTextFont;
		}
		int forCompSize = mHeight/mSeparateNums;

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
		this.mTextFont = textFont;
	}

	// 描画情報の初期化
	void cleanSetup() {
		mGraphics.setFont(mp20);
		mGraphics.setColor(Color.black);
	}

	// タイトル表示
	private void showTitle() {
		if (mTitle != null) {
			mGraphics.setFont(getTextFont());
			mGraphics.drawString(mTitle, mStartX, mStartY + 20);
		}
	}

	// 画像表示
	private void showTitleImg() {
		if (mTitleImg != null) {
			int startY = mStartY;
			if (mTitle != null) {
				startY += mHeight/mSeparateNums;
			}
			mGraphics.drawImage(mTitleImg, mStartX, startY, mWidth, mHeight/mSeparateNums, mApplet);
		}
	}

	// 選択ボタン表示
	private void showSelectionBtns() {
		if (mSelectBtns != null) {
			int widthSeparateNums = mAnswers.length;
			for (int selectBtnIndex = 0; selectBtnIndex < mAnswers.length; selectBtnIndex++) {
				JComboBox<String> combo = mSelectBtns.get(selectBtnIndex);
				int startX = mStartX + (mWidth / widthSeparateNums) * selectBtnIndex;
				combo.setBounds(startX, mStartY + mHeight/mSeparateNums, mWidth/mAnswers.length, mHeight/mSeparateNums);
				combo.setVisible(true);
			}
		}
	}

	// 選択ボタンの生成
	private void createJComboBoxBtns() {
		if (mApplet != null && mAnswers != null) {
			if (mSelectBtns == null) {
				mSelectBtns = new ArrayList<JComboBox<String>>();
			} else {
				mSelectBtns.clear();
			}
			for (int selectBtnIndex = 0; selectBtnIndex < mAnswers.length; selectBtnIndex++) {
				mSelectBtns.add(new JComboBox<String>(mSelections));
				mApplet.add(mSelectBtns.get(selectBtnIndex));
			}
		}
	}

	// ダイアログの縦分割数を更新
	private void updateSeparateNums() {
		mSeparateNums = 1;
		if (mTitle != null) {
			mSeparateNums++;
		}
		if (mTitleImg != null) {
			mSeparateNums++;
		}
		if (mAnswers != null) {
			mSeparateNums++;
		}
	}

	public void setResultListener(ResultListener resultListener) {
		this.mResultListener = resultListener;
	}

	public void setSelections(String[] selections) {
		this.mSelections = selections;
	}

	public void setTitle(String title) {
		this.mTitle = title;
		updateSeparateNums();
	}

	public void setTitleImg(Image titleImg) {
		this.mTitleImg = titleImg;
		updateSeparateNums();
	}

	public void setAnswers(String[] answers) {
		this.mAnswers = answers;
		updateSeparateNums();
		createJComboBoxBtns();
	}

	private void setAllBtnVisibility(boolean isVisible) {
		mYesBtn.setVisible(isVisible);
		mNoBtn.setVisible(isVisible);
		if (mApplet != null && mAnswers != null &&mSelectBtns != null) {
			for (int selectBtnIndex = 0; selectBtnIndex < mAnswers.length; selectBtnIndex++) {
				mSelectBtns.get(selectBtnIndex).setVisible(isVisible);
			}
		}
	}

	private void setAllBtnIsEnable(boolean isEnable) {
		mYesBtn.setEnabled(isEnable);
		mNoBtn.setEnabled(isEnable);
		if (mApplet != null && mAnswers != null &&mSelectBtns != null) {
			for (int selectBtnIndex = 0; selectBtnIndex < mAnswers.length; selectBtnIndex++) {
				mSelectBtns.get(selectBtnIndex).setEnabled(isEnable);
			}
		}
	}

	// ダイアログを非表示にする
	public void clearDialog() {
		setAllBtnVisibility(false);
		setAllBtnIsEnable(false);
	}

}
