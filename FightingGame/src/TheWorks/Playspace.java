package TheWorks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class Playspace extends JPanel implements ActionListener, KeyListener {
	private static final int DELAY = 20;
	private Timer timer;
	public static final int GRAVITY = -4;
	public int area; // the size of the window on screen
	public Player[] players; // Array of players
	public Image[] images; // Player Images
	public int[] pAccelX; // acceleration of players X
	public int[] pAccelY; // acceleration of players Y
	public Image[] itemImages;
	public ArrayList<Item> items; // currently displayed items
	public KeyListener keylistener = this;
	public boolean APressed;
	public boolean SPressed;
	public boolean WPressed;
	public boolean WReleased;
	public boolean DPressed;
	public boolean UpPressed;
	public boolean UpReleased;
	public boolean LeftPressed;
	public boolean RightPressed;
	public boolean DownPressed;
	public final Item[] ITEMS = { new Stick() }; // all item types
	public final int WIDTH = 1000;
	public final int HEIGHT = 800;
	public final int playercount = 2; // number of players throughout the game
	public final int itemcount = 1; // the number of items on board at start
	public final int dashspeed = 7;
	public final int playerspeed = 7;
	public final int jumpheight = 12;
	public final int fallspeed = -25;
	public boolean jump1 = false; // if player1 is jumping
	public boolean jump2 = false; // if player2 is jumping
	public boolean fall[] = { false, false }; // if either player is falling
	public int[] jumps = { 0, 0 }; // number of jumps for each player
	public JTextArea[] healthBars;

	public Playspace(int i) { // Constructor, breaks Main from static.
		super(); // Sets up JPanel
		// Array inits
		players = new Player[playercount];
		images = new Image[playercount];
		pAccelX = new int[playercount];
		pAccelY = new int[playercount];
		items = new ArrayList<Item>(itemcount);
		itemImages = new Image[itemcount];
		healthBars = new JTextArea[playercount];

		// Timer setup
		timer = new Timer(DELAY, this);
		// Space setup
		initSpace(i);
		// Timer start
		timer.start(); // Starts timer
	}

	public void initSpace(int m) { // loads both characters, and sets up space

		// Adds both players to board array
		if (m == 1)
			add(new Player("src/resources/stickBlue.png"));
		if (m != 1)
			add(new Player("src/resources/stickBlue.png"), new Player("src/resources/stickRed.png"));

		// Loads ONLY images for PLAYERS
		for (int i = 0; i < players.length; i++)
			images[i] = loadObject(players[i].getImageDir());

		// Scales the images to correct size
		for (int i = 0; i < images.length; i++)
			images[i] = images[i].getScaledInstance(90, 150, Image.SCALE_SMOOTH);

		// Loads items, and by design, Item images
		for (int i = 0; i < items.size(); i++) {
			for (int k = 0; k < ITEMS.length; k++) {
				double chance = Math.random();
				if (chance <= ITEMS[0].getDropRate()) {
					items.add(ITEMS[i]);
				}
			}
		}

		// Sets health bars
		for (int i = 0; i < healthBars.length; i++) {
			healthBars[i] = new JTextArea("" + players[i].getHealth());
			this.add(healthBars[i]);
		}

		// Sets start pos of players X
		for (int i = 0; i < players.length; i++)
			if (i == 0) {
				players[i].setX(0);
				players[i].setY(765);
			} else {
				players[i].setX((WIDTH / (i) - 100) - images[i].getWidth(this));
				players[i].setY(765);
			}

		setLocation(0, 0);
		setSize(WIDTH, HEIGHT);
		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);
	}

	// Begin player add
	public void add(Player a) {
		players[0] = a;
	}

	public void add(Player a, Player b) {
		players[0] = a;
		players[1] = b;
	}
	// End player add

	public Image loadObject(String Dir) {

		ImageIcon iIcon = new ImageIcon(Dir);
		Image i = iIcon.getImage();

		return i;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < players.length; i++) {
			g.drawImage(images[i], players[i].getX(), players[i].getY(), this);
			healthBars[i].setPosition(players[i].getX() + images[i].getHeight(this) / 2, 0);

		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		doGravity();
		doAcceleration();
		doMovement();
		collide();
		repaint();
		System.out.println(players[1].getY() + 150);
	}

	private void doAcceleration() {
		// Add acceleration if key is pressed
		// p1
		if (APressed) {
			if (pAccelX[0] == 0) {
				pAccelX[0] = -dashspeed * 2;
			} else {
				pAccelX[0] = -playerspeed * 2;
			}
		}
		if (DPressed) {
			if (pAccelX[0] == 0) {
				pAccelX[0] = dashspeed * 2;
			} else {
				pAccelX[0] = playerspeed * 2;
			}
		}
		if (WPressed && !jump1) {
			jump1 = true;
			pAccelY[0] = jumpheight * 2;
		}
		if (WReleased && jumps[0] <= 2) {
			jump1 = false;
			fall[0] = false;
			jumps[0]++;
			WReleased = false;
		}
		if (SPressed || fall[0]) {
			pAccelY[0] = fallspeed;
			fall[0] = true;
		}

		// p2
		if (LeftPressed) {
			if (pAccelX[1] == 0) {
				pAccelX[1] = -dashspeed * 2;
			} else {
				pAccelX[1] = -playerspeed * 2;
			}
		}
		if (RightPressed) {
			if (pAccelX[1] == 0) {
				pAccelX[1] = dashspeed * 2;
			} else {
				pAccelX[1] = playerspeed * 2;
			}
		}
		if (UpPressed && !jump2) {
			jump2 = true;
			fall[1] = false;
			pAccelY[1] = jumpheight * 2;
		}
		if (UpReleased && jumps[1] <= 2) {
			jump2 = false;
			jumps[1]++;
			UpReleased = false;
		}
		if (DownPressed || fall[1]) {
			pAccelY[1] = fallspeed;
			fall[1] = true;
		}
	}

	private void doGravity() {
		// Gravity Players 1 and 2
		for (int i = 0; i < players.length; i++)
			if (pAccelY[i] <= 0) {
				pAccelY[i] += GRAVITY;
			} else {
				pAccelY[i] -= 1;
			}
	}

	private void collide() {
		// If at a wall, loop or bounce
		for (int i = 0; i < players.length; i++) {
			if (players[i].getX() <= 0 - images[i].getWidth(this))
				players[i].setX(WIDTH - images[i].getWidth(this));
			if (players[i].getX() >= WIDTH + images[i].getWidth(this))
				players[i].setX(0 - images[i].getWidth(this));
			if (!(players[i].getY() + images[i].getHeight(this) + 35 < HEIGHT)) {
				jumps[i] = 0;
				fall[i] = false;
				players[i].setY(HEIGHT - (images[i].getHeight(this) + 35));
			}
		}
	}

	private void doMovement() {
		// if acceleration of x, carry out acceleration, decreasing it by one
		// half
		// each time.
		for (int i = 0; i < players.length; i++) {
			if (pAccelX[i] > 0) {
				players[i].setX(players[i].getX() + pAccelX[i]);
				pAccelX[i]--;
			}
			if (pAccelX[i] < 0) {
				players[i].setX(players[i].getX() + pAccelX[i]);
				pAccelX[i]++;
			}
			if (pAccelY[i] > 0) {
				players[i].setY(players[i].getY() - pAccelY[i]);
				pAccelY[i]--;
			}
			if (pAccelY[i] < 0) {
				players[i].setY(players[i].getY() - pAccelY[i]);
				pAccelY[i]++;
			}
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
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
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			LeftPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			RightPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			UpPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			DownPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
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
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
