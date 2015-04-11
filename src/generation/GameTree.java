package generation;

import java.util.List;

public class GameTree {
	
	private TradeNode root;
	private final int TREE_DEPTH = 2;
	private TradeGenerator generator;
	

	public GameTree() {
		generator = new TradeGenerator();
		root = generator.generateRootTradeNode();
		if (!generateTree(root, TREE_DEPTH))
			throw new RuntimeException("FAILED TO GENERATE A VALID TREE: TIMEOUT");
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
			children = generator.generateTradeNode(node);
		}
		node.setChildren(children);
		for (TradeNode n : children){
			node.parentNode = node;
			generateTree(n, depth-1);
		}
		return true;
	}
}
