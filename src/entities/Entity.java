package entities;

public abstract class Entity implements Visible{
	public static enum EntityType {ITEM, NPC};
	private EntityType type;
	private String name, description;
	
	public String look() {
		return description;
	}
	
	public Entity(String name, String description){
		this.name = name;
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	protected void setEntityType(EntityType et){
		this.type = et;
	}

	public EntityType getEntityType(){
		return this.type;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
