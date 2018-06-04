package virassan.main.states;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

import virassan.input.KeyInput;
import virassan.input.LinkedQueue;
import virassan.input.MouseInput;
import virassan.main.Handler;
import virassan.utils.Utils;

public class LaunchMenu {

	private Handler handler;
	private MouseInput mouseInput;
	private KeyInput keyInput;
	private long menuTimer, menuLast;
	private final long menuWait = 200;
	private boolean isSave;
	
	public LaunchMenu(Handler handler) {
		this.handler = handler;
		mouseInput = handler.getMouseInput();
		keyInput = handler.getKeyInput();
		isSave = true;
		menuTimer = 0;
		menuLast = 0;
	}
	
	public void render(Graphics g){
		if(isSave){
			g.setColor(Color.BLACK);
			g.drawString("To Start a New Game press ENTER", 200, 190);
			g.drawString("To Load the Last Save press SPACE", 200, 220);
			g.drawString("To toggle Fullscreen press F", 200, 300);
		}
	}
	
	public void tick(double delta){
		leftClick();
		menuTimer += (System.currentTimeMillis() - menuLast);
		menuLast = System.currentTimeMillis();
		if(isSave){
			boolean saveFile = new File(Utils.saveDir + "testsave2.json").isFile();
			if(saveFile){
				if(menuTimer > menuWait){
					if(keyInput.space){
						String filepath = "";
						File[] files = Utils.fileFinder(Utils.saveDir, ".json");
						filepath = files[files.length - 1].toString();
						Handler.LAUNCHLOAD.setFilepath(filepath);
						isSave = false;
						menuTimer = 0;
						handler.setState(States.LaunchLoad);
					}
				}
			}else{
				isSave = false;
			}
		}
		if(menuTimer > menuWait){
			if(keyInput.enter){
				isSave = false;
				handler.setState(States.LaunchNew);
				menuTimer = 0;
			}
			if(keyInput.F){
				//TODO: have the game switch fullscreen on/off
				handler.toggleFullScreen();
				menuTimer = 0;
			}
		}
	}
	
	private void leftClick(){
		//TODO: make an actual launch menu - Oh~ fancy i know
		LinkedQueue clicks = mouseInput.getLeftClicks();
		
	}

	
	private void setWindowSize(int height, int width){
		
	}
	
}
