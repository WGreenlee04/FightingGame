package Threads;

import java.awt.Image;

import Items.Item;
import Items.Player;
import TheWorks.Playspace;
import TheWorks.ToolBox;

public class ThreadAnimate extends Thread {

	private Playspace space;
	private Item animationSubjectItem;
	private Player attacker;
	private Player animationSubjectPlayer;
	private final int degrees = 5;
	private final int endRotation = 100;
	private final int delay = 10;
	private final ToolBox Tools = new ToolBox(space);
	private Image currentFrame;
	private boolean running;

	public ThreadAnimate(Item o, Playspace p) {

		this.space = p;
		this.animationSubjectItem = o;
		this.attacker = o.getPlayer();
		this.running = true;
	}

	public ThreadAnimate(Player o, Playspace p) {

		this.space = p;
		this.animationSubjectPlayer = o;
		this.running = true;
	}

	/* Creates a new damage thread for the player, with item's damage */
	private void damage(Player player, Item item) {
		ThreadDamage damage = new ThreadDamage(item, player, space);
		ThreadSound sound = new ThreadSound("src/resources/" + item.getName() + "Hit.wav");
		damage.start();
		sound.start();
	}

	private void closeThread() {
		animationSubjectItem.setAnimated(false);
		running = false;
		this.interrupt();
	}

	@Override
	public void run() {
		if (animationSubjectItem != null) {
			Image original = animationSubjectItem.getCurrentImage();
			for (int i = degrees; i <= endRotation; i += degrees) {
				if (animationSubjectItem.getPlayer().getDirection() == -1) {
					currentFrame = Tools.rotateObject(original, -i);
				} else {
					currentFrame = Tools.rotateObject(original, i);
				}
				// THIS NEEDS TO BE MOVED INTO THE ANIMATE CLASS, AND CALLED ON HIT FRAMES
				for (Player target : space.getPlayers()) {
					if (!target.equals(attacker) && !target.isBeingDamaged()
							&& attacker.getItem().getHitbox().intersects(target.getHitbox())) {
						damage(target, attacker.getItem());
					}
				}

				animationSubjectItem.setyOffset(i + 1);
				animationSubjectItem.setWidth(currentFrame.getWidth(space));
				animationSubjectItem.setHeight(currentFrame.getHeight(space));
				animationSubjectItem.setCurrentImage(currentFrame);
				try {
					this.sleep(delay);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			animationSubjectItem.setxOffset(0);
			animationSubjectItem.setyOffset(0);
			animationSubjectItem.setCurrentImage(original);
			animationSubjectItem.setWidth(original.getWidth(space));
			animationSubjectItem.setHeight(original.getHeight(space));
		}
		closeThread();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
