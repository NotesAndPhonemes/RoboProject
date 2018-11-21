package models;

import lejos.hardware.Button;
import java.io.File;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import menu.TrickMenu;
import music.MusicPlayer;
import music.PlayList;
import music.Song;
import pidController.AdvPID_Controller;
import programs.Draw;
import programs.FollowLine;
import programs.MasterMind;
import lejos.utility.TextMenu;

public class Robot {

	private MotionController motionController;
	private AdvPID_Controller pidController;
	private ColorSensor CS;
	private MasterMind mastermind;
	private Draw draw;
	private Dragon dragon;

	public Robot() {
		super();
		this.motionController = new MotionController();
		this.pidController = new AdvPID_Controller();
		this.CS = new ColorSensor();
		this.dragon = new Dragon(motionController);
		this.mastermind = new MasterMind(CS, motionController);
		this.draw = new Draw(motionController);
		new TrickMenu(this);
		new MusicPlayer();
	}

	public Draw getDraw() {
		return draw;
	}

	public MotionController getMotionController() {
		return motionController;
	}

	// maak een keuze voor een programma
	public void run() {
		LCD.clear();
		String[] items = { "Volg een lijn", "Speel Mastermind", "Ga tekenen", "Speel playlist", "DEMO Dragon",
				"DEMO volglijn" };
		TextMenu selectMenu = new TextMenu(items, 2, "Wat wil je doen?");
		int selectedItem = selectMenu.select();
		if (selectedItem == 0) {

			// FollowLine followline1 = new FollowLine(pidController, CS, motionController,
			// dragon);
			// followline1.followLine();
			// Sound.twoBeeps();
			// File sample = dragon.sample_dragons_daughter.getFile();
			//
			// Sound.playSample(sample, Sound.VOL_MAX);

			initAndRunFollowLine();

		} else if (selectedItem == 1) {
			// TODO
			// Mastermind mastermind = = new MasterMind(CS);
			// mastermind.run();
		} else if (selectedItem == 2) {
			// TODO
			// Draw draw = new Draw(motionController);
			// draw.run();
		} else if (selectedItem == 3) {
			MusicPlayer musicPlayer = new MusicPlayer();
			musicPlayer.play();
		} else if (selectedItem == 4) {
			// dragon.run();

			Runnable demoDragon = dragon;
			Runnable playlistDragon = dragon.playlist;

			Thread thread1 = new Thread(playlistDragon);
			Thread thread2 = new Thread(demoDragon);

			thread1.start();
			thread2.start();

		} else if (selectedItem == 5) {
			FollowLine followline = new FollowLine(pidController, CS, motionController);
			// followline.calibrate();
			// LCD.clear();
			// LCD.drawString("Druk ENTER", 3, 3);
			// LCD.drawString("om te starten", 2, 4);
			// Button.ENTER.waitForPress();
			followline.calibrate();
			followline.run();
		}
	}

	// TODO: MAke runners for MAstermind; after all runners made, and marvin is one,
	// we can remove the getters above here for tricks.
	public void runMusic() {
		MusicPlayer musicPlayer = new MusicPlayer();
		musicPlayer.run();
	}

	public void runDraw() {
		draw.runDrawSequence();
	}

	public void initAndRunFollowLine() {
		LCD.clear();
		LCD.drawString("Druk ENTER", 3, 3);
		LCD.drawString("om te starten", 2, 4);
		Delay.msDelay(500);
		Button.ENTER.waitForPress();

		Delay.msDelay(3000);
		dragon.run();

		PlayList playlist = new PlayList();
		Song theme = new Song("", new File("daenerys_theme_got.wav"));
		playlist.addSong(theme);

		Runnable playlistDragon = playlist;
		Runnable followLine = new FollowLine(pidController, CS, motionController);

		Thread thread1 = new Thread(playlistDragon);
		Thread thread2 = new Thread(followLine);

		// Start threads
		thread1.start();
		thread2.start();
	}

	public void runMasterMind() {
		mastermind.playMasterMind();
	}

}