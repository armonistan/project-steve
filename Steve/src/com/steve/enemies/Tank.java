package com.steve.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.projectiles.Acorn;

public class Tank extends Enemy {
	
	private Sprite face;

	public Tank(float x, float y) {
		super(x, y, 15, 1, 2, 2, 0.5f, 0.5f, 3, 0, 75);
		shootTime = 0.5f;
		sightDistance = 600;
		
		face = new Sprite(SteveDriver.atlas, 21 * SteveDriver.TEXTURE_WIDTH, 1 * SteveDriver.TEXTURE_LENGTH,
				2 * SteveDriver.TEXTURE_WIDTH, 2 * SteveDriver.TEXTURE_LENGTH);
		
		shotCap = 10;
		hasShotCounter = 11;
		
		moneyAmount = 10;
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
		super.addProjectile(new Acorn(avatar.getX() + SteveDriver.TEXTURE_WIDTH / 2, avatar.getY() + SteveDriver.TEXTURE_LENGTH / 2), dx, dy);
	}
	
	@Override
	protected Vector2 decideMove() {
		return super.randomMove();
	}
}