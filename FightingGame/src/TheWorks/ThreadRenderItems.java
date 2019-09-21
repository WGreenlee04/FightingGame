package TheWorks;

public class ThreadRenderItems extends Thread {

	private Playspace space;
	private boolean running;

	public ThreadRenderItems(Playspace p) {
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
			for (Item item : space.getItems()) {
				if (item.getPlayer() != null) {
					item.setX(item.getPlayer().getX());
					item.setY(item.getPlayer().getY());
					if (item.getDirection() != item.getPlayer().getDirection()) {
						item.setDirection(item.getPlayer().getDirection());
						item.setCurrentImage(space.getTools().flipObject(item.getCurrentImage()));
					}
				}
			}

			running = false;
		}
	}
}
