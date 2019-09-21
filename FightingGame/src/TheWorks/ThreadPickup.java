package TheWorks;

import java.awt.Rectangle;

public class ThreadPickup extends Thread {

	private Playspace space;
	private boolean running;

	public ThreadPickup(Playspace p) {
		super();
		this.space = p;
		running = true;
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {

		while (running) {

			int i = 0;
			if (space.isLShiftPressed() && space.isRunnableP1() && space.getPlayers()[i].getItem() == null) {
				space.setLShiftPressed(false);
				Rectangle[] itemRectangles = new Rectangle[space.getItems().size()];
				Rectangle p1 = new Rectangle(space.getPlayers()[i].getX(), space.getPlayers()[i].getY(),
						space.getPlayers()[i].getWidth(), space.getPlayers()[i].getHeight());
				for (Item item : space.getItems()) {
					itemRectangles[space.getItems().indexOf(item)] = new Rectangle(item.getX(), item.getY(),
							item.getWidth(), item.getHeight());
					if (itemRectangles[space.getItems().indexOf(item)].intersects(p1) && item.getPlayer() == null) {
						space.getPlayers()[i].setItem(item);
						item.setPlayer(space.getPlayers()[i]);
					}
				}
			}

			i = 1;
			if (space.isRShiftPressed() && space.isRunnableP2() && space.getPlayers()[i].getItem() == null) {
				space.setRShiftPressed(false);
				Rectangle[] itemRectangles = new Rectangle[space.getItems().size()];
				Rectangle p2 = new Rectangle(space.getPlayers()[i].getX(), space.getPlayers()[i].getY(),
						space.getPlayers()[i].getWidth(), space.getPlayers()[i].getHeight());
				for (Item item : space.getItems()) {
					itemRectangles[space.getItems().indexOf(item)] = new Rectangle(item.getX(), item.getY(),
							item.getWidth(), item.getHeight());
					if (itemRectangles[space.getItems().indexOf(item)].intersects(p2) && item.getPlayer() == null) {
						space.getPlayers()[i].setItem(item);
						item.setPlayer(space.getPlayers()[i]);
					}
				}
			}

			running = false;
		}
	}
}
