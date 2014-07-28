package com.steve.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.helpers.CollisionHelper;
import com.steve.stages.Field;

public class Projectile {
	protected Sprite avatar;
	private float damage;
	protected float directionX;
	protected float directionY;
	private boolean snakeFriendly;
	private boolean dead;
	private float projectileTime;
	protected boolean ignoreCollisions;
	
	protected float speed;

	public Projectile(float x, float y, int atlasPositionX, int atlasPositionY, int atlasBoundsX, int atlasBoundsY,
			float damage, boolean snakeFriendly) {
		this.damage = (snakeFriendly) ? damage : 
			(SteveDriver.snake.getSnakeTier() == 1) ? damage : (damage/SteveDriver.snake.getSnakeArmor());
		this.snakeFriendly = snakeFriendly;
		dead = false;
		
		avatar = new Sprite(new TextureRegion(SteveDriver.assets.get("data/SpriteAtlasDouble.png", Texture.class), atlasPositionX * SteveDriver.TEXTURE_SIZE,
				atlasPositionY * SteveDriver.TEXTURE_SIZE, atlasBoundsX * SteveDriver.TEXTURE_SIZE, atlasBoundsY * SteveDriver.TEXTURE_SIZE));
		avatar.setPosition(x, y);
	
		projectileTime = 100;
	}
	
	public Projectile(float x, float y, int atlasPositionX, int atlasPositionY, int atlasBoundsX, int atlasBoundsY,
			float percentDamage, boolean snakeFriendly, float projectileTime) {
		this(x, y, atlasPositionX, atlasPositionY, atlasBoundsX, atlasBoundsY, percentDamage, snakeFriendly);
	
		this.projectileTime = projectileTime;
		ignoreCollisions = false;
	}
	
	public void CheckBulletTime() {
		if (SteveDriver.constants.get("bulletTime") != 0f) {
			projectileTime *= 3;
			speed /= 3;
		}
	}
	
	public void update() {
		if(this.projectileTime > 0f) {
			this.projectileTime-=1f;
		}
		else {
			kill();
			return;
		}
		
		if (getAlive()) {
			move();
			if(!ignoreCollisions)
				checkCollisions();
		}
	}
	
	public void draw() {
		if (SteveDriver.guiHelper.isOnScreen(avatar.getX(), avatar.getY(), avatar.getOriginX(), avatar.getOriginY())) {
			avatar.draw(SteveDriver.batch);
		}
	}
	
	private void move(){		
		avatar.setPosition(avatar.getX() + directionX * Gdx.graphics.getDeltaTime(),
				avatar.getY() + directionY * Gdx.graphics.getDeltaTime());
	}
	
	private void checkCollisions() {
		TiledMapTileLayer layer = Field.blockers;
		
		int topCornerX = (int)avatar.getX() / SteveDriver.TEXTURE_SIZE;
		int topCornerY = (int)avatar.getY() / SteveDriver.TEXTURE_SIZE;

		Cell cell = layer.getCell(topCornerX, topCornerY);
		
		if (cell != null) {
			kill();
			return;
		}
	}
	
	public Sprite getAvatar() {
		return avatar;
	}
	
	public boolean getFriendly() {
		return snakeFriendly;
	}
	
	public void kill() {
		if (!dead) {
			dead = true;
			SteveDriver.field.projectilesToRemove.add(this);
		}
	}
	
	public boolean getAlive() {
		return !dead;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public void setDirection(float dx, float dy) {
		directionX = MathUtils.clamp(dx, -1, 1) * speed;
		directionY = MathUtils.clamp(dy, -1, 1) * speed;
		
		avatar.setRotation(CollisionHelper.angleFromDirectionVector(dx, dy));
	}
}
