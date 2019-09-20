package TheWorks;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Player {
	Item item;
	Image image;
	String imageDir;
	String darkImageDir;
	int health;
	int accelX;
	int accelY;
	int X;
	int Y;
	int Width;
	int Height;
	int direction;
	int jumps;
	boolean falling;
	boolean jumping;
	boolean dark;
	boolean directionBool;

	public Player(String imageDir, String darkImageDir) {
		health = 1000;
		Width = 90;
		Height = 150;
		jumps = 0;
		falling = false;
		jumping = false;
		dark = false;
		image = new ImageIcon(imageDir).getImage();
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

	public int getAccelX() {
		return accelX;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setAccelX(int accelX) {
		this.accelX = accelX;
	}

	public int getAccelY() {
		return accelY;
	}

	public void setAccelY(int accelY) {
		this.accelY = accelY;
	}

	public int getJumps() {
		return jumps;
	}

	public void setJumps(int jumps) {
		this.jumps = jumps;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isDark() {
		return dark;
	}

	public void setDark(boolean dark) {
		this.dark = dark;
	}

	public boolean isDirectionBool() {
		return directionBool;
	}

	public void setDirectionBool(boolean directionBool) {
		this.directionBool = directionBool;
	}

}
