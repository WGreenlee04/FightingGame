package TheWorks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Playspace extends JFrame implements ActionListener {
	private int area; // the size of the window on screen
	private ArrayList<Player> players = new ArrayList<Player>();

	public Playspace() {
		super("FightingGame");
		windowsetup();
	}

	public void add(Player p) {
		players.add(p);
	}

	public void add(Player a, Player b) {
		players.add(a);
		players.add(b);
	}

	public void windowsetup() {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
