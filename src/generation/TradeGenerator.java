package generation;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import javax.json.*;

import entities.Item;
import entities.NPC;

public class TradeGenerator {
// TODO: ensure items do not appear twice in tree (or might end up with cycles)	
	//	 construct the hashmaps
	
	private List<NPC> npcList;
	private List<Item> itemList;
	
	private HashMap<NPC, List<Item>> npcTakes = new HashMap<>();
	private HashMap<Item, List<NPC>> givenByNpcs = new HashMap<>();
	
	Random rand = new Random();
	
	public TradeGenerator (String jsonFileName, List<NPC> npcList, List<Item> itemList) {
		this.npcList = npcList;
		this.itemList = itemList;
		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(new FileReader(jsonFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		JsonObject object = jsonReader.readObject();
		Set<String> npcSet = object.keySet();
		JsonObject rules;
		JsonArray givesArray;
		JsonArray takesArray;
		for (String npcName : npcSet) {
			rules = object.getJsonObject(npcName);
			givesArray = rules.getJsonArray("gives");
			takesArray = rules.getJsonArray("takes");
			for (JsonValue gives : givesArray) {
				System.out.println(npcName + " gives " + gives.toString());
			}
			for (JsonValue takes : takesArray) {
				System.out.println(npcName + " takes " + takes.toString());
			}
		}
	}
	
	// ParentTradeNode is the trade which we will perform after the one being generated.
	public TradeNode generateTradeNode (TradeNode parentTradeNode) {
		// item that our new trade must give
		Item giveItem = parentTradeNode.receive;
		
		NPC npc = getRandomNPC(givenByNpcs.get(giveItem));
		if (npc == null) return null;
		
		Item receiveItem = getRandomItem(npcTakes.get(npc));
		if (receiveItem == null) return null;
		
		return new TradeNode(npc, receiveItem, giveItem);
	}
	
	
	private NPC getRandomNPC(List<NPC> npcs) {
		if (npcs == null || npcs.size() == 0) return null;
		return npcs.get(rand.nextInt(npcs.size()));
	}
	
	private Item getRandomItem(List<Item> items) {
		if (items == null || items.size() == 0) return null;
		return items.get(rand.nextInt(items.size()));
	}
}
