package TheWorks;

public class ThreadRenderItems extends Thread {

	private Playspace space;

	public ThreadRenderItems(Playspace p) {
		super();
		this.space = p;
	}

	@Override
	public void run() {
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
	}
}
