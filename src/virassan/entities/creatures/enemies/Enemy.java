package virassan.entities.creatures.enemies;

import java.awt.Color;
import java.util.Random;

import virassan.entities.Entity;
import virassan.entities.creatures.Attack;
import virassan.entities.creatures.Creature;
import virassan.entities.creatures.Player;
import virassan.gfx.Animation;
import virassan.gfx.hud.BouncyText;
import virassan.items.Drop;
import virassan.main.Handler;
import virassan.main.ID;
import virassan.main.Vector2F;

public abstract class Enemy extends Creature{

	public static final int SOLDIER_WIDTH = 32, SOLDIER_HEIGHT = 32;
	
	
	protected EnemyType type;
	protected int exp;
	protected Drop[] drops;
	protected long timer;
	private long lastTime;
	private long attackTimer, lastAttack;
	private int count, ranDir;

	
	//Animation Shit
	protected Animation[] walking;
	protected Animation animation;
	protected Animation walkLeft, walkRight, walkUp, walkDown;
	protected Attack defaultAttack;
	protected int aggroDistance;
	
	public Enemy(Handler handler, float x, float y, int level, ID id, EnemyType type) {
		super(handler, x, y, type.getWidth(), type.getHeight(), level, id);
		this.type = type;
		this.npc = true;
		exp = 55 * level;
		drops = new Drop[10];
		count = 0;
		ranDir = new Random().nextInt(7);
		timer = 0;
		lastTime = System.currentTimeMillis();
	}
	
	/*
	public void tick(){
		
	}
	
	public void render(Graphics g){
		
	}
	*/
	public void move(){
		isMoving = false;
		timer += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		velX = 0;
		velY = 0;
		int playerDist = (int)Math.sqrt((double)(Math.pow(x - handler.getPlayer().getX(), 2) + Math.pow(y - handler.getPlayer().getY(), 2)));
		attackTimer += System.currentTimeMillis()-lastAttack;
		lastAttack = System.currentTimeMillis();
		if(playerDist > aggroDistance){
			if(timer > 1200){
				ranDir = new Random().nextInt(6);
				timer = 0;
				if(count <= 0){
					count = 5;
				}
			}
			if(timer > 400){
				if(ranDir == 0){ // Up
					velY = -speed;
				}else if(ranDir == 1){// Down
					velY = speed;
				}else if(ranDir == 2){// Right
					velX = speed;
				}else if(ranDir == 3){ // Left
					velX = -speed;
				}
				if(ranDir < 4){
					animation = walking[direction];
					animation.start();
					count--;
					isMoving = true;
				}
			}
		}else if(playerDist <= aggroDistance){
			if(timer > 100){
				int xMultiplier = 0;
				int yMultiplier = 0;
				//int xDist = (int)(handler.getPlayer().getX() - x);
				//int yDist = (int)(handler.getPlayer().getY() - y);
				if(handler.getPlayer().getX() > x && handler.getPlayer().getX() + handler.getPlayer().getBounds().x > x + bounds.x + bounds.width + 2){
					direction = 2;
					xMultiplier = 1;
				}else if(handler.getPlayer().getX() < x && handler.getPlayer().getX() + handler.getPlayer().getBounds().x + handler.getPlayer().getBounds().width < x + bounds.x + 2){
					xMultiplier = -1;
					direction = 3;
				}
				if(handler.getPlayer().getY() > y && handler.getPlayer().getY() + handler.getPlayer().getBounds().y > y + bounds.y + bounds.height + 2){
					direction = 1;
					yMultiplier = 1;
				}else if(handler.getPlayer().getY() < y && handler.getPlayer().getY() + handler.getPlayer().getBounds().y + handler.getPlayer().getBounds().height < y + bounds.y + 2){
					yMultiplier = -1;
					direction = 0;
				}
				if(playerDist > speed){
					velY = speed * yMultiplier;
					velX = speed * xMultiplier;
				}else{
					velY = playerDist * yMultiplier;
					velX = playerDist * xMultiplier;
				}
				animation = walking[direction];
				animation.start();
				isMoving = true;
				timer = 0;
			}
			if(attackTimer >= defaultAttack.getSpeed()){
				if(playerDist <= defaultAttack.getWidth() + 15){
					if(!attack(defaultAttack)){
						stats.getList().add(new BouncyText(handler, "Miss!", Color.WHITE, (int)x, (int)y));
					}
					attackTimer =0;
				}
			}
		}
		super.move();
	}
	
	
	public boolean attack(Attack attack){
		if(attackBounds != null){
			attackBounds.width = attack.getWidth();
			attackBounds.height = attack.getHeight();
			for(Entity e : handler.getWorld().getMap().getEntityManager().getEntities()){
				if(e.getId() == ID.Player){
					Player entity = (Player)e;
					if(collisionAttack(entity)){
						entity.getStats().damage(attack.getDamage() + (float)(attack.getDamage() * stats.getDmgMod()));
						entity.setVelX(0);
						entity.setVelY(0);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	
	public void droppedItems(){
		for(int i = 0; i < drops.length; i++){
			if(drops[i].isDropped()){
				int xran = new Random().nextInt(32);
				int yran = new Random().nextInt(32);
				drops[i].setPos(new Vector2F(x + xran, y + yran));
				handler.getItemManager().addItem(drops[i]);
			}
		}
	}
	
	public void gainExp(){
		if(isDead){
			handler.getPlayer().getStats().addExperience(exp);
		}
	}

}