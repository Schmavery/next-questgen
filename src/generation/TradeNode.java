package generation;

import entities.Item;
import entities.NPC;

public class TradeNode {

	public NPC npc;
	// npc will give 'give' for item 'receive'.
	public Item receive, give;
	
	public TradeNode (NPC npc, Item receive, Item give) {
		this.npc = npc;
		this.receive = receive;
		this.give = give;
	}
	
}
