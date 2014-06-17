package com.steve.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.helpers.CollisionHelper;
import com.steve.stages.Field;

public class Enemy {
	public Sprite avatar;
	protected float mapPositionX;
	protected float mapPositionY;
	private int atlasPositionX;
	private int atlasPositionY;
	private int atlasBoundsX;
	private int atlasBoundsY;
	
	protected float moveTimer;
	protected float moveTime;
	
	protected float animateTimer;
	protected float animateTime;
	protected int currentFrame;
	protected int numberFrames;
	
	protected float health;
	
	protected float deathDamage;
	
	int numStepsInDirection = 0;
	int stepsTaken = 0;
	int directionID = 0;
	protected float sightDistance = 100;
	protected float knowledgeDistance = 0; 
	protected int moneyAmount = 0;
	
	protected int treasureAmount = 0;
	
	protected float shootTime;
	protected float shootTimer;
	
	protected int hasShotCounter;
	protected int shotCap;
	
	protected boolean ignoresBlockers;
	protected boolean destroysBlockers;
	
	//Temp variables
	Rectangle tempCollider;
	
	public Enemy(float x, float y, int atlasPositionX, int atlasPositionY, int atlasBoundsX, int atlasBoundsY, float moveTime, float animateTime, int numberFrames, float deathDamage, float health) {
		this.moveTime = moveTime;
		this.animateTime = animateTime;
		this.numberFrames = numberFrames;
		
		this.atlasPositionX = atlasPositionX;
		this.atlasPositionY = atlasPositionY;
		this.atlasBoundsX = atlasBoundsX;
		this.atlasBoundsY = atlasBoundsY;
		
		mapPositionX = x;
		mapPositionY = y;
		
		avatar = new Sprite(new TextureRegion(SteveDriver.atlas,
				atlasPositionX * SteveDriver.TEXTURE_WIDTH, atlasPositionY * SteveDriver.TEXTURE_LENGTH,
				atlasBoundsX * SteveDriver.TEXTURE_WIDTH, atlasBoundsY * SteveDriver.TEXTURE_LENGTH));
		updateAvatar();
		avatar.setPosition(x * SteveDriver.TEXTURE_WIDTH, y * SteveDriver.TEXTURE_LENGTH);
		
		this.health = health;
		this.deathDamage = (SteveDriver.snake.getSnakeTier() == 1) ? deathDamage : deathDamage - (deathDamage*SteveDriver.snake.getSnakeTier()/10);
		this.ignoresBlockers = false;
		this.destroysBlockers = false;
		
		if (SteveDriver.constants.get("bulletTime") > 0f) {
			this.moveTime *= 3;
			this.animateTime *= 3;
		}
		
		tempCollider = new Rectangle();
	}
	
	public void draw() {
		avatar.draw(SteveDriver.batch);
	}
	
	//TODO: Make more robust.
	private void checkProjectiles() {
		for (Projectile p : SteveDriver.field.getProjectiles()) {
			if (p.getFriendly() && p.getAlive()) {
				if (CollisionHelper.isCollide(avatar.getBoundingRectangle(), p.getAvatar().getBoundingRectangle())) {
					this.health -= p.getDamage();
					p.kill();
				}
			}
		}
	}
	
	public void update() {
		checkCollideWithSnake();
		checkProjectiles();
		checkIsDead();
		move();
		animate();
	}
	
	protected void checkIsDead(){
		if(this.health <= 0){
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
				SteveDriver.snake.changeHunger(deathDamage);
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

			if(this.passedBarrierCheck() || ignoresBlockers){
				//we good
			}
			else{
				for(int i = 0; i < 100; i++){
					avatar.setPosition(avatar.getX() - direction.x * SteveDriver.TEXTURE_WIDTH, avatar.getY() - direction.y * SteveDriver.TEXTURE_LENGTH);
					int j = SteveDriver.random.nextInt(4);
					float rotation = 0;
					
					switch(j){
						case 0:
							direction = SteveDriver.VDOWN;
							rotation = SteveDriver.DOWN;
						break;
						case 1:
							direction = SteveDriver.VLEFT;
							rotation = SteveDriver.LEFT;
						break;
						case 2:
							direction = SteveDriver.VRIGHT;
							rotation = SteveDriver.RIGHT;
						break;
						case 3:
							direction = SteveDriver.VUP;
							rotation = SteveDriver.UP;
						break;
					}
					
					avatar.setPosition(avatar.getX() + direction.x * SteveDriver.TEXTURE_WIDTH, avatar.getY() + direction.y * SteveDriver.TEXTURE_LENGTH);		

					if(this.passedBarrierCheck()){
						avatar.setRotation(rotation);
						break;
					}
				}
			}
			
			moveTimer = 0;
		}
		else {
			moveTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
	
	protected void updateAvatar() {
		avatar.setRegion(atlasPositionX * SteveDriver.TEXTURE_WIDTH + SteveDriver.TEXTURE_WIDTH * currentFrame * atlasBoundsX, atlasPositionY * SteveDriver.TEXTURE_LENGTH, atlasBoundsX * SteveDriver.TEXTURE_WIDTH, atlasBoundsY * SteveDriver.TEXTURE_LENGTH);
	}
	
	public void kill() {
		SteveDriver.field.enemiesToRemove.add(this);
		if (treasureAmount != 0) {
			SteveDriver.snake.addTreasure(treasureAmount);
		} else {
			SteveDriver.snake.addMoney(moneyAmount);
		}
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
	
	protected int doesKnow(float deltaX, float deltaY){
		if((deltaX < 0)){
			System.out.println("right");
			return SteveDriver.RIGHT_ID;
		}
		else if((deltaY < 0)){
			System.out.println("up");
			return SteveDriver.UP_ID;
		}
		else if((deltaX > 0)){
			System.out.println("left");
			return SteveDriver.LEFT_ID;
		}
		else if((deltaY > 0)){
			System.out.println("down");
			return SteveDriver.DOWN_ID;
		}
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
			distance = (deltaX * deltaX) + (deltaY * deltaY);
			
			if(distance < sightDistance * sightDistance){
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
	
	protected Vector2 pursuitMoveWithKnowledge(){
		float deltaX;
		float deltaY;
		float distance;
		
		for(Sprite s: SteveDriver.snake.getSegments()){
			deltaY = this.avatar.getY() - s.getY();
			deltaX = this.avatar.getX() - s.getX();
			distance = deltaX * deltaX + deltaY * deltaY;
			
			if(distance < knowledgeDistance * knowledgeDistance){
				if(this.doesKnow(deltaX, deltaY) == SteveDriver.RIGHT_ID){
					stepsTaken = 0;
					directionID = SteveDriver.RIGHT_ID;
					this.avatar.setRotation(SteveDriver.RIGHT);
					return SteveDriver.VRIGHT;
				}
				else if(this.doesKnow(deltaX, deltaY) == SteveDriver.UP_ID){
					stepsTaken = 0;
					directionID = SteveDriver.UP_ID;
					this.avatar.setRotation(SteveDriver.UP);
					return SteveDriver.VUP;
					
				}
				else if(this.doesKnow(deltaX, deltaY) == SteveDriver.LEFT_ID){
					stepsTaken = 0;
					directionID = SteveDriver.LEFT_ID;
					this.avatar.setRotation(SteveDriver.LEFT);
					return SteveDriver.VLEFT;
				}
				else if(this.doesKnow(deltaX, deltaY) == SteveDriver.DOWN_ID){
					stepsTaken = 0;
					directionID = SteveDriver.DOWN_ID;
					this.avatar.setRotation(SteveDriver.DOWN);
					return SteveDriver.VDOWN;
				}
			}
		}
		
		return this.randomMove();		
	}
	
	protected Vector2 flyMove(){
		if(this.avatar.getRotation() == SteveDriver.RIGHT){
			stepsTaken = 0;
			directionID = SteveDriver.RIGHT_ID;
			this.avatar.setRotation(SteveDriver.RIGHT);
			return SteveDriver.VRIGHT;
		}
		else if(this.avatar.getRotation() == SteveDriver.UP){
			stepsTaken = 0;
			directionID = SteveDriver.UP_ID;
			this.avatar.setRotation(SteveDriver.UP);
			return SteveDriver.VUP;
			
		}
		else if(this.avatar.getRotation() == SteveDriver.LEFT){
			stepsTaken = 0;
			directionID = SteveDriver.LEFT_ID;
			this.avatar.setRotation(SteveDriver.LEFT);
			return SteveDriver.VLEFT;
		}
		else{
			stepsTaken = 0;
			directionID = SteveDriver.DOWN_ID;
			this.avatar.setRotation(SteveDriver.DOWN);
			return SteveDriver.VDOWN;
		}
	}
	
	protected boolean passedBarrierCheck(){
		TiledMapTileLayer layer = Field.blockers;
		
		int cellWidth = avatar.getRegionWidth() / SteveDriver.TEXTURE_WIDTH;
		int cellHeight = avatar.getRegionHeight() / SteveDriver.TEXTURE_LENGTH;
		
		int topCornerX = (int)avatar.getX() / SteveDriver.TEXTURE_WIDTH;
		int topCornerY = (int)avatar.getY() / SteveDriver.TEXTURE_LENGTH;
		
		for (int x = 0; x < cellWidth; x++) {
			for (int y = 0; y < cellHeight; y++) {
				Cell cell = layer.getCell(topCornerX + x, topCornerY + y);
				
				tempCollider.x = (topCornerX + x) * SteveDriver.TEXTURE_WIDTH;
				tempCollider.y = (topCornerY + y) * SteveDriver.TEXTURE_LENGTH;
				tempCollider.width = SteveDriver.TEXTURE_WIDTH;
				tempCollider.height = SteveDriver.TEXTURE_LENGTH;
				
				if (cell != null && CollisionHelper.isCollide(tempCollider, this.avatar.getBoundingRectangle())) {
					if(destroysBlockers) {
						SteveDriver.field.destroyBlocker(x + topCornerX, y + topCornerY);
					}
					
					return false;
				}
			}
		}
		
		return true;
	}
	
	protected void decideShoot() {
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
						shoot(1f, 0f);
						
						shootTimer = 0;
						hasShotCounter = 0;
						break;
					}
					else if(this.avatar.getRotation() == SteveDriver.UP
							&& this.doesSee(deltaX, deltaY) == SteveDriver.UP_ID){
						shoot(0f, 1f);
						
						shootTimer = 0;
						hasShotCounter = 0;
						break;
					}
					else if(this.avatar.getRotation() == SteveDriver.LEFT
							&& this.doesSee(deltaX, deltaY) == SteveDriver.LEFT_ID){
						shoot(-1f, 0f);	
							
						shootTimer = 0;
						hasShotCounter = 0;
						break;
					}
					else if(this.avatar.getRotation() == SteveDriver.DOWN 
							&& this.doesSee(deltaX, deltaY) == SteveDriver.DOWN_ID){
						shoot(0f, -1f);
						
						shootTimer = 0;
						hasShotCounter = 0;
						break;
					}
				}
			}	
		}
		else {
			shootTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
	
	protected void shoot(float dx, float dy){
		//Nothing by default
	}
	
	protected void addProjectile(Projectile proj, float dx, float dy) {
		if (proj != null) {
			SteveDriver.field.addProjectile(proj);
			proj.setDirection(dx, dy);
		}
	}

	protected void decideShootAir(){
		if (shootTimer >= shootTime) {
			if(this.avatar.getRotation() == SteveDriver.RIGHT) {
				shoot(1f, 0f);
				
				shootTimer = 0;
				hasShotCounter = 0;
			}
			else if(this.avatar.getRotation() == SteveDriver.UP) {
				shoot(0f,  1f);
				
				shootTimer = 0;
				hasShotCounter = 0;
			}
			else if(this.avatar.getRotation() == SteveDriver.LEFT) {
				shoot(-1f, 0f);	
					
				shootTimer = 0;
				hasShotCounter = 0;
			}
			else if(this.avatar.getRotation() == SteveDriver.DOWN) {
				shoot(0f, -1f);
				
				shootTimer = 0;
				hasShotCounter = 0;
			}
		}	
		else {
			shootTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
}
