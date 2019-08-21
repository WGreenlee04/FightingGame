package TheWorks;

public class Main {

	static int mode;

	public static void main(String[] args) {

		mode = 0;

		Start start = new Start();

		if (mode == 1) {
			// initiate 1 player mode
		} else {
			if (mode == 2) {

				// initiate 2 player mode
				Playspace PlayArea = new Playspace();
				Player P1 = new Player1();
				Player P2 = new Player2();

				PlayArea.add(P1, P2);
			}
		}
	}

	public static void setMode(int m) {
		mode = m;
	}

}
