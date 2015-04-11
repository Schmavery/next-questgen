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
	
	private static final String tradeRulesFileName = "./json/npcTrades.json";
	private static final String itemRulesFileName = "./json/items.json";
	private static final String npcRulesFileName = "./json/npcs.json";
	private static final String itemTagsJsonFileName = "./json/itemTags.json";
	private static final String npcTagsJsonFileName = "./json/npcTags.json";
	
	// NOTE: these tags correspond to the npcTags.
	private static final String npcRulesTagsFileName = "./json/npcTradesTags.json";
	
	private ItemGenerator itemGenerator;
	private NpcGenerator npcGenerator;
	
	
//	private HashMap<String, ArrayList<Item>> npcTakes = new HashMap<>();
//	private HashMap<Item, ArrayList<String>> npcRewards = new HashMap<>();
	
	private HashMap<NPC, ArrayList<Item>> npcTakes = new HashMap<>();
	private HashMap<Item, ArrayList<NPC>> npcRewards = new HashMap<>();
//	npcTagTakesTag
//	npcTagRewardsTag
	
	
	private HashMap<String, ArrayList<Item>> tagTakes = new HashMap<>();
	private HashMap<String, ArrayList<Item>> tagRewards = new HashMap<>();
	
	Random rand = new Random();
	
	public TradeGenerator () {
		itemGenerator = new ItemGenerator(itemRulesFileName, itemTagsJsonFileName);
		npcGenerator = new NpcGenerator(npcRulesFileName, npcTagsJsonFileName);
//		JsonReader jsonReader;
//		try {
//			jsonReader = Json.createReader(new FileReader(tradeRulesFileName));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return;
//		}
//		loadTagRulesInfo();
//		
//		
//		JsonObject object = jsonReader.readObject();
//		Set<String> npcNameSet = object.keySet();
//		JsonObject rules;
//		JsonArray givesArray;
//		JsonArray takesArray;
//		for (String npcName : npcNameSet) {
//			rules = object.getJsonObject(npcName);
//			givesArray = rules.getJsonArray("gives");
//			takesArray = rules.getJsonArray("takes");
//			NPC npc = npcGenerator.getNpc(npcName);
//			for (JsonValue gives : givesArray) {
//				String itemNoun = ((JsonString) gives).getString();
//				Item item = itemGenerator.getItem(itemNoun);
//				ArrayList<String> npcList = npcRewards.get(item);
//				if (npcList == null) {
//					npcList = new ArrayList<>();
//					npcRewards.put(item, npcList);
//				}
//				if (!npcList.contains(npc))
//					npcList.add(npc.);
//			}
//			for (JsonValue takes : takesArray) {
//				String itemNoun = ((JsonString) takes).getString();
//				Item item = itemGenerator.getItem(itemNoun);
//				ArrayList<Item> list = npcTakes.get(npc);
//				if (list == null) {
//					list = new ArrayList<>();
//					npcTakes.put(npc, list);
//				}
//				if (!list.contains(item))
//					list.add(item);
//			}
//			
//			List<String> npcTags = npcGenerator.getTags(npcName);
//			if (npcTags != null) {
//				for (String tag : npcTags) {
//					List<Item> rewardsList = tagRewards.get(tag);
//					if (rewardsList != null) {
//						for (Item item : rewardsList) {
//							ArrayList<NPC> list = npcRewards.get(item);
//							if (list == null) {
//								list = new ArrayList<>();
//								npcRewards.put(item, list);
//							}
//							if (!list.contains(npc))
//								list.add(npc);
//						}
//					}
//					
//					List<Item> takesList = tagTakes.get(tag);
//					if (takesList != null) {
//						for (Item item : takesList) {
//							ArrayList<Item> list = npcTakes.get(npc);
//							if (list == null) {
//								list = new ArrayList<>();
//								npcTakes.put(npc, list);
//							}
//							if (!list.contains(item))
//								list.add(item);				
//						}
//					}
//				}
//			}
//		}
	}
	
//	private void loadTagRulesInfo() {
//		JsonReader jsonReader;
//		try {
//			jsonReader = Json.createReader(new FileReader(npcRulesTagsFileName));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return;
//		}
//		
//		JsonObject object = jsonReader.readObject();
//		Set<String> tagSet = object.keySet();
//		JsonArray requiresArray;
//		JsonArray rewardsArray;
//		JsonObject tag;
//		for (String tagName : tagSet) {
////			tags.add(tagName);
//			tag = object.getJsonObject(tagName);
//			requiresArray = tag.getJsonArray("gives");
//			rewardsArray = tag.getJsonArray("takes");
//			
//			if (requiresArray != null) {
//				for (JsonValue reqVal : requiresArray) {
//					String itemNoun = ((JsonString)reqVal).getString();					
//					Item item = itemGenerator.getItem(itemNoun);
//					ArrayList<Item> list = tagTakes.get(tagName);
//					if (list == null) {
//						list = new ArrayList<>();
//						tagTakes.put(tagName, list);
//					}
//					if (!list.contains(item))
//						list.add(item);
//				}
//			}
//			if (rewardsArray != null) {
//				for (JsonValue rewardVal : rewardsArray) {
//					String itemNoun = ((JsonString)rewardVal).getString();							
//					Item item = itemGenerator.getItem(itemNoun);
//					ArrayList<Item> list = tagRewards.get(tagName);
//					if (list == null) {
//						list = new ArrayList<>();
//						tagRewards.put(tagName, list);
//					}
//					if (!list.contains(item)) {
//						list.add(item);
//					}
//				}
//			}
//		}
//	}
		
	public TradeNode generateRootTradeNode() {
		NPC npc = getRandomNPC(new ArrayList<>(npcTakes.keySet()));
		Item receiveItem = getRandomItem(npcTakes.get(npc));
		if (receiveItem == null) return null;
		return new TradeNode(npc, receiveItem, null);
	}
	
	// ParentTradeNode is the trade which we will perform after the one being generated.
	public TradeNode generateTradeNode (TradeNode parentTradeNode) {
		System.out.println("here");
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
		ArrayList<NPC> npcCandidatesRaw = npcRewards.get(giveItem);
		ArrayList<NPC> npcCandidates = (ArrayList<NPC>) ((npcCandidatesRaw == null) ? null : npcCandidatesRaw.clone());
		if (usedNpcs != null && npcCandidates != null) {
			for (NPC npc : usedNpcs) {
				if (npcCandidates.contains(npc)) {
					npcCandidates.remove(npc);
				}
			}
		}
		
		NPC npc = getRandomNPC(npcCandidates);
		if (npc == null) return null;
		System.out.println("picking: " + npc.getIdentifier());
		
		ArrayList<Item> receiveItemCandidatesRaw = npcTakes.get(npc);
		ArrayList<Item> receiveItemCandidates = (ArrayList<Item>) ((receiveItemCandidatesRaw == null) ? null : receiveItemCandidatesRaw.clone());
		if (usedItems != null && receiveItemCandidates != null) {
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
