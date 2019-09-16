package TheWorks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
	public final int jumpheight = 13;
	public final int fallspeed = -10;
	public boolean jump1 = false; // if player1 is jumping
	public boolean jump2 = false; // if player2 is jumping
	public boolean fall[] = { false, false }; // if either player is falling
	public int[] jumps = { 0, 0 }; // number of jumps for each player
	public JLabel[] healthBars;
	public JLabel[] healthBarIndicators;
	public Color backgroundColor;
	public boolean[] wasAirborne = { false, false };

	public Playspace(int i) { // Constructor, breaks Main from static.
		super(); // Sets up JPanel
		setLayout(null);
		setLocation(0, 0);
		setSize(WIDTH, HEIGHT);
		setFocusable(true);
		addKeyListener(this);
		setVisible(true);

		backgroundColor = new Color(50, 50, 60);
		backgroundColor.brighter();
		setBackground(backgroundColor);

		// Array inits
		players = new Player[playercount];
		images = new Image[playercount];
		pAccelX = new int[playercount];
		pAccelY = new int[playercount];
		items = new ArrayList<Item>(itemcount);
		healthBars = new JLabel[playercount];
		healthBarIndicators = new JLabel[playercount];

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
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue"));
		if (m != 1)
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue"),
					new Player("src/resources/stickRed.png", "src/resources/darkStickRed"));

		// Loads ONLY images for PLAYERS
		for (int i = 0; i < players.length; i++)
			images[i] = loadObject(players[i].getImageDir());

		// Scales the images to correct size
		for (int i = 0; i < images.length; i++)
			images[i] = images[i].getScaledInstance(players[i].getWidth(), players[i].getHeight(), Image.SCALE_SMOOTH);

		// Loads items, and by design, Item images
		for (int i = 0; i < items.size(); i++) {
			items.add(i, ITEMS[0]);
		}

		// Sets health bars and indicators
		for (int i = 0; i < healthBars.length; i++) {
			healthBars[i] = new JLabel();
			this.add(healthBars[i]);
			healthBars[i].setSize(50, 10);
			healthBars[i].setVisible(true);
			healthBars[i].setHorizontalAlignment(SwingConstants.CENTER);
			healthBars[i].setVerticalAlignment(SwingConstants.CENTER);
			healthBars[i].setForeground(Color.white);
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
				players[i].setY(765);
			} else {
				players[i].setX((WIDTH / (i) - 100) - images[i].getWidth(this));
				players[i].setY(765);
			}
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

	public Image rotateObject(Image image, int degrees) {
		double rotationRequired = Math.toRadians(degrees);
		double locationX = image.getWidth(this) / 2;
		double locationY = image.getHeight(this) / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter((BufferedImage) image, null);
	}

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

			// Draw health bars
			healthBars[i].setText("" + players[i].getHealth());
			healthBars[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setSize((int) 50 * (players[i].getHealth() / 1000), 10);

			for (int j = 0; i < items.size(); i++) {
				if (players[i].getDirection() == -1) {
					g.drawImage(rotateObject(items.get(j).getCurrentImage(), 90), players[i].getX() - 20,
							players[i].getY(), this);
				} else {
					g.drawImage(items.get(j).getCurrentImage(), players[i].getX() + 20, players[i].getY(), this);
				}
			}
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
			players[0].setDirection(-1);
		}
		if (DPressed) {
			if (pAccelX[0] == 0) {
				pAccelX[0] = dashspeed * 2;
			} else {
				pAccelX[0] = playerspeed * 2;
			}
			players[0].setDirection(1);
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
		} else if (WReleased) {
			darkenObject(0);
			wasAirborne[0] = true;
		} else if (jumps[0] == 0 && wasAirborne[0] && pAccelY[1] <= 1) {
			lightenObject(0);
			wasAirborne[0] = false;
		}

		if (SPressed || fall[0]) {
			pAccelY[0] += fallspeed;
			fall[0] = true;
		}

		// p2
		if (LeftPressed) {
			if (pAccelX[1] == 0) {
				pAccelX[1] = -dashspeed * 2;
			} else {
				pAccelX[1] = -playerspeed * 2;
				players[1].setDirection(-1);
			}
		}
		if (RightPressed) {
			if (pAccelX[1] == 0) {
				pAccelX[1] = dashspeed * 2;
			} else {
				pAccelX[1] = playerspeed * 2;
				players[1].setDirection(1);
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
		} else if (UpReleased) {
			darkenObject(1);
			wasAirborne[1] = true;
		} else if (jumps[1] == 0 && wasAirborne[1] && pAccelY[1] <= 1) {
			lightenObject(1);
			wasAirborne[1] = false;
		}

		if (DownPressed || fall[1]) {
			pAccelY[1] += fallspeed;
			fall[1] = true;
		}
	}

	private void lightenObject(int i) {
		images[i] = new ImageIcon(players[i].getImageDir()).getImage();

	}

	private void darkenObject(int i) {
		images[i] = new ImageIcon(players[i].getDarkImageDir()).getImage();

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
