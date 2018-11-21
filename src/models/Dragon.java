package models;

import java.io.File;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import music.PlayList;
import music.Song;

/**
 * @author Harmen De Dragon Class bestuurt het hoofd en de staart van de Dragon.
 */
public class Dragon implements Runnable {

	private MotionController motionController;
	private static final int CORRECTION_FACTOR = 4; // correctie factor ivm de tandwielen
	private int speed = 200;
	private int correctedSpeed = speed * CORRECTION_FACTOR;

	public Song sample_dragon;
	public Song sample_dragon_breath;
	public Song sample_dragon_roar;
	public Song theme_song;
	public PlayList playlist;
	public int numberofTimes = 3;
	private boolean play = true;

	
	
	public Dragon(MotionController motionController, int numberofTimes) {
		this(motionController);
		this.numberofTimes = numberofTimes;
	}

	public Dragon(MotionController motionController) {
		this.motionController = motionController;
		this.sample_dragon = new Song("Dragon", new File("dragon.wav"));
		this.sample_dragon_breath = new Song("Dragon breath", new File("dragon_breath.wav"));
		this.sample_dragon_roar = new Song("Dragon roar", new File("dragon_roar.wav"));
		this.theme_song = new Song("Daenerys theme", new File("daenerys_theme_got.wav"));
		this.playlist = new PlayList();
		playlist.addSong(sample_dragon_roar);
		playlist.addSong(theme_song);
		playlist.addSong(sample_dragon);
		playlist.addSong(theme_song);
		playlist.addSong(sample_dragon_breath);
		playlist.addSong(theme_song);
	}

	public void run() {
		Sound.twoBeeps();
		// er is geen extra motortje voor de staart
		// RegulatedMotor[] syncList = {motionController.getmB()};
		// motionController.getmC().synchronizeWith(syncList );
		// motionController.getmC().startSynchronization();
		for (int i = 0; i < numberofTimes && play; i++) {
			headRotateTo(90);
			Delay.msDelay(500);
			headRotateTo(-90);
			Delay.msDelay(500);
			headRotateTo(0);
						Button.waitForAnyPress(500);
			if (Button.ENTER.isDown()) {
				play = false;
			}

		}

		while (Button.ENTER.isUp()) {

		}
		motionController.getmB().close();
	}

	public void headRotateTo(int angle) {
		motionController.getmB().setSpeed(correctedSpeed);
		motionController.getmB().rotateTo(angle * CORRECTION_FACTOR);
		motionController.getmB().waitComplete();
	}

	public void setNumberofTimes(int numberofTimes) {
		this.numberofTimes = numberofTimes;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
