package game;

import java.util.Scanner;

public class GameEngine {
	
	public enum Direction {NORTH, EAST, SOUTH, WEST};
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
			return in.next();			
		} else {
			running = false;
			return "";
		}
	}
	
	public String eval(String input){
		String[] tokens = input.split("\\s+");
		if (tokens.length == 0) return "";
		Direction dir = null;
		switch (tokens[0].toLowerCase()){
		case "look":
			if (tokens.length == 1){
				return state.getCurrRoom().look();
			} else {
				return state.getCurrRoom().look(tokens[1]);
			}
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
			state.move(dir);
			return "You moved "+dir.name()+".\n"+state.getCurrRoom().look();
		}
		return null;
	}
	
	public static void print(String s){
		if (s != null) System.out.println(s);
	}

}
