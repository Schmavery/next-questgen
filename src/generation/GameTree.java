package generation;

import entities.Combination;
import entities.Item;

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
		prettyPrint();
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
		}
		node.setChildren(children);
		for (TradeNode n : children){
			node.parentNode = node;
			generateTree(n, depth-1);
		}
		return true;
	}
	
	public void prettyPrint() {
		prettyPrint(root);
	}
	
	private void prettyPrint(TradeNode node) {
		
	}
}
