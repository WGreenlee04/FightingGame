package TheWorks;

public class ThreadGravity extends Thread {

	private Playspace space;

	public ThreadGravity(Playspace space) {
		super();
		this.space = space;
	}
	
	@Override
	public void run() {

		// Gravity Players 1 and 2
		for (int i = 0; i < space.getPlayers().length; i++)
			if (space.getpAccelY()[i] <= 0) {
				space.setpAccelY(arrayEdit(space.getpAccelY(), i, space.getpAccelY()[i] + space.getGRAVITY()));
			} else {
				space.setpAccelY(arrayEdit(space.getpAccelY(), i, space.getpAccelY()[i] - 2));
			}

		// Gravity on items
		for (Item item : space.getItems()) {
			if (item.getPlayer() == null) {
				item.setY(item.getY() - space.getITEMGRAVITY());
			}
		}
	}
	
	// Returns edited array
		private int[] arrayEdit(int[] array, int i, int value) {
			int[] tempArray = array;
			tempArray[i] = value;
			return tempArray;
		}
}
