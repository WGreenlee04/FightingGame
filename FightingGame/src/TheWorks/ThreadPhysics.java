package TheWorks;

import java.awt.Rectangle;

public class ThreadPhysics extends Thread {

	private Playspace space;
	private boolean running;

	public ThreadPhysics(Playspace p) {
		super();
		this.space = p;
		running = true;
	}

	@Override
	public void run() {

		pickup();

		accel();

		move();

		renderObjects();

		collideAll();

		running = false;
	}

	private void pickup() {
		// The... Pickup... line?
		int i = 0;
		if (space.isLShiftPressed() && space.isRunnableP1() && space.getPlayers()[i].getItem() == null) {
			space.setLShiftPressed(false);
			Rectangle[] itemRectangles = new Rectangle[space.getItems().size()];
			Rectangle p1 = new Rectangle(space.getPlayers()[i].getX(), space.getPlayers()[i].getY(),
					space.getPlayers()[i].getWidth(), space.getPlayers()[i].getHeight());
			for (Item item : space.getItems()) {
				itemRectangles[space.getItems().indexOf(item)] = new Rectangle(item.getX(), item.getY(),
						item.getWidth(), item.getHeight());
				if (itemRectangles[space.getItems().indexOf(item)].intersects(p1) && item.getPlayer() == null) {
					space.getPlayers()[i].setItem(item);
					item.setPlayer(space.getPlayers()[i]);
				}
			}
		}

		i = 1;
		if (space.isRShiftPressed() && space.isRunnableP2() && space.getPlayers()[i].getItem() == null) {
			space.setRShiftPressed(false);
			Rectangle[] itemRectangles = new Rectangle[space.getItems().size()];
			Rectangle p2 = new Rectangle(space.getPlayers()[i].getX(), space.getPlayers()[i].getY(),
					space.getPlayers()[i].getWidth(), space.getPlayers()[i].getHeight());
			for (Item item : space.getItems()) {
				itemRectangles[space.getItems().indexOf(item)] = new Rectangle(item.getX(), item.getY(),
						item.getWidth(), item.getHeight());
				if (itemRectangles[space.getItems().indexOf(item)].intersects(p2) && item.getPlayer() == null) {
					space.getPlayers()[i].setItem(item);
					item.setPlayer(space.getPlayers()[i]);
				}
			}
		}
	}

	private void accel() {
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

		// p2
		i = 1;

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
			} else if (space.getPlayers()[i].getJumps() > 2 && !space.getPlayers()[i].isDark()) { // darker
				space.getPlayers()[i].setImage(
						space.getTools().darkenObject(space.getPlayers()[i].getImage(), space.getPlayers()[i]));
				space.getPlayers()[i].setDark(true);
			} else if (space.getPlayers()[i].getJumps() == 0 && space.getPlayers()[i].isDark()) { // lighter
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

		// Gravity Players 1 and 2
		for (i = 0; i < space.getPlayers().length; i++)
			if (space.getPlayers()[i].getAccelY() <= 0) {
				space.getPlayers()[i].setAccelY(space.getPlayers()[i].getAccelY() + space.getGRAVITY());
			} else {
				space.getPlayers()[i].setAccelY(space.getPlayers()[i].getAccelY() - 2);
			}

		// Gravity on items
		for (Item item : space.getItems()) {
			if (item.getPlayer() == null)
				item.setY(item.getY() - space.getITEMGRAVITY());
		}
	}

	public void move() {

		// if acceleration of x, do acceleration, decreasing it by one each time.
		for (int i = 0; i < space.getPlayers().length; i++) {
			if (space.getPlayers()[i].getAccelX() > 0) {
				space.getPlayers()[i].setX(space.getPlayers()[i].getX() + space.getPlayers()[i].getAccelX());
				space.getPlayers()[i].setAccelX(space.getPlayers()[i].getAccelX() - space.getFRICTION());
				;
			}
			if (space.getPlayers()[i].getAccelX() < 0) {
				space.getPlayers()[i].setX(space.getPlayers()[i].getX() + space.getPlayers()[i].getAccelX());
				space.getPlayers()[i].setAccelX(space.getPlayers()[i].getAccelX() + space.getFRICTION());
			}
			if (space.getPlayers()[i].getAccelY() > 0) {
				space.getPlayers()[i].setY(space.getPlayers()[i].getY() - space.getPlayers()[i].getAccelY());
			}
			if (space.getPlayers()[i].getAccelY() < 0) {
				space.getPlayers()[i].setY(space.getPlayers()[i].getY() - space.getPlayers()[i].getAccelY());
			}
		}
	}

	private void renderObjects() {
		// Sets pos and image for Items
		for (Item item : space.getItems()) {
			if (item.getPlayer() != null) {
				item.setX(item.getPlayer().getX());
				item.setY(item.getPlayer().getY());
				if (item.getDirection() != item.getPlayer().getDirection()) {
					item.setDirection(item.getPlayer().getDirection());
					item.setCurrentImage(space.getTools().flipObject(item.getCurrentImage()));
				}
			}
		}
	}

	private void collideAll() {
		// Collides objects
		for (int i = 0; i < space.getPlayers().length; i++) {

			// If at wall, loop over (haha)
			if (space.getPlayers()[i].getX() < 0 - space.getPlayers()[i].getWidth())
				space.getPlayers()[i].setX(space.getWIDTH());
			if (space.getPlayers()[i].getX() > space.getWIDTH())
				space.getPlayers()[i].setX(0 - space.getPlayers()[i].getWidth());

			// If at floor, don't move through
			if (space.getPlayers()[i].getY() + space.getPlayers()[i].getHeight() + 35 > space.getHEIGHT()) {
				space.getPlayers()[i].setY(space.getHEIGHT() - (space.getPlayers()[i].getHeight() + 35));
				space.getPlayers()[i].setJumps(0);
				space.getPlayers()[i].setFalling(false);
				;
			}
		}

		// When items hit the ground, stop motion
		for (int i = 0; i < space.getItems().size(); i++) {
			if (space.getItems().get(i).getY() + space.getItems().get(i).getCurrentImage().getHeight(space) + 30 > space
					.getHEIGHT()) {
				space.getItems().get(i)
						.setY(space.getHEIGHT() - (space.getItems().get(i).getCurrentImage().getHeight(space) + 30));
			}
		}
	}

	public boolean isRunning() {
		return running;
	}
}
