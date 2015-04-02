package entities;

public class Room implements Visible {

	private int x, y;
	private String name, description;
	
	public Room(String name, String description, int x, int y) {
		this.name = name;
		this.description = description;
		this.x = x;
		this.y = y;
	}
	
	public String look() {
		return description;
	}
	
	public String look(String object) {
		return object;
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
}
