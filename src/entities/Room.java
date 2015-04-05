package entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Room implements Visible {

	private int x, y;
	private String name, description;
	private List<Entity> entities;
	
	public Room(String name, String description, int x, int y) {
		entities = new ArrayList<>();
		this.name = name;
		this.description = description;
		this.x = x;
		this.y = y;
	}
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	
	public String look() {
		StringBuilder sb = new StringBuilder();
		sb.append(description);
		for (Entity e : entities){
			sb.append("\n");
			sb.append(e.look());
		}
		return sb.toString();
	}
	
	public String look(String object) {
		List<Entity> matches = getMatches(object);
		if (matches.size() == 1){
			return matches.get(0).look();
		} else if (matches.isEmpty()){
			return "You can't see one of those here.";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("Do you mean the ");
			Entity prev = null;
			for (Entity e : matches){
				if (prev != null){
					sb.append(prev.getName());
					sb.append(", ");			
				}
				prev = e;
			}
			sb.append("or ");
			sb.append(prev.getName());
			sb.append("?");
			return sb.toString();
		}
	}
	
	private List<Entity> getMatches(String pattern){
		List<Entity> matches = new LinkedList<>();
		for (Entity e : entities){
			if (e.getDescription().toLowerCase().contains(pattern.toLowerCase())){
				matches.add(e);
			}
		}
		return matches;
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
}
