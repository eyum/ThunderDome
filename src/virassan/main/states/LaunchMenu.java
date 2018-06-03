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
	
	private boolean isSave;
	
	public LaunchMenu(Handler handler) {
		this.handler = handler;
		mouseInput = handler.getMouseInput();
		keyInput = handler.getKeyInput();
		isSave = true;
	}
	
	public void render(Graphics g){
		if(isSave){
			g.setColor(Color.BLACK);
			g.drawString("To Start a New Game press ENTER", 200, 190);
			g.drawString("To Load the Last Save press SPACE", 200, 220);
		}
	}
	
	public void tick(double delta){
		leftClick();
		if(isSave){
			boolean saveFile = new File(Utils.saveDir + "testsave2.json").isFile();
			keyInput.tick(delta);
			if(saveFile){
				if(keyInput.space){
					String filepath = "";
					File[] files = Utils.fileFinder(Utils.saveDir, ".json");
					filepath = files[files.length - 1].toString();
					Handler.LAUNCHLOAD.setFilepath(filepath);
					isSave = false;
					handler.setState(States.LaunchLoad);
				}
			}else{
				isSave = false;
			}
			if(keyInput.enter){
				isSave = false;
				handler.setState(States.LaunchNew);
			}
		}
	}
	
	private void leftClick(){
		//TODO: make an actual launch menu - Oh~ fancy i know
		LinkedQueue clicks = mouseInput.getLeftClicks();
		
	}

}
