package Threads;

import TheWorks.Player;

public class ThreadStun extends Thread {

	private Player player;
	private int damage;
	private boolean running;

	public ThreadStun(Player p, int damage) {
		this.player = p;
		this.damage = damage;
	}

	private void closeThread() {
		this.running = false;
		this.interrupt();
	}

	@Override
	public void run() {
		player.setStunned(true);

		try {
			Thread.sleep(((1000 - player.getHealth()) / damage) * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		player.setStunned(false);

		closeThread();
	}
}
