package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Pickup extends Sprite {
	private boolean active;
	
	public Pickup(float x, float y, int atlasX, int atlasY) {
		super(new TextureRegion(SteveDriver.atlas, atlasX, atlasY, 16, 16));
		
		this.setPosition(x * 16, y * 16);
		active = true;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void consume(Snake snake) {
		active = false;
	}
}