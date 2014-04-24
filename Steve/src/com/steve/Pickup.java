package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class PickUp extends Sprite {
	private boolean active;
	
	public PickUp(float x, float y, int atlasX, int atlasY) {
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
	
	public Rectangle getRectangle(){
		return this.getBoundingRectangle();
	}
}