package generation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import entities.Item;

public class ItemGenerator {
	// This is a fancy flyweight
	private HashMap<String, Item> items = new HashMap<>();

	private ArrayList<String> nouns = new ArrayList<>();
	private HashMap<String, ArrayList<String>> preModsMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> postModsMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> locsMap = new HashMap<>();
	
	private ArrayList<String> tags = new ArrayList<>();
	private HashMap<String, ArrayList<String>> preModsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> postModsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> locsTagMap = new HashMap<>();

	private Random random;
	public ItemGenerator(String itemsJsonFileName, String itemTagsJsonFileName) {
		random = new Random();
		loadItemTagInfo(itemTagsJsonFileName);
		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(new FileReader(itemsJsonFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		JsonObject object = jsonReader.readObject();
		Set<String> nounSet = object.keySet();
		JsonArray preMods;
		JsonArray postMods;
		JsonArray locs;
		JsonArray itemTags;
		JsonObject itemRules;
		for (String noun : nounSet) {
			nouns.add(noun);
			itemRules = object.getJsonObject(noun);
			preMods = itemRules.getJsonArray("pre-modifiers");
			postMods = itemRules.getJsonArray("post-modifiers");
			locs = itemRules.getJsonArray("locations");
			itemTags = itemRules.getJsonArray("tags");
			if (preMods != null) {
				for (JsonValue preModVal : preMods) {
					String preMod = ((JsonString)preModVal).getString();
					addToHashmapList(noun, preMod, preModsMap);
				}
			}
			if (postMods != null) {
				for (JsonValue postModVal : postMods) {
					String postMod = ((JsonString)postModVal).getString();
					addToHashmapList(noun, postMod, postModsMap);
				}
			}
			if (locs != null) {
				for (JsonValue locVal : locs) {
					String loc = ((JsonString)locVal).getString();
					addToHashmapList(noun, loc, locsMap);
				}
			}
			if (itemTags != null) {
				for (JsonValue tagVal : itemTags) {
					String tag = ((JsonString)tagVal).getString();
					addToHashmapList(noun, preModsTagMap.get(tag), preModsMap);
					addToHashmapList(noun, postModsTagMap.get(tag), postModsMap);
					addToHashmapList(noun, locsTagMap.get(tag), locsMap);
				}
			}
		}
		
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
		JsonObject tag;
		for (String tagName : tagSet) {
			tags.add(tagName);
			tag = object.getJsonObject(tagName);
			preMods = tag.getJsonArray("pre-modifiers");
			postMods = tag.getJsonArray("post-modifiers");
			locs = tag.getJsonArray("locations");
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
	
	private void addToHashmapList(String key, ArrayList<String> appendList, HashMap<String, ArrayList<String>> map) {
		if (appendList == null || appendList.size() == 0)
			return;
		ArrayList<String> list = map.get(key);
		if (list == null) {
			list = new ArrayList<>();
			map.put(key, list);
		}
		list.addAll(appendList);
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
		String preString = getRandom(preModsMap.get(noun)) + " ";
		if (preString.length() <= 1) preString = "";
		String postString = " " + getRandom(postModsMap.get(noun));
		if (postString.length() <= 1) postString = "";
		String locString = " " + getRandom(locsMap.get(noun));
		if (locString.length() <= 1) locString = "";
		String name = preString+noun+postString;
		return new Item(noun, name, "You see a "+name+locString);
	}
	
	private String getRandom(ArrayList<String> list) {
		if (list == null || list.size() == 0) return "";
		return list.get(random.nextInt(list.size()));
	}
}
