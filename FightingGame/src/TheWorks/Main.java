package TheWorks;

public class Main {

	static int mode;
	static boolean pressed;
	static Runnable r;

	public static void main(String[] args) {

		mode = 0;

		Start startwindow = new Start();

		r = new Runnable() {
			public void run() {
				if (mode == 1) {
					// initiate 1 player mode
				} else if (mode == 2) {

					// initiate 2 player mode
					Application application = new Application();
					Player P1 = new Player1();
					Player P2 = new Player2();

					application.getPlayspace().add(P1, P2);
				}
			}
		};
	}

	public static void setMode(int m) {
		mode = m;
	}

	public static Runnable getRun() {
		return r;
	}

}
