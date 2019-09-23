package TheWorks;

import javax.swing.ImageIcon;

public class Stick extends Item {

	public Stick(Playspace space) {
		super(35, 20, 0, 0, 20, 100, new ImageIcon("src/resources/weaponStick.png").getImage(), 80, 80, space);
	}

	@Override
	public String getName() {
		return "weaponStick";
	}

}
