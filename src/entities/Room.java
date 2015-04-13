package entities;

import java.util.ArrayList;
import java.util.List;

import entities.Entity.EntityType;
import game.GameEngine.Direction;
import game.GameState;
import game.Utils;

public class Room implements Visible {

	private int x, y;
	private String name, description;
	private List<Entity> entities;
	private GameState gs;
	
	public Room(GameState gs, String name, String description, int x, int y) {
		entities = new ArrayList<>();
		this.name = name;
		this.description = description;
		this.x = x;
		this.y = y;
		this.gs = gs;
	}
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	
	public void removeEntity(Entity e){
		entities.remove(e);
	}
	
	public String look() {
		StringBuilder sb = new StringBuilder();
		sb.append(description);
		int[] offsets = {0, -1, 0, 1};
		for (int i = 0; i < offsets.length; i++){
			int newX = x + offsets[i];
			int newY = y + offsets[(i+1)%offsets.length];
			if (gs.getRoom(newX, newY) != null){
				sb.append("\n");
				// Don't kill me I swear I'm innocent
				sb.append("There is an exit to the "+Direction.values()[i].name().toLowerCase()+".");
			}
		}
		for (Entity e : entities){
			sb.append("\n");
			sb.append(e.look());
		}
		return sb.toString();
	}
	
	public String look(String object) {
		List<Entity> matches = getMatches(object, EntityType.ANY);
		if (matches.size() == 1){
			if (matches.get(0).getEntityType() == EntityType.ITEM){
				return matches.get(0).look();				
			} else {
				return ((NPC) matches.get(0)).talk();	
			}
		} else if (matches.isEmpty()){
			return "You can't see one of those here.";
		} else {
			return Utils.multiMatchResponse(matches);
		}
	}
	
	public List<Entity> getMatches(String pattern, EntityType type){
		return Utils.getMatches(pattern, type, entities);
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
}
