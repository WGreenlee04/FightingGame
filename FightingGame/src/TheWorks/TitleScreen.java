package TheWorks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TitleScreen extends JPanel implements KeyListener {

	private static final long serialVersionUID = -6610051464023407144L;

	private final Color backgroundColor; // Color of backdrop
	private final int WIDTH; // Width of panel
	private final int HEIGHT; // Height of panel

	private Application app;

	public TitleScreen(Application app) {
		super();

		// Application variables
		this.app = app;
		HEIGHT = this.app.getHeight();
		WIDTH = this.app.getWidth();
		setLocation(0, 0);
		setSize(WIDTH, HEIGHT);
		setFocusable(true);
		setVisible(true);
		addKeyListener(this);
		backgroundColor = new Color(100, 80, 60);
		backgroundColor.brighter();
		setBackground(backgroundColor);
	}

	public void setup() {

		JOptionPane.showMessageDialog(this, "Title screen to be added");
		JOptionPane.showMessageDialog(this, "For now, press 1 or 2 for Single or Multiplayer");

		/*
		 * URL url = null; try { url = new
		 * URL("https://www.youtube.com/watch?v=iaA8Xkl5kTc"); } catch
		 * (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * if (url != null) { showVideo(url); }
		 */
	}

	private void showVideo(URL url) {
		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);

		try {
			// create a player to play the media specified in the URL
			Player mediaPlayer = Manager.createRealizedPlayer(url);

			// get the components for the video and the playback controls
			Component video = mediaPlayer.getVisualComponent();
			Component controls = mediaPlayer.getControlPanelComponent();

			if (video != null)
				add(video, BorderLayout.CENTER); // add video component
			if (controls != null)
				add(controls, BorderLayout.SOUTH); // add controls

			mediaPlayer.start(); // start playing the media clip
		} // end try
		catch (NoPlayerException noPlayerException) {
			JOptionPane.showMessageDialog(null, "No media player found");
		} // end catch
		catch (CannotRealizeException cannotRealizeException) {
			JOptionPane.showMessageDialog(null, "Could not realize media player.");
		} // end catch
		catch (IOException iOException) {
			JOptionPane.showMessageDialog(null, "Error reading from the source.");
		} // end catch
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_1) {
			app.initiateFrame(1);
			setFocusable(false);
		}
		if (e.getKeyCode() == KeyEvent.VK_2) {
			app.initiateFrame(2);
			setFocusable(false);
		}
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
