/*
 * A menu for the tricks; The menu isa linked list. 
 * 
 * Author: Joey Weidema // Jorik Noorda
 * 
 */

package menu;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import models.Robot;

public class TrickMenu {

	private final String valueFirstTrick = "VL";
	private final String nameFirstTrick = "Volg de Lijn";
	private final String valueSecondTrick = "MM";
	private final String nameSecondTrick = "Master Mind";
	private final String valueThirdTrick = "DR";
	private final String nameThirdTrick = "Draw";
	private final String valueFourthTrick = "MP";
	private final String nameFourthTrick = "Music Player";

	MenuItem menuItems;
	Robot robot;
	MenuItem currentItem;

	public TrickMenu(Robot robot) {
		super();
		this.robot = robot;
		MenuItem firstItem = new MenuItem(valueFirstTrick, nameFirstTrick);
		MenuItem secondItem = new MenuItem(valueSecondTrick, nameSecondTrick);
		MenuItem thirdItem = new MenuItem(valueThirdTrick, nameThirdTrick);
		MenuItem fourthItem = new MenuItem(valueFourthTrick, nameFourthTrick);

		// linking elements
		firstItem.setNext(secondItem);
		secondItem.setNext(thirdItem);
		thirdItem.setNext(fourthItem);

		// wrapping list
		firstItem.wrapList();

		// setting a wrapped list as menu item
		menuItems = firstItem;
		// setting initial item
		currentItem = firstItem;
	}

	public void drawMenu() {

		LCD.clear();
		LCD.drawString(((MenuItem) (currentItem.getPrevious())).trickName, 0, 1);
		LCD.drawString("    /\\", 0, 2);
		LCD.drawString(currentItem.trickName.toUpperCase(), 0, 3);
		LCD.drawString("    \\/", 0, 4);
		LCD.drawString(((MenuItem) (currentItem.getNext())).trickName, 0, 5);
	}

	public void play(String value) {

		LCD.clear();
		Delay.msDelay(1000);

		switch (value) {
		case valueFirstTrick:
			robot.initAndRunFollowLine();
			break;
		case valueSecondTrick:
			robot.runMasterMind();
			break;
		case valueThirdTrick:
			robot.runDraw();
			break;
		case valueFourthTrick:
			robot.runMusic();
			break;
		}
	}

	public void pressDown() {

		currentItem = currentItem.getNext();

		runMenu();

	}

	public void pressEnter() {

		play(currentItem.getValue());
	}

	public void pressEsc() {

		LCD.drawString("Shutting down menu sequence", 0, 3);

		Sound.beepSequence();
		System.exit(1);

	}

	public void pressUp() {

		currentItem = currentItem.getPrevious();

		runMenu();
	}

	public void runMenu() {

		drawMenu();

		int button = Button.waitForAnyPress();

		switch (button) {
		case Button.ID_DOWN:
			pressDown();
			break;
		case Button.ID_UP:
			pressUp();
			break;
		case Button.ID_ENTER:
			pressEnter();
			break;
		case Button.ID_ESCAPE:
			pressEsc();
			break;
		}
		runMenu();
	}
}
