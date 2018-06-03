package virassan.utils;

import java.util.ArrayList;
import java.util.HashMap;

import virassan.entities.creatures.player.Player;
import virassan.items.Equip;
import virassan.items.Item;

//TODO: cannot use serialization so possibly continue using JSON

/**
 * Save will collect all data to be written to a JSON file and organizes it
 * @author Virassan
 *
 */
public class Save {

	private int level, currentExp;
	private String name, mapID;
	private float x, y, health, mana, stamina, gold;
	
	private int charisma, strength, resilience, dexterity, intelligence;
	
	//TODO: use NPC, Enemy, and Quest IDs for save data
	private ArrayList<String> quests;
	private HashMap<String, Integer> killList;
	private HashMap<String, ArrayList<Boolean>> npcLikedMet;
	private ArrayList<String> skills;
	private ArrayList<String> skillBar;
	
	// Retrieved via Player's Stats
	private HashMap<Equip, Item> equips;
	private HashMap<Item, Integer> items;

	public Save(Player player) {
		//TODO: finish this
		
		// BASIC INFO
		level = player.getStats().getLevel();
		gold = player.getGold();
		currentExp = player.getStats().getExperience();
		name = player.getName();
		mapID = player.getHandler().getMapID();
		x = player.getX();
		y = player.getY();
		health = player.getStats().getHealth();
		mana = player.getStats().getMana();
		
		// TRAITS
		stamina = player.getStats().getStamina();
		charisma = player.getTraits().getCharisma();
		strength = player.getTraits().getStrength();
		resilience = player.getTraits().getResilience();
		dexterity = player.getTraits().getDexterity();
		intelligence = player.getTraits().getIntelligence();
		
		// QUESTS
		quests = new ArrayList<>();
		for(String q : player.getQuestLog().getAllQuestIDs()){
			quests.add(q);
		}
		
		// ITEMS
		equips = player.getStats().getEquip();
		items = player.getInventory().getItemMap();
		
	}

	
	
}
