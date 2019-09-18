package TheWorks;

import java.awt.Color;
import java.awt.Dimension;
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
	public static final int ITEMGRAVITY = 6; // Linear gravity for items
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
	public boolean[] wasAirborne;
	public ArrayList<Item> items = new ArrayList<Item>(); // currently displayed
															// items
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
	public Color backgroundColor;

	public Playspace(int i) { // Constructor, breaks Main from static.
		// Sets up JPanel
		super();
		super.setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
		wasAirborne = new boolean[playercount];
		fall = new boolean[playercount];
		direction = new boolean[playercount];
		jumps = new int[playercount];

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
			images[i] = scalePlayer(images[i], players[i]);

		// Loads items, and by design, Item images
		for (int i = 0; i < itemcount; i++) {
			items.add(new Stick());
		}
		for (int i = 0; i < itemcount; i++) {
			double spawnVal = Math.random();
			double locationVal = Math.random();
			for (int I = 0; I < ITEMS.length; I++) {
				if (ITEMS[I].getDropRate() >= spawnVal) {
					items.set(i, ITEMS[I]);
					items.get(i).setX((int) ((WIDTH / 3) + (WIDTH / 3 * locationVal) + items.get(i).getWidth()));
				}
			}
			items.get(i).setCurrentImage(
					scaleObject(items.get(i).getCurrentImage(), items.get(i).getWidth(), items.get(i).getHeight()));

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

		// Set default array values
		for (int i = 0; i < playercount; i++) {
			wasAirborne[i] = false;
			fall[i] = false;
			direction[i] = true;
			jumps[i] = 0;
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
			g.drawImage(images[i], players[i].getX(), players[i].getY(), this);

			// Draw health bars
			healthBars[i].setText("" + players[i].getHealth());
			healthBars[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setLocation(players[i].getX() + 18, players[i].getY() - 10);
			healthBarIndicators[i].setSize((int) 50 * (players[i].getHealth() / 1000), 10);
		}
		for (int i = 0; i < items.size(); i++)
			// Draw items
			g.drawImage(items.get(i).getCurrentImage(), items.get(i).getX(), items.get(i).getY(), this);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		// Player status methods, including animations
		pickup();

		// Position methods
		doMovement();
		doGravity();
		doAcceleration();
		collide();
		renderItems();
		repaint();
	}

	private void pickup() {

		if (LShiftPressed && players[0].getItem() == null) {
			Rectangle[] itemRectangles = new Rectangle[items.size()];
			Rectangle p1 = new Rectangle(players[0].getX(), players[0].getY(), images[0].getWidth(this),
					images[0].getHeight(this));
			for (int i = 0; i < items.size(); i++) {
				itemRectangles[i] = new Rectangle(
						items.get(i).getX() - ((int) items.get(i).getCurrentImage().getWidth(this) / 2),
						items.get(i).getY() + ((int) items.get(i).getCurrentImage().getHeight(this) / 2),
						items.get(i).getCurrentImage().getWidth(this), items.get(i).getCurrentImage().getHeight(this));
				if (itemRectangles[i].intersects(p1) && items.get(i).getPlayer() == null) {
					players[0].setItem(items.get(i));
					items.get(i).setPlayer(players[0]);
				}
			}
		}

		if (RShiftPressed && players[1].getItem() == null) {
			Rectangle[] itemRectangles = new Rectangle[items.size()];
			Rectangle p2 = new Rectangle(players[1].getX(), players[1].getY(), images[1].getWidth(this),
					images[1].getHeight(this));
			for (int i = 0; i < items.size(); i++) {
				itemRectangles[i] = new Rectangle(
						items.get(i).getX() - ((int) items.get(i).getCurrentImage().getWidth(this)),
						items.get(i).getY() + ((int) items.get(i).getCurrentImage().getHeight(this)),
						items.get(i).getCurrentImage().getWidth(this), items.get(i).getCurrentImage().getHeight(this));
				System.out.println(itemRectangles[i].getX());
				if (itemRectangles[i].intersects(p2) && items.get(i).getPlayer() == null) {
					players[1].setItem(items.get(i));
					items.get(i).setPlayer(players[1]);
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

	private void doAcceleration() {
		// Add acceleration if key is pressed
		// p1

		// Left Movement w/ dash
		if (APressed && !DPressed) {
			if (pAccelX[0] == 0) {
				pAccelX[0] = -dashspeed * 2;
			} else {
				pAccelX[0] = -playerspeed * 2;
			}
			players[0].setDirection(-1);
		}

		// Right Movement w/ dash
		if (DPressed && !APressed) {
			if (pAccelX[0] == 0) {
				pAccelX[0] = dashspeed * 2;
			} else {
				pAccelX[0] = playerspeed * 2;
			}
			players[0].setDirection(1);
		}

		// If you didn't just jump, and pressed jump, jump
		if (WPressed && !jump1) {
			jump1 = true;
			pAccelY[0] = jumpheight * 2;
		}

		// You just jumped, and we need to increase jump counter and reset jump
		if (WReleased && jumps[0] <= 2) {
			jump1 = false;
			fall[0] = false;
			jumps[0]++;
			WReleased = false;
		} else if (jumps[0] > 2) { // oh yeah and if you're out of jumps, we set
									// you to a darker color
			images[0] = darkenObject(images[0], players[0]);
			wasAirborne[0] = true;
		} else if (jumps[0] == 0 && wasAirborne[0]) { // once your jumps are
														// back, you can be
														// light
			images[0] = lightenObject(images[0], players[0]);
			wasAirborne[0] = false;
		}

		if (SPressed || fall[0]) {
			pAccelY[0] += fallspeed;
			fall[0] = true;
		}

		// p2

		// Left Movement w/ dash
		if (LeftPressed && !RightPressed) {
			if (pAccelX[1] == 0) {
				pAccelX[1] = -dashspeed * 2;
			} else {
				pAccelX[1] = -playerspeed * 2;
			}
			players[1].setDirection(-1);
		}

		// Right Movement w/ dash
		if (RightPressed && !LeftPressed) {
			if (pAccelX[1] == 0) {
				pAccelX[1] = dashspeed * 2;
			} else {
				pAccelX[1] = playerspeed * 2;
			}
			players[1].setDirection(1);
		}

		// If you didn't just jump, and pressed jump, jump
		if (UpPressed && !jump2) {
			jump2 = true;
			fall[1] = false;
			pAccelY[1] = jumpheight * 2;
		}

		// You just jumped, and we need to increase jump counter and reset jump
		if (UpReleased && jumps[1] <= 2) {
			jump2 = false;
			jumps[1]++;
			UpReleased = false;
		} else if (jumps[1] > 2) { // oh yeah and if you're out of jumps, we set
									// you to a darker color
			images[1] = darkenObject(images[1], players[1]);
			wasAirborne[1] = true;
		} else if (jumps[1] == 0 && wasAirborne[1]) { // once your jumps are
														// back, you can be
														// light
			images[1] = lightenObject(images[1], players[1]);
			wasAirborne[1] = false;
		}

		// Fast falling
		if (DownPressed || fall[1]) {
			pAccelY[1] += fallspeed;
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

		// Gravity on items
		for (Item item : items) {
			if (item.getPlayer() == null) {
				item.setY(item.getY() + ITEMGRAVITY);
			}
		}
	}

	private void collide() {

		// looping through the number of players
		for (int i = 0; i < players.length; i++) {

			// If at wall, loop
			if (players[i].getX() <= 0 - images[i].getWidth(this))
				players[i].setX(WIDTH);
			if (players[i].getX() >= WIDTH + images[i].getWidth(this))
				players[i].setX(0 - images[i].getWidth(this));

			// If at floor, don't move through
			if (players[i].getY() + images[i].getHeight(this) + 35 > HEIGHT) {
				jumps[i] = 0;
				fall[i] = false;
				players[i].setY(HEIGHT - (images[i].getHeight(this) + 35));
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

		if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
			RShiftPressed = true;
			RShiftReleased = false;
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

		if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
			LShiftPressed = true;
			LShiftReleased = false;
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
