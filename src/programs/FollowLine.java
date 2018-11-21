package programs;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.TextMenu;
import models.*;
import pidController.AdvPID_Controller;
import programs.*;

public class FollowLine implements Runnable {

	
	private AdvPID_Controller pidController;
	private ColorSensor CS;
	private MotionController motionController;
	private Dragon dragon;
	private boolean rightSide;
	private boolean hasDragon = false;
	private static final int DRAGON_SPEED = 1000;
	private final static int STACK_SIZE = 10; // grootte van de stack voor de moving average wordt gebruikt om de bewegingen van de dragon vloeiend te maken
	private final static float CORRECTION_FACTOR = 2; // corrigeerd de waarde voor de dragon bewegingen

	// default
	private static final float SPEED_DEFAULT = (float) 100;
	private static final float KP_VALUE_DEFAULT = (float) 1.0;
	private static final float KI_VALUE_DEFAULT = (float) 0.0;
	private static final float KD_VALUE_DEFAULT = (float) 0.0;

	// normale track zonder onderbrekingen
	private static final float SPEED_NORMAL = (float) 375;
	private static final float KP_VALUE_NORMAL = (float) 1.5;
	private static final float KI_VALUE_NORMAL = (float) 0.0;
	private static final float KD_VALUE_NORMAL = (float) 2.3;

	// moeilijk track met onderbrekingen
	private static final float SPEED_DIFFICULT = (float) 200;
	private static final float KP_VALUE_DIFFICULT = (float) 0.5;
	private static final float KI_VALUE_DIFFICULT = (float) 0.0;
	private static final float KD_VALUE_DIFFICULT = (float) 1.0;

	// test instellingen
	private float speedTest = (float) 375;
	private float kpTest = (float) 1.3;
	private float kiTest = (float) 0.0;
	private float kdTest = (float) 2.0;

	public FollowLine(AdvPID_Controller pidController, ColorSensor CS, MotionController motionController, Dragon dragon) {
		this(pidController, CS, motionController);
		this.dragon = dragon;
		this.hasDragon = true;
	}
	
	public FollowLine(AdvPID_Controller pidController, ColorSensor CS, MotionController motionController) {
		super();
		this.pidController = pidController;
		this.CS = CS;
		this.motionController = motionController;
		this.rightSide = true;
		this.pidController.setStackSize(STACK_SIZE);
	}
	
	public void run() {
		calibrate();
		followLine();
//		Sound.twoBeeps();
//		
//		Runnable playlistDragon = dragon.playlist;
//		Runnable followLine = new FollowLine(pidController, CS, motionController, dragon);
//
//		Thread thread1 = new Thread(playlistDragon);
//		Thread thread2 = new Thread(followLine);
//
//		// Start threads
//		thread1.start();
//		thread2.start();
	}
	
	// hiermee kan de robot gecalibreerd worden
	public void calibrate() {

		// Calibratie aanvraag
		LCD.clear();
		CS.setBlackWhiteFromCalibration();

		// maak een keuze of de track linksom gaat of rechtsom
		LCD.clear();
		String[] items2 = { "Track Rechtsom", "Track Linkssom" };
		TextMenu selectMenu2 = new TextMenu(items2, 2, "Type Track");
		int selectedItem2 = selectMenu2.select();
		if (selectedItem2 == 1) {
			rightSide = false;
		}

		// hier wordt een keuze gemaakt voor het type track
		LCD.clear();
		String[] items1 = { "Normale Track", "Moeilijke Track", "Test", "Default" };
		TextMenu selectMenu1 = new TextMenu(items1, 2, "Racemodus");
		int selectedItem1 = selectMenu1.select();

		if (selectedItem1 == 0) {
			pidController.setKp(KP_VALUE_NORMAL);
			pidController.setKi(KI_VALUE_NORMAL);
			pidController.setKd(KD_VALUE_NORMAL);
			motionController.setSnelheid(SPEED_NORMAL);

		} else if (selectedItem1 == 1) {
			pidController.setKp(KP_VALUE_DIFFICULT);
			pidController.setKi(KI_VALUE_DIFFICULT);
			pidController.setKd(KD_VALUE_DIFFICULT);
			motionController.setSnelheid(SPEED_DIFFICULT);

		} else if (selectedItem1 == 2) {
			pidController.setKp(kpTest);
			pidController.setKi(kiTest);
			pidController.setKd(kdTest);
			motionController.setSnelheid(speedTest);

		} else if (selectedItem1 == 3) {
			pidController.setKp(KP_VALUE_DEFAULT);
			pidController.setKi(KI_VALUE_DEFAULT);
			pidController.setKd(KD_VALUE_DEFAULT);
			motionController.setSnelheid(SPEED_DEFAULT);

		}
	

	}
	
	public void followLine() {
		LCD.clear();
		LCD.drawString("Druk ENTER", 3, 3);
		LCD.drawString("om te starten", 2, 4);
		Button.ENTER.waitForPress();
		Sound.twoBeeps();

		LCD.clear();
		if(hasDragon) {
			LCD.drawString("Go Zwelgje!!!", 3, 3);
			dragon.setSpeed(DRAGON_SPEED);
		}
		else {
			LCD.drawString("Volg lijn!", 3, 3);
		}
		

		float speed = motionController.getSnelheid();
		float midpoint = AdvPID_Controller.getMidpoint();

		// Zet motoren aan
		motionController.vooruitOfAchteruit('V');
		motionController.getmB().forward();

		// Zo lang de knop niet ingedrukt wordt, volgt de robot een lijn
		while (Button.ENTER.isUp()) {
			// variable om het resultaat van de sensor op te slaan
			float currentBrightness = CS.getCurrentNormalisedBrightness();
			float correction = pidController.getCorrection(currentBrightness);
			float movingAverage = pidController.getMovingAverage();
			
			// dragon bewegingen
			if(hasDragon) {
				dragon.headRotateTo((int) (90 * movingAverage * CORRECTION_FACTOR));
			}
			
			
			// rightSide = true, dan rijdt de robot rechtsom, ander linksom
			// beide motoren worden bijgestuurd met de correctiefactor
			if (rightSide) {
				motionController.setEngineSpeed(speed * (midpoint - correction), speed * (midpoint + correction));
			} else {
				motionController.setEngineSpeed(speed * (midpoint + correction), speed * (midpoint - correction));
			}
		}
		LCD.clear();
		LCD.drawString("Gestopt", 3, 3);
		Delay.msDelay(3000);
		if(hasDragon) {
			dragon.run();
		}
		LCD.clear();
		motionController.close();
		CS.close();

	}

}
