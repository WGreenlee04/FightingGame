package TheWorks;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Start extends JFrame {

	public Start() {
		super("Start");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(200, 90);
		JButton mode2 = new JButton("Multiplayer");
		JButton mode1 = new JButton("Singleplayer");
		this.add(mode2, BorderLayout.NORTH);
		this.add(mode1, BorderLayout.SOUTH);
		mode2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mode2.isEnabled()) {
					Main.setMode(2);
					Main.pressed();
				}
				if (!mode2.isEnabled()) {
					System.out.println();
				}
			}
		});
	}

}
