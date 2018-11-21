/*
 * A class that controls most of the movement of the robot.
 * 
 * @author Jorik Noorda en Joey Weidema
 */

package models;

import lejos.hardware.motor.*;
import lejos.hardware.port.*;

public class MotionController {
	private float snelheid;
	private final int rotationsToDegrees = ((360 / 61) / 2); // 1 rotatie van het wiel --> maakt een hoek van 85
																// graden??
	public EV3LargeRegulatedMotor mA;
	private EV3LargeRegulatedMotor mD;
	private EV3MediumRegulatedMotor mB;

	// Wiel-eigenschappen om afstand (rotaties) te berekenen)
	private final double DIAMETER = 4.2;
	private final double CIRCUMFERENCE = DIAMETER * Math.PI;

	public MotionController() {
		super();
		this.mA = new EV3LargeRegulatedMotor(MotorPort.A);
		this.mD = new EV3LargeRegulatedMotor(MotorPort.D);
		this.mB = new EV3MediumRegulatedMotor(MotorPort.B);

	}

	public MotionController(float snelheid) {
		this();
		this.snelheid = snelheid;
	}

	public void backward(int speed) {

		mA.setSpeed(speed);
		mD.setSpeed(speed);

		mA.backward();
		mD.backward();
	}

	public void close() {
		// Geef de motoren vrij
		mA.close();
		mD.close();
		mB.close();
	}

	public void forward(int speed) {

		mA.setSpeed(speed);
		mD.setSpeed(speed);

		mA.forward();
		mD.forward();
	}

	public EV3MediumRegulatedMotor getmB() {
		return mB;
	}

	// A method to transform rotations to actual length (distance) in cm
	public int getRotationDegreesFromLength(double length) {
		double rotations = length / CIRCUMFERENCE;
		return (int) (rotations * 360);
	}

	public float getSnelheid() {
		return snelheid;
	}

	// A method to transform rotations to degrees and then rotate to
	public void rotateTo(char direction, int degrees) {

		int rotations = degrees * rotationsToDegrees;

		if (direction == 'L') {

			// forward positive numbers
			mA.rotate(rotations, true);
			// backward negative numbers
			mD.rotate(rotations * -1, true);
			waitComplete();
		}

		else if (direction == 'R') {

			// forward positive numbers
			mA.rotate(rotations * -1, true);
			// backward negative numbers
			mD.rotate(rotations, true);
			waitComplete();
		}
	}

	public void roteer(char richting, double draaisnelheid) {
		if (richting == 'L') {
			// mA.rotate(Gradenstapje); // 1 rotaties (360 graden) is ongeveer 30 graden in
			// real life
			setEngineSpeed(draaisnelheid, 0);
		} else if (richting == 'R') {
			setEngineSpeed(0, draaisnelheid);
		} else {
			System.out.println("Deze richting bestaat niet!");
		}
	}

	public void setEngineSpeed(double engineLeft, double engineRight) {
		this.mA.setSpeed((float) engineLeft);
		this.mD.setSpeed((float) engineRight);
	}

	public void setmB(EV3MediumRegulatedMotor mB) {
		this.mB = mB;
	}

	public void setRotations(int rotations) {
		this.mA.rotate(rotations, true);
		this.mD.rotate(rotations, true);
	}

	public void setSnelheid(float snelheid) {
		this.snelheid = snelheid;
	}

	// A method used to make a circle.
	public void turnCircularRight(int speed, double turnFactor) {

		int turningSpeed = (int) (speed * turnFactor);

		mA.setSpeed(turningSpeed);
		mD.setSpeed(speed);

		mA.forward();
		mD.forward();
	}

	public void volledigeStop() {
		mA.stop();
		mD.stop();
	}

	public void vooruitOfAchteruit(char voorOfAchter) {
		mA.setSpeed(snelheid);
		mD.setSpeed(snelheid);
		// Geef snelheid in graden/sec
		if (voorOfAchter == 'V') {
			mA.forward();
			mD.forward();
		} else if (voorOfAchter == 'A') {
			mA.backward();
			mD.backward();
		}
	}

	public void waitComplete() {
		mA.waitComplete();
		mD.waitComplete();
	}
}
