package virassan.main.states;

import java.awt.Graphics;

import virassan.entities.creatures.player.Player;
import virassan.gfx.hud.HUDManager;
import virassan.input.KeyInput;
import virassan.input.MouseInput;
import virassan.main.Handler;

public class MenuCharacter {

	private Handler handler;
	private Player player;
	private MouseInput mouseInput;
	private KeyInput keyInput;
	
	//TODO: possibly add a detailed Character Info Page with stuff like "Slimes killed: 20" etc
	
	public MenuCharacter(Handler handler) {
		this.handler = handler;
		mouseInput = handler.getMouseInput();
		keyInput = handler.getKeyInput();
		player = handler.getPlayer();
	}

	
	public void render(Graphics g){
		
	}
	
	
	public void tick(double delta){
		player = handler.getPlayer();
		keyInput();
		drag();
		if(!mouseInput.getLeftClicks().isEmpty()){
			leftClick();
		}
		if(!mouseInput.getRightClicks().isEmpty()){
			rightClick();
		}
		hover();
		HUDManager.MENUTIMER += (System.currentTimeMillis() - HUDManager.MENULAST);
		HUDManager.MENULAST = System.currentTimeMillis();
	}
	
	private void keyInput(){
		
	}
	
	private void leftClick(){
		
	}
	
	private void rightClick(){
		
	}
	
	private void drag(){
		
	}
	
	private void hover(){
		
	}
}
