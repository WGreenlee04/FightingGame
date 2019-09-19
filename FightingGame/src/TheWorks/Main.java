package TheWorks;

public class Main {

	private static int mode;
	private static Runnable r;

	public static void main(String[] args) {

		mode = 0;
		Start startWindow = new Start();
		startWindow.pingUser();

		r = new Runnable() {
			public void run() {
				if (mode == 1) {
					// initiate 1 player mode
					Application application = new Application();
					application.initiateFrame(mode);
				} else if (mode == 2) {

					// initiate 2 player mode
					Application application = new Application();
					application.initiateFrame(mode);
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
