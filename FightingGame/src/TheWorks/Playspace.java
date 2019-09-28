package TheWorks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Items.Item;
import Items.Player;
import Items.Stick;
import Threads.ThreadPhysics;
import Threads.ThreadSound;

public class Playspace extends JPanel implements KeyListener, Runnable {

	private static final long serialVersionUID = 2089638191057847879L;

	// Constants and Classes
	private final Application app; // Parent window
	private final Color backgroundColor; // Color of backdrop
	private final ToolBox Tools; // All Image tools and methods
	private final int WIDTH; // Width of panel
	private final int HEIGHT; // Height of panel
	private final int PLAYERCOUNT; // Changing amount of players
	private final int DELAY = 20; // Delay of timer in ms
	private final int GRAVITY = -4; // Quadratic gravity for players
	private final int ITEMGRAVITY = -6; // Linear gravity for items
	private final int FRICTION = 2; // Deceleration on objects
	private final int ITEMCOUNT = 2; // The number of items on board at start
	private final int DASHSPEED = 4; // Speed at which players change direction
	private final int PLAYERSPEED = 8; // Speed of players
	private final int JUMPHEIGHT = 13; // Height of jump
	private final int FALLSPEED = -10; // Speed of fast fall

	// Stuff for game loop
	private final int TICKS_PER_SECOND = 50;
	private final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
	private final int MAX_FRAMESKIP = 10;
	private long nextGameTick;
	private int loops;
	private boolean gameRunning;;

	// Here we go... Threads...
	private ThreadPhysics doPhysics;
	private ThreadSound playSound;
	private Thread gameLoop;

	// Variables
	private boolean[] runnable; // We don't want null pointers
	private boolean developerMode;

	// Arrays
	private Player[] players; // Array of players
	private JLabel[] healthBars; // number on bar
	private JLabel[] healthBarIndicators; // bar itself
	private ArrayList<Item> items; // current items

	/** Constructor, sets frame and arrays up **/
	public Playspace(int mode, Application app) {

		// Sets up JPanel
		super();

		// Application variables
		this.app = app;
		HEIGHT = this.app.getHeight();
		WIDTH = this.app.getWidth();
		setLayout(null);
		setLocation(0, 0);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setFocusable(true);
		addKeyListener(this);
		requestFocusInWindow();
		backgroundColor = new Color(50, 50, 60);
		backgroundColor.brighter();
		setBackground(backgroundColor);

		// Gotta make all of these the right size...
		PLAYERCOUNT = mode;

		// Array inits
		players = new Player[PLAYERCOUNT];
		healthBars = new JLabel[PLAYERCOUNT];
		healthBarIndicators = new JLabel[PLAYERCOUNT];
		runnable = new boolean[PLAYERCOUNT];
		items = new ArrayList<Item>();

		// Toolbox setup
		Tools = new ToolBox(this);

		// Dev mode
		this.developerMode = false;

		// Adds both players to board array
		switch (mode) {
		case 1:
			players[0] = new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png", this);
			break;

		case 2:
			players[0] = new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png", this);
			players[1] = new Player("src/resources/stickRed.png", "src/resources/darkStickRed.png", this);
			break;

		default:
			players[0] = new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png", this);
			players[1] = new Player("src/resources/stickRed.png", "src/resources/darkStickRed.png", this);
			break;

		}

		// Loads items
		for (int i = 0; i < ITEMCOUNT; i++) {
			int spawnVal = (int) (Math.random() * 100);
			if (spawnVal < new Stick(this).getDropRate()) {
				items.add(new Stick(this));
			}
		}

		// Loads item images
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
				players[i].setX((WIDTH / (i) - 100) - players[i].getImage().getWidth(this));
				players[i].setY(HEIGHT);
			}
	}

	/** Checks validity and initiates game **/
	public void setup() {

		// Try{} Catches
		checkPlayerMethodValidity();

		// Thread setup
		initThreads();
		playSound("src/resources/Clayfighter (SNES) - Taffy's Theme.wav");

	}

	/** Checks if each Player exists to prevent null pointers **/
	private void checkPlayerMethodValidity() {

		for (int i = 0; i < players.length; i++)
			try {
				if (players[i] != null) {
					runnable[i] = true;
				} else {
					runnable[i] = false;
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				runnable[i] = false;
			}
	}

	/** Initiates threads for values **/
	private void initThreads() {
		// Thread Constructor Calls
		doPhysics = new ThreadPhysics(this);
		gameLoop = new Thread(this);

		// Thread start
		doPhysics.start();
		gameLoop.start();
	}

	/** !!UNSTABLE!! Resets the physics thread **/
	private void resetThreads() {

		// If physics is done doing thing
		if (doPhysics.isAlive()) {
			return;
		} else {
			// Start it again
			doPhysics = new ThreadPhysics(this);
			doPhysics.start();
		}
	}

	/** Begins background music player **/
	public void playSound(String soundFile) {

		playSound = new ThreadSound(soundFile);
		playSound.start();
	}

	/** Creates new hitboxes for Items and Players **/
	public void createHitboxes() {

		for (Player p : players) {
			p.setHitbox(new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight()));
		}

		for (Item i : items) {
			i.setHitbox(new Rectangle(i.getX(), i.getY(), i.getWidth(), i.getHeight()));
		}
	}

	/** This is the main game loop thread **/
	@Override
	public void run() {
		gameRunning = true;
		nextGameTick = System.currentTimeMillis();
		while (gameRunning) {

			loops = 0;

			while (System.currentTimeMillis() > nextGameTick && loops < MAX_FRAMESKIP) {
				resetThreads();

				nextGameTick += SKIP_TICKS;
				loops++;
			}

			repaint();
		}

	}

	/** Draws everything on screen **/
	@Override
	public void paintComponent(Graphics g) {

		// Draws main graphics
		super.paintComponent(g);

		// Loop for each player
		for (int i = 0; i < players.length; i++) {

			// Draw players
			g.drawImage(players[i].getImage(), players[i].getX(), players[i].getY(), this);

			if (developerMode) {
				g.draw3DRect(players[i].getX(), players[i].getY(), players[i].getWidth(), players[i].getHeight(), true);
			}

			// Draw health bars
			healthBars[i].setText("" + players[i].getHealth());
			healthBars[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setSize(50 * (players[i].getHealth() / 1000), 10);
		}

		// Draw items loop
		for (Item item : items) {

			if (item != null) {

				// Draw item images
				g.drawImage(item.getCurrentImage(), item.getX(), item.getY(), this);

				if (developerMode) {
					g.draw3DRect(item.getX(), item.getY(), item.getWidth(), item.getHeight(), true);
				}
			}
		}
	}

	/** Keypress detection **/
	@Override
	public void keyPressed(KeyEvent e) {

		int i = 0;
		if (runnable[i]) {
			// WASD Controls
			if (e.getKeyCode() == KeyEvent.VK_A) {
				players[i].setLeftPressed(true);
				players[i].setDirection(-1);
			}

			if (e.getKeyCode() == KeyEvent.VK_D) {
				players[i].setRightPressed(true);
				players[i].setDirection(1);
			}

			if (e.getKeyCode() == KeyEvent.VK_S) {
				players[i].setDownPressed(true);
			}

			if (e.getKeyCode() == KeyEvent.VK_W) {
				players[i].setUpPressed(true);
				players[i].setUpReleased(false);
			}

			if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT
					&& players[i].isShiftReleased()) {
				players[i].setShiftPressed(true);
				players[i].setShiftReleased(false);
			}
		}

		i = 1;
		if (runnable[i]) {
			// ULDR Controls
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				players[i].setLeftPressed(true);
				players[i].setDirection(-1);
			}

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				players[i].setRightPressed(true);
				players[i].setDirection(1);
			}

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				players[i].setDownPressed(true);
			}

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				players[i].setUpPressed(true);
				players[i].setUpReleased(false);
			}

			if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT
					&& players[i].isShiftReleased()) {
				players[i].setShiftPressed(true);
				players[i].setShiftReleased(false);
			}
		}
	}

	/** Key Release detection **/
	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_F11) {
			developerMode = !developerMode;
		}

		int i = 0;
		// WASD Controls
		if (e.getKeyCode() == KeyEvent.VK_A) {
			players[i].setLeftPressed(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			players[i].setDownPressed(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_D) {
			players[i].setRightPressed(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_W) {
			players[i].setUpPressed(false);
			players[i].setUpReleased(true);
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
			players[i].setShiftPressed(false);
			players[i].setShiftReleased(true);
		}

		i = 1;
		// ULDR Controls
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			players[i].setUpPressed(false);
			players[i].setUpReleased(true);
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			players[i].setDownPressed(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			players[i].setLeftPressed(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			players[i].setRightPressed(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
			players[i].setShiftPressed(false);
			players[i].setShiftReleased(true);
		}
	}

	/** We need this, but would rather forget it... **/
	@Override
	public void keyTyped(KeyEvent e) {
	}

	// ALL GETTERS AND SETTERS //
	public boolean[] getPlayerRunnable() {
		return runnable;
	}

	public void setPlayerRunnable(boolean[] runnable) {
		this.runnable = runnable;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public ToolBox getTools() {
		return Tools;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public int getPLAYERCOUNT() {
		return PLAYERCOUNT;
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public int getDELAY() {
		return DELAY;
	}

	public int getGRAVITY() {
		return GRAVITY;
	}

	public int getITEMGRAVITY() {
		return ITEMGRAVITY;
	}

	public int getFRICTION() {
		return FRICTION;
	}

	public int getITEMCOUNT() {
		return ITEMCOUNT;
	}

	public int getDASHSPEED() {
		return DASHSPEED;
	}

	public int getPLAYERSPEED() {
		return PLAYERSPEED;
	}

	public int getJUMPHEIGHT() {
		return JUMPHEIGHT;
	}

	public int getFALLSPEED() {
		return FALLSPEED;
	}

	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}

	public boolean isDeveloperMode() {
		return developerMode;
	}

	public void setDeveloperMode(boolean developerMode) {
		this.developerMode = developerMode;
	}
}