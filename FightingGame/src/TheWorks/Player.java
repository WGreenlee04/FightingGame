package TheWorks;

public class Player {
	int health;
	int damage;
	String imageDir;
	int X;
	int Y;
	int rotation;

	public Player(String imageDir) {
		health = 1000;
		damage = 35;
		this.imageDir = imageDir;
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

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

}
