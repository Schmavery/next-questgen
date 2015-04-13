package generation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import entities.Item;
import entities.NPC;

public class TradeGenerator {
	
	private static final String combineItemsRules = "./json/combineItemTagRules.json";
	private static final String itemTagsJsonFileName = "./json/itemTags.json";
	private static final String npcTagsJsonFileName = "./json/npcTags.json";
		
	private ItemGenerator itemGenerator;
	private NpcGenerator npcGenerator;
	private final double combineTradeRatio = 0.4;
	
	public static final long GEN_SEED = System.currentTimeMillis();
//	public static final long GEN_SEED = 1428897294239l;
	
	// reward tag --> list of recipes.  recipe is a list of tags
	private HashMap<String, List<List<String>>> combineRewardToRecipeMap = new HashMap<>();
	
	Random rand;
	
	public TradeGenerator () {
		rand = new Random(GEN_SEED);
		System.out.println("Seed: "+GEN_SEED);
		itemGenerator = new ItemGenerator(itemTagsJsonFileName);
		npcGenerator = new NpcGenerator(npcTagsJsonFileName);
		loadCombineRules();
	}
	
	private void loadCombineRules() {
		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(new FileReader(combineItemsRules));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		JsonArray rulesArray = jsonReader.readArray();
		JsonArray requiresArray;
		String reward;
		for (JsonValue combineRuleVal : rulesArray) {
			JsonObject combineRule = (JsonObject)combineRuleVal;
			requiresArray = combineRule.getJsonArray("takes");
			reward = combineRule.getJsonString("gives").getString();
			ArrayList<String> recipe = new ArrayList<>();
			for (JsonValue requiresVal : requiresArray) {
				String tag = ((JsonString)requiresVal).getString();
				recipe.add(tag);
			}
			List<List<String>> list = combineRewardToRecipeMap.get(reward);
			if (list == null) {
				list = new ArrayList<>();
				combineRewardToRecipeMap.put(reward, list);
			}
			list.add(recipe);
		}
		
	}
		
	public TradeNode generateRootTradeNode() {
		NPC npc = npcGenerator.getRandomNpc();
		Item receiveItem = getRandomItem(npcGenerator.getNPCTakes(npc, itemGenerator));
		if (receiveItem == null) return null;
		return new TradeNode(npc, receiveItem, null);
	}
	
	public void removeUsedNPCs(TradeNode node, List<NPC> npcs){
		if (node.isTrade()){
			while (npcs.contains(node.npc)) {
				npcs.remove(node.npc);
			}
		}
		if (node.getChildren() == null) return;
		for (TradeNode child : node.getChildren()){
			removeUsedNPCs(child, npcs);
		}
	}
	
	public void removeUsedItems(TradeNode node, List<Item> items){
		for (Item item : node.getReceives()) {
			while(items.contains(item)) {
				items.remove(item);
			}
		}
		if (node.getChildren() == null) return;
		for (TradeNode child : node.getChildren()){
			removeUsedItems(child, items);
		}
	}
	
	
	// ParentTradeNode is the trade which we will perform after the one being generated.
	public List<TradeNode> generateTradeNodes (TradeNode parentTradeNode, TradeNode root) {
		List<TradeNode> result = null;
		if (rand.nextDouble() < combineTradeRatio) {
			result = generateCombineTradeNodes(parentTradeNode, root);

		}
		if (result == null) {
			result = generateNpcTradeNodes(parentTradeNode, root);
		} 
		return result;
	}
	
	// List of a List of a List to the List from the List, rapitty-rap-rap
	private List<TradeNode> generateCombineTradeNodes (TradeNode parentTradeNode, TradeNode root) {
		List<TradeNode> result = new ArrayList<>();
		for (Item giveItem : parentTradeNode.getReceives()) {
			List<String> giveTags = itemGenerator.getTags(giveItem.getIdentifier());
			
			List<List<List<Item>>> recipeCandidates = new ArrayList<>();
			
			for (String giveTag : giveTags) {
				List<List<String>> recipes = combineRewardToRecipeMap.get(giveTag);
				if (recipes == null) continue;
				for (List<String> recipe : recipes) {
					List<List<Item>> recipeCandidate = new ArrayList<>(); // will have two elements, each filled with possible items for that slot in 'recipe'
					for (String tag : recipe) {
						List<Item> items = new ArrayList<>(itemGenerator.getItemsWithTag(tag));
						removeUsedItems(root, items);
						for (TradeNode sibling : result) {
							removeUsedItems(sibling, items);
						}
						if (items.size() > 0) {
							recipeCandidate.add(items);
						}
					}
					recipeCandidates.add(recipeCandidate);
				}
			}
			if (recipeCandidates.size() == 0) return null;
			List<List<Item>> recipe =  recipeCandidates.get(rand.nextInt(recipeCandidates.size()));
			if (recipe.size() < 2) return null;
			List<Item> receives = new ArrayList<>(2);
			
			if (recipe.get(0).isEmpty()) return null;
			receives.add(getRandomItem(recipe.get(0)));
			recipe.get(1).removeAll(receives);
			
			if (recipe.get(1).isEmpty()) return null;
			receives.add(getRandomItem(recipe.get(1)));
			
			TradeNode node = new TradeNode(receives, giveItem);

			result.add(node);
			parentTradeNode.addChild(node);
		}
		if (result.size() == 0) {
			parentTradeNode.clearChildren();
			return null;
		}
		return result;
	}
	
	private List<TradeNode> generateNpcTradeNodes (TradeNode parentTradeNode, TradeNode root) {
		// item that our new trade must give
		List<TradeNode> result = new ArrayList<>();
		for (Item giveItem : parentTradeNode.getReceives()) {

			List<NPC> npcCandidatesRaw = npcGenerator.getNPCRewards(giveItem, itemGenerator);
			List<NPC> npcCandidates = new ArrayList<>(npcCandidatesRaw);
			removeUsedNPCs(root, npcCandidates);
			for (TradeNode sibling : result) {
				removeUsedNPCs(sibling, npcCandidates);
			}
			NPC npc = getRandomNPC(npcCandidates);
			if (npc == null) return null;
			
			List<Item> receiveItemCandidatesRaw = npcGenerator.getNPCTakes(npc, itemGenerator);
			List<Item> receiveItemCandidates = new ArrayList<>(receiveItemCandidatesRaw);
			removeUsedItems(root, receiveItemCandidates);
			for (TradeNode sibling : result) {
				removeUsedItems(sibling, receiveItemCandidates);
			}
			
			Item receiveItem = getRandomItem(receiveItemCandidates);
			if (receiveItem == null) return null;
			TradeNode tn = new TradeNode(npc, receiveItem, giveItem);
			result.add(tn);
			parentTradeNode.addChild(tn);
		}
		if (result.size() == 0) {
			parentTradeNode.clearChildren();
			return null;
		}
		return result;
	}


	
	
	private NPC getRandomNPC(List<NPC> npcs) {
		if (npcs == null || npcs.size() == 0) return null;
		return npcs.get(rand.nextInt(npcs.size()));
	}
	
	private Item getRandomItem(List<Item> items) {
		if (items == null || items.size() == 0) {
//			throw new RuntimeException("RETURNING NULL THING");
			return null;
		}
		return items.get(rand.nextInt(items.size()));
	}
	
}
