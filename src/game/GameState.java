package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Combination;
import entities.Item;
import entities.Room;
import game.GameEngine.Direction;
import generation.GameTree;
import generation.TradeNode;

public class GameState {
	public static final int GRID_WIDTH = 1;
	public static final int GRID_HEIGHT = 1;
	private Room[][] grid = new Room[GRID_WIDTH][GRID_HEIGHT];
	private Room currRoom;
	private Random rand;
	private List<Item> inventory = new ArrayList<>();
	private List<Combination> combinations = new ArrayList<>();
	
	public GameState() {
		rand = new Random();
		
		for (int i = 0; i < GRID_WIDTH; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				grid[i][j] = new Room(this, "name", "This is the room at " + i + ", " + j, i, j);
			}
		}
		
		GameTree gameTree = new GameTree();
		addTree(gameTree.getRoot());
		currRoom = grid[rand.nextInt(GRID_WIDTH)][rand.nextInt(GRID_HEIGHT)];
	}
	
	public void addTree(TradeNode node){
		if (node.isTrade()){
			node.npc.setTradeRule(node);
			grid[rand.nextInt(GRID_WIDTH)][rand.nextInt(GRID_HEIGHT)].addEntity(node.npc);			
		} else {
			combinations.add(new Combination(node.getReceives().get(0), 
					node.getReceives().get(1), node.getReward()));
		}
		if (node.isLeaf()) {
			for (Item r : node.getReceives()){
				grid[rand.nextInt(GRID_WIDTH)][rand.nextInt(GRID_HEIGHT)].addEntity(r);				
			}
		} else {
			for (TradeNode tn : node.getChildren()){
				addTree(tn);
			}
		}
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
