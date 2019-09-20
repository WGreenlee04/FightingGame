package TheWorks;

import java.awt.Image;

public class ThreadAccelerationP1 extends Thread {

	private Playspace space;

	public ThreadAccelerationP1(Playspace p) {
		super();
		this.space = p;
	}

	@Override
	public void run() {

		// p1
		int i = 0;

		if (space.isRunnableP1()) {

			// Left Movement w/ dash
			if (space.isAPressed() && !space.isDPressed()) {
				if (space.getpAccelX()[i] == 0) {
					space.setpAccelX(arrayEdit(space.getpAccelX(), i, -space.getDASHSPEED() * 2));
				} else {
					space.setpAccelX(arrayEdit(space.getpAccelX(), i, -space.getPLAYERSPEED() * 2));
				}
			}

			// Right Movement w/ dash
			if (space.isDPressed() && !space.isAPressed()) {
				if (space.getpAccelX()[i] == 0) {
					space.setpAccelX(arrayEdit(space.getpAccelX(), i, space.getDASHSPEED() * 2));
				} else {
					space.setpAccelX(arrayEdit(space.getpAccelX(), i, space.getPLAYERSPEED() * 2));
				}
			}

			// If you didn't just jump, and pressed jump, jump
			if (space.isWPressed() && !space.isJump1()) {
				space.setJump1(true);
				space.setpAccelY(arrayEdit(space.getpAccelY(), i, space.getJUMPHEIGHT() * 2));
			}

			// You just jumped, and we need to increase jump counter and reset jump
			if (space.isWReleased() && space.getJumps()[i] <= 2) {
				space.setJump1(false);
				space.setFall(arrayEdit(space.getFall(), i, false));
				space.setJumps(arrayEdit(space.getJumps(), i, space.getJumps()[i] + 1));
				space.setWReleased(false);
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
			if (space.isSPressed() || space.getFall()[i]) {
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
