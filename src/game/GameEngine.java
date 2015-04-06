package game;

import java.util.List;
import java.util.Scanner;

import entities.Entity;
import entities.Entity.EntityType;
import entities.Item;
import entities.NPC;

public class GameEngine {
	
	public enum Direction {NORTH, WEST, SOUTH, EAST};
	public GameState state;
	public boolean running;
	public Scanner in;
	public static void main(String[] args) {
		GameEngine engine = new GameEngine();
		engine.start();
	}
	
	public void start() {
		running = true;
		state = new GameState();
		in = new Scanner(System.in);
		print(state.getCurrRoom().look());
		while (running){
			print(eval(read()));
		}
		in.close();
	}
	
	public String read(){
		System.out.print(">> ");
		if (in.hasNext()){
			return in.nextLine();			
		} else {
			running = false;
			return "";
		}
	}
	
	public String eval(String input){
		String[] tokens = input.split("\\s+");
		if (tokens.length == 0) return "";
		Direction dir = null;
		List<Entity> matches;
		switch (tokens[0].toLowerCase()){
		case "look":
		case "l":
			if (tokens.length == 1){
				return state.getCurrRoom().look();
			} else {
				return state.getCurrRoom().look(Utils.stripFirst(tokens));
			}
		case "take":
		case "get":
			matches = state.getCurrRoom().getMatches(Utils.stripFirst(tokens), EntityType.ITEM);
			if (matches.isEmpty()){
				return "You can't see that here.";
			} else if (matches.size() == 1){
				state.getInventory().add((Item) matches.get(0));
				state.getCurrRoom().removeEntity(matches.get(0));
				return "You get the "+matches.get(0).getName();
			} else {
				return Utils.multiMatchResponse(matches);
			}
		case "give":
			String[] split = Utils.stripFirst(tokens).split("\\s+to\\s+");
		case "trade":
			split = Utils.stripFirst(tokens).split("\\s+with\\s+");
			if (split.length == 2){
				// First element is item, second is NPC target.
				List<Entity> itemMatch = Utils.getMatches(split[0], EntityType.ITEM, state.getInventory());
				List<Entity> npcMatch = state.getCurrRoom().getMatches(split[1], EntityType.NPC);
				if (itemMatch.isEmpty()){
					return "You don't have one of those.";
				} else if (npcMatch.isEmpty()){
					return "You can't see that here.";
				} else if (npcMatch.size() > 1){
					return Utils.multiMatchResponse(npcMatch);
				} else if (itemMatch.size() > 1){
					return Utils.multiMatchResponse(itemMatch);
				} else {
					Item oldItem = (Item) itemMatch.get(0);
					Item newItem = ((NPC) (npcMatch.get(0))).trade(oldItem);
					state.getInventory().add(newItem);
					return "You give the "+oldItem.getName()+" to the "+npcMatch.get(0).getName()+
							"and recieve a "+newItem.getName()+".";
				}
			}
			break;
		case "drop":
			matches = Utils.getMatches(Utils.stripFirst(tokens), EntityType.ITEM, state.getInventory());
			if (matches.isEmpty()){
				return "You can't see that here.";
			} else if (matches.size() == 1){
				state.getCurrRoom().addEntity(matches.get(0));
				state.getInventory().remove((Item) matches.get(0));
				return "You drop the "+matches.get(0).getName();
			} else {
				return Utils.multiMatchResponse(matches);
			}
		case "inventory":
		case "inv":
		case "i":
			return state.lookInventory();
		case "exit":
		case "quit":
			running = false;
			break;
		case "north":
		case "n":
			dir = Direction.NORTH;
			break;
		case "south":
		case "s":
			dir = Direction.SOUTH;
			break;
		case "east":
		case "e":
			dir = Direction.EAST;
			break;
		case "west":
		case "w":
			dir = Direction.WEST;
			break;
		}
		if (dir != null){
			if (state.move(dir)){
				return "You moved "+dir.name()+".\n"+state.getCurrRoom().look();				
			} else {
				return "You cannot move "+dir.name()+".";
			}
		}
		return "Sorry, I didn't understand.";
	}
	
	public static void print(String s){
		if (s != null) System.out.println(s);
	}


}
