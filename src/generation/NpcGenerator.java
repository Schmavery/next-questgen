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
import entities.NPC;


public class NpcGenerator {
	private HashMap<String, NPC> npcs = new HashMap<>();

	private ArrayList<String> npcNames = new ArrayList<>();
	private HashMap<String, ArrayList<String>> preModsMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> postModsMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> locsMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> requestDialogMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> acceptDialogMap = new HashMap<>();
	
	private ArrayList<String> tags = new ArrayList<>();
	private HashMap<String, ArrayList<String>> preModsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> postModsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> locsTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> requestDialogTagMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> acceptDialogTagMap = new HashMap<>();


	private Random random;

	public NpcGenerator (String npcRulesFileName, String npcTagsJsonFileName) {
		random = new Random();
		loadNpcTagInfo(npcTagsJsonFileName);
		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(new FileReader(npcRulesFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		JsonObject object = jsonReader.readObject();
		Set<String> nameSet = object.keySet();
		JsonArray preMods;
		JsonArray postMods;
		JsonArray locs;
		JsonArray requestDialogs;
		JsonArray acceptDialogs;
		JsonArray npcTags;
		JsonObject npcRules;
		for (String name : nameSet) {
			npcNames.add(name);
			npcRules = object.getJsonObject(name);
			preMods = npcRules.getJsonArray("pre-modifiers");
			postMods = npcRules.getJsonArray("post-modifiers");
			locs = npcRules.getJsonArray("locations");
			requestDialogs = npcRules.getJsonArray("request-dialogs");
			acceptDialogs = npcRules.getJsonArray("accept-dialogs");
			npcTags = npcRules.getJsonArray("tags");
			if (preMods != null) {
				for (JsonValue preModVal : preMods) {
					String preMod = ((JsonString)preModVal).getString();
					addToHashmapList(name, preMod, preModsMap);
				}
			}
			if (postMods != null) {
				for (JsonValue postModVal : postMods) {
					String postMod = ((JsonString)postModVal).getString();
					addToHashmapList(name, postMod, postModsMap);
				}
			}
			if (locs != null) {
				for (JsonValue locVal : locs) {
					String loc = ((JsonString)locVal).getString();
					addToHashmapList(name, loc, locsMap);
				}
			}
			if (requestDialogs != null) {
				for (JsonValue requestDialogVal : requestDialogs) {
					String requestDialog = ((JsonString)requestDialogVal).getString();
					addToHashmapList(name, requestDialog, requestDialogMap);
				}
			}
			if (acceptDialogs != null) {
				for (JsonValue acceptDialogVal : acceptDialogs) {
					String acceptDialog = ((JsonString)acceptDialogVal).getString();
					addToHashmapList(name, acceptDialog, acceptDialogMap);
				}
			}
			if (npcTags != null) {
				for (JsonValue tagVal : npcTags) {
					String tag = ((JsonString)tagVal).getString();
					addToHashmapList(name, preModsTagMap.get(tag), preModsMap);
					addToHashmapList(name, postModsTagMap.get(tag), postModsMap);
					addToHashmapList(name, locsTagMap.get(tag), locsMap);
					addToHashmapList(name, requestDialogTagMap.get(tag), requestDialogMap);
					addToHashmapList(name, acceptDialogTagMap.get(tag), acceptDialogMap);
				}
			}
		}
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
		JsonObject tag;
		for (String tagName : tagSet) {
			tags.add(tagName);
			tag = object.getJsonObject(tagName);
			preMods = tag.getJsonArray("pre-modifiers");
			postMods = tag.getJsonArray("post-modifiers");
			locs = tag.getJsonArray("locations");
			requestDialogs = tag.getJsonArray("request-dialogs");
			acceptDialogs = tag.getJsonArray("accept-dialogs");
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
	
	public NPC getNpc (String npcName) {
		NPC npc = npcs.get(npcName);
		if (npc == null) {
			npc = generateNPC(npcName);
			npcs.put(npcName, npc);
		}
		return npc;
	}

//	requestDialogMap = new HashMap<>();
//	private HashMap<String, ArrayList<String>> acceptDialogMap
	private NPC generateNPC(String name) {
		String preString = getRandom(preModsMap.get(name)) + " ";
		if (preString.length() <= 1) preString = "";
		String postString = " " + getRandom(postModsMap.get(name));
		if (postString.length() <= 1) postString = "";
		String locString = " " + getRandom(locsMap.get(name));
		if (locString.length() <= 1) locString = "";
		String requestDialog = " " + getRandom(requestDialogMap.get(name));
		if (requestDialog.length() <= 1) requestDialog = "";
		String acceptDialog = " " + getRandom(acceptDialogMap.get(name));
		if (acceptDialog.length() <= 1) acceptDialog = "";
		String fullName = preString+name+postString;

		return new NPC(name, fullName, "You see a "+name+locString, requestDialog, acceptDialog);
	}
	
	private String getRandom(ArrayList<String> list) {
		if (list == null || list.size() == 0) return "";
		return list.get(random.nextInt(list.size()));
	}
}
