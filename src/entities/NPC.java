package entities;

import generation.TradeNode;

public class NPC extends Entity {
	
	String requestDialog, acceptDialog, finishedDialog;
	boolean tradeCompleted = false;
	Item requires, reward;
	
	
	public NPC(String identifier, String name, String description, String requestDialog, String acceptDialog, String finishedDialog) {
		super(identifier, name, description);
		setEntityType(EntityType.NPC);
		this.acceptDialog = acceptDialog;
		this.requestDialog = requestDialog;
		this.finishedDialog = finishedDialog;
	}
	
	public void setTradeRule(TradeNode node) {
		requires = node.receive;
		reward = node.give;
		// TODO: Fix a/an by looking at whether next word starts with vowel or consonant.
		//		 replace markers in dialog with item names.
		// https://github.com/rigoneri/indefinite-article.js/blob/master/indefinite-article.js
		// cool ^that looks fine. (only if we have time though, not that important).
		tradeCompleted = false;
		requestDialog = requestDialog
				.replaceAll("\\[give\\]", requires == null ? "" : requires.getName())
				.replaceAll("\\[take\\]", reward == null ? "" : reward.getName());
		acceptDialog = acceptDialog
				.replaceAll("\\[give\\]", requires == null ? "" : requires.getName())
				.replaceAll("\\[take\\]", reward == null ? "" : reward.getName());
		finishedDialog = finishedDialog
				.replaceAll("\\[give\\]", requires == null ? "" : requires.getName())
				.replaceAll("\\[take\\]", reward == null ? "" : reward.getName());
		if (reward == null) {
			reward = new Item("Victory", "The pride of Victory!", "The pride of Victory!");
			acceptDialog = "Congratulations, you have won the game!";
		}
	}
	
	public boolean tradeCompleted() {
		return tradeCompleted;
	}
	
	public Item getRequiredItem() {
		return requires;
	}
	
	public String acceptTradeTalk() {
		return acceptDialog;
	}
	
	public String talk(){
		if (tradeCompleted){
			return finishedDialog;
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
