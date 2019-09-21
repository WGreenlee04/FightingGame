package TheWorks;

public class ThreadMovement extends Thread {

	private Playspace space;
	private boolean running;

	public ThreadMovement(Playspace space) {
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

			// if acceleration of x, carry out acceleration, decreasing it by one
			// each time.
			for (int i = 0; i < space.getPlayers().length; i++) {
				if (space.getPlayers()[i].getAccelX() > 0) {
					space.getPlayers()[i].setX(space.getPlayers()[i].getX() + space.getPlayers()[i].getAccelX());
					space.getPlayers()[i].setAccelX(space.getPlayers()[i].getAccelX() - space.getFRICTION());
					;
				}
				if (space.getPlayers()[i].getAccelX() < 0) {
					space.getPlayers()[i].setX(space.getPlayers()[i].getX() + space.getPlayers()[i].getAccelX());
					space.getPlayers()[i].setAccelX(space.getPlayers()[i].getAccelX() + space.getFRICTION());
				}
				if (space.getPlayers()[i].getAccelY() > 0) {
					space.getPlayers()[i].setY(space.getPlayers()[i].getY() - space.getPlayers()[i].getAccelY());
				}
				if (space.getPlayers()[i].getAccelY() < 0) {
					space.getPlayers()[i].setY(space.getPlayers()[i].getY() - space.getPlayers()[i].getAccelY());
				}
			}
			running = false;
		}
	}

}
