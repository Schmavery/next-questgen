package generation;

import entities.Item;
import entities.NPC;

public class TradeNode {

	public NPC npc;
	// npc will give 'give' for item 'receive'.
	public Item receive, give;
	
	public TradeNode (Item receive, Item give) {
		this.receive = receive;
		this.give = give;
	}
	
}
