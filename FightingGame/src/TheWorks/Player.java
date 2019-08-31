package TheWorks;

public class Player {
	int health;
	Item item;
	String imageDir;
	int X;
	int Y;
	int rotation;

	public Player(String imageDir) {
		health = 1000;
		this.imageDir = imageDir;
	}

	public void setHealth(int h) {
		health = h;

	}

	public int getHealth() {
		return health;
	}

	public void setItem(Item i) {
		item = i;

	}

	public Item getItem() {
		return item;
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
