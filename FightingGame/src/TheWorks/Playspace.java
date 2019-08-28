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
		g.drawImage(p1_img, (int) p1.getPoint().getX(), (int) p1.getPoint().getY(), null);
		g.drawImage(p2_img, this.getWidth() - ((int) p2.getPoint().getX() + 100), (int) p2.getPoint().getY(), null);
	}

	public void initSpace(int i) { // loads both characters, and sets up space

		if (i == 1)
			add(new Player("src/resources/stickBlueResize.png"));
		if (i != 1)
			add(new Player("src/resources/stickBlueResize.png"), new Player("src/resources/stickRedResize.png"));

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
		if (p1.getPoint().getY() + (p1_img.getHeight(this)) < this.getHeight())
			p1.getPoint().translate(0, GRAVITY);
		if (p2.getPoint().getY() + (p2_img.getHeight(this)) < this.getHeight())
			p2.getPoint().translate(0, GRAVITY);

		// If at a wall, bounce p1
		if (p1.getPoint().getX() <= 0) {
			p1.getPoint().translate(1, 0);
			p1_X = -p1_X / 4;
		}
		if (p1.getPoint().getX() >= this.getWidth() - p1_img.getWidth(this)) {
			p1.getPoint().translate(-1, 0);
			p1_X = -p1_X / 4;
		}

		// if acceleration of x, carry out acceleration, decreasing it by one each time.
		if (p1_X > 0 && p1.getPoint().getX() > 0) {
			p1.getPoint().translate(p1_X, 0);
			p1_X--;
		}
		if (p1_X < 0 && p1.getPoint().getX() > 0) {
			p1.getPoint().translate(p1_X, 0);
			p1_X++;
		}
		if (p2_X > 0 && p1.getPoint().getX() > 0) {
			p2.getPoint().translate(p2_X, 0);
			p2_X--;
		}
		if (p2_X < 0 && p1.getPoint().getX() > 0) {
			p2.getPoint().translate(p2_X, 0);
			p2_X++;
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_A) {
			if (p1_X >= 0) {
				p1_X = -15;
			} else {
				p1_X = -10;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			if (p1_X <= 0) {
				p1_X = 15;
			} else {
				p1_X = 10;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (p2_X >= 0) {
				p2_X = 15;
			} else {
				p2_X = 10;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (p2_X <= 0) {
				p2_X = -15;
			} else {
				p2_X = -10;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
