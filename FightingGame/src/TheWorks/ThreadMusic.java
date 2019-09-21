package TheWorks;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ThreadMusic extends Thread {

	private Clip clip;

	public ThreadMusic() {
	}

	@Override
	public void run() {

		try {
			// get the sound file as a resource out of my jar file;
			// the sound file must be in the same directory as this class file.
			// the input stream portion of this recipe comes from a javaworld.com article.
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(
					new File("/FightingGame/src/resources/Clayfighter (SNES) - Taffy's Theme.wav").getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {
			System.out.println(false);
		}
	}

	public void play() {
		clip.start();
	}

	public void pause() {
		clip.stop();
	}

}
