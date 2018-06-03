package virassan.utils;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import virassan.entities.creatures.enemies.EnemyType;
import virassan.entities.creatures.player.Player;
import virassan.entities.creatures.utils.BuffTracker;
import virassan.entities.creatures.utils.SkillTracker;
import virassan.items.Equip;
import virassan.items.Item;
import virassan.main.Game;
import virassan.main.Handler;

/**
 * Nifty Utility Stuff
 * @author Virassan
 *
 */
public class Utils {
	
	public static final String saveDir = "res/saves/";
	public static final String mapDir = "res/worlds/maps/";
	public static final String staticAssetDir = "res/textures/";
	public static final String entityAssetDir = "res/textures/entities/";
	public static final String entityAssetPath = "/textures/entities/";
	public static final String errorLogDir = "res\\logs\\";
	
	
	public static File[] fileFinder(String dirName, String fileExt){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() { 
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(fileExt); }
        } );
    }
	
	/**
	 * Converts String to an int
	 * @param number
	 * @return the int - defaults at 0 if the string passed isn't a pure number
	 */
	public static int parseInt(String number){
		try{
			return Integer.parseInt(number);
		}catch(NumberFormatException e){
			System.out.println("Error Message Utils_parseInt NUMBER FORMAT EXCEPTION.");
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int positive(int var){
		return Math.abs(var);
	}
	
	
	/**
	 * Keeps the variable given from going below the min or above the max
	 * @param var the variable to be checked
	 * @param min the minimum that variable can be
	 * @param max the maxiumum that variable can be
	 * @return if above the max, returns the max, if below the min, returns the min, if neither it returns the variable unchanged
	 */
	public static int clamp(int var, int min, int max){
		if(max < min){
			max = min;
		}
		if(var >= max)
			return var = max;
		else if(var <= min)
			return var = min;
		else
			return var;
	}

	/**
	 * Keeps the variable given from going below the min or above the max
	 * @param var the variable to be checked
	 * @param min the minimum that variable can be
	 * @param max the maxiumum that variable can be
	 * @return if above the max, returns the max, if below the min, returns the min, if neither it returns the variable unchanged
	 */
	public static float clamp(float var, float min, float max){
		if(max < min){
			max = min;
		}
		if(var >= max)
			return var = max;
		else if(var <= min)
			return var = min;
		else
			return var;
	}
	
	/**
	 * Takes the image and removes the alpha amount from its opacity
	 * @param image The image wanted to become transparent
	 * @param alpha The numerical value to remove from the image's current opacity.
	 * @return A copied transparent image
	 */
	public static BufferedImage transparency(BufferedImage image, int alpha){
		BufferedImage ugh = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int y = 0; y < image.getHeight(); y++){
			for(int x = 0; x < image.getWidth(); x++){
				int rgb = image.getRGB(x, y);
				Color color = new Color(rgb, true);
				int red = color.getRed();
				int blue = color.getBlue();
				int green = color.getGreen();
				int a = color.getAlpha();
				if(red == 255 && blue == 255 && green == 255){
					ugh.setRGB(x, y, rgb);
				}else{
					Color target = new Color(red, green, blue, clamp(a-alpha, 0, 255));
					ugh.setRGB(x, y, target.getRGB());
				}
				// System.out.println("New RGB: " + ugh.getRGB(x, y) + "  Old RGB: " + image.getRGB(x, y));
			}
		}
		return ugh;
	}
	
	/**
	 * Takes the incoming String and wraps it to a new "line" if its length goes beyond the width
	 * @param text String to wrap
	 * @param width Width of area the String is allowed to be before it wraps
	 * @param textMetrics The FontMetrics of the Graphics, necessary to find the text's string width
	 * @return an Array of Strings of the text wrapped
	 */
	public static ArrayList<String> wrapText(String text, int width, FontMetrics textMetrics){
		ArrayList<String> map = new ArrayList<String>();
		String textToDraw = text;
		String[] arr =  textToDraw.split(" ");
		int nIndex = 0;
		while(nIndex < arr.length){
			String line = arr[nIndex++];
			while(nIndex < arr.length && textMetrics.stringWidth(line + " " + arr[nIndex]) < width){
				line = line + " " + arr[nIndex];
				nIndex++;
			}
			map.add(line);
		}
		return map;
	}
	
	/**
	 * Creates an error log text file
	 * @return
	 */
	public static String createErrorLog(){
		File[] files = Utils.fileFinder(errorLogDir, ".txt");
		Long lastMod = Long.MAX_VALUE;
		File choice = null;
		if(files.length > 8){
			for(File f : files){
				if(f.lastModified() < lastMod){
					choice = f;
					lastMod = f.lastModified();
				}
			}
		}
		if(choice != null){
			boolean p = choice.delete();
			if(!p){
				System.out.println("Error Message: Utils_createErrorLog Failed to delete file");
			}
		}
		
		String filepath = null;
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("ss_mm_HH-yy_MM_dd");
		LocalDateTime now = LocalDateTime.now();
		filepath = errorLogDir + now.format(dateTimeFormat) + ".txt";
		try{
			File file = new File(filepath);
			boolean b = file.createNewFile();
			if(!b){
				System.out.println("Error Message: Utils_createErrorLog File not created - " + filepath);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return filepath;
	}
	
	/**
	 * Appends the errorlog text file
	 * @param s The error message to be appended to the file
	 */
	public static void addErrorToLog(String s){
		try{
			DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			String time = now.format(dateTimeFormat);
			s = s + " " + time + System.getProperty("line.separator");
			Files.write(Paths.get(Game.errorLogFilename), s.getBytes(), StandardOpenOption.APPEND);
		}catch(IOException e){
			System.out.println("Error Message: Utils_addErrorToLog Could not append to file");
			e.printStackTrace();
		}catch(Exception k){
			System.out.println("Error Message: Utils_addErrorToLog UNKNOWN EXCEPTION");
			k.printStackTrace();
		}
	}
	
	
	
	
	// SAVE GAME UTILS
	//TODO: fix and replace and move these methods to the Save class - cannot do serialization
	
	
	/**
	 * For Mapping Quest Requirement current Amounts
	 * @param amount
	 * @param req
	 * @return
	 */
	public static Map toMap(int amount, Object req){
		HashMap<String, Object> map = new HashMap<>();
		String name;
		if(req instanceof Enum){
			name = ((Enum) req).name();
		}else{
			name = req.toString();
		}
		map.put("req", name);
		map.put("amt", amount);
		return map;
	}
	
	/*
	public static Map toMap(QuestTracker quest){
		HashMap<String, Object> map = new HashMap<>();
		map.put("quest", quest.getQuest().name());
		return map;
	}
	*/
	
	public static Map toMap(EnemyType type, int amount){
		HashMap<String, Object> map = new HashMap<>();
		map.put("enemy_id", type.getEnemyID());
		map.put("num", amount);
		return map;
	}
	
	public static Map toMap(BuffTracker buff){
		HashMap<String, Object> map = new HashMap<>();
		map.put("buff", buff.getBuff().name());
		int timer = 0;
		if(buff.getTimer() >= 1000){
			timer = 1000;
		}else{
			timer = (int)buff.getTimer();
		}
		map.put("buff_time", timer);
		return map;
	}
	
	public static Map toMap(SkillTracker skill){
		HashMap<String, String> map = new HashMap<>();
		map.put("skill", skill.getSkillType().name());
		return map;
	}
	
	/**
	 * Used for SkillBar
	 * @param skill the skill 
	 * @param index what slot on the skillbar it's in
	 * @return
	 */
	public static Map toMap(SkillTracker skill, int index){
		HashMap<String, Object> map = new HashMap<>();
		if(skill != null){
			map.put("skill", skill.getSkillType().name());
		}else{
			map.put("skill", "");
		}
		map.put("index", index);
		return map;
	}
	
	public static Map toMap(Item item){
		HashMap<String, String> map = new HashMap<>();
		if(item != null){
			map.put("item", item.name());
		}else{
			map.put("item", "");
		}
		return map;
	}
	
	public static Map toMap(Item item, int stack){
		HashMap<String, Object> map = new HashMap<>();
		map.put("item", item.name());
		map.put("stack", stack);
		return map;
	}
	
	/**
	 * Saves important Game Info into a JSON file
	 * @param handler The Game's Handler
	 */
	@SuppressWarnings("unchecked")
	public static void saveGame(Handler handler){
		Player player = handler.getPlayer();
		JSONObject obj = new JSONObject();
		obj.put("player_name", player.getName());
		obj.put("player_level", new Integer(player.getStats().getLevel()));
		obj.put("player_exp", new Integer(player.getStats().getExperience()));
		obj.put("player_gold", new Integer((int)player.getGold()));
		obj.put("char", new Integer(player.getTraits().getCharisma()));
		obj.put("dex", new Integer(player.getTraits().getDexterity()));
		obj.put("int", new Integer(player.getTraits().getIntelligence()));
		obj.put("str", new Integer(player.getTraits().getStrength()));
		obj.put("res", new Integer(player.getTraits().getResilience()));
		obj.put("x", new Integer((int)handler.getPlayer().getX()));
		obj.put("y", new Integer((int)handler.getPlayer().getY()));
		obj.put("map", handler.getMap().getFilepath());
		JSONArray stuff = new JSONArray();
		
		// EQUIP
		JSONObject equips = new JSONObject();
		JSONArray equip = new JSONArray();
		for(Equip slot : player.getStats().getEquip().keySet()){
			equip.add(toMap(player.getStats().getEquip().get(slot)));
		}
		equips.put("equip", equip);
		stuff.add(equips);
		
		// INVENTORY
		JSONObject inv = new JSONObject();
		JSONArray inventory = new JSONArray();
		for(Item item : player.getInventory().getItemMap().keySet()){
			inventory.add(toMap(item, player.getInventory().getItemMap().get(item)));
		}
		inv.put("inventory", inventory);
		stuff.add(inv);
		
		// SKILL BAR
		JSONObject skillbar = new JSONObject();
		JSONArray skillBar = new JSONArray();
		for(int i = 0; i < player.getSkillBar().length; i++){
			skillBar.add(toMap(player.getSkillBar()[i], i));
		}
		skillbar.put("skillbar", skillBar);
		stuff.add(skillbar);
		
		// ACTIVE QUESTS
		JSONObject aQuests = new JSONObject();
		JSONArray activeQuests = new JSONArray();
		
		/*
		for(QuestTracker quest : player.getQuestLog().getActive()){
			activeQuests.add(toMap(quest));
			JSONObject reqObj = new JSONObject();
			JSONArray reqs = new JSONArray();
			for(Object req : quest.getQuest().getHashMap().keySet()){
				reqs.add(toMap(quest.getCurAmt(req), req));
			}
			reqObj.put("reqs", reqs);
			activeQuests.add(reqObj);
		}
		*/
		
		aQuests.put("active_quests", activeQuests);
		stuff.add(aQuests);
		
		// COMPLETED QUESTS
		JSONObject cQuests = new JSONObject();
		JSONArray completedQuests = new JSONArray();
		
		/*
		for(QuestTracker quest : player.getQuestLog().getComplete()){
			completedQuests.add(toMap(quest));
		}
		*/
		
		cQuests.put("completed_quests", completedQuests);
		stuff.add(cQuests);
		
		// BUFFS
		JSONObject bList = new JSONObject();
		JSONArray buffs = new JSONArray();
		for(BuffTracker buff : player.getStats().getBuffs()){
			if(buff.getBuff() != null){
				buffs.add(toMap(buff));
			}
		}
		bList.put("buffs", buffs);
		stuff.add(bList);
		
		// SKILL LIST
		JSONObject sList = new JSONObject();
		JSONArray skills = new JSONArray();
		for(SkillTracker skill : player.getSkills()){
			skills.add(toMap(skill));
		}
		sList.put("skills", skills);
		stuff.add(sList);
		obj.put("arrays", stuff);
	
		try {
			FileWriter file = new FileWriter("c:\\Users\\Virassan\\Documents\\!ThunderDome\\ThunderDome\\ThunderDome\\res\\saves\\testsave.json");
			file.write(obj.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
