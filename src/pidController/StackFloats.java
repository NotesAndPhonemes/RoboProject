package pidController;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * @author Harmen
 * Berekend het zwevende gemiddelde van een aantal waardes
 */
public class StackFloats {
	
	private Queue<Float> queue = new ArrayDeque<>();
	private int size;
		
	
	public StackFloats(int size) {
		this();
		this.size = size;
	}



	public StackFloats() {
		super();
	}



	public Queue<Float> getQueue() {
		return queue;
	}



	public void setQueue(Queue<Float> queue) {
		this.queue = queue;
	}



	public int getSize() {
		return size;
	}



	public void setSize(int size) {
		this.size = size;
	}

	
	public void addValue(float value) {
		if (queue.size() >= size) {
			queue.remove();
		}
		queue.add(value);
	}

	@Override
	public String toString() {
		String waardes = "";
		for (Float value: queue) {
			waardes += String.format("%.2f ", value);
		}
		return "[" + waardes + "]";
	}
	
	
}	