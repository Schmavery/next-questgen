package entities;

import generation.TradeNode;

public class NPC extends Entity {
	
	public NPC(String identifier, String name, String description, String requestDialog, String acceptDialog) {
		super(identifier, name, description);
		setEntityType(EntityType.NPC);
		this.acceptDialog = acceptDialog;
		this.requestDialog = requestDialog;
	}
	
	public void setTradeRule(TradeNode node) {
		requires = node.receive;
		reward = node.give;
		// TODO: Fix a/an by looking at whether next word starts with vowel or consonant.
		//		 replace markers in dialog with item names.
		// https://github.com/rigoneri/indefinite-article.js/blob/master/indefinite-article.js
		tradeCompleted = false;
		requestDialog = requestDialog
				.replaceAll("\\[give\\]", requires == null ? "" : requires.getName())
				.replaceAll("\\[take\\]", reward == null ? "" : reward.getName());
		acceptDialog = acceptDialog
				.replaceAll("\\[give\\]", requires == null ? "" : requires.getName())
				.replaceAll("\\[take\\]", reward == null ? "" : reward.getName());
		if (reward == null)
			reward = new Item("victory", "Congratulations, you have won the game!", "Congratulations, you have won the game!");
	}

	String requestDialog, acceptDialog;
	boolean tradeCompleted = false;
	Item requires, reward;
	
	public boolean tradeCompleted() {
		return tradeCompleted;
	}
	
	public Item getRequiredItem() {
		return requires;
	}
	
	public String talk(){
		if (tradeCompleted){
			return acceptDialog;
		} else {
			return requestDialog;
		}
	}
	
	public Item trade(Item i){
		if (i.equals(requires) && !tradeCompleted) {
			tradeCompleted = true;
			return reward;
		} else
			return null;
	}
}
