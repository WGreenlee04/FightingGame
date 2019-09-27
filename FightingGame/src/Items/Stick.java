package Items;

import javax.swing.ImageIcon;

import TheWorks.Playspace;

public class Stick extends Item {

	public Stick(Playspace space) {
		super(35, 15, 0, 0, 20, 200, new ImageIcon("src/resources/weaponStick.png").getImage(), 80, 80, space);
	}

	@Override
	public String getName() {
		return "weaponStick";
	}

}
