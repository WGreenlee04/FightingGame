package TheWorks;

public class ThreadDamage extends Thread {

	private Player target;
	private Item item;
	private Playspace space;
	private boolean running;

	public ThreadDamage(Item item, Player playerHit, Playspace space) {
		this.target = playerHit;
		this.space = space;
		this.item = item;
		this.running = true;
	}

	@Override
	public void run() {

		if (item.getDirection() == -1) {
			target.setAccelX(target.getAccelX() - item.getForce());
			target.setStunned(true);
		} else {
			target.setAccelX(target.getAccelX() + item.getForce());
			target.setStunned(true);
		}
		while (Math.abs(target.getAccelX()) > space.getPLAYERSPEED()) {
			target.setStunned(true);
		}
		target.setStunned(false);
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
