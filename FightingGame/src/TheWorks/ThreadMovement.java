package TheWorks;

public class ThreadMovement extends Thread {

	private Playspace space;

	public ThreadMovement(Playspace space) {
		super();
		this.space = space;
	}

	@Override
	public void run() {

		// if acceleration of x, carry out acceleration, decreasing it by one
		// each time.
		for (int i = 0; i < space.getPlayers().length; i++) {
			if (space.getpAccelX()[i] > 0) {
				space.getPlayers()[i].setX(space.getPlayers()[i].getX() + space.getpAccelX()[i]);
				space.setpAccelX(arrayEdit(space.getpAccelX(), i, space.getpAccelX()[i] - space.getFRICTION()));
				;
			}
			if (space.getpAccelX()[i] < 0) {
				space.getPlayers()[i].setX(space.getPlayers()[i].getX() + space.getpAccelX()[i]);
				space.setpAccelX(arrayEdit(space.getpAccelX(), i, space.getpAccelX()[i] + space.getFRICTION()));
			}
			if (space.getpAccelY()[i] > 0) {
				space.getPlayers()[i].setY(space.getPlayers()[i].getY() - space.getpAccelY()[i]);
			}
			if (space.getpAccelY()[i] < 0) {
				space.getPlayers()[i].setY(space.getPlayers()[i].getY() - space.getpAccelY()[i]);
			}
		}
	}

	private int[] arrayEdit(int[] array, int i, int value) {
		int[] tempArray = array;
		tempArray[i] = value;
		return tempArray;
	}

}
