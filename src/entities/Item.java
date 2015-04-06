package entities;

public class Item extends Entity {
	private String coreNoun;
	public Item(String coreNoun, String name, String description) {
		super(name, description);
		this.coreNoun = coreNoun;
		setEntityType(EntityType.ITEM);
	}
	
	public String getCoreNoun() {
		return coreNoun;
	}
}
