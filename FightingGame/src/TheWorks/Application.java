package TheWorks;

import javax.swing.JFrame;

public class Application extends JFrame {

	Playspace pspace;

	public Application(int i) {

		initUI();

		if (i == 1) {
			pspace.add(new Player1());
		} else if (i == 2) {
			pspace.add(new Player1(), new Player2());
		}

	}

	private void initUI() {

		pspace = new Playspace();
		add(pspace);

		setSize(1000, 1000);

		setTitle("Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public Playspace getPlayspace() {
		return pspace;
	}

}
