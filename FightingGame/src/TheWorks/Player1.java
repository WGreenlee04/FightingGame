package TheWorks;

import java.awt.image.BufferedImage;

public class Player1 extends Player {

	@Override
	public void setHealth(int h) {
		health = h;

	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setDamage(int d) {
		damage = d;

	}

	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public BufferedImage getImage() {
		return img;
	}

	@Override
	public void setImage(BufferedImage b) {
		img = b;

	}

}
