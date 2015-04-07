package entities;

public class NPC extends Entity {
	// TODO: Fix a/an by looking at whether next word starts with vowel or consonant.
	public NPC(String identifier, String name, String description) {
		super(identifier, name, description);
		setEntityType(EntityType.NPC);
	}

	//TODO: State for before/after trade 
	// (feel free to change my placeholder stuff)
	Item before, after;
	
	// I'm wondering if we should have a "Trade" object encapsulating whether the trade was successful,
	// the item you get in return and what the NPC says.
	// Then again, check what I did in GameEngine (the 'trade' case).  Maybe that's enough
	
	public Item trade(Item i){
		if (i.equals(before))
			return after;
		else
			return null;
	}
}
