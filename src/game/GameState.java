package game;

import java.util.Random;

import entities.Room;
import game.GameEngine.Direction;

public class GameState {
	public static final int GRID_WIDTH = 10;
	public static final int GRID_HEIGHT = 10;
	private Room[][] grid = new Room[GRID_WIDTH][GRID_HEIGHT];
	private Room currRoom;
	private Random rand;
	
	public GameState() {
		rand = new Random();
		for (int i = 0; i < GRID_WIDTH; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				grid[i][j] = new Room("name", "this is room at " + i + ", " + j, i, j);
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
}
