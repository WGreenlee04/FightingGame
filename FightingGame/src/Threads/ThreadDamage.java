package Threads;

import Items.Item;
import Items.Player;
import TheWorks.Playspace;

public class ThreadDamage extends Thread {

	private Player target;
	private Item item;
	private Playspace space;
	private boolean running;

	public ThreadDamage(Item item, Player playerHit, Playspace space) {
		this.target = playerHit;
		this.space = space;
		this.item = item;
		target.setBeingDamaged(true);
		this.running = true;
	}

	private void closeThread() {
		target.setBeingDamaged(false);
		running = false;
		this.interrupt();
	}

	private void stunPlayer() {
		if (!target.isStunned()) {
			ThreadStun t = new ThreadStun(target, item.getDamage());
			t.start();
		}
	}

	@Override
	public void run() {

		if (item.getDirection() == -1) {
			target.setAccelX(target.getAccelX() - item.getForce());
			target.setHealth(target.getHealth() - item.getDamage());
			stunPlayer();
		} else {
			target.setAccelX(target.getAccelX() + item.getForce());
			target.setHealth(target.getHealth() - item.getDamage());
			stunPlayer();
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
