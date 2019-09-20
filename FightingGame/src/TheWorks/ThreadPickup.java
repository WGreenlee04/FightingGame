package TheWorks;

import java.awt.Rectangle;

public class ThreadPickup extends Thread {

	Playspace space;

	public ThreadPickup(Playspace p) {
		super();
		this.space = p;
	}

	@Override
	public void run() {
		if (space.isLShiftPressed() && space.isRunnableP1() && space.getPlayers()[0].getItem() == null) {
			space.setLShiftPressed(false);
			Rectangle[] itemRectangles = new Rectangle[space.getItems().size()];
			Rectangle p1 = new Rectangle(space.getPlayers()[0].getX(), space.getPlayers()[0].getY(),
					space.getImages()[0].getWidth(space), space.getImages()[0].getHeight(space));
			for (Item item : space.getItems()) {
				itemRectangles[space.getItems().indexOf(item)] = new Rectangle(item.getX(), item.getY(),
						item.getWidth(), item.getHeight());
				if (itemRectangles[space.getItems().indexOf(item)].intersects(p1) && item.getPlayer() == null) {
					space.getPlayers()[0].setItem(item);
					item.setPlayer(space.getPlayers()[0]);
				}
			}
		}

		if (space.isRShiftPressed() && space.isRunnableP2() && space.getPlayers()[1].getItem() == null) {
			space.setRShiftPressed(false);
			Rectangle[] itemRectangles = new Rectangle[space.getItems().size()];
			Rectangle p2 = new Rectangle(space.getPlayers()[1].getX(), space.getPlayers()[1].getY(),
					space.getImages()[1].getWidth(space), space.getImages()[1].getHeight(space));
			for (Item item : space.getItems()) {
				itemRectangles[space.getItems().indexOf(item)] = new Rectangle(item.getX(), item.getY(),
						item.getWidth(), item.getHeight());
				if (itemRectangles[space.getItems().indexOf(item)].intersects(p2) && item.getPlayer() == null) {
					space.getPlayers()[1].setItem(item);
					item.setPlayer(space.getPlayers()[1]);
				}
			}
		}

	}
}
