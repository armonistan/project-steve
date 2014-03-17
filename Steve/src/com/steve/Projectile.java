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

	public Projectile(Vector2 position, Vector2 atlasPosition, Vector2 atlasBounds, float percentDamage, boolean snakeFriendly, Vector2 direction) {
		this.percentDamage = percentDamage;
		this.snakeFriendly = snakeFriendly;
		this.direction = direction;
		dead = false;
		
		avatar = new Sprite(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x * SteveDriver.TEXTURE_WIDTH,
				(int)atlasPosition.y * SteveDriver.TEXTURE_LENGTH, (int)atlasBounds.x* SteveDriver.TEXTURE_WIDTH, (int)atlasBounds.y * SteveDriver.TEXTURE_LENGTH));
		avatar.setPosition(position.x, position.y);
		avatar.setRotation(CollisionHelper.angleFromDirectionVector(direction));
	}
	
	public void render(SpriteBatch batch) {		
		avatar.draw(batch);
		
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
