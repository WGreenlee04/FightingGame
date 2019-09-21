package TheWorks;

public class ThreadGravity extends Thread {

	private Playspace space;
	private boolean running;

	public ThreadGravity(Playspace space) {
		super();
		this.space = space;
		running = true;
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {

		while (running) {

			// Gravity Players 1 and 2
			for (int i = 0; i < space.getPlayers().length; i++)
				if (space.getPlayers()[i].getAccelY() <= 0) {
					space.getPlayers()[i].setAccelY(space.getPlayers()[i].getAccelY() + space.getGRAVITY());
				} else {
					space.getPlayers()[i].setAccelY(space.getPlayers()[i].getAccelY() - 2);
				}

			// Gravity on items
			for (Item item : space.getItems()) {
				if (item.getPlayer() == null) {
					item.setY(item.getY() - space.getITEMGRAVITY());
				}
			}
			running = false;
		}
	}
}
