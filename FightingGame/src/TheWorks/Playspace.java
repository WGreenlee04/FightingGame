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
	private static final int GRAVITY = 10;
	private int area; // the size of the window on screen
	public Player p1;
	public Player p2;
	public Image p1_img;
	public Image p2_img;
	public int p1_X;
	public int p2_X;
	public int p1_Y;
	public int p2_Y;
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

	public Playspace(int i) { // Constructor, breaks Main from static.
		super(); // Sets up JPanel
		initSpace(i);
		timer = new Timer(DELAY, this);
		timer.start(); // Starts timer
	}

	// Begin player add
	public void add(Player p) {
		p1 = p;
	}

	public void add(Player a, Player b) {
		p1 = a;
		p2 = b;
	}
	// End player add

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(p1_img, (int) p1.getPoint().getX(), (int) p1.getPoint().getY(), this);
		g.drawImage(p2_img, (int) p2.getPoint().getX(), (int) p2.getPoint().getY(), this);
	}

	public void initSpace(int i) { // loads both characters, and sets up space

		if (i == 1)
			add(new Player("src/resources/stickBlueResize.png"));
		if (i != 1)
			add(new Player("src/resources/stickBlueResize.png"), new Player("src/resources/stickRedResize.png"));

		p2.getPoint().translate(WIDTH, 0);

		load1();
		if (p2 != null) {
			load2();
		}
		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);
	}

	public void load1() {
		ImageIcon ii = new ImageIcon(p1.getImageDir());
		p1_img = ii.getImage();
	}

	public void load2() {
		ImageIcon ii = new ImageIcon(p2.getImageDir());
		p2_img = ii.getImage();
	}

	public Image loadObject(String Dir) {
		ImageIcon ii = new ImageIcon(Dir);
		Image i = ii.getImage();

		return i;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Gravity Players 1 and 2
		if (p1.getPoint().getY() + (p1_img.getHeight(this)) < HEIGHT)
			p1_Y -= GRAVITY;
		if (p2.getPoint().getY() + (p2_img.getHeight(this)) < HEIGHT)
			p2_Y -= GRAVITY;

		// If at a wall, bounce p1
		if (p1.getPoint().getX() <= 0) {
			p1.getPoint().translate((WIDTH - (int) p1.getPoint().getX()), 0);
		}
		if (p1.getPoint().getX() >= WIDTH + p1_img.getWidth(this)) {
			p1.getPoint().translate(-(WIDTH - (int) p1.getPoint().getX()), 0);
		}
		if (p1.getPoint().getY() - (p1_img.getHeight(this)) < HEIGHT)
			p1_Y = 0;

		// If at wall, bounce p2
		if (p2.getPoint().getX() <= 0 - p2_img.getWidth(this)) {
			p2.getPoint().translate((WIDTH - (int) p2.getPoint().getX()), 0);
		}
		if (p2.getPoint().getX() >= WIDTH + p2_img.getWidth(this)) {
			p2.getPoint().translate(-(WIDTH - (int) p2.getPoint().getX()), 0);
		}
		if (p2.getPoint().getY() - (p2_img.getHeight(this)) < HEIGHT)
			p2_Y = 0;

		// Add acceleration if key is pressed
		// p1
		if (APressed) {
			if (p1_X >= 0) {
				p1_X = -15 * 2;
			} else {
				p1_X = -10 * 2;
			}
		}
		if (DPressed) {
			if (p1_X <= 0) {
				p1_X = 15 * 2;
			} else {
				p1_X = 10 * 2;
			}
		}

		// p2
		if (LeftPressed) {
			if (p2_X >= 0) {
				p2_X = -15 * 2;
			} else {
				p2_X = -10 * 2;
			}
		}
		if (RightPressed) {
			if (p2_X <= 0) {
				p2_X = 15 * 2;
			} else {
				p2_X = 10 * 2;
			}
		}

		// if acceleration of x, carry out acceleration, decreasing it by one
		// each time.

		// p1
		if (p1_X > 0) {
			p1.getPoint().translate(p1_X / 2, 0);
			p1_X--;
		}
		if (p1_X < 0) {
			p1.getPoint().translate(p1_X / 2, 0);
			p1_X++;
		}
		if (p1_Y > 0) {
			p1.getPoint().translate(-p1_Y / 2, 0);
			p1_Y--;
		}
		if (p1_Y < 0) {
			p1.getPoint().translate(-p1_Y / 2, 0);
			p1_Y++;
		}
		// p2
		if (p2_X > 0) {
			p2.getPoint().translate(p2_X / 2, 0);
			p2_X--;
		}
		if (p2_X < 0) {
			p2.getPoint().translate(p2_X / 2, 0);
			p2_X++;
		}
		if (p2_Y > 0) {
			p2.getPoint().translate(-p2_Y / 2, 0);
			p2_Y--;
		}
		if (p2_Y < 0) {
			p2.getPoint().translate(-p2_Y / 2, 0);
			p2_Y++;
		}
		//
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
