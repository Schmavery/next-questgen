package entities;

public class Item extends Entity {

	public Item(String identifier, String name, String description) {
		super(identifier, name, description);
		setEntityType(EntityType.ITEM);
	}
}
