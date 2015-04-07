package generation;

import com.sun.corba.se.impl.orbutil.graph.Node;

import entities.Item;

// leaves are items that exist in the world, 
public class GameTree {
	
	private TradeNode root;
	private final int TREE_DEPTH = 2;
	private final int MAX_RESTART_COUNT = 1000;
	private final int MAX_TRY_COUNT = 100;
	private TradeGenerator generator;
	public static void main(String[] args) {
		GameTree gt = new GameTree();	
		
	}
	public GameTree() {
		generator = new TradeGenerator("./json/npcrules.json", "./json/items.json", "./json/npcs.json");
		if (!generateTree())
			throw new RuntimeException("FAILED TO GENERATE A VALID TREE: TIMEOUT");
		TradeNode n = root;
		while (n != null) {
			System.out.println(n.toString());
			n = n.childNode;
		}
	}
	
	private int tryCount = 0;
	private int restartCount = 0;
	private boolean generateTree() {
		if (restartCount > MAX_RESTART_COUNT)
			return false;
		root = generator.generateRootTradeNode();
		TradeNode prev = root;
		TradeNode node = null;
		for (int i = 0; i < TREE_DEPTH; i++) {
			while (node == null) {
				if (tryCount > MAX_TRY_COUNT) {
					tryCount = 0;
					restartCount++;
					return generateTree();
				}
				node = generator.generateTradeNode(prev);
				tryCount++;
			}
			prev.childNode = node;
			node.parentNode = prev;
			prev = node;
			node = null;
		}
		return true;
	}
}
