package TheWorks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Playspace extends JPanel implements ActionListener {
	private int area; // the size of the window on screen
	private ArrayList<Player> players = new ArrayList<Player>();

	public Playspace() { // Constructor, breaks Main from static.
		super("FightingGame"); // Sets up JFrame
		windowsetup(); // Configures JFrame
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

	public void windowsetup() {
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
