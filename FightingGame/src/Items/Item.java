package Items;

import java.awt.Image;
import java.awt.Rectangle;

import TheWorks.Playspace;
import TheWorks.ToolBox;

public class Item {
	private final Playspace space;
	private final ToolBox Tools;
	private final Image originalImage;
	private final int dropRate;
	private int width;
	private int height;
	private Player player;
	private Image currentImage;
	private Rectangle hitbox;
	private int damage;
	private int force;
	private int x;
	private int xOffset;
	private int y;
	private int yOffset;
	private int accelX;
	private int accelY;
	private int attackDelay;
	private int direction;
	private boolean animated;

	public Item(int damage, int force, int x, int y, int attackDelay, int dropRate, Image img, int width, int height,
			Playspace space) {
		this.Tools = new ToolBox(space);
		this.space = space;
		this.damage = damage;
		this.force = force;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.attackDelay = attackDelay;
		this.dropRate = dropRate;
		this.originalImage = Tools.scaleObject(img, width, height);
		this.currentImage = originalImage;
		this.direction = 1;
		this.animated = false;

	}

	public String getName() {
		return "";
	}

	public Playspace getSpace() {
		return space;
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

	public Image getOriginalImage() {
		return originalImage;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public int getDropRate() {
		return dropRate;
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

	public int getAttackDelay() {
		return attackDelay;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public void setAttackDelay(int attackDelay) {
		this.attackDelay = attackDelay;
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

	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public boolean isAnimated() {
		return animated;
	}

	public void setAnimated(boolean animated) {
		this.animated = animated;
	}
}
