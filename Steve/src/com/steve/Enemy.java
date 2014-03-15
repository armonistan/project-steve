package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
	public Sprite avatar;
	protected Vector2 mapPosition;
	
	protected float moveTimer;
	protected float moveTime;
	
	protected float animateTimer;
	protected float animateTime;
	protected int currentFrame;
	protected int numberFrames;
	protected Vector2 atlasPosition;
	protected Vector2 atlasBounds;
	
	public Enemy(Vector2 position, Vector2 atlasPosition, Vector2 atlasBounds, float moveTime, float animateTime, int numberFrames) {
		mapPosition = position;
		this.moveTime = moveTime;
		this.animateTime = animateTime;
		this.numberFrames = numberFrames;
		this.atlasPosition = atlasPosition;
		this.atlasBounds = atlasBounds;
		
		avatar = new Sprite(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x * SteveDriver.TEXTURE_WIDTH, (int)atlasPosition.y * SteveDriver.TEXTURE_LENGTH, (int)atlasBounds.x* SteveDriver.TEXTURE_WIDTH, (int)atlasBounds.y * SteveDriver.TEXTURE_LENGTH));
		updateAvatar();
		avatar.setPosition(mapPosition.x * SteveDriver.TEXTURE_WIDTH, mapPosition.y * SteveDriver.TEXTURE_LENGTH);
	}
	
	public void render(SpriteBatch batch) {
		if (moveTimer >= moveTime) {
			decideMove();
			
			moveTimer = 0;
		}
		else {
			moveTimer += Gdx.graphics.getRawDeltaTime();
		}
		
		if (animateTimer >= animateTime) {
			currentFrame = (currentFrame + 1) % numberFrames;
			updateAvatar();
			
			animateTimer = 0;
		}
		else {
			animateTimer += Gdx.graphics.getRawDeltaTime();
		}
		
		update();
		
		avatar.draw(batch);
	}
	
	protected void update() {
		//TODO: Define basic update behavior
		//Should override
	}
	
	protected void decideMove() {
		//Default to nothing
	}
	
	protected void move(Vector2 direction) {
		float test = (float)(Math.atan2(direction.y, direction.x) + Math.PI / 2);
		
		//TODO: Make better.
		avatar.setRotation(test * 180 / (float)Math.PI + 180);
		
		avatar.setPosition(avatar.getX() + direction.x * SteveDriver.TEXTURE_WIDTH, avatar.getY() + direction.y * SteveDriver.TEXTURE_LENGTH);
	}
	
	protected void updateAvatar() {
		avatar.setRegion(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x * SteveDriver.TEXTURE_WIDTH + SteveDriver.TEXTURE_WIDTH * currentFrame * (int)atlasBounds.x, (int)atlasPosition.y * SteveDriver.TEXTURE_LENGTH, (int)atlasBounds.x* SteveDriver.TEXTURE_WIDTH, (int)atlasBounds.y * SteveDriver.TEXTURE_LENGTH));
	}
	
	public void kill() {
		SteveDriver.field.enemiesToRemove.add(this);
	}
}
