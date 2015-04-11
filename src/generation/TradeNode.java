package generation;

import java.util.ArrayList;
import java.util.List;

import entities.Item;
import entities.NPC;

public class TradeNode {

	public NPC npc;
	// npc will give 'give' for item 'receive'.
	private List<Item> receives = new ArrayList<>(2);
	public Item reward;
	private List<TradeNode> children;
	public TradeNode parentNode;
	
	public TradeNode (NPC npc, Item receive, Item result) {
		this.npc = npc;
		this.receives.add(receive);
		this.reward = result;
	}
	
	public String toString () {
		StringBuilder sb = new StringBuilder();
		if (isTrade()){
			sb.append("NPC:" + npc.getName() + "\n");			
		} else {
			sb.append("Combination:\n");
		}
		if (reward != null)
			sb.append("Gives: " + reward.getName() + "\n");
		for (Item receive : receives){
			sb.append("Wants: " + receive.getName() + "\n");
		}
		return sb.toString();
	}
	
	public boolean isTrade(){
		return npc != null;
	}
	
	public void addChild(TradeNode tn){
		if (children == null){
			children = new ArrayList<>();
		}
		children.add(tn);
	}
	
	public List<TradeNode> getChildren(){
		return children;
	}
	
	public List<Item> getReceives(){
		return receives;
	}
	
	public Item getReward(){
		return reward;
	}
	
	public void setChildren(List<TradeNode> tns){
		this.children = tns;
	}
	
	public boolean isLeaf(){
		return children == null;
	}
	
}
