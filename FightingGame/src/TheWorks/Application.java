package TheWorks;

import javax.swing.JFrame;

public class Application extends JFrame {

	Playspace pspace;

	public Application(int i) {

		initUI(i);

	}

	private void initUI(int i) {

		pspace = new Playspace(i);
		add(pspace);

		setSize(1000, 800);

		setTitle("StickFight");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public Playspace getPlayspace() {
		return pspace;
	}

}
