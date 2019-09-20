package TheWorks;

public class ThreadAccelerationP2 extends Thread {

	private Playspace space;

	public ThreadAccelerationP2(Playspace p) {
		super();
		this.space = p;
	}

	@Override
	public void run() {

		// p2
		int i = 1;

		if (space.isRunnableP2()) {

			// Left Movement w/ dash
			if (space.isLeftPressed() && !space.isRightPressed()) {
				if (space.getPlayers()[i].getAccelX() == 0) {
					space.getPlayers()[i].setAccelX(-space.getDASHSPEED() * 2);
				} else {
					space.getPlayers()[i].setAccelX(-space.getPLAYERSPEED() * 2);
				}
			}

			// Right Movement w/ dash
			if (space.isRightPressed() && !space.isLeftPressed()) {
				if (space.getPlayers()[i].getAccelX() == 0) {
					space.getPlayers()[i].setAccelX(space.getDASHSPEED() * 2);
				} else {
					space.getPlayers()[i].setAccelX(space.getPLAYERSPEED() * 2);
				}
			}

			// If you didn't just jump, and pressed jump, jump
			if (space.isUpPressed() && !space.getPlayers()[i].isJumping()) {
				space.getPlayers()[i].setJumping(true);
				space.getPlayers()[i].setAccelY(space.getJUMPHEIGHT() * 2);
			}

			// You just jumped, and we need to increase jump counter and reset jump
			if (space.isUpReleased() && space.getPlayers()[i].getJumps() <= 2) {
				space.getPlayers()[i].setJumping(false);
				space.getPlayers()[i].setFalling(false);
				space.getPlayers()[i].setJumps(space.getPlayers()[i].getJumps() + 1);
				space.setUpReleased(false);
			} else if (space.getPlayers()[i].getJumps() > 2 && !space.getPlayers()[i].isDark()) { // darker color, out
																									// of jumps
				space.getPlayers()[i].setImage(
						space.getTools().darkenObject(space.getPlayers()[i].getImage(), space.getPlayers()[i]));
				space.getPlayers()[i].setDark(true);
			} else if (space.getPlayers()[i].getJumps() == 0 && space.getPlayers()[i].isDark()) { // you can be light
				space.getPlayers()[i].setImage(
						space.getTools().lightenObject(space.getPlayers()[i].getImage(), space.getPlayers()[i]));
				space.getPlayers()[i].setDark(false);
			}

			// Fast falling
			if (space.isDownPressed() || space.getPlayers()[i].isFalling()) {
				space.getPlayers()[i].setAccelY(space.getPlayers()[i].getAccelY() + space.getFALLSPEED());
				space.getPlayers()[i].setFalling(true);
			}
		}
	}

}
