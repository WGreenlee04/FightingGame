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
