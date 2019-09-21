package TheWorks;

public class ThreadAccelerationP1 extends Thread {

	private Playspace space;
	private boolean running;

	public ThreadAccelerationP1(Playspace p) {
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

			// p1
			int i = 0;

			if (space.isRunnableP1()) {

				// Left Movement w/ dash
				if (space.isAPressed() && !space.isDPressed()) {
					if (space.getPlayers()[i].getAccelX() == 0) {
						space.getPlayers()[i].setAccelX(-space.getDASHSPEED() * 2);
					} else {
						space.getPlayers()[i].setAccelX(-space.getPLAYERSPEED() * 2);
					}
				}

				// Right Movement w/ dash
				if (space.isDPressed() && !space.isAPressed()) {
					if (space.getPlayers()[i].getAccelX() == 0) {
						space.getPlayers()[i].setAccelX(space.getDASHSPEED() * 2);
					} else {
						space.getPlayers()[i].setAccelX(space.getPLAYERSPEED() * 2);
					}
				}

				// If you didn't just jump, and pressed jump, jump
				if (space.isWPressed() && !space.getPlayers()[i].isJumping()) {
					space.getPlayers()[i].setJumping(true);
					space.getPlayers()[i].setAccelY(space.getJUMPHEIGHT() * 2);
				}

				// You just jumped, and we need to increase jump counter and reset
				// jump
				if (space.isWReleased() && space.getPlayers()[i].getJumps() <= 2) {
					space.getPlayers()[i].setJumping(false);
					space.getPlayers()[i].setFalling(false);
					space.getPlayers()[i].setJumps(space.getPlayers()[i].getJumps() + 1);
					space.setWReleased(false);
				} else if (space.getPlayers()[i].getJumps() > 2 && !space.getPlayers()[i].isDark()) { // Darker
					space.getPlayers()[i].setImage(
							space.getTools().darkenObject(space.getPlayers()[i].getImage(), space.getPlayers()[i]));
					space.getPlayers()[i].setDark(true);
				} else if (space.getPlayers()[i].getJumps() == 0 && space.getPlayers()[i].isDark()) { // Lighter
					space.getPlayers()[i].setImage(
							space.getTools().lightenObject(space.getPlayers()[i].getImage(), space.getPlayers()[i]));
					space.getPlayers()[i].setDark(false);
				}

				// Fast falling
				if (space.isSPressed() || space.getPlayers()[i].isFalling()) {
					space.getPlayers()[i].setAccelY(space.getPlayers()[i].getAccelY() + space.getFALLSPEED());
					space.getPlayers()[i].setFalling(true);
				}
			}

			running = false;
		}
	}
}
