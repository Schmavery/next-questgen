package generation;

import entities.Item;
import entities.NPC;

public class TradeNode {

	public NPC npc;
	// npc will give 'give' for item 'receive'.
	public Item receive, give;
	public TradeNode childNode;
	public TradeNode parentNode;
	
	public TradeNode (NPC npc, Item receive, Item give) {
		this.npc = npc;
		this.receive = receive;
		this.give = give;
	}
	
	public String toString () {
		String s = "NPC:" + npc.getName() + "\n";
		if (give != null)
			s += "Gives: " + give.getName() + "\n";
		if (receive != null)
			s += "Wants: " + receive.getName() + "\n";
		return s;
	}
}
