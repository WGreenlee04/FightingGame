package TheWorks;

import java.awt.image.BufferedImage;
import java.io.File;

public abstract class Player {
	int health;
	int damage;
	File file;
	BufferedImage img;

	public Player() {

	}

	public abstract void setHealth(int h);

	public abstract int getHealth();

	public abstract void setDamage(int d);

	public abstract int getDamage();

	public abstract BufferedImage getImage();

	public abstract void setImage(BufferedImage b);
}
