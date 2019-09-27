package TheWorks;

import javax.swing.JFrame;

public class Application extends JFrame {

	private static final long serialVersionUID = 4651748800360341847L;
	private Playspace pspace;
	private TitleScreen title;

	public Application() {
		setSize(1000, 800);
		setTitle("StickFight");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void initiateFrame(int m) {
		this.remove(title);
		pspace = new Playspace(m, this);
		add(pspace);
		pspace.initSpace();
	}

	public void startGame() {
		title = new TitleScreen(this);
		this.add(title);
		title.setup();

	}

	public Playspace getPlayspace() {
		return pspace;
	}

}
