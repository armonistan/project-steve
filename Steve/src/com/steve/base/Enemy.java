package com.steve.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.helpers.CollisionHelper;

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
	
	protected float deathDamage;
	
	int numStepsInDirection = 0;
	int stepsTaken = 0;
	int directionID = 0;
	protected int sightDistance = 100;
	protected int moneyAmount = 0;
	
	protected float shootTime;
	protected float shootTimer;
	
	protected int hasShotCounter;
	protected int shotCap;
	
	public Enemy(float x, float y, Vector2 atlasPosition, Vector2 atlasBounds, float moveTime, float animateTime, int numberFrames, float deathDamage) {
		this.moveTime = moveTime;
		this.animateTime = animateTime;
		this.numberFrames = numberFrames;
		this.atlasPosition = atlasPosition;
		this.atlasBounds = atlasBounds;
		
		mapPosition = new Vector2(x, y);
		
		avatar = new Sprite(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x * SteveDriver.TEXTURE_WIDTH, (int)atlasPosition.y * SteveDriver.TEXTURE_LENGTH, (int)atlasBounds.x* SteveDriver.TEXTURE_WIDTH, (int)atlasBounds.y * SteveDriver.TEXTURE_LENGTH));
		updateAvatar();
		avatar.setPosition(mapPosition.x * SteveDriver.TEXTURE_WIDTH, mapPosition.y * SteveDriver.TEXTURE_LENGTH);
		
		healthPercentage = 100;
		this.deathDamage = (SteveDriver.snake.getSnakeTier() == 1) ? deathDamage : deathDamage - (deathDamage*SteveDriver.snake.getSnakeTier()/10);
	}
	
	public void render() {
		
		update();
		
		avatar.draw(SteveDriver.batch);
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
		checkCollideWithSnake();
		checkProjectiles();
		checkIsDead();
		move();
		animate();
	}
	
	protected void checkIsDead(){
		if(this.healthPercentage <= 0){
			this.kill();
		}
	}
	
	protected void animate(){
		if (animateTimer >= animateTime) {
			currentFrame = (currentFrame + 1) % numberFrames;
			updateAvatar();
			
			animateTimer = 0;
		}
		else {
			animateTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
	
	protected void checkCollideWithSnake(){
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(s.getBoundingRectangle(), avatar.getBoundingRectangle())) {
				SteveDriver.snake.changeHungerByPercent(deathDamage);
				//TODO: Replace with something better.
				kill();
				break;
			}
		}
	}
	
	protected Vector2 decideMove() {
		return null;
	}
	
	protected void move() {
		if (moveTimer >= moveTime){
			Vector2 direction = decideMove();
			avatar.setPosition(avatar.getX() + direction.x * SteveDriver.TEXTURE_WIDTH, avatar.getY() + direction.y * SteveDriver.TEXTURE_LENGTH);		
			//avatar.setRotation(test * 180 / (float)Math.PI + 180);
			moveTimer = 0;
		}
		else {
			moveTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
	
	protected void updateAvatar() {
		avatar.setRegion(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x * SteveDriver.TEXTURE_WIDTH + SteveDriver.TEXTURE_WIDTH * currentFrame * (int)atlasBounds.x, (int)atlasPosition.y * SteveDriver.TEXTURE_LENGTH, (int)atlasBounds.x* SteveDriver.TEXTURE_WIDTH, (int)atlasBounds.y * SteveDriver.TEXTURE_LENGTH));
	}
	
	public void kill() {
		SteveDriver.field.enemiesToRemove.add(this);
		SteveDriver.snake.addMoney(moneyAmount);
	}
	
	public float getXPosition(){
		return this.avatar.getX();
	}
	
	public float getYPosition(){
		return this.avatar.getY();
	}
	
	public Rectangle getRectangle(){
		return this.avatar.getBoundingRectangle();
	}
	
	protected Vector2 randomMove(){
			if(stepsTaken == numStepsInDirection){
				numStepsInDirection = SteveDriver.random.nextInt(3)+1;
				directionID = (SteveDriver.random.nextInt(2) == 0 ? directionID : 
					(SteveDriver.random.nextInt(2) == 0 ? directionID+1 : directionID-1));
				directionID = Math.abs(directionID)%4;
				stepsTaken = 0;
			}
			else{
				stepsTaken++;
			}
			
			switch(directionID){
				case SteveDriver.RIGHT_ID:
					this.avatar.setRotation(SteveDriver.RIGHT);
					return SteveDriver.VRIGHT;	
				
				case SteveDriver.UP_ID:
					this.avatar.setRotation(SteveDriver.UP);
					return SteveDriver.VUP;
				
				case SteveDriver.LEFT_ID:
					this.avatar.setRotation(SteveDriver.LEFT);
					return SteveDriver.VLEFT;
				
				case SteveDriver.DOWN_ID:
					this.avatar.setRotation(SteveDriver.DOWN);
					return SteveDriver.VDOWN;
			}
			
			return null;
	}
	
	//returned int corresponds to direction id
	protected int doesSee(float deltaX, float deltaY){
		if((deltaY == 0 && deltaX < 0))
			return SteveDriver.RIGHT_ID;
		else if((deltaY < 0 && deltaX == 0)){
			return SteveDriver.UP_ID;
		}
		else if((deltaY == 0 && deltaX > 0)){
			return SteveDriver.LEFT_ID;
		}
		else if((deltaY > 0 && deltaX == 0))
			return SteveDriver.DOWN_ID;
		else 
			return -1;//invalid id. means dont see a thing
	}
	
	protected Vector2 pursuitMoveWithSight(){
		float deltaX;
		float deltaY;
		float distance;
		
		for(Sprite s: SteveDriver.snake.getSegments()){
			deltaY = this.avatar.getY() - s.getY();
			deltaX = this.avatar.getX() - s.getX();
			distance = (float)Math.sqrt((double)((deltaX * deltaX) + (deltaY * deltaY)));
			
			if(distance < sightDistance){
				if(this.avatar.getRotation() == SteveDriver.RIGHT 
						&& this.doesSee(deltaX, deltaY) == SteveDriver.RIGHT_ID){
					stepsTaken = 0;
					directionID = SteveDriver.RIGHT_ID;
					this.avatar.setRotation(SteveDriver.RIGHT);
					return SteveDriver.VRIGHT;
				}
				else if(this.avatar.getRotation() == SteveDriver.UP
						&& this.doesSee(deltaX, deltaY) == SteveDriver.UP_ID){
					stepsTaken = 0;
					directionID = SteveDriver.UP_ID;
					this.avatar.setRotation(SteveDriver.UP);
					return SteveDriver.VUP;
					
				}
				else if(this.avatar.getRotation() == SteveDriver.LEFT
						&& this.doesSee(deltaX, deltaY) == SteveDriver.LEFT_ID){
					stepsTaken = 0;
					directionID = SteveDriver.LEFT_ID;
					this.avatar.setRotation(SteveDriver.LEFT);
					return SteveDriver.VLEFT;
				}
				else if(this.avatar.getRotation() == SteveDriver.DOWN 
						&& this.doesSee(deltaX, deltaY) == SteveDriver.DOWN_ID){
					stepsTaken = 0;
					directionID = SteveDriver.DOWN_ID;
					this.avatar.setRotation(SteveDriver.DOWN);
					return SteveDriver.VDOWN;
				}
			}
		}
		
		return this.randomMove();
	}
	
	
	protected void shoot(Projectile proj){
		float deltaX;
		float deltaY;
		float distance;
		
		if (shootTimer >= shootTime) {
			for(Sprite s: SteveDriver.snake.getSegments()){
				deltaY = this.avatar.getY() - s.getY();
				deltaX = this.avatar.getX() - s.getX();
				distance = CollisionHelper.distanceSquared(deltaX, deltaY, deltaX, deltaY);
			
				if(distance < sightDistance * sightDistance){
					if(this.avatar.getRotation() == SteveDriver.RIGHT 
							&& this.doesSee(deltaX, deltaY) == SteveDriver.RIGHT_ID){
						SteveDriver.field.projectiles.add(proj);
						proj.setDirection(1, 0);
						
						shootTimer = 0;
						hasShotCounter = 0;
					}
					else if(this.avatar.getRotation() == SteveDriver.UP
							&& this.doesSee(deltaX, deltaY) == SteveDriver.UP_ID){
						SteveDriver.field.projectiles.add(proj);
						proj.setDirection(0,  1);
						
						shootTimer = 0;
						hasShotCounter = 0;
					}
					else if(this.avatar.getRotation() == SteveDriver.LEFT
							&& this.doesSee(deltaX, deltaY) == SteveDriver.LEFT_ID){
							SteveDriver.field.projectiles.add(proj);
						proj.setDirection(-1, 0);	
							
						shootTimer = 0;
						hasShotCounter = 0;
					}
					else if(this.avatar.getRotation() == SteveDriver.DOWN 
							&& this.doesSee(deltaX, deltaY) == SteveDriver.DOWN_ID){
						SteveDriver.field.projectiles.add(proj);
						proj.setDirection(0, -1);
						
						shootTimer = 0;
						hasShotCounter = 0;
					}
				}
			}	
		}
		else {
			shootTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
}
