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
	
	public TradeNode (NPC npc, Item receive, Item reward) {
		this.npc = npc;
		this.receives.add(receive);
		this.reward = reward;
	}
	
	// combine node
	public TradeNode (List<Item> receives, Item reward) {
		this.receives.addAll(receives);
		this.reward = reward;
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
			if (npc != null){
				System.out.println(npc.getName()+":"+(receive == null));
			} else {
				System.out.println(reward.getName()+(receive == null));
			}
			sb.append("Wants: " + receive.getName() + "\n");
		}
		if (children != null){
			sb.append("Children:{");
			for (TradeNode node : children){
				sb.append(node);
			}
			sb.append("}");			
		} else {
			sb.append("No children.");
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
	
	public void clearChildren(){
		children = null;
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
