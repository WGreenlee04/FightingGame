package TheWorks;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Start extends JFrame {

	public Start() {
		super("Start");
		this.setVisible(true);
		this.setSize(300, 100);
		JTextArea mode1 = new JTextArea("Multiplayer");
		this.add(mode1);
	}

}
