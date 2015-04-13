package generation;

import java.util.List;

public class GameTree {
	
	private TradeNode root;
	private final int TREE_DEPTH = 7;
	private TradeGenerator generator;
	
	private final int MAX_LOCAL_RETRIES = 10000;
	private final int MAX_GLOBAL_RETRIES = 100;
	
	private int localRetriesCount = 0;
	private int globalRetriesCount = 0;
	
	public GameTree() {
		generator = new TradeGenerator();
		root = generator.generateRootTradeNode();
		if (!generateTree(root, TREE_DEPTH))
			throw new RuntimeException("FAILED TO GENERATE A VALID TREE: TIMEOUT");
		System.out.println(root);
	}
	
	public TradeNode getRoot() {
		return root;
	}
	
	private boolean generateTree(TradeNode node, int depth) {
		if (depth == 0){
			return true;
		}
		List<TradeNode> children = null;
		while (children == null) {
			children = generator.generateTradeNodes(node, root);
			if (localRetriesCount++ > MAX_LOCAL_RETRIES) {
				if (globalRetriesCount++ > MAX_GLOBAL_RETRIES) {
					return false;
				}
				localRetriesCount = 0;
				TradeNode root = generator.generateRootTradeNode();
				return generateTree(root, TREE_DEPTH);
			}
		}
		for (TradeNode n : children){
			node.parentNode = node;
			if (!generateTree(n, depth-1)){
				return false;
			}
		}
		return true;
	}
}
