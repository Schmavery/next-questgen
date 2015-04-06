package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Item;
import entities.Room;
import game.GameEngine.Direction;

public class GameState {
	public static final int GRID_WIDTH = 10;
	public static final int GRID_HEIGHT = 10;
	private Room[][] grid = new Room[GRID_WIDTH][GRID_HEIGHT];
	private Room currRoom;
	private Random rand;
	private List<Item> inventory;
	
	private String[] testNouns = {"cat", "dog", "bicycle", "block of jello", "sword", "cannonball"};
	private String[] testAdjs = {"red", "shiny", "blue", "expensive-looking", "sharp", "bizarre"};
	private String[] testLocs = {"on a shelf", "on the ground", "lying abandoned", "in the dust", "nearby"};
	
	private String rStr(String[] arr){
		return arr[rand.nextInt(arr.length)];
	}
	
	public GameState() {
		rand = new Random();
		inventory = new ArrayList<>();
		for (int i = 0; i < GRID_WIDTH; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				grid[i][j] = new Room(this, "name", "This is the room at " + i + ", " + j, i, j);
				for (int r = rand.nextInt(5); r > 0; r--){
					String noun = rStr(testNouns);
					String name = rStr(testAdjs)+" "+noun;
					grid[i][j].addEntity(new Item(noun, name, 
							"You see a "+name+" "+rStr(testLocs)+"."));
				}
			}
		}
		currRoom = grid[rand.nextInt(GRID_WIDTH)][rand.nextInt(GRID_HEIGHT)];
	}
	
	public Room getRoom(int x, int y) {
		if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
			return null;
		}
		return grid[x][y];
	}
	
	public Room getCurrRoom(){
		return currRoom;
	}

	
	public boolean move(Direction dir){
		int newX, newY;
		switch (dir){
		case EAST:
			newX = currRoom.getX() + 1;
			newY = currRoom.getY();
			break;
		case NORTH:
			newX = currRoom.getX();
			newY = currRoom.getY() - 1;
			break;
		case SOUTH:
			newX = currRoom.getX();
			newY = currRoom.getY() + 1;
			break;
		case WEST:
			newX = currRoom.getX() - 1;
			newY = currRoom.getY();
			break;
		default:
			newX = currRoom.getX();
			newY = currRoom.getY();
		}
		if (getRoom(newX, newY) != null){
			currRoom = getRoom(newX, newY);
			return true;
		} else {
			return false;
		}
	}


	public List<Item> getInventory(){
		return inventory;
	}
	
	public String lookInventory(){
		if (inventory.isEmpty()){
			return "You aren't carrying anything.";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("You are currently carrying:");
			for (Item i : inventory){
				sb.append("\n\t");
				sb.append(i.getName());
			}
			return sb.toString();
		}
	}
}
