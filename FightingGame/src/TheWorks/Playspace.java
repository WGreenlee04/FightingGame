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
	private static final int GRAVITY = -20;
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
	public boolean DPressed;
	public boolean UpPressed;
	public boolean LeftPressed;
	public boolean RightPressed;
	public boolean DownPressed;
	public final int WIDTH = 1000;
	public final int HEIGHT = 500;
	public final int playercount = 2;
	public final int dashspeed = 60;
	public final int playerspeed = 15;

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

	// Begin player add
	public void add(Player a) {
		players[0] = a;
	}

	public void add(Player a, Player b) {
		players[0] = a;
		players[1] = b;
	}
	// End player add

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < players.length; i++)
			g.drawImage(images[i], players[i].getX(), players[i].getY(), this);
	}

	public void initSpace(int i) { // loads both characters, and sets up space

		if (i == 1)
			add(new Player("src/resources/stickBlueResize.png"));
		if (i != 1)
			add(new Player("src/resources/stickBlueResize.png"), new Player("src/resources/stickRedResize.png"));

		for (i = 0; i < 2; i++)
			images[i] = loadObject(players[i].getImageDir());

		setSize(1000, 500);
		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);
	}

	public Image loadObject(String Dir) {
		ImageIcon ii = new ImageIcon(Dir);
		Image i = ii.getImage();

		return i;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Gravity Players 1 and 2
		for (int i = 0; i < players.length; i++)
			if (-players[i].getY() - (images[i].getHeight(this)) / 2 > -HEIGHT)
				pAccelY[i] = GRAVITY;

		// If at a wall, loop
		for (int i = 0; i < players.length; i++) {
			if (players[i].getX() <= 0 - images[i].getWidth(this))
				players[i].setX(WIDTH - images[i].getWidth(this));
			if (players[i].getX() >= WIDTH + images[i].getWidth(this))
				players[i].setX(0 - images[i].getWidth(this));
			if (-players[i].getY() - (images[i].getHeight(this) + 50) < -HEIGHT)
				players[i].setY(players[i].getY() + GRAVITY / 2);
			;
		}

		// Add acceleration if key is pressed
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
		if (WPressed) {
			pAccelY[0] = 20;
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
		if (UpPressed) {
			pAccelY[1] = 20;
		}

		// if acceleration of x, carry out acceleration, decreasing it by one half
		// each time.
		for (int i = 0; i < players.length; i++) {
			if (pAccelX[i] > 0) {
				players[i].setX(players[i].getX() + pAccelX[i] / 2);
				pAccelX[i]--;
			}
			if (pAccelX[i] < 0) {
				players[i].setX(players[i].getX() + pAccelX[i] / 2);
				pAccelX[i]++;
			}
			if (pAccelY[i] > 0) {
				players[i].setY(players[i].getY() - pAccelY[i] / 2);
				pAccelY[i]--;
			}
			if (pAccelY[i] < 0) {
				players[i].setY(players[i].getY() - pAccelY[i] / 2);
				pAccelY[i]++;
			}
		}
		repaint();
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
			DPressed = true;
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
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			UpPressed = false;
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
