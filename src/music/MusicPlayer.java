package music;

import java.io.File;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

/**
 * @author Harmen Speelt music en gefet de titel op het display
 *
 */
public class MusicPlayer implements Playable {

	private PlayList playlist;
	private boolean play = true;
	private boolean repeat = true;
	
	
	public MusicPlayer(PlayList playlist, boolean repeat) {
		super();
		this.playlist = playlist;
		this.repeat = repeat;
	}

	public MusicPlayer() {
		super();
		this.playlist = new PlayList();
		Song song1 = new Song("Mission Impossible", new File("mission_impossible"));
		Song song2 = new Song("Star Wars", new File("star_wars.wav"));
		Song song3 = new Song("Carnaval Festival", new File("carnaval_festival.wav"));
		playlist.addSong(song1);
		playlist.addSong(song2);
		playlist.addSong(song3);
	}

	public void run() {
		play();
	}

	public void play() {
		do {
			for (int i = 0; i < playlist.getLenght() && play; i++) {
				LCD.clear();
				if (playlist.getSongFile(i).exists()) {
					LCD.drawString(playlist.getTitle(i), 0, 3);
					Sound.playSample(playlist.getSongFile(i), Sound.VOL_MAX);
				}
				Button.waitForAnyPress(500);
				if (Button.ENTER.isDown()) {
					play = false;
				}

			}
		} while (play && repeat); 

	}

	public Playable getPlaylist() {
		return playlist;
	}
	
	public void setPlaylist(PlayList playlist) {
		this.playlist = playlist;
	}

	public void stopPlay() {
		play = false;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	

}
