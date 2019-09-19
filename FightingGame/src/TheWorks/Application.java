package TheWorks;

import javax.swing.JFrame;

public class Application extends JFrame {

	private Playspace pspace;

	public Application() {
		setSize(1000, 800);
		setTitle("StickFight");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		pspace = new Playspace();
	}

	public void initiateFrame(int m) {
		add(pspace);
		pspace.initSpace(m);
	}

	public Playspace getPlayspace() {
		return pspace;
	}

}
