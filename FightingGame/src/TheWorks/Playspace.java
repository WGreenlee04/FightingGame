package TheWorks;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Playspace extends JPanel implements ActionListener {
	private int area; // the size of the window on screen
	public Player1 p1;
	public Player2 p2;
	public Image p1_img;
	public Image p2_img;

	public Playspace() { // Constructor, breaks Main from static.
		super(); // Sets up JPanel
		initSpace();
	}

	// Begin player add
	public void add(Player1 p) {
		p1 = p;
	}

	public void add(Player1 a, Player2 b) {
		p1 = a;
		p2 = b;
	}
	// End player add

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(p1_img, 0, 0, null);
	}

	public void initSpace() {
		load1(p1);
		if (p2 != null) {
			load2(p2);
		}
	}

	public void load1(Player1 p) {
		ImageIcon ii = new ImageIcon("src/resources/stickBlue.png");
		p1_img = ii.getImage();
	}

	public void load2(Player2 p) {
		ImageIcon ii = new ImageIcon("src/resources/stickRed.png");
		Image p2_img = ii.getImage();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
