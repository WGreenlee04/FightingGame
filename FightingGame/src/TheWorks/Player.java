package TheWorks;

import java.awt.Point;

public class Player {
	int health;
	int damage;
	String imageDir;
	Point point;

	public Player() {
		health = 1000;
		damage = 35;
		point = new Point(0, 0);
	}

	public void setHealth(int h) {
		health = h;

	}

	public int getHealth() {
		return health;
	}

	public void setDamage(int d) {
		damage = d;

	}

	public int getDamage() {
		return damage;
	}

	public String getImageDir() {
		return imageDir;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point p) {
		point = p;
	}
}
