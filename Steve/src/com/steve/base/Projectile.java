package com.steve.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.helpers.CollisionHelper;
import java.math.*;

public class Projectile {
	private Sprite avatar;
	private float percentDamage;
	protected float directionX;
	protected float directionY;
	private boolean snakeFriendly;
	private boolean dead;
	private float projectileTime;
	
	protected float speed;

	public Projectile(float x, float y, float atlasPositionX, float atlasPositionY, float atlasBoundsX, float atlasBoundsY,
			float percentDamage, boolean snakeFriendly, float dx, float dy) {
		this.percentDamage = (snakeFriendly) ? percentDamage : 
			(SteveDriver.snake.getSnakeTier() == 1) ? percentDamage : (percentDamage/SteveDriver.snake.getSnakeArmor());
		this.snakeFriendly = snakeFriendly;
		this.directionX = dx;
		this.directionY = dy;
		dead = false;
		
		avatar = new Sprite(new TextureRegion(SteveDriver.atlas, atlasPositionX * SteveDriver.TEXTURE_WIDTH,
				atlasPositionY * SteveDriver.TEXTURE_LENGTH, atlasBoundsX * SteveDriver.TEXTURE_WIDTH, atlasBoundsY * SteveDriver.TEXTURE_LENGTH));
		avatar.setPosition(x, y);
		avatar.setRotation(CollisionHelper.angleFromDirectionVector(dx, dy));
	
		projectileTime = 100;
	}
	
	public void update() {
		if(this.projectileTime > 0) {
			this.projectileTime--;
		}
		else {
			kill();
			return;
		}
		
		if (getAlive()) {
			move();
			checkCollisions();
		}
	}
	
	public void draw() {
		avatar.draw(SteveDriver.batch);
		
		if (!getAlive()) {
			//System.out.println("Drawn when dead.");
		}
	}
	
	private void move(){		
		avatar.setPosition(avatar.getX() + directionX * Gdx.graphics.getDeltaTime(),
				avatar.getY() + directionY * Gdx.graphics.getDeltaTime());
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
		directionX = MathUtils.clamp(dx, 0, 1) * speed;
		directionY = MathUtils.clamp(dy, 0, 1) * speed;
	}
}
