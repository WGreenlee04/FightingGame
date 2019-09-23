package TheWorks;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Player {
	private Playspace space;
	private Item item;
	private Image image;
	private String imageDir;
	private String darkImageDir;
	private Rectangle hitbox;
	private int health;
	private int accelX;
	private int accelY;
	private int x;
	private int y;
	private int width;
	private int height;
	private int direction;
	private int jumps;
	private boolean stunned;
	private boolean falling;
	private boolean jumping;
	private boolean dark;
	private boolean directionBool;
	private ToolBox Tools;

	public Player(String imageDir, String darkImageDir, Playspace p) {
		Tools = new ToolBox(p);
		space = p;
		health = 1000;
		width = 90;
		height = 150;
		jumps = 0;
		falling = false;
		jumping = false;
		dark = false;
		image = new ImageIcon(imageDir).getImage();
		image = Tools.scalePlayer(image, this);
		this.imageDir = imageDir;
		this.darkImageDir = darkImageDir;
	}

	public Point leftHandItemLocation() {
		int x = (this.x + 10);
		int y = (this.y + this.height / 2 + 16);
		return new Point(x, y);
	}

	public Point rightHandItemLocation() {
		int x = (this.x - 4);
		int y = (this.y + this.height / 2 + 14);
		return new Point(x, y);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public Playspace getSpace() {
		return space;
	}

	public void setSpace(Playspace space) {
		this.space = space;
	}

	public boolean isStunned() {
		return stunned;
	}

	public void setStunned(boolean stunned) {
		this.stunned = stunned;
	}

}
