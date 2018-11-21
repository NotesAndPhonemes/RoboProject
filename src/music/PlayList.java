package music;

import java.io.File;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.Sound;

/**
 * @author Harmen hier in kan een playlist worden samengesteld en afgespeeld
 *
 */
public class PlayList implements Runnable, Playable {

	private ArrayList<Song> playlist;
	private boolean play = true;
	// hier de liedjes toevoegen

	public PlayList() {
		super();
		this.playlist = new ArrayList<Song>();
	}

	public void run() {
		play();
	}

	@Override
	public void play() {
		while (play) {
			for (int i = 0; i < playlist.size() && play; i++) {
				if (getSongFile(i).exists()) {
					Sound.playSample(getSongFile(i), Sound.VOL_MAX);
				}
				Button.waitForAnyPress(500);
				if (Button.ENTER.isDown()) {
					play = false;
				}
			}

		}

	}

	@Override
	public void stopPlay() {
		play = false;
	}

	public void addSong(Song song) {
		playlist.add(song);
	}

	public File getSongFile(int index) {
		File songFile = playlist.get(index).getFile();
		return songFile;
	}

	public int getLenght() {
		int lenght = playlist.size();
		return lenght;
	}

	public String getTitle(int index) {
		String title = playlist.get(index).getTitle();
		return title;
	}

}
