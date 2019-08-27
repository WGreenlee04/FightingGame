package TheWorks;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Playspace extends JPanel implements ActionListener {
	private static final int DELAY = 0;
	private int area; // the size of the window on screen
	public Player p1;
	public Player p2;
	public Image p1_img;
	public Image p2_img;
	private Timer timer;

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
		g.drawImage(p1_img, 300, 0, null);
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

	}

	public void load1() {
		ImageIcon ii = new ImageIcon(p1.getImageDir());
		p1_img = ii.getImage();
	}

	public void load2() {
		ImageIcon ii = new ImageIcon(p2.getImageDir());
		Image p2_img = ii.getImage();
	}

	public Image loadObject(String Dir) {
		ImageIcon ii = new ImageIcon(Dir);
		Image i = ii.getImage();

		return i;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
