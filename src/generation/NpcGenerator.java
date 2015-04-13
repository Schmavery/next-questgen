package generation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import entities.Item;
import entities.NPC;


public class NpcGenerator {
	private HashMap<String, NPC> npcs = new HashMap<>();

	private HashMap<String, ArrayList<String>> preModsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> postModsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> locsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> requestDialogTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> acceptDialogTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> finishedDialogTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> nameTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> tagNameMap = new HashMap<>();

	private HashMap<String, ArrayList<String>> npcTakesTag = new HashMap<>();
	private HashMap<String, ArrayList<String>> npcRewardsTag = new HashMap<>();


	private Random random;

	public NpcGenerator (String npcTagsJsonFileName) {
		random = new Random(TradeGenerator.GEN_SEED + 1);
		loadNpcTagInfo(npcTagsJsonFileName);
	}
	
	private void loadNpcTagInfo(String npcTagsJsonFileName) {
		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(new FileReader(npcTagsJsonFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		JsonObject object = jsonReader.readObject();
		Set<String> tagSet = object.keySet();
		JsonArray preMods;
		JsonArray postMods;
		JsonArray locs;
		JsonArray requestDialogs;
		JsonArray acceptDialogs;
		JsonArray finishedDialogs;
		JsonArray namesList;
		JsonArray givesList;
		JsonArray takesList;
		JsonObject tag;
		for (String tagName : tagSet) {
			tag = object.getJsonObject(tagName);
			preMods = tag.getJsonArray("pre-modifiers");
			postMods = tag.getJsonArray("post-modifiers");
			locs = tag.getJsonArray("locations");
			requestDialogs = tag.getJsonArray("request-dialogs");
			acceptDialogs = tag.getJsonArray("accept-dialogs");
			finishedDialogs = tag.getJsonArray("finished-dialogs");
			namesList = tag.getJsonArray("names");
			givesList = tag.getJsonArray("gives");
			takesList = tag.getJsonArray("takes");
			if (preMods != null) {
				for (JsonValue preModVal : preMods) {
					String preMod = ((JsonString)preModVal).getString();
					addToHashmapList(tagName, preMod, preModsTagMap);
				}
			}
			if (postMods != null) {
				for (JsonValue postModVal : postMods) {
					String postMod = ((JsonString)postModVal).getString();
					addToHashmapList(tagName, postMod, postModsTagMap);
				}
			}
			if (locs != null) {
				for (JsonValue locVal : locs) {
					String loc = ((JsonString)locVal).getString();
					addToHashmapList(tagName, loc, locsTagMap);
				}
			}
			if (requestDialogs != null) {
				for (JsonValue requestDialogVal : requestDialogs) {
					String requestDialog = ((JsonString)requestDialogVal).getString();
					addToHashmapList(tagName, requestDialog, requestDialogTagMap);
				}
			}
			if (acceptDialogs != null) {
				for (JsonValue acceptDialogVal : acceptDialogs) {
					String acceptDialog = ((JsonString)acceptDialogVal).getString();
					addToHashmapList(tagName, acceptDialog, acceptDialogTagMap);
				}
			}
			if (finishedDialogs != null) {
				for (JsonValue finishedDialogVal : finishedDialogs) {
					String finishedDialog = ((JsonString)finishedDialogVal).getString();
					addToHashmapList(tagName, finishedDialog, finishedDialogTagMap);
				}
			}
			if (namesList != null) {
				for (JsonValue nameVal : namesList) {
					String name = ((JsonString)nameVal).getString();
					addToHashmapList(name, tagName, nameTagMap);
					addToHashmapList(tagName, name, tagNameMap);
				}
			}
			if (takesList != null) {
				for (JsonValue takeVal : takesList) {
					String takeTag = ((JsonString)takeVal).getString();
					addToHashmapList(tagName, takeTag, npcTakesTag);
				}
			}
			if (givesList != null) {
				for (JsonValue giveVal : givesList) {
					String giveTag = ((JsonString)giveVal).getString();
					addToHashmapList(giveTag, tagName, npcRewardsTag);
				}
			}
		}
	}
	
	private void addToHashmapList(String key, String element, HashMap<String, ArrayList<String>> map) {
		if (element == null)
			return;
		ArrayList<String> list = map.get(key);
		if (list == null) {
			list = new ArrayList<>();
			map.put(key, list);
		}
		list.add(element);
	}
		
	public List<String> getTags(String npcName) {
		return nameTagMap.get(npcName);
	}
	
	public NPC getNpc (String npcName) {
		NPC npc = npcs.get(npcName);
		if (npc == null) {
			npc = generateNPC(npcName);
			npcs.put(npcName, npc);
		}
		return npc;
	}
	
	public NPC getRandomNpc () {
		List<String> npcNames = getAllNPCNames();
		return getNpc(getRandom(npcNames));
	}
	
	private List<String> getAllNPCNames() {
		List<String> npcNames = new ArrayList<>();
		for (ArrayList<String> names : tagNameMap.values()) {
			for (String name : names) {
				npcNames.add(name);
			}
		}
		return npcNames;
	}
	
	public List<Item> getNPCTakes(NPC npc, ItemGenerator itemGen) {
		ArrayList<Item> takesItems = new ArrayList<>();
		List<String> npcTags = getTags(npc.getIdentifier());
		for (String npcTag : npcTags) {
			List<String> itemTags = npcTakesTag.get(npcTag);
			for (String itemTag : itemTags) {
				takesItems.addAll(itemGen.getItemsWithTag(itemTag));
			}
		}
		return takesItems;
	}
	
	public List<NPC> getNPCRewards(Item itemTagName, ItemGenerator itemGenerator) {
		ArrayList<NPC> rewardNPCs = new ArrayList<>();
		
		List<String> itemTags = itemGenerator.getTags(itemTagName.getIdentifier());
		for (String itemTag : itemTags) {
			List<String> npcTags = npcRewardsTag.get(itemTag);
			if (npcTags == null) continue;
			for (String npcTag: npcTags) {
				rewardNPCs.addAll(getNPCsWithTag(npcTag));
			}
		}
		return rewardNPCs;
	}
	
	private List<NPC> getNPCsWithTag(String npcTag) {
		List<NPC> npcs = new ArrayList<>();
		for (String npcName : tagNameMap.get(npcTag)){
			npcs.add(getNpc(npcName));
		}
		return npcs;
	}
	
	private NPC generateNPC(String name) {
		String tag = getRandom(getTags(name));
		String preString = getRandom(preModsTagMap.get(tag)) + " ";
		if (preString.length() <= 1) preString = "";
		String postString = " " + getRandom(postModsTagMap.get(tag));
		if (postString.length() <= 1) postString = "";
		String locString = " " + getRandom(locsTagMap.get(tag));
		if (locString.length() <= 1) locString = "";
		String requestDialog = " " + getRandom(requestDialogTagMap.get(tag));
		if (requestDialog.length() <= 1) requestDialog = "";
		String acceptDialog = " " + getRandom(acceptDialogTagMap.get(tag));
		if (acceptDialog.length() <= 1) acceptDialog = "";
		String finishedDialog = " " + getRandom(finishedDialogTagMap.get(tag));
		if (finishedDialog.length() <= 1) finishedDialog = "";

		String fullName = preString+name+postString;

		return new NPC(name, fullName, "You see a "+name+locString, requestDialog, acceptDialog, finishedDialog);
	}
	
	private String getRandom(List<String> list) {
		if (list == null || list.size() == 0) return "";
		return list.get(random.nextInt(list.size()));
	}
}
