package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.ArrayList;

import escape_game.InputDialog.ResultListener;

public class Entrance extends Field{

	Image gennkann;//扉あり
	Image gennkann2;//扉なし
	Image moyou;
	Image moyouForDialog; // 謎の回答入力ダイアログ用

	private boolean isCheckDoor; //最序盤、玄関の扉の調査フラグ
	private boolean opened; //玄関が開いているかどうか
	private boolean latd; //玄関の模様を見ている最中かどうか
	private boolean isDoorCheckedFirst; //ダイアログメッセージを初回は無効にする用のフラグ

	private InputDialog dialog;

	private String[] answers = {"6", "3", "2", "3", "7"};

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
			// 脱出用ダイアログ
			if (dialog == null) {
				// mainpro.message_add("何か入力してみるか...");
				dialog = new InputDialog(mainpro, mainpro.buffer, mainpro, mainpro , 250, 200, 200, 200, null, moyouForDialog, answers);
				dialog.updateScreenSize(mainpro.screen_size_x, mainpro.screen_size_y);
				dialog.setTitleImg(moyouForDialog);
				dialog.setAnswers(answers);
				dialog.resetSelectionIndex(5);
				dialog.setResultListener(new ResultListener() {

					@Override
					public void ok() {
						if (dialog == null) {
							System.out.println("error: not found dialog");
						}

						String[] selections = dialog.getSelections();
						int[] selectionIndex = dialog.getSelectionIndex();
						String[] answers = dialog.getAnswers();

						for (int index = 0; index < answers.length; index++) {
							String tgtSelection = selections[selectionIndex[index]];
							System.out.println("selections[" + index +"]: " + tgtSelection + ", answers[" + index + "]: " + answers[index]);

							if (!tgtSelection.equals(answers[index])) {
								System.out.println("password-mismatch...");
								return;
							}
						}
						opened = true;
						// mainpro.message_add("(ピー...ガチャッ)");
						// mainpro.message_add("やった、ドアが開いたぞ！！");
						System.out.println("password-match!!!");
					}

					@Override
					public void cancel() {
						// mainpro.message_add("何も起こらない、パスワードが違うのだろうか。。。");
						System.out.println("cancel");
					}
				});
			}
			dialog.show(mainpro.buffer);
		}
		else if (dialog != null) {
			if (dialog.getIsInputDialogShown()) {
				dialog.show(mainpro.buffer);
			} else {
				dialog.clearDialog();
				dialog = null;
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
				mainpro.message_add("この扉の模様・・・何か入力しないと開かないってわけか");
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
		moyouForDialog = mainpro.getImage(codeBase, "../material_data/escape_game/entrance/moyou_for_dialog.png");
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

	@Override
	ArrayList<String> getTrueFlags() {
		ArrayList<String> trueFlags = new ArrayList<String>();
		if (isCheckDoor) {
			trueFlags.add("isCheckDoor");
		}
		if (opened) {
			trueFlags.add("opened");
		}
		if (latd) {
			trueFlags.add("latd");
		}
		return trueFlags;
	}

	public InputDialog getShownDialog() {
		return dialog;
	}
}
