package generation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Set;

import javax.json.*;

import entities.NPC;

public class NPCRules {
	
	public NPCRules (String jsonFileName) {
		JsonReader jsonReader;
		try {
			jsonReader = Json.createReader(new FileReader(jsonFileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		JsonObject object = jsonReader.readObject();
		Set<String> npcSet = object.keySet();
		JsonObject rules;
		JsonArray givesArray;
		JsonArray takesArray;
		NPC npc;
		for (String npcString : npcSet) {
			rules = object.getJsonObject(npcString);
//			npc = new NPC(npcString);
			givesArray = rules.getJsonArray("gives");
			takesArray = rules.getJsonArray("takes");
			for (JsonValue gives : givesArray) {
				System.out.println(npcString + " gives " + gives.toString());
			}
			for (JsonValue takes : takesArray) {
				System.out.println(npcString + " takes " + takes.toString());
			}
		}
		
//		JsonArray rules = object.getJsonArray("rules");
//		for (JsonValue jsonValue : rules) {
//			
//			System.out.println(jsonValue.toString());
//		}
//		 
//		 jsonReader.close();
		 
		 
		 
//        JSONParser parser = new JSONParser();
//        
//        try {
// 
//            Object obj = parser.parse(new FileReader(
//                    "/Users/<username>/Documents/file1.txt"));
// 
//            JSONObject jsonObject = (JSONObject) obj;
// 
//            String name = (String) jsonObject.get("Name");
//            String author = (String) jsonObject.get("Author");
//            JSONArray companyList = (JSONArray) jsonObject.get("Company List");
// 
//            System.out.println("Name: " + name);
//            System.out.println("Author: " + author);
//            System.out.println("\nCompany List:");
//            Iterator<String> iterator = companyList.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }

	}
}
