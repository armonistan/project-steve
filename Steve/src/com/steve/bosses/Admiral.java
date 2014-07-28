package com.steve.bosses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.projectiles.Acorn;

public class Admiral extends Enemy {
	private Sprite face;
	private Carrier carrier;
	float xOffSet;
	float yOffSet;

	public Admiral(float x, float y, float xOff, float yOff, Carrier c) {
		super(x + xOff, y + yOff, 37, 9, 2, 3, 0.5f, 0.5f, 2, 0, 75);
		shootTime = 0.5f;
		sightDistance = 600;
		
		face = new Sprite(SteveDriver.assets.get("data/SpriteAtlasDouble.png", Texture.class), 21 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE,
				2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE);
		
		shotCap = 10;
		hasShotCounter = 11;
		
		moneyAmount = 0;
		this.avatar.setRotation(SteveDriver.RIGHT);
		carrier = c;
		xOffSet = xOff;
		yOffSet = yOff;
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
		avatar.setPosition(carrier.getX() + (carrier.getRotation() == SteveDriver.UP ? xOffSet *
				SteveDriver.TEXTURE_SIZE : carrier.getWidth() - xOffSet * SteveDriver.TEXTURE_SIZE),
				carrier.getY() + yOffSet * SteveDriver.TEXTURE_SIZE);
		avatar.setRotation(carrier.getRotation() == SteveDriver.UP ? SteveDriver.RIGHT : SteveDriver.LEFT);
		
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