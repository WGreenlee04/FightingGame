package TheWorks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Playspace extends JPanel implements ActionListener, KeyListener {
	public final int WIDTH = 1000;
	public final int HEIGHT = 800;
	public Timer timer;
	public ToolBox Tools;
	public KeyListener keylistener = this;
	public final int DELAY = 20;
	public final int GRAVITY = -4; // Quadratic gravity for players
	public final int ITEMGRAVITY = -6; // Linear gravity for items
	public final int FRICTION = 2; // Deceleration on objects
	public final int ITEMCOUNT = 2; // the number of items on board at start
	public final int DASHSPEED = 4;
	public final int PLAYERSPEED = 8;
	public final int JUMPHEIGHT = 13;
	public final int FALLSPEED = -10;
	public final Item[] ITEMS = { new Stick() }; // all item types
	public Player[] players; // Array of players
	public Image[] images; // Player Images
	public int[] pAccelX; // acceleration of players X
	public int[] pAccelY; // acceleration of players Y
	public int[] jumps; // number of jumps for each player
	public JLabel[] healthBars; // number on bar
	public JLabel[] healthBarIndicators; // bar itself
	public boolean[] direction;
	public boolean[] isDark;
	public ArrayList<Item> items = new ArrayList<Item>(); // current items
	public Color backgroundColor;
	public boolean WPressed, WReleased, APressed, SPressed, DPressed, LShiftPressed, LShiftReleased, UpPressed,
			UpReleased, LeftPressed, DownPressed, RightPressed, RShiftPressed, RShiftReleased;
	public int PLAYERCOUNT = 2;
	public boolean jump1 = false; // if player1 is jumping
	public boolean jump2 = false; // if player2 is jumping
	public boolean fall[]; // if either player is falling

	// Constructor, breaks Main from static.
	public Playspace(int mode) {

		// Sets up JPanel
		super();
		setLayout(null);
		setLocation(0, 0);
		setSize(WIDTH, HEIGHT);
		setFocusable(true);
		addKeyListener(this);
		setVisible(true);
		backgroundColor = new Color(50, 50, 60);
		backgroundColor.brighter();
		setBackground(backgroundColor);

		// Gotta make all of these the right size...
		PLAYERCOUNT = mode;

		// Array inits
		players = new Player[PLAYERCOUNT];
		images = new Image[PLAYERCOUNT];
		pAccelX = new int[PLAYERCOUNT];
		pAccelY = new int[PLAYERCOUNT];
		healthBars = new JLabel[PLAYERCOUNT];
		healthBarIndicators = new JLabel[PLAYERCOUNT];
		fall = new boolean[PLAYERCOUNT];
		direction = new boolean[PLAYERCOUNT];
		jumps = new int[PLAYERCOUNT];
		isDark = new boolean[PLAYERCOUNT];

		// Timer setup
		timer = new Timer(DELAY, this);

		// Toolbox setup
		Tools = new ToolBox(this);
	}

	// loads both characters, and sets up space
	public void initSpace(int mode) {

		// Adds both players to board array
		switch (mode) {
		case 1:
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png"));
			PLAYERCOUNT = 1;
			break;

		case 2:
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png"),
					new Player("src/resources/stickRed.png", "src/resources/darkStickRed.png"));
			PLAYERCOUNT = 2;
			break;

		default:
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png"),
					new Player("src/resources/stickRed.png", "src/resources/darkStickRed.png"));
			PLAYERCOUNT = 2;
			break;

		}

		// Loads ONLY images for PLAYERS
		for (int i = 0; i < players.length; i++)
			images[i] = Tools.loadObject(players[i].getImageDir());

		// Scales the images to correct size
		for (int i = 0; i < images.length; i++)
			images[i] = Tools.scalePlayer(images[i], players[i]);

		// Loads items, and by design, Item images
		for (int i = 0; i < ITEMCOUNT; i++) {
			items.add(i, new Stick());
		}

		for (Item item : items) {
			int spawnVal = (int) (Math.random() * 100);
			for (Item ITEM : ITEMS) {
				if (ITEM.getDropRate() >= spawnVal) {
					items.set(items.indexOf(item), ITEM);
				}
			}
		}

		for (Item item : items) {
			item.setX((int) ((WIDTH / 3) + (WIDTH / 3 * Math.random())));
			Image scaledImage = Tools.scaleObject(item.getCurrentImage(), item.getWidth(), item.getHeight());
			item.setCurrentImage(scaledImage);
		}

		// Sets health bars and indicators
		for (int i = 0; i < healthBars.length; i++) {
			// Add bars
			healthBars[i] = new JLabel();
			this.add(healthBars[i]);
			healthBars[i].setSize(50, 10);
			healthBars[i].setVisible(true);
			healthBars[i].setHorizontalAlignment(SwingConstants.CENTER);
			healthBars[i].setVerticalAlignment(SwingConstants.CENTER);
			healthBars[i].setForeground(Color.white);

			// Add indicators
			healthBarIndicators[i] = new JLabel();
			this.add(healthBarIndicators[i]);
			healthBarIndicators[i].setVisible(true);
			healthBarIndicators[i].setBackground(Color.red);
			healthBarIndicators[i].setOpaque(true);
		}

		// Sets start pos of players X
		for (int i = 0; i < players.length; i++)
			if (i == 0) {
				players[i].setX(0);
				players[i].setY(HEIGHT);
			} else {
				players[i].setX((WIDTH / (i) - 100) - images[i].getWidth(this));
				players[i].setY(HEIGHT);
			}

		// Set default array values
		for (int i = 0; i < PLAYERCOUNT; i++) {
			fall[i] = false;
			direction[i] = true;
			jumps[i] = 0;
			isDark[i] = false;
		}

		// Timer start
		timer.start(); // Starts timer
	}

	// Begin player add
	// SinglePlayer
	public void add(Player a) {
		players[0] = a;
	}

	// 2 Player
	public void add(Player a, Player b) {
		players[0] = a;
		players[1] = b;
	}
	// End player add

	// Triggered when "timer" completes a cycle
	@Override
	public void actionPerformed(ActionEvent arg0) {

		// Player status methods, including animations
		pickup();

		// Position methods
		doAccelerationP1();
		doAccelerationP2();
		doMovement();
		doGravity();
		doCollision();
		renderItems();
		repaint();
	}

	public void pickup() {

		boolean runnableP1;
		try {
			if (players[1] != null) {
				runnableP1 = true;
			} else {
				runnableP1 = false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			runnableP1 = false;
		}

		if (LShiftPressed && players[0].getItem() == null && runnableP1) {
			LShiftPressed = false;
			Rectangle[] itemRectangles = new Rectangle[items.size()];
			Rectangle p1 = new Rectangle(players[0].getX(), players[0].getY(), images[0].getWidth(this),
					images[0].getHeight(this));
			for (Item item : items) {
				itemRectangles[items.indexOf(item)] = new Rectangle(item.getX(), item.getY(), item.getWidth(),
						item.getHeight());
				if (itemRectangles[items.indexOf(item)].intersects(p1) && item.getPlayer() == null) {
					players[0].setItem(item);
					item.setPlayer(players[0]);
				}
			}
		}

		boolean runnableP2;
		try {
			if (players[2] != null) {
				runnableP2 = true;
			} else {
				runnableP2 = false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			runnableP2 = false;
		}

		if (RShiftPressed && players[1].getItem() == null && runnableP2) {
			RShiftPressed = false;
			Rectangle[] itemRectangles = new Rectangle[items.size()];
			Rectangle p2 = new Rectangle(players[1].getX(), players[1].getY(), images[1].getWidth(this),
					images[1].getHeight(this));
			for (Item item : items) {
				itemRectangles[items.indexOf(item)] = new Rectangle(item.getX(), item.getY(), item.getWidth(),
						item.getHeight());
				if (itemRectangles[items.indexOf(item)].intersects(p2) && item.getPlayer() == null) {
					players[1].setItem(item);
					item.setPlayer(players[1]);
				}
			}
		}

	}

	public void renderItems() {
		for (Item item : items) {
			if (item.getPlayer() != null) {
				item.setX(item.getPlayer().getX());
				item.setY(item.getPlayer().getY());
				if (item.getDirection() != item.getPlayer().getDirection()) {
					item.setDirection(item.getPlayer().getDirection());
					item.setCurrentImage(Tools.flipObject(item.getCurrentImage()));
				}
			}
		}
	}

	public void doAccelerationP1() {
		// p1
		int i = 0;

		boolean runnable;
		try {
			if (players[i] != null) {
				runnable = true;
			} else {
				runnable = false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			runnable = false;
		}
		if (runnable) {
			// Left Movement w/ dash
			if (APressed && !DPressed) {
				if (pAccelX[i] == 0) {
					pAccelX[i] = -DASHSPEED * 2;
				} else {
					pAccelX[i] = -PLAYERSPEED * 2;
				}
				players[i].setDirection(-1);
			}

			// Right Movement w/ dash
			if (DPressed && !APressed) {
				if (pAccelX[i] == 0) {
					pAccelX[i] = DASHSPEED * 2;
				} else {
					pAccelX[i] = PLAYERSPEED * 2;
				}
				players[i].setDirection(1);
			}

			// If you didn't just jump, and pressed jump, jump
			if (WPressed && !jump1) {
				jump1 = true;
				pAccelY[i] = JUMPHEIGHT * 2;
			}

			// You just jumped, and we need to increase jump counter and reset jump
			if (WReleased && jumps[i] <= 2) {
				jump1 = false;
				fall[i] = false;
				jumps[i]++;
				WReleased = false;
			} else if (jumps[i] > 2 && !isDark[i]) { // darker color, out of jumps
				images[i] = Tools.darkenObject(images[i], players[i]);
				isDark[i] = true;
			} else if (jumps[i] == 0 && isDark[i]) { // you can be light
				images[i] = Tools.lightenObject(images[i], players[i]);
				isDark[i] = false;
			}

			// Fast falling
			if (SPressed || fall[i]) {
				pAccelY[i] += FALLSPEED;
				fall[i] = true;
			}
		}
	}

	public void doAccelerationP2() {
		// p2
		int i = 1;
		boolean runnable;
		try {
			if (players[i] != null) {
				runnable = true;
			} else {
				runnable = false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			runnable = false;
		}
		if (runnable) {
			// Left Movement w/ dash
			if (LeftPressed && !RightPressed) {
				if (pAccelX[i] == 0) {
					pAccelX[i] = -DASHSPEED * 2;
				} else {
					pAccelX[i] = -PLAYERSPEED * 2;
				}
				players[i].setDirection(-1);
			}

			// Right Movement w/ dash
			if (RightPressed && !LeftPressed) {
				if (pAccelX[i] == 0) {
					pAccelX[i] = DASHSPEED * 2;
				} else {
					pAccelX[i] = PLAYERSPEED * 2;
				}
				players[i].setDirection(1);
			}

			// If you didn't just jump, and pressed jump, then jump
			if (UpPressed && !jump2) {
				jump2 = true;
				fall[i] = false;
				pAccelY[i] = JUMPHEIGHT * 2;
			}

			// You just jumped, and we need to increase jump counter and reset jump
			if (UpReleased && jumps[i] <= 2) {
				jump2 = false;
				jumps[i]++;
				UpReleased = false;
			} else if (jumps[i] > 2 && !isDark[i]) {// darker color, out of jumps
				images[i] = Tools.darkenObject(images[i], players[i]);
				isDark[i] = true;

			} else if (jumps[i] == 0 && isDark[i]) { // lighter color
				images[i] = Tools.lightenObject(images[i], players[i]);
				isDark[i] = false;
			}

			// Fast falling
			if (DownPressed || fall[1]) {
				pAccelY[i] += FALLSPEED;
				fall[i] = true;
			}
		}
	}

	public void doGravity() {

		// Gravity Players 1 and 2
		for (int i = 0; i < players.length; i++)
			if (pAccelY[i] <= 0) {
				pAccelY[i] += GRAVITY;
			} else {
				pAccelY[i] -= 2;
			}

		// Gravity on items
		for (Item item : items) {
			if (item.getPlayer() == null) {
				item.setY(item.getY() - ITEMGRAVITY);
			}
		}
	}

	public void doCollision() {

		// looping through the number of players
		for (int i = 0; i < players.length; i++) {

			// If at wall, loop
			if (players[i].getX() < 0 - players[i].getWidth())
				players[i].setX(WIDTH);
			if (players[i].getX() > WIDTH)
				players[i].setX(0 - players[i].getWidth());

			// If at floor, don't move through
			if (players[i].getY() + players[i].getHeight() + 35 > HEIGHT) {
				players[i].setY(HEIGHT - (players[i].getHeight() + 35));
				jumps[i] = 0;
				fall[i] = false;
			}
		}

		// When items hit the ground, stop motion
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getY() + items.get(i).getCurrentImage().getHeight(this) + 30 > HEIGHT) {
				items.get(i).setY(HEIGHT - (items.get(i).getCurrentImage().getHeight(this) + 30));
			}
		}
	}

	public void doMovement() {
		// if acceleration of x, carry out acceleration, decreasing it by one
		// each time.
		for (int i = 0; i < players.length; i++) {
			if (pAccelX[i] > 0) {
				players[i].setX(players[i].getX() + pAccelX[i]);
				pAccelX[i] -= FRICTION;
			}
			if (pAccelX[i] < 0) {
				players[i].setX(players[i].getX() + pAccelX[i]);
				pAccelX[i] += FRICTION;
			}
			if (pAccelY[i] > 0) {
				players[i].setY(players[i].getY() - pAccelY[i]);
			}
			if (pAccelY[i] < 0) {
				players[i].setY(players[i].getY() - pAccelY[i]);
			}
		}

	}

	// Keypress detection
	@Override
	public void keyPressed(KeyEvent e) {

		// WASD Controls
		if (e.getKeyCode() == KeyEvent.VK_A) {
			APressed = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_D) {
			DPressed = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			SPressed = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_W) {
			WPressed = true;
			WReleased = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
			LShiftPressed = true;
			LShiftReleased = false;
		}

		// ULDR Controls
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			LeftPressed = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			RightPressed = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			UpPressed = true;
			UpReleased = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			DownPressed = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
			RShiftPressed = true;
			RShiftReleased = false;
		}
	}

	// Key Release detection
	@Override
	public void keyReleased(KeyEvent e) {

		// WASD Controls
		if (e.getKeyCode() == KeyEvent.VK_A) {
			APressed = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			SPressed = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_D) {
			DPressed = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_W) {
			WPressed = false;
			WReleased = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
			LShiftPressed = false;
			LShiftReleased = true;
		}

		// ULDR Controls
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			UpPressed = false;
			UpReleased = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			DownPressed = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			LeftPressed = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			RightPressed = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
			RShiftPressed = false;
			RShiftReleased = true;
		}
	}

	// We need this, but would rather forget it...
	@Override
	public void keyTyped(KeyEvent e) {
	}

	// Draws everything on screen
	@Override
	public void paintComponent(Graphics g) {

		// Draws main graphics
		super.paintComponent(g);

		// Loop for each player
		for (int i = 0; i < players.length; i++) {

			// Draw players
			g.drawImage(images[i], players[i].getX(), players[i].getY(), this);

			// Draw health bars
			healthBars[i].setText("" + players[i].getHealth());
			healthBars[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setSize((int) 50 * (players[i].getHealth() / 1000), 10);
		}

		// Draw items loop
		for (Item item : items) {

			// Draw item images
			g.drawImage(item.getCurrentImage(), item.getX(), item.getY(), this);
		}
	}
}
