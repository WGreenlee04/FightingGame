package TheWorks;

import javax.swing.ImageIcon;

public class Fist extends Item {

	public Fist(Playspace space) {
		super(0, 30, 0, 0, 20, 100, new ImageIcon("src/resources/weaponFist.png").getImage(), 80, 80, space);
	}

	@Override
	public String getName() {
		return "weaponFist";
	}
}
