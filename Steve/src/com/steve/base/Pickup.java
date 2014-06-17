package com.steve.base;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.steve.Snake;
import com.steve.SteveDriver;

public class Pickup extends Sprite {
	private boolean active;
	protected int points;
	
	public Pickup(float x, float y, int atlasX, int atlasY, int points) {
		super(new TextureRegion(SteveDriver.atlas, atlasX, atlasY, 16, 16));
		this.setPosition(x * 16, y * 16);
		active = true;
		this.points = points;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void consume(Snake snake) {
		active = false;
		snake.addMoney(points); //TODO: Temp
	}
	
	public Rectangle getRectangle() {
		return this.getBoundingRectangle();
	}
}