package virassan.gfx;

import java.awt.image.BufferedImage;

import virassan.entities.statics.Statics;
import virassan.world.maps.Tile;
import virassan.world.maps.Tiles;

/**
 * Loads, divvies up SpriteSheets, and saves BufferedImages to variables
 * @author Virassan
 *
 */

public class Assets {
	
	public static final int IMAGE_WIDTH = 32, IMAGE_HEIGHT = 32;
	public static final int ITEM_WIDTH = 16, ITEM_HEIGHT = 16;
	
	private BufferedImage front_1, front_Up, front_3, left_1, left_Up, left_3, right_1, right_Up, right_3, back_1, back_Up, back_3;
	public static BufferedImage lava, water, idk, grass, flowers_1, flowers_2, stone_rock, animeDudeStanding, button_blue, button_green, button_pink, button_red;
	
	
	// Items
	public static BufferedImage slime, apple, bandaid, toast;
	
	private BufferedImage[] walkingLeft, 
			walkingRight, 
			walkingUp, 
			walkingDown, 
			standing;
	
	/**
	 * Initializes publically accessed images
	 */
	public static void init(){
		SpriteSheet testing = new SpriteSheet(ImageLoader.loadImage("/textures/tiles/TextureSpriteSheet.png"));
		SpriteSheet animeDude = new SpriteSheet(ImageLoader.loadImage("/textures/entities/static/testing_anime_dude.png"));
		SpriteSheet items = new SpriteSheet(ImageLoader.loadImage("/textures/items/TestingItemsSpriteSheet.png"));
		button_blue = ImageLoader.loadImage("/textures/buttons/Button_blue.png");		
		button_green = ImageLoader.loadImage("/textures/buttons/Button_green.png");
		button_pink = ImageLoader.loadImage("/textures/buttons/Button_pink.png");
		button_red = ImageLoader.loadImage("/textures/buttons/Button_red.png");
		
		
		animeDudeStanding = animeDude.sprite(32, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		
		//TODO: Load Assets from new texture SpriteSheet
		//lava = testing.sprite(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		grass = testing.sprite(0, 0, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
		water = testing.sprite(Tile.TILE_WIDTH, 0, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
		
		
		flowers_1 = testing.sprite(Tile.TILE_WIDTH * 3, 0, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
		flowers_2 = testing.sprite(Tile.TILE_WIDTH * 4, 0, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
		stone_rock = testing.sprite(Tile.TILE_WIDTH * 2, 0, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
	
		// TILES BRO
		Tiles.init();
		Statics.init();
		
		// ITEMS YO
		apple = items.sprite(0, 0, ITEM_WIDTH, ITEM_HEIGHT);
		slime = items.sprite(ITEM_WIDTH, 0, ITEM_WIDTH, ITEM_HEIGHT);
		bandaid = items.sprite(ITEM_WIDTH * 2, 0, ITEM_WIDTH, ITEM_HEIGHT);
		toast = items.sprite(ITEM_WIDTH * 3, 0, ITEM_WIDTH, ITEM_HEIGHT);
	}
	
	/**
	 * Constructs a new Asset - used for walking Animation
	 * @param filepath the walking Animation SpriteSheet file
	 */
	public Assets(String filepath){
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage(filepath));
		
		
		front_1 = sheet.sprite(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		front_Up = sheet.sprite(IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		front_3 = sheet.sprite(IMAGE_WIDTH*2, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		left_1 = sheet.sprite(0, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
		left_Up = sheet.sprite(IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
		left_3 = sheet.sprite(IMAGE_WIDTH*2, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
		right_1 = sheet.sprite(0, IMAGE_HEIGHT*2, IMAGE_WIDTH, IMAGE_HEIGHT);
		right_Up = sheet.sprite(IMAGE_WIDTH, IMAGE_HEIGHT*2, IMAGE_WIDTH, IMAGE_HEIGHT);
		right_3 = sheet.sprite(IMAGE_WIDTH*2, IMAGE_HEIGHT*2, IMAGE_WIDTH, IMAGE_HEIGHT);
		back_1 = sheet.sprite(0, IMAGE_HEIGHT*3, IMAGE_WIDTH, IMAGE_HEIGHT);
		back_Up = sheet.sprite(IMAGE_WIDTH, IMAGE_HEIGHT*3, IMAGE_WIDTH, IMAGE_HEIGHT);
		back_3 = sheet.sprite(IMAGE_WIDTH*2, IMAGE_HEIGHT*3, IMAGE_WIDTH, IMAGE_HEIGHT);
		
		walkingLeft = new BufferedImage[3];
		walkingRight = new BufferedImage[3];
		walkingUp = new BufferedImage[3];
		walkingDown = new BufferedImage[3];
		standing = new BufferedImage[1];
		
		walkingLeft[0] = left_1;
		walkingLeft[1] = left_Up;
		walkingLeft[2] = left_3;
		walkingRight[0] = right_1;
		walkingRight[1] = right_Up;
		walkingRight[2] = right_3;
		walkingUp[0] = back_1;
		walkingUp[1] = back_Up;
		walkingUp[2] = back_3;
		walkingDown[0] = front_1;
		walkingDown[1] = front_Up;
		walkingDown[2] = front_3;
		standing[0] = front_1;
	}
	
	public Assets(String filepath, int numberOfFrames, int imageWidth, int imageHeight){
		walkingLeft = new BufferedImage[numberOfFrames];
		walkingRight = new BufferedImage[numberOfFrames];
		walkingUp = new BufferedImage[numberOfFrames];
		walkingDown = new BufferedImage[numberOfFrames];
		
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage(filepath));
		for(int k = 0; k < 4; k++){
			for(int i = 0; i < numberOfFrames; i++){
				if(k == 0){
					walkingUp[i] = sheet.sprite(i * imageWidth, k * imageHeight, imageWidth, imageHeight);
				}else if(k == 1){
					walkingDown[i] = sheet.sprite(i * imageWidth, k * imageHeight, imageWidth, imageHeight);
				}else if(k == 2){
					walkingRight[i] = sheet.sprite(i * imageWidth, k * imageHeight, imageWidth, imageHeight);
				}else if(k == 3){
					walkingLeft[i] = sheet.sprite(i * imageWidth, k * imageHeight, imageWidth, imageHeight);
				}
			}
		}
		/*
		for(int j = 0; j < 4; j++){
			if(j == 0){
				walkingUp[0] = sheet.sprite(j * imageWidth, 4 * imageHeight, imageWidth, imageHeight);
			}else if(j == 1){
				walkingDown[0] = sheet.sprite(j * imageWidth, 4 * imageHeight, imageWidth, imageHeight);
			}else if(j == 2){
				walkingRight[0] = sheet.sprite(j * imageWidth, 4 * imageHeight, imageWidth, imageHeight);
			}else if(j == 3){
				walkingLeft[0] = sheet.sprite(j * imageWidth, 4 * imageHeight, imageWidth, imageHeight);
			}
		}
		*/
	}


// GETTERS AND SETTERS
	
	public BufferedImage getFront_1() {
		return front_1;
	}


	public void setFront_1(BufferedImage front_1) {
		this.front_1 = front_1;
	}


	public BufferedImage getFront_Up() {
		return front_Up;
	}


	public void setFront_Up(BufferedImage front_Up) {
		this.front_Up = front_Up;
	}


	public BufferedImage getFront_3() {
		return front_3;
	}


	public void setFront_3(BufferedImage front_3) {
		this.front_3 = front_3;
	}


	public BufferedImage getLeft_1() {
		return left_1;
	}


	public void setLeft_1(BufferedImage left_1) {
		this.left_1 = left_1;
	}


	public BufferedImage getLeft_Up() {
		return left_Up;
	}


	public void setLeft_Up(BufferedImage left_Up) {
		this.left_Up = left_Up;
	}


	public BufferedImage getLeft_3() {
		return left_3;
	}


	public void setLeft_3(BufferedImage left_3) {
		this.left_3 = left_3;
	}


	public BufferedImage getRight_1() {
		return right_1;
	}


	public void setRight_1(BufferedImage right_1) {
		this.right_1 = right_1;
	}


	public BufferedImage getRight_Up() {
		return right_Up;
	}


	public void setRight_Up(BufferedImage right_Up) {
		this.right_Up = right_Up;
	}


	public BufferedImage getRight_3() {
		return right_3;
	}


	public void setRight_3(BufferedImage right_3) {
		this.right_3 = right_3;
	}


	public BufferedImage getBack_1() {
		return back_1;
	}


	public void setBack_1(BufferedImage back_1) {
		this.back_1 = back_1;
	}


	public BufferedImage getBack_Up() {
		return back_Up;
	}


	public void setBack_Up(BufferedImage back_Up) {
		this.back_Up = back_Up;
	}


	public BufferedImage getBack_3() {
		return back_3;
	}


	public void setBack_3(BufferedImage back_3) {
		this.back_3 = back_3;
	}


	public BufferedImage[] getWalkingLeft() {
		return walkingLeft;
	}


	public void setWalkingLeft(BufferedImage[] walkingLeft) {
		this.walkingLeft = walkingLeft;
	}


	public BufferedImage[] getWalkingRight() {
		return walkingRight;
	}


	public void setWalkingRight(BufferedImage[] walkingRight) {
		this.walkingRight = walkingRight;
	}


	public BufferedImage[] getWalkingUp() {
		return walkingUp;
	}


	public void setWalkingUp(BufferedImage[] walkingUp) {
		this.walkingUp = walkingUp;
	}


	public BufferedImage[] getWalkingDown() {
		return walkingDown;
	}


	public void setWalkingDown(BufferedImage[] walkingDown) {
		this.walkingDown = walkingDown;
	}


	public BufferedImage[] getStanding() {
		return standing;
	}


	public void setStanding(BufferedImage[] standing) {
		this.standing = standing;
	}
	
}
