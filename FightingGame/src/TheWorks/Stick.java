package TheWorks;

import javax.swing.ImageIcon;

public class Stick extends Item {

	public Stick() {
		super(35, 0, 0, 5, 1.00);
		currentImage = new ImageIcon("src/resources/weaponStick").getImage();
	}

}
