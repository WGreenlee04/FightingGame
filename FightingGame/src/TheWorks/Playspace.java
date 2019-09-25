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

	private static final long serialVersionUID = 2089638191057847879L;

	// Constants and Classes
	private Application app;
	private Timer timer;
	private ToolBox Tools;
	private final int WIDTH; // Width of panel
	private final int HEIGHT; // Height of panel
	private final int DELAY = 20; // Delay of actions in ms
	private final int GRAVITY = -4; // Quadratic gravity for players
	private final int ITEMGRAVITY = -6; // Linear gravity for items
	private final int FRICTION = 2; // Deceleration on objects
	private final int ITEMCOUNT = 2; // The number of items on board at start
	private final int DASHSPEED = 4; // Speed at which players change direction
	private final int PLAYERSPEED = 8; // Speed of players
	private final int JUMPHEIGHT = 13; // Height of jump
	private final int FALLSPEED = -10; // Speed of fast fall
	private final Color backgroundColor; // Color of backdrop
	private final int PLAYERCOUNT; // Changing amount of players

	// Here we go... Threads...
	private ThreadPhysics doPhysics;
	private ThreadSound playSound;

	// Variables
	private boolean[] runnable; // We don't want null pointers
	private boolean developerMode;

	// Arrays
	private Player[] players; // Array of players
	private JLabel[] healthBars; // number on bar
	private JLabel[] healthBarIndicators; // bar itself
	private ArrayList<Item> items; // current items
	private ArrayList<Thread> threads; // Active threads

	/** Constructor, sets up frame and arrays **/
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
		healthBars = new JLabel[PLAYERCOUNT];
		healthBarIndicators = new JLabel[PLAYERCOUNT];
		runnable = new boolean[PLAYERCOUNT];
		items = new ArrayList<Item>();
		threads = new ArrayList<Thread>();

		// Timer setup
		timer = new Timer(DELAY, this);

		// Toolbox setup
		Tools = new ToolBox(this);

		// Dev mode
		this.developerMode = false;

		// Adds both players to board array
		switch (mode) {
		case 1:
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png", this));
			break;

		case 2:
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png", this),
					new Player("src/resources/stickRed.png", "src/resources/darkStickRed.png", this));
			break;

		default:
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png", this),
					new Player("src/resources/stickRed.png", "src/resources/darkStickRed.png", this));
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

	/** Sets all array defaults and sets images **/
	public void initSpace() {

		// Try{}Catches
		checkPlayerMethodValidity();

		// Thread setup
		initThreads();
		playSound("src/resources/Clayfighter (SNES) - Taffy's Theme.wav");

		// Timer start
		timer.start(); // Starts timer
	}

	// SinglePlayer
	private void add(Player a) {
		players[0] = a;
	}

	// 2 Player
	private void add(Player a, Player b) {
		players[0] = a;
		players[1] = b;
	}

	/** Checks if each player exists to prevent null pointers **/
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

		// Thread Constructor Call
		doPhysics = new ThreadPhysics(this);

		// Thread array setup
		threads.add(doPhysics);
	}

	/** Initiates threads for values **/
	private void resetThreads() {

		// Old Thread for replacing
		Thread Acceleration = doPhysics;

		// Thread Constructor Call
		doPhysics = new ThreadPhysics(this);

		// Updates Array
		threads.set(threads.indexOf(Acceleration), doPhysics);

		// Start thread
		for (Thread t : threads) {
			if (!t.equals(playSound))
				t.start();
		}
	}

	/** Begins background music player **/
	public void playSound(String soundFile) {

		playSound = new ThreadSound(soundFile);
		threads.add(playSound);
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

	/** Triggered when "timer" completes a cycle **/
	@Override
	public void actionPerformed(ActionEvent arg0) {
		resetThreads();
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

			// Draw item images
			g.drawImage(item.getCurrentImage(), item.getX(), item.getY(), this);

			if (developerMode) {
				g.draw3DRect(item.getX(), item.getY(), item.getWidth(), item.getHeight(), true);
			}
		}
	}

	/** ALL GETTERS AND SETTERS **/
	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public ToolBox getTools() {
		return Tools;
	}

	public boolean[] getRunnable() {
		return runnable;
	}

	public void setRunnable(boolean[] runnable) {
		this.runnable = runnable;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public ArrayList<Thread> getThreads() {
		return threads;
	}

	public void setThreads(ArrayList<Thread> threads) {
		this.threads = threads;
	}

	public int getPLAYERCOUNT() {
		return PLAYERCOUNT;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public JLabel[] getHealthBars() {
		return healthBars;
	}

	public void setHealthBars(JLabel[] healthBars) {
		this.healthBars = healthBars;
	}

	public JLabel[] getHealthBarIndicators() {
		return healthBarIndicators;
	}

	public void setHealthBarIndicators(JLabel[] healthBarIndicators) {
		this.healthBarIndicators = healthBarIndicators;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
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
}