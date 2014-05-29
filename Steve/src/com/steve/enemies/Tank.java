package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.projectiles.Acorn;

public class Tank extends Enemy {
	
	private Sprite face;

	public Tank(float x, float y) {
		super(x, y, new Vector2(15, 1), new Vector2(2, 2), 0.5f, 0.5f, 3, 75);
		shootTime = 0.5f;
		sightDistance = 600;
		
		face = new Sprite(SteveDriver.atlas, 21 * SteveDriver.TEXTURE_WIDTH, 1 * SteveDriver.TEXTURE_LENGTH,
				2 * SteveDriver.TEXTURE_WIDTH, 2 * SteveDriver.TEXTURE_LENGTH);
		
		shotCap = 10;
		hasShotCounter = 11;
		
		moneyAmount = 10;
	}
	
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
		super.shoot(new Acorn(avatar.getX() + SteveDriver.TEXTURE_WIDTH / 2, avatar.getY() + SteveDriver.TEXTURE_LENGTH / 2, 0, 0));
		super.update();
	}
	
	protected Vector2 decideMove() {
		return super.randomMove();
	}
}