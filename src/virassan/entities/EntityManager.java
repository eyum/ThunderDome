package virassan.entities;

import java.awt.Graphics;

import java.util.ArrayList;
import java.util.Comparator;

import virassan.entities.creatures.npcs.Merchant;
import virassan.entities.creatures.player.Player;
import virassan.entities.statics.StaticEntity;
import virassan.main.Handler;
import virassan.utils.Utils;

public class EntityManager {

	private Handler handler;
	private Player player;
	private ArrayList<Entity> entities;
	private ArrayList<StaticEntity> statics;
	private boolean isPaused;
	
	private Comparator<Entity> renderSorter = new Comparator<Entity>(){
		@Override
		public int compare(Entity a, Entity b) {
			if(a.getY() + a.getHeight() < b.getY() + b.getHeight())
				return -1;
			return 1;
		}
		
	};
	
	public EntityManager(Handler handler){
		this.handler = handler;
		entities = new ArrayList<Entity>();
		statics = new ArrayList<StaticEntity>();
		isPaused = false;
	}

	public void tick(double delta){
		for(int i = 0; i < entities.size(); i++){
			if(!entities.get(i).isDead()){
				if(!(entities.get(i) instanceof Player)){
					if(!isPaused){
						entities.get(i).tick(delta);
					}else{
						if(entities.get(i) instanceof Merchant){
							entities.get(i).tick(delta);
						}
					}
				}else{
					entities.get(i).tick(delta);
				}
			}else{
				entities.remove(entities.get(i));
				i--;
			}
		}
		entities.sort(renderSorter);
		for(StaticEntity e : statics){
			if(!isPaused){
				e.tick(delta);
			}
		}
	}
	
	public void render(Graphics g){
		int xStart = (int)handler.getGameCamera().getxOffset() - 60;
		int xEnd = (int)(handler.getGameCamera().getWidth() + handler.getGameCamera().getxOffset()) + 60;
		int yStart = (int)handler.getGameCamera().getyOffset() - 60;
		int yEnd = (int)(handler.getGameCamera().getyOffset() + handler.getHeight()) + 60;
		for(StaticEntity e : statics){
			if(e.getX() >= xStart-e.getWidth() && e.getX() <= xEnd && e.getY() >= yStart-e.getHeight() && e.getY() <= yEnd){
				e.render(g);
			}
		}
		for(Entity e : entities){
			try{
				if(e.getX() >= xStart-e.getWidth() && e.getX() <= xEnd && e.getY() >= yStart-e.getHeight() && e.getY() <= yEnd){
					e.render(g);
				}
			}catch(NullPointerException k){
				String mesg = "Error Message: EntityManager_render Entity is: " + e;
				System.out.println(mesg);
				Utils.addErrorToLog(mesg);
				k.printStackTrace();
			}
		}
	}

	public void addEntity(Entity e){
		entities.add(e);
	}
	
	public void addStatic(StaticEntity e){
		statics.add(e);
	}
	
	//Getters and Setters
	
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		for(Entity e : entities){
			if(e instanceof Player){
				e.isDead(true);
			}
		}
		this.player = player;
		addEntity(player);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}
	
	public void setPaused(boolean b){
		if(!b){
			for(Entity i : entities){
				i.unPause();
			}
		}
		handler.getMouseInput().getLeftClicks().clear();
		handler.getMouseInput().getRightClicks().clear();
		isPaused = b;
	}
	
	public boolean getPaused(){
		return isPaused;
	}
}
