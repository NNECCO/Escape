package escape_game;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;

public class Ending {
	private Image endCard = null;
	private Applet applet;
	private int drawScreenX, drawScreenY;

	Ending(Applet applet, int drawScreenX, int drawScreenY) {
		this.applet = applet;
		this.drawScreenX = drawScreenX;
		this.drawScreenY = drawScreenY;
	}

	public void paint(Graphics buffer) {
		buffer.drawImage(endCard, 0, 0, drawScreenX, drawScreenY, applet);
	}

	public void setEndingImage() {
		endCard = applet.getImage(applet.getCodeBase(), "../material_data/escape_game/other/endcard.png");
	}


}
