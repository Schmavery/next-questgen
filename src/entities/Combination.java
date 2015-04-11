package entities;

public class Combination {
	Item item1, item2, result;
	public Combination(Item i1, Item i2, Item result){
		this.item1 = i1;
		this.item2 = i2;
		this.result = result;
	}
	
	public Item tryCombine(Item i1, Item i2){
		if (i1.equals(item1) && i2.equals(i2)
				|| i1.equals(i2) && i2.equals(i1)){
			return result;
		}
		return null;
	}
}
