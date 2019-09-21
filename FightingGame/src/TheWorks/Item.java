package TheWorks;

import java.awt.Image;
import java.awt.Rectangle;

public class Item {
	private Player player;
	private Playspace space;
	private Image currentImage;
	private Rectangle hitbox;
	private int damage;
	private int x;
	private int y;
	private int accelX;
	private int accelY;
	private int width;
	private int height;
	private int attackDelay;
	private int dropRate;
	private int direction;

	public Item() {
	}

	public Item(int damage, int x, int y, int attackDelay, int dropRate, Image img, int width, int height) {
		this.damage = damage;
		this.x = x;
		this.y = y;
		this.attackDelay = attackDelay;
		this.dropRate = dropRate;
		this.currentImage = img;
		this.width = width;
		this.height = height;
		this.direction = 1;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
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

	public int getAccelX() {
		return accelX;
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

	public int getAttackDelay() {
		return attackDelay;
	}

	public void setAttackDelay(int attackDelay) {
		this.attackDelay = attackDelay;
	}

	public int getDropRate() {
		return dropRate;
	}

	public void setDropRate(int dropRate) {
		this.dropRate = dropRate;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Playspace getSpace() {
		return space;
	}

	public void setSpace(Playspace space) {
		this.space = space;
	}

	public Image getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(Image currentImage) {
		this.currentImage = currentImage;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

}
