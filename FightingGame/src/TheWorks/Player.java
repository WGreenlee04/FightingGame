package TheWorks;

public class Player {
	int health;
	Item item;
	String imageDir;
	String darkImageDir;
	int X;
	int Y;
	int Width;
	int Height;
	int direction;

	public Player(String imageDir, String darkImageDir) {
		health = 1000;
		Width = 90;
		Height = 150;
		this.imageDir = imageDir;
		this.darkImageDir = darkImageDir;
	}

	public int getWidth() {
		return Width;
	}

	public void setWidth(int width) {
		Width = width;
	}

	public int getHeight() {
		return Height;
	}

	public void setHeight(int height) {
		Height = height;
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

	public void setDirection(int i) {
		direction = i;

	}

	public int getDirection() {
		return direction;
	}

	public String getDarkImageDir() {
		return darkImageDir;
	}

	public void setDarkImageDir(String darkImageDir) {
		this.darkImageDir = darkImageDir;
	}

	public void setImageDir(String imageDir) {
		this.imageDir = imageDir;
	}

}
