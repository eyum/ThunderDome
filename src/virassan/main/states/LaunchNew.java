package virassan.main.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import virassan.entities.creatures.player.Player;
import virassan.entities.creatures.utils.Skill;
import virassan.entities.creatures.utils.SkillTracker;
import virassan.main.Handler;
import virassan.utils.SaveRead;
import virassan.utils.Utils;

public class LaunchNew {

	private Handler handler;
	private String mapID;
	private int curLoadCount;
	private String curLoadMes;
	
	private final String[] loadMessages = new String[] {"Creating Map", "Creating Character"};
	
	public LaunchNew(Handler handler) {
		this.handler = handler;
		curLoadCount = 0;
		curLoadMes = "";
		mapID = "haj_vi_01";
	}

	public void render(Graphics g){
		if(curLoadCount > 1){
			g.setColor(Handler.LAVENDER);
			g.fillRect(0,0, handler.getWidth(), handler.getHeight());
			g.setColor(Color.BLACK);
			g.setFont(new Font("Verdana", Font.BOLD, 18));
			g.drawString(curLoadMes, 300, 350);
		}
	}
	
	public void tick(double delta){
		if(curLoadCount == 0){
			System.out.println("Update Message: LaunchLoad_tick Loading map MAPID is: " + mapID);
			if(mapID == null){
				String mesg = "Error Message: LaunchLoad_tick MAPID IS NULL";
				System.out.println(mesg);
				Utils.addErrorToLog(mesg);
			}
			try{
				handler.setMapID(mapID);
			}catch(NullPointerException e){
				String mesg = "Error Message: LaunchLoad_tick MAPID IS NULL";
				System.out.println(mesg);
				Utils.addErrorToLog(mesg);
				e.printStackTrace();
			}
			curLoadCount++;
			curLoadMes = loadMessages[0];
		}
		
		else if(curLoadCount == 1){
			
			Player player = handler.getPlayer();
			player.setName("Player");
			player.addNewSkill(new SkillTracker(player, Skill.STAB, player.getAnimation()));
			Handler.GAMESTATE.setPlayer(handler.getPlayer());
			curLoadCount++;
			curLoadMes = loadMessages[1];
		}
		
		else if(curLoadCount == 2){
			curLoadCount++;
			System.out.println("Update Message: LaunchLoad_tick Loading Map within LoadMap");
			Handler.LOADMAP.setIsLoadSave(false);
			handler.setState(States.LoadMap);
		}
		
		
	}
	
}
