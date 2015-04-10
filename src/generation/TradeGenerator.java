package generation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.json.*;

import entities.Item;
import entities.NPC;

public class TradeGenerator {
// TODO: ensure items do not appear twice in tree (or might end up with cycles)	
	//	 construct the hashmaps
	
	private ItemGenerator itemGenerator;
	private NpcGenerator npcGenerator;
	
	private HashMap<NPC, ArrayList<Item>> npcTakes = new HashMap<>();
	private HashMap<Item, ArrayList<NPC>> givenByNpcs = new HashMap<>();
	
	Random rand = new Random();
	
	public TradeGenerator (String tradeRulesFileName, String itemRulesFileName, String npcRulesFileName, String itemTagsJsonFileName) {
		itemGenerator = new ItemGenerator(itemRulesFileName, itemTagsJsonFileName);
		npcGenerator = new NpcGenerator(npcRulesFileName);
		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(new FileReader(tradeRulesFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		JsonObject object = jsonReader.readObject();
		Set<String> npcNameSet = object.keySet();
		JsonObject rules;
		JsonArray givesArray;
		JsonArray takesArray;
		for (String npcName : npcNameSet) {
			rules = object.getJsonObject(npcName);
			givesArray = rules.getJsonArray("gives");
			takesArray = rules.getJsonArray("takes");
			NPC npc = npcGenerator.getNpc(npcName);
			for (JsonValue gives : givesArray) {
				String itemNoun = ((JsonString) gives).getString();
				Item item = itemGenerator.getItem(itemNoun);
				ArrayList<NPC> list = givenByNpcs.get(item);
				if (list == null) {
					list = new ArrayList<>();
					givenByNpcs.put(item, list);
				}
				list.add(npc);
			}
			for (JsonValue takes : takesArray) {
				String itemNoun = ((JsonString) takes).getString();
				Item item = itemGenerator.getItem(itemNoun);
				ArrayList<Item> list = npcTakes.get(npc);
				if (list == null) {
					list = new ArrayList<>();
					npcTakes.put(npc, list);
				}
				list.add(item);				
			}
		}
	}
		
	public TradeNode generateRootTradeNode() {
		NPC npc = getRandomNPC(new ArrayList<>(npcTakes.keySet()));
		Item receiveItem = getRandomItem(npcTakes.get(npc));
		if (receiveItem == null) return null;
		return new TradeNode(npc, receiveItem, null);
	}
	
	// ParentTradeNode is the trade which we will perform after the one being generated.
	public TradeNode generateTradeNode (TradeNode parentTradeNode) {
		// item that our new trade must give
		Item giveItem = parentTradeNode.receive;
		Set<NPC> usedNpcs = new HashSet<>();
		Set<Item> usedItems = new HashSet<>();
		TradeNode node = parentTradeNode;
		while (node != null) {
			usedNpcs.add(node.npc);
			usedItems.add(node.give);
			node = node.parentNode;
		}
		ArrayList<NPC> npcCandidates = givenByNpcs.get(giveItem);
		if (usedNpcs != null && npcCandidates != null) {
			npcCandidates = (ArrayList<NPC>) npcCandidates.clone();
			for (NPC npc : usedNpcs) {
				if (npcCandidates.contains(npc))
					npcCandidates.remove(npc);
			}
		}
		
		NPC npc = getRandomNPC(npcCandidates);
		if (npc == null) return null;
		
		ArrayList<Item> receiveItemCandidates = npcTakes.get(npc);
		if (usedItems != null && receiveItemCandidates != null) {
			receiveItemCandidates = (ArrayList<Item>) receiveItemCandidates.clone();
			for (Item item : usedItems) {
				if (receiveItemCandidates.contains(item))
					receiveItemCandidates.remove(item);
			}
		}
		
		Item receiveItem = getRandomItem(receiveItemCandidates);
		if (receiveItem == null) return null;
		
		return new TradeNode(npc, receiveItem, giveItem);
	}
	
	
	private NPC getRandomNPC(ArrayList<NPC> npcs) {
		if (npcs == null || npcs.size() == 0) return null;
		return npcs.get(rand.nextInt(npcs.size()));
	}
	
	private Item getRandomItem(ArrayList<Item> items) {
		if (items == null || items.size() == 0) return null;
		return items.get(rand.nextInt(items.size()));
	}
	
}
