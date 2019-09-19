package TheWorks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
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
	public static final int GRAVITY = -4; // Quadratic gravity for players
	public static final int ITEMGRAVITY = -6; // Linear gravity for items
	public static final int FRICTION = 2;
	public final int WIDTH = 1000;
	public final int HEIGHT = 800;
	public final int playercount = 2; // number of players throughout the game
	public final int itemcount = 2; // the number of items on board at start
	public final int dashspeed = 4;
	public final int playerspeed = 8;
	public final int jumpheight = 13;
	public final int fallspeed = -10;
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
	public ArrayList<Item> items = new ArrayList<Item>(); // currently displayed
															// items
	public Color backgroundColor;
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
	public boolean LShiftPressed;
	public boolean LShiftReleased;
	public boolean RShiftPressed;
	public boolean RShiftReleased;
	public boolean jump1 = false; // if player1 is jumping
	public boolean jump2 = false; // if player2 is jumping
	public boolean fall[]; // if either player is falling

	// Constructor, breaks Main from static.
	public Playspace(int i) {
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

		// Array inits
		players = new Player[playercount];
		images = new Image[playercount];
		pAccelX = new int[playercount];
		pAccelY = new int[playercount];
		healthBars = new JLabel[playercount];
		healthBarIndicators = new JLabel[playercount];
		fall = new boolean[playercount];
		direction = new boolean[playercount];
		jumps = new int[playercount];
		isDark = new boolean[playercount];

		// Timer setup
		timer = new Timer(DELAY, this);

		// Space setup
		initSpace(i);

		// Timer start
		timer.start(); // Starts timer
	}

	public void initSpace(int m) { // loads both characters, and sets up space

		// Adds both players to board array
		switch (m) {
		case 1:
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png"));
			break;

		case 2:
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png"),
					new Player("src/resources/stickRed.png", "src/resources/darkStickRed.png"));
			break;

		default:
			add(new Player("src/resources/stickBlue.png", "src/resources/darkStickBlue.png"),
					new Player("src/resources/stickRed.png", "src/resources/darkStickRed.png"));
			break;

		}

		// Loads ONLY images for PLAYERS
		for (int i = 0; i < players.length; i++)
			images[i] = loadObject(players[i].getImageDir());

		// Scales the images to correct size
		for (int i = 0; i < images.length; i++)
			images[i] = scalePlayer(images[i], players[i]);

		// Loads items, and by design, Item images
		for (int i = 0; i < itemcount; i++) {
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
			Image scaledImage = scaleObject(item.getCurrentImage(), item.getWidth(), item.getHeight());
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
		for (int i = 0; i < playercount; i++) {
			fall[i] = false;
			direction[i] = true;
			jumps[i] = 0;
			isDark[i] = false;
		}
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

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < players.length; i++) {
			// Draw players
			g.drawImage(images[i], players[i].getX(), players[i].getY(), this);

			// Draw health bars
			healthBars[i].setText("" + players[i].getHealth());
			healthBars[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setSize((int) 50 * (players[i].getHealth() / 1000), 10);
		}

		// Draw items
		for (Item item : items) {
			g.drawImage(item.getCurrentImage(), item.getX(), item.getY(), this);
		}
	}

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

	private void pickup() {

		if (LShiftPressed && players[0].getItem() == null) {
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

		if (RShiftPressed && players[1].getItem() == null) {
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

	private void renderItems() {
		for (Item item : items) {
			if (item.getPlayer() != null) {
				item.setX(item.getPlayer().getX());
				item.setY(item.getPlayer().getY());
				if (item.getDirection() != item.getPlayer().getDirection()) {
					item.setDirection(item.getPlayer().getDirection());
					item.setCurrentImage(flipObject(item.getCurrentImage()));
				}
			}
		}
	}

	private void doAccelerationP1() {
		// p1
		int i = 0;
		// Left Movement w/ dash
		if (APressed && !DPressed) {
			if (pAccelX[i] == 0) {
				pAccelX[i] = -dashspeed * 2;
			} else {
				pAccelX[i] = -playerspeed * 2;
			}
			players[i].setDirection(-1);
		}

		// Right Movement w/ dash
		if (DPressed && !APressed) {
			if (pAccelX[i] == 0) {
				pAccelX[i] = dashspeed * 2;
			} else {
				pAccelX[i] = playerspeed * 2;
			}
			players[i].setDirection(1);
		}

		// If you didn't just jump, and pressed jump, jump
		if (WPressed && !jump1) {
			jump1 = true;
			pAccelY[i] = jumpheight * 2;
		}

		// You just jumped, and we need to increase jump counter and reset jump
		if (WReleased && jumps[i] <= 2) {
			jump1 = false;
			fall[i] = false;
			jumps[i]++;
			WReleased = false;
		} else if (jumps[i] > 2 && !isDark[i]) { // darker color, out of jumps
			images[i] = darkenObject(images[i], players[i]);
			isDark[i] = true;
		} else if (jumps[i] == 0 && isDark[i]) { // you can be light
			images[i] = lightenObject(images[i], players[i]);
			isDark[i] = false;
		}

		// Fast falling
		if (SPressed || fall[i]) {
			pAccelY[i] += fallspeed;
			fall[i] = true;
		}
	}

	private void doAccelerationP2() {
		// p2
		int i = 1;
		// Left Movement w/ dash
		if (LeftPressed && !RightPressed) {
			if (pAccelX[i] == 0) {
				pAccelX[i] = -dashspeed * 2;
			} else {
				pAccelX[i] = -playerspeed * 2;
			}
			players[i].setDirection(-1);
		}

		// Right Movement w/ dash
		if (RightPressed && !LeftPressed) {
			if (pAccelX[i] == 0) {
				pAccelX[i] = dashspeed * 2;
			} else {
				pAccelX[i] = playerspeed * 2;
			}
			players[i].setDirection(1);
		}

		// If you didn't just jump, and pressed jump, then jump
		if (UpPressed && !jump2) {
			jump2 = true;
			fall[i] = false;
			pAccelY[i] = jumpheight * 2;
		}

		// You just jumped, and we need to increase jump counter and reset jump
		if (UpReleased && jumps[i] <= 2) {
			jump2 = false;
			jumps[i]++;
			UpReleased = false;
		} else if (jumps[i] > 2 && !isDark[i]) {// darker color, out of jumps
			images[i] = darkenObject(images[i], players[i]);
			isDark[i] = true;

		} else if (jumps[i] == 0 && isDark[i]) { // lighter color
			images[i] = lightenObject(images[i], players[i]);
			isDark[i] = false;
		}

		// Fast falling
		if (DownPressed || fall[1]) {
			pAccelY[i] += fallspeed;
			fall[i] = true;
		}
	}

	private void doGravity() {

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

	private void doCollision() {

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
				resetJumps(i);
			}
		}

		// When items hit the ground, stop motion
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getY() + items.get(i).getCurrentImage().getHeight(this) + 30 > HEIGHT) {
				items.get(i).setY(HEIGHT - (items.get(i).getCurrentImage().getHeight(this) + 30));
			}
		}
	}

	private void doMovement() {
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

	// IMAGE TOOLBOX START
	private Image rotateObject(Image image, int degrees) {
		double rotationRequired = Math.toRadians(degrees);
		double locationX = image.getWidth(this) / 2;
		double locationY = image.getHeight(this) / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter((BufferedImage) image, null);
	}

	private Image loadObject(String Dir) {

		ImageIcon iIcon = new ImageIcon(Dir);
		Image i = iIcon.getImage();

		return i;
	}

	private Image lightenObject(Image image, Player player) {
		image = new ImageIcon(player.getImageDir()).getImage();
		image = scalePlayer(image, player);
		return image;
	}

	private Image darkenObject(Image image, Player player) {
		image = new ImageIcon(player.getDarkImageDir()).getImage();
		image = scalePlayer(image, player);
		return image;
	}

	private Image flipObject(Image image) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return (Image) op.filter(toBufferedImage(image), null);
	}

	private Image scaleObject(Image image, int width, int height) {
		return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	private Image scalePlayer(Image image, Player player) {
		return image.getScaledInstance(player.getWidth(), player.getHeight(), Image.SCALE_SMOOTH);

	}

	private static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	private void resetJumps(int i) {
		jumps[i] = 0;
		fall[i] = false;
	}
	// IMAGE TOOLBOX END

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
}
