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

import java.util.Map.Entry;

import entities.Item;

public class ItemGenerator {
	// This is a fancy flyweight
	private HashMap<String, Item> items = new HashMap<>();

	private ArrayList<String> tags = new ArrayList<>();
	private HashMap<String, ArrayList<String>> preModsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> postModsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> locsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> nounsTagMap = new HashMap<>();

	private Random random;
	public ItemGenerator(String itemTagsJsonFileName) {
		random = new Random(TradeGenerator.GEN_SEED + 2);
		loadItemTagInfo(itemTagsJsonFileName);
	}
	
		
	private void loadItemTagInfo(String itemTagsJsonFileName) {
		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(new FileReader(itemTagsJsonFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		JsonObject object = jsonReader.readObject();
		Set<String> tagSet = object.keySet();
		JsonArray preMods;
		JsonArray postMods;
		JsonArray locs;
		JsonArray nouns;
		JsonObject tag;
		for (String tagName : tagSet) {
			tags.add(tagName);
			tag = object.getJsonObject(tagName);
			preMods = tag.getJsonArray("pre-modifiers");
			postMods = tag.getJsonArray("post-modifiers");
			locs = tag.getJsonArray("locations");
			nouns = tag.getJsonArray("nouns");
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
			if (nouns != null) {
				for (JsonValue nounVal : nouns) {
					String noun = ((JsonString)nounVal).getString();
					addToHashmapList(noun, tagName, nounsTagMap);
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

	public List<Item> getItemsWithTag(String tag) {
		List<Item> items = new ArrayList<Item>();
		for (Entry<String, ArrayList<String>> entry : nounsTagMap.entrySet()) {
			if (entry.getValue().contains(tag)){
				items.add(getItem(entry.getKey()));
			}
		}
		return items;
	}
	
	public List<String> getTags(String itemName) {
		return nounsTagMap.get(itemName);
	}

	public Item getItem(String itemNoun) {
		Item item = items.get(itemNoun);
		if (item == null) {
			item = generateItem(itemNoun);
			items.put(itemNoun, item);
		}
		return item;
	}
	
	private Item generateItem(String noun) {
		List<String> tagsList = nounsTagMap.get(noun);
		if (tagsList == null) return null;
		ArrayList<String> preModsMap = new ArrayList<>();
		ArrayList<String> postModsMap = new ArrayList<>();
		ArrayList<String> locsMap = new ArrayList<>();
		for (String tag : tagsList) {
			List<String> preMods = preModsTagMap.get(tag);
			List<String> postMods = postModsTagMap.get(tag);
			List<String> locs = locsTagMap.get(tag);
			if (preMods != null)
				preModsMap.addAll(preMods);
			if (postMods != null)
				postModsMap.addAll(postMods);
			if (locs != null)
				locsMap.addAll(locs);
		}
		
		String preString = getRandom(preModsMap) + " ";
		if (preString.length() <= 1) preString = "";
		String postString = " " + getRandom(postModsMap);
		if (postString.length() <= 1) postString = "";
		String locString = " " + getRandom(locsMap);
		if (locString.length() <= 1) locString = "";
		String name = preString+noun+postString;
		return new Item(noun, name, "You see a "+name+locString);
	}
	
	private String getRandom(ArrayList<String> list) {
		if (list == null || list.size() == 0) return "";
		return list.get(random.nextInt(list.size()));
	}
}
