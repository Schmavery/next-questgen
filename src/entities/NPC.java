package entities;

import generation.TradeNode;

public class NPC extends Entity {
	
	public NPC(String identifier, String name, String description, String requestDialog, String acceptDialog) {
		super(identifier, name, description);
		setEntityType(EntityType.NPC);
		this.requestDialog = requestDialog;
		this.acceptDialog = acceptDialog;
	}
	
	public void setTradeRule(TradeNode node) {
		requires = node.receive;
		reward = node.give;
		// TODO: Fix a/an by looking at whether next word starts with vowel or consonant.
		//		 replace markers in dialog with item names.
		tradeCompleted = false;
		if (reward == null)
			reward = new Item("victory", "Congratulations, you have won the game!", "Congratulations, you have won the game!");
	}

	String requestDialog, acceptDialog;
	boolean tradeCompleted = false;
	Item requires, reward;
	
	// I'm wondering if we should have a "Trade" object encapsulating whether the trade was successful,
	// the item you get in return and what the NPC says.
	// Then again, check what I did in GameEngine (the 'trade' case).  Maybe that's enough
	public boolean tradeCompleted() {
		return tradeCompleted;
	}
	
	public Item getRewardItem() {
		return reward;
	}
	
	public Item trade(Item i){
		if (i.equals(requires) && !tradeCompleted) {
			tradeCompleted = true;
			return reward;
		} else
			return null;
	}
}
