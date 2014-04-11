package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Tank extends Enemy {
	private float shootTime;
	private float shootTimer;
	private Sprite face;

	public Tank(Vector2 position) {
		super(position, new Vector2(15, 1), new Vector2(2, 2), 0.5f, 0.5f, 3);
		shootTime = 0.5f;
		
		face = new Sprite(SteveDriver.atlas, 21 * SteveDriver.TEXTURE_WIDTH, 1 * SteveDriver.TEXTURE_LENGTH,
				2 * SteveDriver.TEXTURE_WIDTH, 2 * SteveDriver.TEXTURE_LENGTH);
		face.setPosition(avatar.getX(), avatar.getY());
	}
	
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		if (shootTimer <= shootTime / 2) {
			face.setPosition(avatar.getX(), avatar.getY());
			face.setRotation(avatar.getRotation());
			face.draw(batch);
		}
	}
	
	protected void decideMove() {
		super.moveRandomly();
	}

	protected void update() {
		if (shootTimer >= shootTime) {
			//TODO: We want to get rid of new Vector2's when we don't need them.
			SteveDriver.field.projectiles.add(new Acorn(new Vector2(avatar.getX() + SteveDriver.TEXTURE_WIDTH / 2, avatar.getY() + SteveDriver.TEXTURE_LENGTH / 2),
					new Vector2(MathUtils.cosDeg(avatar.getRotation()), MathUtils.sinDeg(avatar.getRotation())).scl(100)));
			
			shootTimer = 0;
		}
		else {
			shootTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
}