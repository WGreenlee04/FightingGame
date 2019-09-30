package Threads;

import java.util.ArrayList;

import Items.Fist;
import Items.Item;
import Items.Player;
import TheWorks.Playspace;

public class ThreadPhysics extends Thread {

	private Playspace space;
	private boolean running;

	public ThreadPhysics(Playspace p) {
		super();
		this.space = p;
		running = true;
	}

	private void animateItem(Item object) {
		ThreadAnimate animation = new ThreadAnimate(object, space);
		object.setAnimated(true);
		animation.start();
	}

	private void damagePlayer(Player player, Item item) {
		ThreadDamage damage = new ThreadDamage(item, player, space);
		ThreadSound sound = new ThreadSound("src/resources/" + item.getName() + "Hit.wav");
		damage.start();
		sound.start();
	}

	@Override
	public void run() {

		space.createHitboxes();

		pickup();

		hit();

		accel();

		gravity();

		move();

		wallLoop();

		renderObjects();

		running = false;

		this.interrupt();
	}

	private void setUnarmed() {
		for (Player p : space.getPlayers()) {
			if (p.getItem() == null) {
				Item unarmed = new Fist(space);
				unarmed.setCurrentImage(space.getTools().scaleObject(unarmed.getCurrentImage(), unarmed.getWidth(),
						unarmed.getHeight()));
				unarmed.setPlayer(p);
				p.setItem(unarmed);
				ArrayList<Item> tempArray = space.getItems();
				tempArray.add(unarmed);
				space.setItems(tempArray);
				renderObjects();

			}
		}
	}

	private void pickup() {
		// The... Pickup... line?
		for (Player p : space.getPlayers()) {
			if (p.isShiftPressed() && (p.getItem() == null || p.getItem() instanceof Fist)) {
				p.setShiftPressed(false);
				for (Item item : space.getItems()) {
					if (item.getHitbox().intersects(p.getHitbox()) && item.getPlayer() == null
							&& (p.getItem() == null || p.getItem() instanceof Fist) && !p.isStunned()) {
						if (p.getItem() instanceof Fist)
							p.getItem().setPlayer(null);
						p.setItem(item);
						item.setPlayer(p);
					}
				}
			}
		}
	}

	private void hit() {
		for (Player attacker : space.getPlayers()) {
			if (attacker.isShiftPressed() && attacker.getItem() != null) {
				attacker.setShiftPressed(false);
				if (!attacker.getItem().isAnimated()) {
					animateItem(attacker.getItem());

					// THIS NEEDS TO BE MOVED INTO THE ANIMATE CLASS, AND CALLED ON HIT FRAMES
					for (Player target : space.getPlayers()) {
						if (!target.equals(attacker) && !target.isBeingDamaged()
								&& attacker.getItem().getHitbox().intersects(target.getHitbox())) {
							damagePlayer(target, attacker.getItem());
						}
					}
				}
			}
		}
	}

	private void accel() {
		// All players
		for (int i = 0; i < space.getPlayers().length; i++) {

			if (space.getPlayerRunnable()[i] && !space.getPlayers()[i].isStunned()) {
				// Left Movement w/ dash
				if (space.getPlayers()[i].isLeftPressed() && !space.getPlayers()[i].isRightPressed()) {
					if (space.getPlayers()[i].getAccelX() == 0) {
						space.getPlayers()[i].setAccelX(-space.getDASHSPEED() * 2);
					} else {
						space.getPlayers()[i].setAccelX(-space.getPLAYERSPEED() * 2);
					}
				}

				// Right Movement w/ dash
				if (space.getPlayers()[i].isRightPressed() && !space.getPlayers()[i].isLeftPressed()) {
					if (space.getPlayers()[i].getAccelX() == 0) {
						space.getPlayers()[i].setAccelX(space.getDASHSPEED() * 2);
					} else {
						space.getPlayers()[i].setAccelX(space.getPLAYERSPEED() * 2);
					}
				}

				// If you didn't just jump, and pressed jump, jump
				if (space.getPlayers()[i].isUpPressed() && !space.getPlayers()[i].isJumping()) {
					space.getPlayers()[i].setJumping(true);
					space.getPlayers()[i].setAccelY(space.getJUMPHEIGHT() * 2);
				}

				// You just jumped, and we need to increase jump counter and
				// reset
				// jump
				if (space.getPlayers()[i].isUpReleased() && space.getPlayers()[i].getJumps() <= 2) {
					space.getPlayers()[i].setJumping(false);
					space.getPlayers()[i].setFalling(false);
					space.getPlayers()[i].setJumps(space.getPlayers()[i].getJumps() + 1);
					space.getPlayers()[i].setUpReleased(false);
				} else if (space.getPlayers()[i].isUpReleased() && space.getPlayers()[i].getJumps() > 2
						&& !space.getPlayers()[i].isDark()) { // Darker
					space.getPlayers()[i].setImage(
							space.getTools().darkenObject(space.getPlayers()[i].getImage(), space.getPlayers()[i]));
					space.getPlayers()[i].setDark(true);
				} else if (space.getPlayers()[i].getJumps() == 0 && space.getPlayers()[i].isDark()) { // Lighter
					space.getPlayers()[i].setImage(
							space.getTools().lightenObject(space.getPlayers()[i].getImage(), space.getPlayers()[i]));
					space.getPlayers()[i].setDark(false);
				}

				// Fast falling
				if (space.getPlayers()[i].isDownPressed() || space.getPlayers()[i].isFalling()) {
					space.getPlayers()[i].setAccelY(space.getPlayers()[i].getAccelY() + space.getFALLSPEED());
					space.getPlayers()[i].setFalling(true);
				}
			}
		}
	}

	private void gravity() {

		// Gravity on players
		for (Player p : space.getPlayers())
			if (p.getAccelY() <= 0) {
				p.setAccelY(p.getAccelY() + space.getGRAVITY());
			} else {
				p.setAccelY(p.getAccelY() - 2);
			}

		// Gravity on items
		for (Item i : space.getItems()) {
			if (i.getPlayer() == null) {
				if (i.getAccelY() <= 0) {
					i.setAccelY(i.getAccelY() + space.getITEMGRAVITY());
				} else {
					i.setAccelY(i.getAccelY() - 2);
				}
			}
		}
	}

	private void move() {

		// if acceleration of x, do acceleration, decreasing it by one each
		// time.
		for (Player p : space.getPlayers()) {

			if (p.getAccelX() > 0) {
				p.setX(p.getX() + p.getAccelX());
				p.setAccelX(p.getAccelX() - space.getFRICTION());
			}
			if (p.getAccelX() < 0) {
				p.setX(p.getX() + p.getAccelX());
				p.setAccelX(p.getAccelX() + space.getFRICTION());
			}

			if (p.getAccelY() > 0) {
				p.setY(p.getY() - p.getAccelY());
			}
			if (!isCollidingFloor(p)) {
				if (p.getAccelY() < 0) {
					p.setY(p.getY() - p.getAccelY());
				}
			} else {
				p.setAccelY(0);
				p.setY(space.getFLOOR() - p.getHeight());
				p.setJumps(0);
				p.setFalling(false);
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
			if (!isCollidingFloor(i)) {
				if (i.getAccelY() < 0) {
					i.setY(i.getY() - i.getAccelY());
				}
			} else {
				i.setAccelY(0);
				i.setY(space.getFLOOR() - i.getHeight());
			}
		}
	}

	private void wallLoop() {

		for (Player p : space.getPlayers()) {

			// If at wall, loop over (haha)
			if (p.getX() < 0 - p.getWidth())
				p.setX(space.getWIDTH());
			if (p.getX() > space.getWIDTH())
				p.setX(0 - p.getWidth());
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

				if (!item.isAnimated()) {
					if (item.getDirection() == 1
							&& !space.getTools().CompareImages(item.getCurrentImage(), item.getOriginalImage())) {
						item.setCurrentImage(item.getOriginalImage());
					}

					if (item.getDirection() == -1
							&& space.getTools().CompareImages(item.getCurrentImage(), item.getOriginalImage())) {
						item.setCurrentImage(space.getTools().flipObject(item.getCurrentImage()));
					}
				}

				if (item.getDirection() == -1) {
					item.setX(
							(int) item.getPlayer().leftHandItemLocation().getX() - item.getWidth() + item.getxOffset());
					item.setY((int) item.getPlayer().leftHandItemLocation().getY() - item.getHeight()
							+ item.getyOffset());
				}
				if (item.getDirection() == 1) {
					item.setX((int) item.getPlayer().rightHandItemLocation().getX() + item.getWidth()
							+ item.getxOffset());
					item.setY((int) item.getPlayer().rightHandItemLocation().getY() - item.getHeight()
							+ item.getyOffset());
				}
			}
		}
	}

	private boolean isCollidingFloor(Player p) {

		boolean colliding = false;

		// If at floor, colliding
		if (p.getY() + p.getHeight() + (-p.getAccelY()) > space.getFLOOR()) {

			colliding = true;
		}
		return colliding;
	}

	private boolean isCollidingFloor(Item i) {

		boolean colliding = false;

		// When items hit the ground, stop motion
		if (i.getY() + i.getHeight() + (-i.getAccelY()) > space.getFLOOR()) {
			colliding = true;
		}

		return colliding;
	}

	private boolean isCollidingPlayers(Player p) {
		return false;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
