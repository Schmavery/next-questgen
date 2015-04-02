package game;

import entities.Room;

public class GameState {
	public static final int GRID_WIDTH = 10;
	public static final int GRID_HEIGHT = 10;
	private Room[][] grid = new Room[GRID_WIDTH][GRID_HEIGHT];
	
	public GameState() {
		for (int i = 0; i < GRID_WIDTH; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				grid[i][j] = new Room("name", "this is room at " + i + ", " + j, i, j);
			}
		}
	}
	
	public Room getRoom(int x, int y) {
		if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
			return null;
		}
		return grid[x][y];
	}
	
}
