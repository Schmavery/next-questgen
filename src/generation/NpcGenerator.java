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

import entities.NPC;


public class NpcGenerator {
	
	private ArrayList<String> npcNames = new ArrayList<>();
	private HashMap<String, ArrayList<String>> preModsMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> postModsMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> locsMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> requestDialogMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> acceptDialogMap = new HashMap<>();
	private Random random;

	public NpcGenerator (String npcRulesFileName) {
		random = new Random();
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
		JsonObject npcRules;
		for (String name : nameSet) {
			npcNames.add(name);
			npcRules = object.getJsonObject(name);
			preMods = npcRules.getJsonArray("pre-modifiers");
			postMods = npcRules.getJsonArray("post-modifiers");
			locs = npcRules.getJsonArray("locations");
			requestDialogs = npcRules.getJsonArray("request-dialogs");
			acceptDialogs = npcRules.getJsonArray("accept-dialogs");
			if (preMods != null) {
				for (JsonValue preModVal : preMods) {
					String preMod = ((JsonString)preModVal).getString();
					ArrayList<String> list = preModsMap.get(name);
					if (list == null) {
						list = new ArrayList<>();
						preModsMap.put(name, list);
					}
					list.add(preMod);
				}
			}
			if (postMods != null) {
				for (JsonValue postModVal : postMods) {
					String postMod = ((JsonString)postModVal).getString();
					ArrayList<String> list = postModsMap.get(name);
					if (list == null) {
						list = new ArrayList<>();
						postModsMap.put(name, list);
					}
					list.add(postMod);
				}
			}
			if (locs != null) {
				for (JsonValue locVal : locs) {
					String loc = ((JsonString)locVal).getString();
					ArrayList<String> list = locsMap.get(name);
					if (list == null) {
						list = new ArrayList<>();
						locsMap.put(name, list);
					}
					list.add(loc);
				}
			}
			if (requestDialogs != null) {
				for (JsonValue requestDialogVal : requestDialogs) {
					String requestDialog = ((JsonString)requestDialogVal).getString();
					ArrayList<String> list = requestDialogMap.get(name);
					if (list == null) {
						list = new ArrayList<>();
						requestDialogMap.put(name, list);
					}
					list.add(requestDialog);
				}
			}
			if (acceptDialogs != null) {
				for (JsonValue acceptDialogVal : acceptDialogs) {
					String acceptDialog = ((JsonString)acceptDialogVal).getString();
					ArrayList<String> list = acceptDialogMap.get(name);
					if (list == null) {
						list = new ArrayList<>();
						acceptDialogMap.put(name, list);
					}
					list.add(acceptDialog);
				}
			}
		}
	}
	
	public NPC generateNPC(String name) {
		String preString = getRandom(preModsMap.get(name)) + " ";
		if (preString.length() <= 1) preString = "";
		String postString = " " + getRandom(postModsMap.get(name));
		if (postString.length() <= 1) postString = "";
		String locString = " " + getRandom(locsMap.get(name));
		if (locString.length() <= 1) locString = "";
		String fullName = preString+name+postString;

		return new NPC(name, fullName, "You see a "+name+locString);
	}
	
	private String getRandom(ArrayList<String> list) {
		if (list == null || list.size() == 0) return "";
		return list.get(random.nextInt(list.size()));
	}
}
