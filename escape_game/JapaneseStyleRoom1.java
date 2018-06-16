package escape_game;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;

//和室1(下側の方の和室)
public class JapaneseStyleRoom1 extends Field {
	//部屋
	Image image_room;
	//ドア
	Image image_dkDoor;
	Image image_higherClosetDoor;
	Image image_lowerClosetDoor;
	Image image_jSRoom2Door;
	
	//ドアの開閉フラグ
	private boolean isHClosetDoorOpened = false;
	private boolean isLClosetDoorOpened = false;
	private boolean isJSRoom2DoorOpened = false;
	//窓確認フラグ
	private boolean isCheckedWindow = false;
	//押入れ確認フラグ
	private boolean isCheckedHigherCloset = false;
	private boolean isCheckedLowerCloset = false;
	
	Mainpro mainpro;
	
	
	public JapaneseStyleRoom1(Mainpro mainpro) {
		this.mainpro = mainpro;
	}
	
	
	@Override
	boolean is_character_in(int cx, int cy) {
		String here = here(cx, cy);
		if(here.equals("inside")) {
			return true;
		}
		return false;
	}

	@Override
	String here(int cx, int cy) {
		return "inside";
	}

	@Override
	void examine_result(String ex_result) {
	}

	@Override
	void examine_effect(String examine_point, String item) {
	}

	@Override
	String return_text() {
		return null;
	}

	@Override
	boolean final_text() {
		return false;
	}

	@Override
	String hint() {
		if (isCheckedWindow == false) return "ここの窓からは出られるだろうか？";
		return "この部屋にはほとんど物が無い。使えそうなものもなさそうかな。";
	}

	@Override
	void showMap(ImageObserver field) {
		mainpro.buffer.drawImage(image_room, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		/*DK<->和室1のドアは常に開放
		mainpro.buffer.drawImage(image_dkDoor, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);*/
		if (isHClosetDoorOpened == false) mainpro.buffer.drawImage(image_higherClosetDoor, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		if (isLClosetDoorOpened == false) mainpro.buffer.drawImage(image_lowerClosetDoor, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
		if (isJSRoom2DoorOpened == false) mainpro.buffer.drawImage(image_jSRoom2Door, 0, 0, mainpro.screen_size_x, mainpro.screen_size_y-100, field);
	}

	@Override
	void setImages(URL codeBase) {
		image_room = mainpro.getImage(codeBase,"../material_data/escape_game/japanese_room1/washitu1.png");
		image_dkDoor = mainpro.getImage(codeBase,"../material_data/escape_game/japanese_room1/F4.png");;
		image_higherClosetDoor = mainpro.getImage(codeBase,"../material_data/escape_game/japanese_room1/F1.png");;
		image_lowerClosetDoor = mainpro.getImage(codeBase,"../material_data/escape_game/japanese_room1/F2.png");;
		image_jSRoom2Door = mainpro.getImage(codeBase,"../material_data/escape_game/japanese_room1/F3.png");;
	}

	@Override
	public String toString() {
		return "JapaneseStyleRoom1";
	}

	@Override
	void setFlagTrue(String flagString) {
		if (flagString.equals("isHClosetDoorOpened")) isHClosetDoorOpened = true;
		else if (flagString.equals("isLClosetDoorOpened")) isLClosetDoorOpened = true;
		else if (flagString.equals("isJSRoom2DoorOpened")) isJSRoom2DoorOpened = true;
		else if (flagString.equals("isCheckedWindow")) isCheckedWindow = true;
		else if (flagString.equals("isCheckedHigherCloset")) isCheckedHigherCloset = true;
		else if (flagString.equals("isCheckedLowerCloset")) isCheckedLowerCloset = true;
		else System.out.println("次のフラグ名に対応するフラグはありませんでした:"+flagString);
	}

}
