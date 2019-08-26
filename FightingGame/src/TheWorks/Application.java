package TheWorks;

import javax.swing.JFrame;

public class Application extends JFrame {

	Playspace p;

	public Application() {

		initUI();
	}

	private void initUI() {

		p = new Playspace();
		add(p);

		setSize(250, 200);

		setTitle("Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public Playspace getPlayspace() {
		return p;
	}

}
