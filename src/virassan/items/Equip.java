package virassan.items;

import java.awt.image.BufferedImage;

import virassan.gfx.Assets;

public enum Equip {

	MAINHAND("Main-Hand", Assets.defaultMain),
	OFFHAND("Off-Hand", Assets.defaultOff),
	HEAD("Head", Assets.defaultHead),
	CHEST("Chest", Assets.defaultChest),
	LEGS("Legs", Assets.defaultLegs),
	FEET("Feet", Assets.defaultFeet),
	SHOULDERS("Shoulders", Assets.defaultShoulders),
	HANDS("Hands", Assets.defaultHands),
	ACCESS1("Accessory 1", Assets.defaultAcc1),
	ACCESS2("Accessory 2", Assets.defaultAcc2);
	
	private String name;
	private BufferedImage defaultImage;
	
	private Equip(String name, BufferedImage defaultImage){
		this.name = name;
		this.defaultImage = defaultImage;
	}
	
	public String toString(){
		return name;
	}
	
	public BufferedImage getImage(){
		return defaultImage;
	}
	
	public String getName(){
		return name;
	}
}
