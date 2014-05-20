package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Tank extends Enemy {
	
	private Sprite face;

	public Tank(float x, float y) {
		super(x, y, new Vector2(15, 1), new Vector2(2, 2), 0.5f, 0.5f, 3, 75);
		shootTime = 0.5f;
		sightDistance = 600;
		
		face = new Sprite(SteveDriver.atlas, 21 * SteveDriver.TEXTURE_WIDTH, 1 * SteveDriver.TEXTURE_LENGTH,
				2 * SteveDriver.TEXTURE_WIDTH, 2 * SteveDriver.TEXTURE_LENGTH);
		face.setPosition(avatar.getX(), avatar.getY());
		
		shotCap = 10;
		hasShotCounter = 11;
		
		moneyAmount = 10;
	}
	
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		if (hasShotCounter < shotCap) {
			face.setPosition(avatar.getX(), avatar.getY());
			face.setRotation(avatar.getRotation());
			face.draw(batch);
			hasShotCounter++;
		}
	}
	
	@Override
	protected void update(){
		super.shoot();
		super.update();
	}
	
	protected Vector2 decideMove() {
		return super.randomMove();
	}
}