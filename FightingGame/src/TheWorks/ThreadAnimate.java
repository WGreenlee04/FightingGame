package TheWorks;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ThreadAnimate extends Thread {

	private Playspace space;
	private Item animationSubjectItem;
	private Player animationSubjectPlayer;
	private final int degrees = 5;
	private final int endRotation = 90;
	private final int delay = 20;
	private final ToolBox Tools = new ToolBox(space);
	private boolean running;

	public ThreadAnimate(Item o, Playspace p) {

		this.space = p;
		this.animationSubjectItem = o;
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
				for (int i = degrees; i <= endRotation; i += degrees) {
					animationSubjectItem.setCurrentImage(
							new ImageIcon("src/resources/" + animationSubjectItem.getName() + i + ".png").getImage());
					for (Player p : space.getPlayers())
						if (!(p.equals(animationSubjectItem.getPlayer()))
								&& p.getHitbox().intersects(animationSubjectItem.getHitbox()) && !(p.isStunned())) {
							ThreadDamage damage = new ThreadDamage(animationSubjectItem, p, space);
							damage.start();
							ThreadSound sound = new ThreadSound(
									"src/resources/" + animationSubjectItem.getName() + "Hit.wav", space);
							sound.start();
						}

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
