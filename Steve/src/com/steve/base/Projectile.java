package com.steve.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.helpers.CollisionHelper;

public class Projectile {
	private Sprite avatar;
	private float percentDamage;
	private Vector2 direction;
	private boolean snakeFriendly;
	private boolean dead;
	private float projectileTime;
	
	protected float speed;

	public Projectile(float x, float y, Vector2 atlasPosition, Vector2 atlasBounds,
			float percentDamage, boolean snakeFriendly, float dx, float dy) {
		this.percentDamage = (snakeFriendly) ? percentDamage : 
			(SteveDriver.snake.getSnakeTier() == 1) ? percentDamage : percentDamage - (percentDamage*SteveDriver.snake.getSnakeTier()/10);
		this.snakeFriendly = snakeFriendly;
		this.direction = new Vector2(dx, dy);
		dead = false;
		
		avatar = new Sprite(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x * SteveDriver.TEXTURE_WIDTH,
				(int)atlasPosition.y * SteveDriver.TEXTURE_LENGTH, (int)atlasBounds.x* SteveDriver.TEXTURE_WIDTH, (int)atlasBounds.y * SteveDriver.TEXTURE_LENGTH));
		avatar.setPosition(x, y);
		avatar.setRotation(CollisionHelper.angleFromDirectionVector(dx, dy));
	
		projectileTime = 100;
	}
	
	public void update() {
		if(this.projectileTime > 0)
			this.projectileTime--;
		else
			kill();
		move();
		checkCollisions();
	}
	
	public void draw() {
		avatar.draw(SteveDriver.batch);
	}
	
	private void move(){
		avatar.setPosition(avatar.getX() + direction.x * Gdx.graphics.getRawDeltaTime(), avatar.getY() + direction.y * Gdx.graphics.getRawDeltaTime());
	}
	
	private void checkCollisions() {
		TiledMapTileLayer layer = (TiledMapTileLayer)SteveDriver.field.map.getLayers().get(1);
		
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				
				if (cell != null && CollisionHelper.isCollide(new Rectangle(x * SteveDriver.TEXTURE_WIDTH, y * SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH), avatar.getBoundingRectangle())) {
					kill();
				}
			}
		}
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
	
	public void setDirection(float dx, float dy) {
		direction.x = dx * speed;
		direction.y = dy * speed;
	}
}
