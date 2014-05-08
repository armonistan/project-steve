package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
	private Sprite avatar;
	private float percentDamage;
	private Vector2 direction;
	private boolean snakeFriendly;
	private boolean dead;
	private float projectileTime;

	public Projectile(float x, float y, Vector2 atlasPosition, Vector2 atlasBounds,
			float percentDamage, boolean snakeFriendly, float dx, float dy) {
		this.percentDamage = percentDamage;
		this.snakeFriendly = snakeFriendly;
		this.direction = new Vector2(dx, dy);
		dead = false;
		
		avatar = new Sprite(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x * SteveDriver.TEXTURE_WIDTH,
				(int)atlasPosition.y * SteveDriver.TEXTURE_LENGTH, (int)atlasBounds.x* SteveDriver.TEXTURE_WIDTH, (int)atlasBounds.y * SteveDriver.TEXTURE_LENGTH));
		avatar.setPosition(x, y);
		avatar.setRotation(CollisionHelper.angleFromDirectionVector(dx, dy));
	
		projectileTime = 100;
	}
	
	public void render(SpriteBatch batch) {		
		avatar.draw(batch);
		
		if(this.projectileTime > 0)
			this.projectileTime--;
		else
			kill();
		
		avatar.setPosition(avatar.getX() + direction.x * Gdx.graphics.getRawDeltaTime(), avatar.getY() + direction.y * Gdx.graphics.getRawDeltaTime());
	}
	
	public Sprite getAvatar() {
		return avatar;
	}
	
	public boolean getFriendly() {
		return snakeFriendly;
	}
	
	public void kill() {
		dead = true;
		SteveDriver.field.projectilesToRemove.add(this);
	}
	
	public boolean getAlive() {
		return !dead;
	}
	
	public float getPercentDamage() {
		return percentDamage;
	}
}
