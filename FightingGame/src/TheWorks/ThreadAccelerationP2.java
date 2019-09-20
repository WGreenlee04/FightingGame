package TheWorks;

import java.awt.Image;

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
				if (space.getpAccelX()[i] == 0) {
					space.setpAccelX(arrayEdit(space.getpAccelX(), i, -space.getDASHSPEED() * 2));
				} else {
					space.setpAccelX(arrayEdit(space.getpAccelX(), i, -space.getPLAYERSPEED() * 2));
				}
			}

			// Right Movement w/ dash
			if (space.isRightPressed() && !space.isLeftPressed()) {
				if (space.getpAccelX()[i] == 0) {
					space.setpAccelX(arrayEdit(space.getpAccelX(), i, space.getDASHSPEED() * 2));
				} else {
					space.setpAccelX(arrayEdit(space.getpAccelX(), i, space.getPLAYERSPEED() * 2));
				}
			}

			// If you didn't just jump, and pressed jump, jump
			if (space.isUpPressed() && !space.isJump2()) {
				space.setJump2(true);
				space.setpAccelY(arrayEdit(space.getpAccelY(), i, space.getJUMPHEIGHT() * 2));
			}

			// You just jumped, and we need to increase jump counter and reset jump
			if (space.isUpReleased() && space.getJumps()[i] <= 2) {
				space.setJump2(false);
				space.setFall(arrayEdit(space.getFall(), i, false));
				space.setJumps(arrayEdit(space.getJumps(), i, space.getJumps()[i] + 1));
				space.setUpReleased(false);
			} else if (space.getJumps()[i] > 2 && !space.getIsDark()[i]) { // darker color, out of jumps
				space.setImages(arrayEdit(space.getImages(), i,
						space.getTools().darkenObject(space.getImages()[i], space.getPlayers()[i])));
				space.setIsDark(arrayEdit(space.getIsDark(), i, true));
			} else if (space.getJumps()[i] == 0 && space.getIsDark()[i]) { // you can be light
				space.setImages(arrayEdit(space.getImages(), i,
						space.getTools().lightenObject(space.getImages()[i], space.getPlayers()[i])));
				space.setIsDark(arrayEdit(space.getIsDark(), i, false));
			}

			// Fast falling
			if (space.isDownPressed() || space.getFall()[i]) {
				space.setpAccelY(arrayEdit(space.getpAccelY(), i, space.getpAccelY()[i] + space.getFALLSPEED()));
				space.setFall(arrayEdit(space.getFall(), i, true));
			}
		}
	}

	// Returns edited array
	private int[] arrayEdit(int[] array, int i, int value) {
		int[] tempArray = array;
		tempArray[i] = value;
		return tempArray;
	}

	private Image[] arrayEdit(Image[] array, int i, Image value) {
		Image[] tempArray = array;
		tempArray[i] = value;
		return tempArray;
	}

	private boolean[] arrayEdit(boolean[] array, int i, boolean value) {
		boolean[] tempArray = array;
		tempArray[i] = value;
		return tempArray;
	}

}
