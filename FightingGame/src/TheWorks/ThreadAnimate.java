package TheWorks;

public class ThreadAnimate extends Thread {

	private Playspace space;
	private final ToolBox Tools = new ToolBox(space);
	private Item animationSubjectItem;
	private Player animationSubjectPlayer;
	private int animationSpeed;
	private int animationLength;

	public ThreadAnimate(Item o, Playspace p, int s, int l) {

		this.space = p;
		this.animationSubjectItem = o;
		this.animationSpeed = s;
		this.animationLength = l;
	}

	public ThreadAnimate(Player o, Playspace p) {

		this.space = p;
		this.animationSubjectPlayer = o;
	}

	@Override
	public void run() {
		if (animationSubjectItem != null) {
			for (int i = 0; i <= animationLength; i++) {
				animationSubjectItem
						.setCurrentImage(Tools.rotateObject(animationSubjectItem.getCurrentImage(), animationSpeed));
				try {
					this.sleep(50);
				} catch (Exception e) {
					System.out.print("AnimationSleeping");
				}
			}
		}
	}
}
