package TheWorks;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Start extends JFrame {

	public Start() {
		super("Start");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(200, 90);
		setLocationRelativeTo(null);
		JButton mode2 = new JButton("Multiplayer");
		JButton mode1 = new JButton("Singleplayer");
		add(mode2, BorderLayout.NORTH);
		add(mode1, BorderLayout.SOUTH);
		mode2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mode2.isEnabled()) {
					Main.setMode(2);
					Main.getRun().run();
					setVisible(false);
				}
				if (mode1.isEnabled()) {
					Main.setMode(1);
					Main.getRun().run();
					setVisible(false);
				}
			}
		});
	}

}
