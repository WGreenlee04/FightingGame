package TheWorks;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Playspace extends JPanel implements ActionListener {
	private int area; // the size of the window on screen
	private ArrayList<Player> players = new ArrayList<Player>();

	public Playspace() { // Constructor, breaks Main from static.
		super(); // Sets up JPanel
	}

	// Begin player add
	public void add(Player p) {
		players.add(p);
	}

	public void add(Player a, Player b) {
		players.add(a);
		players.add(b);
	}
	// End player add

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawCharacter(g);
	}

	private void drawCharacter(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
