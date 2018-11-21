/*
 * A class for that handles the items (tricks) in a menu. It extends from LinkedList 
 * 
 * Author: Joey Weidema // Jorik Noorda
 * 
 */

package menu;

public class MenuItem extends LinkedList {

	String trickName = "EMPTY";

	public MenuItem(String value, String trickName) {
		super(value);
		this.trickName = trickName;
	}

	@Override
	public MenuItem getNext() {
		return (MenuItem) (super.getNext());

	}

	@Override
	public MenuItem getPrevious() {
		return (MenuItem) (super.getPrevious());

	}

	public String getTrickName() {
		return trickName;
	}

	public String getValue() {
		return this.get(0);
	}

	public void setTrickName(String trickName) {
		this.trickName = trickName;
	}

}
