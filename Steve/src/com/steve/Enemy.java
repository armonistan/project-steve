package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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
	
	protected float healthPercentage;
	
	public Enemy(float x, float y, Vector2 atlasPosition, Vector2 atlasBounds, float moveTime, float animateTime, int numberFrames) {
		mapPosition = new Vector2(x, y);
		this.moveTime = moveTime;
		this.animateTime = animateTime;
		this.numberFrames = numberFrames;
		this.atlasPosition = atlasPosition;
		this.atlasBounds = atlasBounds;
		
		avatar = new Sprite(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x * SteveDriver.TEXTURE_WIDTH, (int)atlasPosition.y * SteveDriver.TEXTURE_LENGTH, (int)atlasBounds.x* SteveDriver.TEXTURE_WIDTH, (int)atlasBounds.y * SteveDriver.TEXTURE_LENGTH));
		updateAvatar();
		avatar.setPosition(mapPosition.x * SteveDriver.TEXTURE_WIDTH, mapPosition.y * SteveDriver.TEXTURE_LENGTH);
		
		healthPercentage = 100;
	}
	
	public void render(SpriteBatch batch) {
		checkProjectiles();
		
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
	
	//TODO: Make more robust.
	private void checkProjectiles() {
		for (Projectile p : SteveDriver.field.projectiles) {
			if (p.getFriendly() && p.getAlive()) {
				if (CollisionHelper.isCollide(avatar.getBoundingRectangle(), p.getAvatar().getBoundingRectangle())) {
					this.healthPercentage -= p.getPercentDamage();
					p.kill();
				}
			}
		}
	}
	
	protected void update() {
		if(this.healthPercentage <= 0){
			this.kill();
		}
		//TODO: Define basic update behavior
		//Should override
	}
	
	protected void decideMove() {
		//Default to nothing
	}
	
	protected void move(float dx, float dy) {
		avatar.setRotation(MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees - 90);
		
		avatar.setPosition(avatar.getX() + dx * SteveDriver.TEXTURE_WIDTH, avatar.getY() + dy * SteveDriver.TEXTURE_LENGTH);
	}
	
	protected void updateAvatar() {
		avatar.setRegion(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x * SteveDriver.TEXTURE_WIDTH + SteveDriver.TEXTURE_WIDTH * currentFrame * (int)atlasBounds.x, (int)atlasPosition.y * SteveDriver.TEXTURE_LENGTH, (int)atlasBounds.x* SteveDriver.TEXTURE_WIDTH, (int)atlasBounds.y * SteveDriver.TEXTURE_LENGTH));
	}
	
	public void kill() {
		SteveDriver.field.enemiesToRemove.add(this);
	}
	
	public float getXPosition(){
		return this.avatar.getX();
	}
	
	public float getYPosition(){
		return this.avatar.getY();
	}
	
	protected void moveRandomly() {
		//TODO: This can be more sophisticated.
		if (avatar.getRotation() == SteveDriver.UP || avatar.getRotation() == SteveDriver.DOWN) {
			move(SteveDriver.random.nextBoolean() ? 1 : -1, 0);
		}
		else {
			move(0, SteveDriver.random.nextBoolean() ? 1 : -1);
		}
	}
	
	protected void followSnake() {
		//TODO: This can be more sophisticated.
		float directionToSnakeX = avatar.getX() + avatar.getOriginX() - SteveDriver.snake.getHeadPosition().x;
		float directionToSnakeY = avatar.getY() + avatar.getOriginY() - SteveDriver.snake.getHeadPosition().y;
		float angleToSnake = MathUtils.atan2(directionToSnakeY, directionToSnakeX);
		
		if (angleToSnake > MathUtils.PI / 4f && angleToSnake <= MathUtils.PI * 3f / 4f) {
			move(SteveDriver.VUP.x, SteveDriver.VUP.y);
		}
		else if (angleToSnake > MathUtils.PI * 3f / 4f && angleToSnake <= MathUtils.PI * 5f / 4f) {
			move(SteveDriver.VLEFT.x, SteveDriver.VLEFT.y);
		}
		else if (angleToSnake > MathUtils.PI * 5f / 4f && angleToSnake <= MathUtils.PI * 7f / 4f) {
			move(SteveDriver.VDOWN.x, SteveDriver.VDOWN.y);
		}
		else {
			move(SteveDriver.VRIGHT.x, SteveDriver.VRIGHT.y);
		}
	}
}
