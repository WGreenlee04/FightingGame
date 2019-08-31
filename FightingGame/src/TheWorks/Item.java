package TheWorks;

import java.awt.Image;

public class Item {
	int damage;
	int X;
	int Y;
	int attackDelay;
	double dropRate;
	Image currentImage;

	public Item(int d, int x, int y, int aD, double dR) {
		damage = d;
		X = x;
		Y = y;
		attackDelay = aD;
		dropRate = dR;
	}

	public Image getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(Image currentImage) {
		this.currentImage = currentImage;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
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

	public double getDropRate() {
		return dropRate;
	}

	public void setDropRate(double dropRate) {
		this.dropRate = dropRate;
	}

}
