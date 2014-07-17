package com.steve.bosses;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.projectiles.Acorn;

public class Admiral extends Enemy {
	
	private Sprite face;

	public Admiral(float x, float y) {
		super(x, y, 37, 9, 2, 3, 0.5f, 0.5f, 2, 0, 75);
		shootTime = 0.5f;
		sightDistance = 600;
		
		face = new Sprite(SteveDriver.atlas, 21 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE,
				2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE);
		
		shotCap = 10;
		hasShotCounter = 11;
		
		moneyAmount = 0;
		this.avatar.setRotation(SteveDriver.RIGHT);
	}
	
	@Override
	public void draw() {
		super.draw();
		
		if (hasShotCounter < shotCap) {
			face.setPosition(avatar.getX(), avatar.getY());
			face.setRotation(avatar.getRotation());
			face.draw(SteveDriver.batch);
			hasShotCounter++;
		}
	}
	
	@Override
	public void update(){
		super.decideShoot();
		super.update();
	}
	
	@Override
	public void shoot(float dx, float dy) {
	}
	
	@Override
	protected void move(){
		
	}
}