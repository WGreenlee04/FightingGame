package TheWorks;

import javax.swing.ImageIcon;

public class Stick extends Item {

	public Stick() {
		super(35, 500, 0, 5, 100);
		currentImage = new ImageIcon("src/resources/weaponStick.png").getImage();
		Width = 80;
		Height = 80;
	}

}
