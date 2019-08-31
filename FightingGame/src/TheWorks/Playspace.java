package TheWorks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Playspace extends JPanel implements ActionListener, KeyListener {
	private static final int DELAY = 25;
	private static final int GRAVITY = -10;
	private int area; // the size of the window on screen
	public Player[] players;
	public Image[] images;
	public int[] pAccelX;
	public int[] pAccelY;
	private Timer timer;
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
	public final int WIDTH = 1000;
	public final int HEIGHT = 800;
	public final int playercount = 2;
	public final int dashspeed = 30;
	public final int playerspeed = 7;
	public final int jumpheight = 9;
	public final int fallspeed = -20;
	public boolean jump1 = false;
	public boolean jump2 = false;
	public boolean fall[] = { false, false };
	public int[] jumps = { 0, 0 };

	public Playspace(int i) { // Constructor, breaks Main from static.
		super(); // Sets up JPanel
		players = new Player[playercount];
		images = new Image[playercount];
		pAccelX = new int[playercount];
		pAccelY = new int[playercount];
		initSpace(i);
		timer = new Timer(DELAY, this);
		timer.start(); // Starts timer
	}

	public void initSpace(int i) { // loads both characters, and sets up space

		if (i == 1)
			add(new Player("src/resources/stickBlue.png"));
		if (i != 1)
			add(new Player("src/resources/stickBlue.png"), new Player("src/resources/stickRed.png"));

		for (i = 0; i < images.length; i++)
			images[i] = loadObject(players[i].getImageDir());

		for (i = 0; i < images.length; i++)
			images[i] = images[i].getScaledInstance(90, 150, Image.SCALE_SMOOTH);

		for (i = 0; i < players.length; i++)
			if (i == 0) {
				players[i].setX(0);
			} else {
				players[i].setX((WIDTH / (i) - 100) - images[i].getWidth(this));
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
		for (int i = 0; i < players.length; i++)
			g.drawImage(images[i], players[i].getX(), players[i].getY(), this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		doGravity();

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

		doMovement();
		collide();
		repaint();
		System.out.println(players[1].getY() + 150);
	}

	private void doGravity() {
		// Gravity Players 1 and 2
		for (int i = 0; i < players.length; i++)
			if (pAccelY[i] <= 0) {
				pAccelY[i] = GRAVITY;
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
