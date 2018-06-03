package virassan.main.states;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import virassan.entities.Entity;
import virassan.entities.creatures.Creature;
import virassan.entities.creatures.enemies.Enemy;
import virassan.entities.creatures.npcs.Merchant;
import virassan.entities.creatures.npcs.NPC;
import virassan.entities.creatures.player.Player;
import virassan.entities.creatures.utils.SkillTracker;
import virassan.gfx.hud.HUDManager;
import virassan.input.KeyInput;
import virassan.input.LinkedQueue;
import virassan.input.MouseInput;
import virassan.items.ItemManager;
import virassan.main.Handler;
import virassan.utils.Utils;
import virassan.world.maps.Map;

public class GameState {

	private Handler handler;
	private Player player;
	private MouseInput mouseInput;
	private KeyInput keyInput;
	private HUDManager hud;
	
	private boolean isDragged;
	private long attackTimer, attackLast, attackWait = 200;
	
	public GameState(Handler handler) {
		this.handler = handler;
		mouseInput = handler.getMouseInput();
		keyInput = handler.getKeyInput();
		player = handler.getPlayer();
		isDragged = false;
		hud = new HUDManager(handler);
	}

	public void tick(double delta){
		handler.getMap().tick(delta);
		hud.tick(delta);
		// Mouse Input
		drag();
		if(!mouseInput.getLeftClicks().isEmpty()){
			leftClick();
		}
		if(!mouseInput.getRightClicks().isEmpty()){
			rightClick();
		}
		hover();
		keyInput();
	}
	
	private void keyInput(){
		HUDManager.MENUTIMER += (System.currentTimeMillis() - HUDManager.MENULAST);
		HUDManager.MENULAST = System.currentTimeMillis();
		boolean pause = handler.getEntityManager().getPaused();
		// If NOT paused
		if(!pause){
			if(HUDManager.MENUTIMER > HUDManager.MENUWAIT){
				// Inventory
				if(keyInput.I){
					handler.getEntityManager().setPaused(true);
					handler.setState(States.MenuInventory);
					HUDManager.MENUTIMER = 0;
				} // Character Sheet
				else if(keyInput.C){
					HUDManager.MENUTIMER = 0;
					handler.getEntityManager().setPaused(true);
					handler.setState(States.MenuCharacter);
				} // Quest Log
				else if(keyInput.L){
					HUDManager.MENUTIMER = 0;
					handler.getEntityManager().setPaused(true);
					handler.setState(States.MenuQuest);
				} // Help Menu
				else if(keyInput.esc){
					HUDManager.MENUTIMER = 0;
					handler.getEntityManager().setPaused(true);
					handler.setState(States.MenuSettings);
				} // Skillbook
				else if(keyInput.K){
					HUDManager.MENUTIMER = 0;
					handler.getEntityManager().setPaused(true);
					handler.setState(States.MenuSkills);
				} // Level Up Menu
				else if(keyInput.T){
					HUDManager.MENUTIMER = 0;
					handler.getEntityManager().setPaused(true);
					handler.setState(States.MenuLevelUp);
				} // NPC Interaction/Dialog
				else if(keyInput.F){
					outer: {
						for(Entity e : handler.getMap().getEntityManager().getEntities()){
							if(e instanceof NPC){
								int temp = (int)(Math.sqrt(Math.pow(e.getX() - player.getX(), 2) + Math.pow(e.getY() - player.getY(),2)));
								if(temp <= ((NPC)e).getInteractDist()){
									HUDManager.MENUTIMER = 0;
									handler.getEntityManager().setPaused(true);
									Handler.NPCDIALOG.NPCInteract((NPC)e);
									handler.setState(States.NPCDialog);
									break outer;
								}
							}else if(e instanceof Merchant){
								HUDManager.MENUTIMER = 0;
								handler.getEntityManager().setPaused(true);
								handler.setState(States.NPCShop);
								Handler.NPCSHOP.merchantInteract((Merchant)e);
								break outer;
							}
						}
					}
				}else if(keyInput.space){
					HUDManager.MENUTIMER = 0;
					handler.getEntityManager().setPaused(!handler.getEntityManager().getPaused());
				}else if(keyInput.one || keyInput.two || keyInput.three || keyInput.four || keyInput.five){
					int index = 0;
					if(keyInput.two){
						index = 1;
					}else if(keyInput.three){
						index = 2;
					}else if(keyInput.four){
						index = 3;
					}else if(keyInput.five){
						index = 4;
					}
					player.setSkillActive(true);
					player.setActiveSkill(player.getSkillBar()[index]);
					HUDManager.MENUTIMER = 0;
				}
			}
		}else{ 
			if(HUDManager.MENUTIMER > HUDManager.MENUWAIT){
				if(keyInput.space){
					HUDManager.MENUTIMER = 0;
					handler.getEntityManager().setPaused(!handler.getEntityManager().getPaused());
				}
			}
		}
	}
	
	private void leftClick(){
		LinkedQueue clicks = mouseInput.getLeftClicks();
		if(player.getSkillActive()){
			if(clicks.element() != null){
				outer :{
					while(clicks.element() != null){
						Point head = clicks.poll().getObject();
						double x = head.getX();
						double y = head.getY();
						Class target = null;
						try{
							target = player.getActiveSkill().getTarget();
						}catch(NullPointerException e){
							String mesg = "Error Message: GameState_leftClick Active Skill is null" + System.getProperty("line.separator") +
									"Active Skill is: " + player.getActiveSkill() + System.getProperty("line.separator") +
									"Player Skills: " + player.getSkills();
							System.out.println(mesg);
							Utils.addErrorToLog(mesg);
							e.printStackTrace();
						}
						for(Entity e : handler.getEntityManager().getEntities()){
							if(e.findClass() == target){
								if(new Rectangle((int)x, (int)y, 1, 1).intersects(new Rectangle((int)(e.getX() - handler.getGameCamera().getxOffset()), (int)(e.getY() - handler.getGameCamera().getyOffset()), e.getWidth(), e.getHeight()))){
									player.skillAction(player.getActiveSkill().getTarget(), player.getActiveSkill(), e);
									break outer;
								}
							}
						}
					}
				}
				player.setSkillActive(false);
			}
		}else if(!player.getSkillActive()){
			outer :{
				while(clicks.element() != null){
					Point head = clicks.poll().getObject();
					double x = head.getX();
					double y = head.getY();
					for(Entity e : handler.getEntityManager().getEntities()){
						if(e instanceof NPC){
							if(new Rectangle((int)x, (int)y, 1, 1).intersects(new Rectangle((int)e.getX(), (int)e.getY(), e.getWidth(), e.getHeight()))){
								handler.getEntityManager().setPaused(true);
								Handler.NPCDIALOG.NPCInteract((NPC)e);
								System.out.println("Message: GameState_leftClick npcID: " + ((NPC)e).getNPCID());
								handler.setState(States.NPCDialog);
								break outer;
							}
						}else if(e instanceof Merchant){
							if(new Rectangle((int)x, (int)y, 1, 1).intersects(new Rectangle((int)(e.getX() - handler.getGameCamera().getxOffset()), (int)(e.getY() - handler.getGameCamera().getyOffset()), e.getWidth(), e.getHeight()))){
								handler.setState(States.NPCShop);
								Handler.NPCSHOP.merchantInteract((Merchant)e);
								break outer;
							}
						}else if(e instanceof Enemy){
							if(new Rectangle((int)x, (int)y, 1, 1).intersects(new Rectangle((int)(e.getX() - handler.getGameCamera().getxOffset()), (int)(e.getY() - handler.getGameCamera().getyOffset()), e.getWidth(), e.getHeight()))){
								attackTimer += System.currentTimeMillis() - attackLast;
								if(attackTimer > attackWait){
									try{
										player.attack(player.getStats().getMainH(), (Creature)e);
									}catch(NullPointerException n){
										if(player.getStats().getMainH() == null){
											player.getStats().addEventText("No Weapon Equiped");
										}
									}
									attackTimer = 0;
									attackLast = System.currentTimeMillis();
								}
								break outer;
							}
						}
					}
					for(SkillTracker s : player.getSkillBar()){
						if(s != null){
							if(new Rectangle((int)x, (int)y, 1, 1).intersects(s.getBounds())){
								player.setSkillActive(true);
								player.setActiveSkill(s);
								break outer;
							}
						}
					}
					if(hud.getExitButton().intersects(x, y, 1, 1)){
						handler.getGame().close();
					}
				}
			}
		}
	}
	
	private void rightClick(){
		// TODO : add right click actions!
		// Maybe used right click to interact with static objects
		// maybe use right click on merchant to skip any dialog and go straight to merchant screen
		LinkedQueue clicks = mouseInput.getRightClicks();
		if(handler.getEntityManager().getPaused()){
			
		}else{
			if(player.getSkillActive()){
				player.setSkillActive(false);
			}
		}
	}
	
	private void hover(){
		//TODO: add hover actions
		// show npc name when hover over their avatar
		// show enemy name, level, and health
	}
	
	private void drag(){
		//TODO: any possible drag actions?
		// maybe for some skills the mouse is dragged??
		if(mouseInput.isDragged()){
			isDragged = true;
			Rectangle startRect =  new Rectangle(mouseInput.getStartDrag().x, mouseInput.getStartDrag().y, 1, 1);
		}else{
			if(isDragged){
				Rectangle startRect, endRect;
				try{
					startRect =  new Rectangle(mouseInput.getStartDrag().x, mouseInput.getStartDrag().y, 1, 1);
				}catch(NullPointerException e){
					startRect = new Rectangle(0, 0, 0, 0);
				}try{
					endRect = new Rectangle(mouseInput.getEndDrag().x, mouseInput.getEndDrag().y, 1, 1);
				}catch(NullPointerException e){
					endRect = new Rectangle(0, 0, 0, 0);
				}
			}
			isDragged = false;
		}
	}
	
	/**
	 * Renders the World 
	 * @param g
	 */
	public void render(Graphics g){
		// Rendering of EntityManager is inside Map
		handler.getMap().render(g);
		hud.render(g);
	}
	
	
	// GETTERS AND SETTERS
	
	public ItemManager getItemManager(){
		return hud.getItemManager();
	}
	
	public HUDManager getHUD(){
		return hud;
	}
	
	public Player getPlayer(){
		return handler.getPlayer();
	}
	
	public Map getMap(){
		return handler.getMap();
	}
	
	public void setPlayer(Player p){
		player = p;
	}
	
}
