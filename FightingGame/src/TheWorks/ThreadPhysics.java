package TheWorks;

import java.util.ArrayList;

public class ThreadPhysics extends Thread {

	private Playspace space;
	private boolean running;
	private ArrayList<ThreadAnimate> animations; // Active animation threads

	public ThreadPhysics(Playspace p) {
		super();
		this.space = p;
		this.running = true;
		this.animations = new ArrayList<ThreadAnimate>();
	}

	private void animateItem(Item object) {
		ThreadAnimate animation = new ThreadAnimate(object, space);
		if (!animations.contains(animation)
				|| (animations.contains(animation) && !(animations.get(animations.indexOf(animation)).isAlive()))) {
			animations.add(animation);
			animation.start();
		}

	}

	@Override
	public void run() {

		space.createHitboxes();

		pickup();

		accel();

		gravity();

		move();

		collideAll();

		renderObjects();

		running = false;
	}

	private void pickup() {
		// The... Pickup... line?
		int i = 0;
		if (space.isLShiftPressed() && space.isRunnableP1() && space.getPlayers()[i].getItem() == null) {
			space.setLShiftPressed(false);
			for (Item item : space.getItems()) {
				if (item.getHitbox().intersects(space.getPlayers()[i].getHitbox()) && item.getPlayer() == null
						&& space.getPlayers()[i].getItem() == null) {
					space.getPlayers()[i].setItem(item);
					item.setPlayer(space.getPlayers()[i]);
				}
			}
		} else if (space.isLShiftPressed()) {
			space.setLShiftPressed(false);
			animateItem(space.getPlayers()[i].getItem());
		}

		i = 1;
		if (space.isRShiftPressed() && space.isRunnableP2() && space.getPlayers()[i].getItem() == null) {
			space.setRShiftPressed(false);
			for (Item item : space.getItems()) {
				if (item.getHitbox().intersects(space.getPlayers()[i].getHitbox()) && item.getPlayer() == null
						&& space.getPlayers()[i].getItem() == null) {
					space.getPlayers()[i].setItem(item);
					item.setPlayer(space.getPlayers()[i]);
				}
			}
		} else if (space.isRShiftPressed()) {
			space.setRShiftPressed(false);
			animateItem(space.getPlayers()[i].getItem());
		}
	}

	private void accel() {
		// p1
		int i = 0;

		if (space.isRunnableP1()) {

			if (!space.getPlayers()[i].isStunned()) {

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
			} else if (space.isWReleased() && space.getPlayers()[i].getJumps() > 2 && !space.getPlayers()[i].isDark()) { // Darker
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

			if (!space.getPlayers()[i].isStunned()) {
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
			} else if (space.isUpReleased() && space.getPlayers()[i].getJumps() > 2
					&& !space.getPlayers()[i].isDark()) { // darker
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
	}

	private void gravity() {

		// Gravity Players 1 and 2
		for (Player p : space.getPlayers())
			if (p.getAccelY() <= 0) {
				p.setAccelY(p.getAccelY() + space.getGRAVITY());
			} else {
				p.setAccelY(p.getAccelY() - 2);
			}

		// Gravity on items
		for (Item item : space.getItems()) {
			if (item.getPlayer() == null) {
				item.setAccelY(space.getITEMGRAVITY());
			}
		}
	}

	private void move() {

		// if acceleration of x, do acceleration, decreasing it by one each time.
		for (Player p : space.getPlayers()) {
			if (p.getAccelX() > 0) {
				p.setX(p.getX() + p.getAccelX());
				p.setAccelX(p.getAccelX() - space.getFRICTION());
				;
			}
			if (p.getAccelX() < 0) {
				p.setX(p.getX() + p.getAccelX());
				p.setAccelX(p.getAccelX() + space.getFRICTION());
			}
			if (p.getAccelY() > 0) {
				p.setY(p.getY() - p.getAccelY());
			}
			if (p.getAccelY() < 0) {
				p.setY(p.getY() - p.getAccelY());
			}
		}

		for (Item i : space.getItems()) {
			if (i.getAccelX() > 0) {
				i.setX(i.getX() + i.getAccelX());
				i.setAccelX(i.getAccelX() - space.getFRICTION());
				;
			}
			if (i.getAccelX() < 0) {
				i.setX(i.getX() + i.getAccelX());
				i.setAccelX(i.getAccelX() + space.getFRICTION());
			}
			if (i.getAccelY() > 0) {
				i.setY(i.getY() - i.getAccelY());
			}
			if (i.getAccelY() < 0) {
				i.setY(i.getY() - i.getAccelY());
			}
		}
	}

	private void collideAll() {
		// Collides objects
		for (Player p : space.getPlayers()) {

			// If at wall, loop over (haha)
			if (p.getX() < 0 - p.getWidth())
				p.setX(space.getWIDTH());
			if (p.getX() > space.getWIDTH())
				p.setX(0 - p.getWidth());

			// If at floor, don't move through
			if (p.getY() + p.getHeight() + 35 > space.getHEIGHT()) {
				p.setY(space.getHEIGHT() - (p.getHeight() + 35));
				p.setJumps(0);
				p.setFalling(false);
				;
			}
		}

		// When items hit the ground, stop motion
		for (Item i : space.getItems()) {
			if (i.getY() + i.getHeight() + 35 > space.getHEIGHT()) {
				i.setY(space.getHEIGHT() - (i.getHeight() + 35));
			}
		}
	}

	private void renderObjects() {

		// Sets pos and image for Items
		for (Item item : space.getItems()) {
			if (item.getPlayer() != null) {
				if (item.getDirection() != item.getPlayer().getDirection()) {
					item.setDirection(item.getPlayer().getDirection());
					if (item.getCurrentImage().getHeight(space) > 0 && item.getCurrentImage().getWidth(space) > 0)
						item.setCurrentImage(space.getTools().flipObject(item.getCurrentImage()));
				}
				if (item.getDirection() == -1) {
					item.setX((int) item.getPlayer().leftHandItemLocation().getX());
					item.setY((int) item.getPlayer().leftHandItemLocation().getY());
				}
				if (item.getDirection() == 1) {
					item.setX((int) item.getPlayer().rightHandItemLocation().getX());
					item.setY((int) item.getPlayer().rightHandItemLocation().getY());
				}
			}
		}
	}

	public boolean isRunning() {
		return running;
	}
}
