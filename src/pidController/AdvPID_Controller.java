package pidController;

public class AdvPID_Controller extends PID_Controller {
	
	private StackFloats stack = new StackFloats();
	private float movingAverage;
	
	public float getMovingAverage() {
		return movingAverage;
	}


	private void calculateMovingAverage() {
		float sum = 0;
		for (Float value : stack.getQueue()) {
			sum += value;
		}
		movingAverage = sum / stack.getSize();
	}


	@Override
	public float getCorrection(float normalizedValue) {
		float error = MIDPOINT - normalizedValue;
		float integral = error + lastIntegral;
		float derivative = error - lastError;
		
		float correction = kp * error + ki * integral + kd * derivative;
		
		this.lastError = error;
		this.lastIntegral = integral;
		calculateMovingAverage();
		return correction;
	}
	
	public void setStackSize(int stackSize) {
		stack.setSize(stackSize);
	}
	
}
