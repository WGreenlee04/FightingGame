package TheWorks;

import javax.swing.ImageIcon;

public class Stick extends Item {

	public Stick() {
		super(35, 50, 0, 0, 20, 100, new ImageIcon("src/resources/weaponStick.png").getImage(), 80, 80);
	}

	@Override
	public String getName() {
		return "weaponStick";
	}

}
