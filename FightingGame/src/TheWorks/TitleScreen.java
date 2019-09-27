package TheWorks;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TitleScreen extends JPanel {

	private static final long serialVersionUID = -6610051464023407144L;
	private Application app;
	private JButton button1;
	private JButton button2;

	public TitleScreen(Application app) {
		super();
		this.setSize(app.getWidth(), app.getHeight());
		this.app = app;
		button1 = new JButton("Singleplayer");
		button2 = new JButton("Multiplayer");
	}

	public void setup() {

		this.setVisible(true);
		this.setLocation(0, 0);

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.initiateFrame(1);
			}
		});
		this.add(button1, BorderLayout.CENTER);
		button1.setVisible(true);

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.initiateFrame(2);
			}
		});
		this.add(button2, BorderLayout.CENTER);
		button2.setVisible(true);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Image background = new ImageIcon("src/resources/background.png").getImage();
		background = background.getScaledInstance(this.getWidth(), this.getHeight(), 0);
		g.drawImage(background, 0, 0, this);
	}
}
