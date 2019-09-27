package TheWorks;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JPanel;

public class TitleScreen extends JPanel implements KeyListener {

	private static final long serialVersionUID = -6610051464023407144L;

	private final Color backgroundColor; // Color of backdrop
	private final int WIDTH; // Width of panel
	private final int HEIGHT; // Height of panel

	private Application app;
	private JButton button1;
	private JButton button2;

	public TitleScreen(Application app) {
		super();

		// Application variables
		this.app = app;
		HEIGHT = this.app.getHeight();
		WIDTH = this.app.getWidth();
		setLocation(0, 0);
		setSize(WIDTH, HEIGHT);
		setFocusable(true);
		addKeyListener(this);
		setVisible(true);
		backgroundColor = new Color(50, 50, 60);
		backgroundColor.brighter();
		setBackground(backgroundColor);
		button1 = new JButton("Singleplayer");
		button2 = new JButton("Multiplayer");
	}

	public void setup() {
		URL url = null;
		try {
			url = new URL("www.youtube.com/watch?v=iaA8Xkl5kTc");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (url != null) {
			showVideo(url);
		}

	}

	private void showVideo(URL url) {

	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
