package game;

import java.util.LinkedList;
import java.util.List;

import entities.Entity;
import entities.Entity.EntityType;

public class Utils {
	
	public static String stripFirst(String[] words){
		StringBuilder sb = new StringBuilder();
		if (words.length > 1){
			sb.append(words[1]);
		}
		for (int i = 2; i < words.length; i++){
			sb.append(" ");
			sb.append(words[i]);
		}
		return sb.toString();
	}
	
	
	/**
	 * Returns entities that match the pattern.  If type is not null, only returns entities of that type.
	 * @param pattern
	 * @param type
	 * @param entities
	 * @return
	 */
	public static List<Entity> getMatches(String pattern, EntityType type, List<? extends Entity> entities){
		List<Entity> matches = new LinkedList<>();
		for (Entity e : entities){
			if (e.getEntityType().equals(type)){
				if (e.getDescription().toLowerCase().contains(pattern.toLowerCase())){
					matches.add(e);
				}				
			}
		}
		return matches;
	}
	
	/**
	 * Generates a response for a pattern with multiple matches. "Do you mean the x, y, or z?"
	 * @param matches
	 * @return
	 */
	public static String multiMatchResponse(List<? extends Entity> matches){
		StringBuilder sb = new StringBuilder();
		sb.append("Do you mean the ");
		Entity prev = null;
		for (Entity e : matches){
			if (prev != null){
				sb.append(prev.getName());
				sb.append(", ");			
			}
			prev = e;
		}
		sb.append("or ");
		sb.append(prev.getName());
		sb.append("?");
		return sb.toString();
	}
}
