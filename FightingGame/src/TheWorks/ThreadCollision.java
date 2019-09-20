package TheWorks;

public class ThreadCollision extends Thread {

	private Playspace space;

	public ThreadCollision(Playspace space) {
		super();
		this.space = space;
	}

	@Override
	public void run() {

		// looping through the number of players
		for (int i = 0; i < space.getPlayers().length; i++) {

			// If at wall, loop over (haha)
			if (space.getPlayers()[i].getX() < 0 - space.getPlayers()[i].getWidth())
				space.getPlayers()[i].setX(space.getWIDTH());
			if (space.getPlayers()[i].getX() > space.getWIDTH())
				space.getPlayers()[i].setX(0 - space.getPlayers()[i].getWidth());

			// If at floor, don't move through
			if (space.getPlayers()[i].getY() + space.getPlayers()[i].getHeight() + 35 > space.getHEIGHT()) {
				space.getPlayers()[i].setY(space.getHEIGHT() - (space.getPlayers()[i].getHeight() + 35));
				space.setJumps(arrayEdit(space.getJumps(), i, 0));
				space.setFall(arrayEdit(space.getFall(), i, false));
				;
			}
		}

		// When items hit the ground, stop motion
		for (int i = 0; i < space.getItems().size(); i++) {
			if (space.getItems().get(i).getY() + space.getItems().get(i).getCurrentImage().getHeight(space) + 30 > space
					.getHEIGHT()) {
				space.getItems().get(i)
						.setY(space.getHEIGHT() - (space.getItems().get(i).getCurrentImage().getHeight(space) + 30));
			}
		}
	}

	// Returns edited array
	private int[] arrayEdit(int[] array, int i, int value) {
		int[] tempArray = array;
		tempArray[i] = value;
		return tempArray;
	}

	private boolean[] arrayEdit(boolean[] array, int i, boolean value) {
		boolean[] tempArray = array;
		tempArray[i] = value;
		return tempArray;
	}

}
