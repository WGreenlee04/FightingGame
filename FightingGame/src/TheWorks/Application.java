package TheWorks;

import javax.swing.JFrame;

public class Application extends JFrame {

	private static final long serialVersionUID = 4651748800360341847L;
	private Playspace pspace;
	private TitleScreen title;

	public static void main(String[] args) {
		new Application().startGame();
	}

	public Application() {
		super();

		setSize(1000, 800);
		setResizable(false);
		setTitle("StickFight");
		setVisible(true);
		setFocusable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

	}

	public void initiateFrame(int m) {
		Application PlayFrame = new Application();
		pspace = new Playspace(m, PlayFrame);
		PlayFrame.add(pspace);
		pspace.setup();
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
