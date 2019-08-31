package TheWorks;

public class Item {
	int damage;
	int X;
	int Y;
	double dropRate;

	public Item(int d, int x, int y, double dR) {
		damage = d;
		X = x;
		Y = y;
		dropRate = dR;
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
