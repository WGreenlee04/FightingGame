package Threads;

import java.awt.Image;

import Items.Item;
import Items.Player;
import TheWorks.Playspace;
import TheWorks.ToolBox;

public class ThreadAnimate extends Thread {

	private Playspace space;
	private Item animationSubjectItem;
	private Player animationSubjectPlayer;
	private final int degrees = 5;
	private final int endRotation = 100;
	private final int delay = 10;
	private final ToolBox Tools = new ToolBox(space);
	private Image currentFrame;
	private boolean running;

	public ThreadAnimate(Item o, Playspace p) {

		this.space = p;
		this.animationSubjectItem = o;
		this.running = true;
	}

	public ThreadAnimate(Player o, Playspace p) {

		this.space = p;
		this.animationSubjectPlayer = o;
		this.running = true;
	}

	private void closeThread() {
		animationSubjectItem.setAnimated(false);
		running = false;
		this.interrupt();
	}

	@Override
	public void run() {
		if (animationSubjectItem != null) {
			Image original = animationSubjectItem.getCurrentImage();
			for (int i = degrees; i <= endRotation; i += degrees) {
				if (animationSubjectItem.getPlayer().getDirection() == -1) {
					currentFrame = Tools.rotateObject(original, -i);
				} else {
					currentFrame = Tools.rotateObject(original, i);
				}

				animationSubjectItem.setyOffset(i + 1);
				animationSubjectItem.setWidth(currentFrame.getWidth(space));
				animationSubjectItem.setHeight(currentFrame.getHeight(space));
				animationSubjectItem.setCurrentImage(currentFrame);
				try {
					this.sleep(delay);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			animationSubjectItem.setxOffset(0);
			animationSubjectItem.setyOffset(0);
			animationSubjectItem.setWidth(original.getWidth(space));
			animationSubjectItem.setHeight(original.getHeight(space));
			animationSubjectItem.setCurrentImage(original);
		}
		closeThread();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
