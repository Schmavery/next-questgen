package generation;

import entities.Item;

// leaves are items that exist in the world, 
public class GameTree {
	
	public static void main(String[] args) {
		ItemGenerator itemGen = new ItemGenerator("./json/items.json");
		Item item = itemGen.generateItem("duck");
		System.out.println(item.look());
	}
}
