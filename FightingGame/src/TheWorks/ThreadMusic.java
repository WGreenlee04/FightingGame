package TheWorks;

import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class ThreadMusic extends Thread {

	public ThreadMusic(String fp) {
	}

	@Override
	public void run() {
		try {
			// get the sound file as a resource out of my jar file;
			// the sound file must be in the same directory as this class file.
			// the input stream portion of this recipe comes from a javaworld.com article.
			InputStream inputStream = getClass()
					.getResourceAsStream("/FightingGame/src/resources/Clayfighter (SNES) - Taffy's Theme.mp3");
			AudioStream audioStream = new AudioStream(inputStream);
			AudioPlayer.player.start(audioStream);
		} catch (Exception e) {
		}
	}

}
