package TheWorks;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ThreadAnimate extends Thread {

	private final int degrees = 5;
	private final int endRotation = 90;
	private int delay;
	private boolean running;
	private Playspace space;
	private final ToolBox Tools = new ToolBox(space);
	private Item animationSubjectItem;
	private Player animationSubjectPlayer;

	public ThreadAnimate(Item o, Playspace p) {

		this.space = p;
		this.animationSubjectItem = o;
		this.delay = o.getAttackDelay();
		this.running = true;
	}

	public ThreadAnimate(Player o, Playspace p) {

		this.space = p;
		this.animationSubjectPlayer = o;
		this.running = true;
	}

	@Override
	public void run() {
		while (running) {
			if (animationSubjectItem != null) {
				Image original = animationSubjectItem.getCurrentImage();
				for (int i = 5; i <= endRotation; i += degrees) {
					animationSubjectItem.setCurrentImage(
							new ImageIcon("src/resources/" + animationSubjectItem.getName() + i + ".png").getImage());

					try {
						this.sleep(delay);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				animationSubjectItem.setCurrentImage(original);
			}
			running = false;
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
