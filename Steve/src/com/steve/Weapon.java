package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon extends Sprite{
	public Weapon(float x, float y, int atlasX, int atlasY) {
	super(new TextureRegion(SteveDriver.atlas, atlasX, atlasY, 16, 16));
		
		this.setPosition(x, y);
	}
	
	public void update(float x, float y){
		this.setPosition(x, y);
		this.weaponBehavoir();
	}
	
	public void weaponBehavoir(){
		//do nothing
		this.rotate(1);
		//above is temp
	}
}
