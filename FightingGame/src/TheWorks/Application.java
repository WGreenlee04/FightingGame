package TheWorks;

import javax.swing.JFrame;

public class Application extends JFrame {

	private static final long serialVersionUID = 4651748800360341847L;
	private Playspace pspace;

	public Application() {
		setSize(1000, 800);
		setTitle("StickFight");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void initiateFrame(int m) {
		pspace = new Playspace(m, this);
		add(pspace);
		pspace.initSpace();
	}

	public Playspace getPlayspace() {
		return pspace;
	}

}
