package TheWorks;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class ThreadSound extends Thread {

	private Clip clip;
	private String soundDir;
	private boolean running;

	public ThreadSound(String soundDir) {
		this.soundDir = soundDir;
		this.running = true;
	}

	private void closeThread() {
		this.running = false;
		this.interrupt();
	}

	@Override
	public void run() {

		if (clip == null) {
			try {
				// get the sound file as a resource out of my jar file;
				// the sound file must be in the same directory as this
				// class file.
				// the input stream portion of this recipe comes from a
				// javaworld.com article.
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(soundDir).getAbsoluteFile());
				clip = AudioSystem.getClip();
				clip.open(inputStream);
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				if (clip != null && soundDir.equals("src/resources/Clayfighter (SNES) - Taffy's Theme.wav"))
					gainControl.setValue(-10.0f); // Reduce volume by 10
													// decibels.
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (clip != null && soundDir.equals("src/resources/Clayfighter (SNES) - Taffy's Theme.wav") && !clip.isActive()
				&& !clip.isActive()) {
			clip.loop(4);
		}

		closeThread();

	}

	public void play() {
		clip.start();
	}

	public void pause() {
		clip.stop();
	}

}
