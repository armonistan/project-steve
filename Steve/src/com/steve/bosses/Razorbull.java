package com.steve.bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.helpers.CollisionHelper;
import com.steve.projectiles.Tree;
import com.steve.stages.Field;

public class Razorbull extends Enemy{
	float slowMoveTime;
	float fastMoveTime;
	
	public Razorbull(float x, float y) {
		super(x, y, 21, 23, 9, 3, .2f, 0.3f, 1, 75, 200);
		sightDistance = 500;//to be refined
		knowledgeDistance = 600;
		super.destroysBlockers = true;
		super.ignoresBlockers = true;
		moneyAmount = 30;
		shootTime = .5f;
		slowMoveTime = .5f;
		fastMoveTime = .1f;
		super.moveTime = slowMoveTime;
	}
	
	@Override
	public void update(){
		this.checkInField();
		super.update();	
		if(this.shootTimer > this.shootTime)
			shoot();
		else
			shootTimer += Gdx.graphics.getRawDeltaTime();
	}
	
	public void shoot() {
		int x = (int)(this.avatar.getX()/SteveDriver.TEXTURE_SIZE);
		int y = (int)(this.avatar.getY()/SteveDriver.TEXTURE_SIZE);
		
		if(this.avatar.getRotation() == SteveDriver.RIGHT){
			y+=2;
			x+=4;
		}
		else if(this.avatar.getRotation() == SteveDriver.UP){
			x-=1;
		}
		else if(this.avatar.getRotation() == SteveDriver.LEFT){
			y-=1;
			x+=3;
		}
		else{
			y+=1;
			x+=10;
		}
		
		SteveDriver.field.createBlockerFormation(x, y);
		shootTimer = 0;
	}

	@Override
	protected Vector2 decideMove() {
		return razorMove();
	}
	
	protected void checkCollideWithSnake(){
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(s.getBoundingRectangle(), avatar.getBoundingRectangle())) {
				SteveDriver.snake.changeHunger(deathDamage);
				break;
			}
		}
	}
	
	protected Vector2 razorMove(){
		if(this.avatar.getRotation() == SteveDriver.RIGHT){
			stepsTaken = 0;
			directionID = SteveDriver.DOWN_ID;
			this.avatar.setRotation(SteveDriver.RIGHT);
			return SteveDriver.VDOWN;
		}
		else if(this.avatar.getRotation() == SteveDriver.UP){
			stepsTaken = 0;
			directionID = SteveDriver.RIGHT_ID;
			this.avatar.setRotation(SteveDriver.UP);
			return SteveDriver.VRIGHT;
			
		}
		else if(this.avatar.getRotation() == SteveDriver.LEFT){
			stepsTaken = 0;
			directionID = SteveDriver.UP_ID;
			this.avatar.setRotation(SteveDriver.LEFT);
			return SteveDriver.VUP;
		}
		else{
			stepsTaken = 0;
			directionID = SteveDriver.LEFT_ID;
			this.avatar.setRotation(SteveDriver.DOWN);
			return SteveDriver.VLEFT;
		}
	}
	
	private void checkInField(){
		for(Sprite s: SteveDriver.snake.getSegments()){
			float deltaY = this.avatar.getY() - s.getY();
			float deltaX = this.avatar.getX() - s.getX();
			
			if(Math.abs(deltaX) > SteveDriver.guiCamera.viewportWidth*1.1f ||
					Math.abs(deltaY) > Gdx.graphics.getHeight()*1.1f){
				this.moveToNextBombingRun();
				moveTime = fastMoveTime;
			}
			else if(Math.abs(deltaX) > SteveDriver.guiCamera.viewportWidth*1f ||
					Math.abs(deltaY) > Gdx.graphics.getHeight()*1f){
				moveTime = slowMoveTime;
			}
			else if(Math.abs(deltaX) < SteveDriver.guiCamera.viewportWidth*.95f ||
					Math.abs(deltaY) < Gdx.graphics.getHeight()*.95f){
				moveTime = fastMoveTime;
			}
		}
	}
	
	private void moveToNextBombingRun(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();
		
		float xPosTopBot = snakePosition.x - SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenWidth")/9f +
				SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenWidth")/9f;
		float xPosLeft = snakePosition.x + SteveDriver.constants.get("screenWidth")/2f
				+ SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenWidth")/4f;
		float xPosRight = snakePosition.x - SteveDriver.constants.get("screenWidth")/2f
				- SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenWidth")/4f;
		float yPosTop = snakePosition.y + SteveDriver.constants.get("screenHeight")/2f
				+ SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenHeight")/4f;
		float yPosBot = snakePosition.y - SteveDriver.constants.get("screenHeight")/2f
				- SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenHeight")/4f;
		float yPosRightLeft = snakePosition.y - SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenHeight")/9f +
				SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenHeight")/9f;
		
		int choiceX = 0; 
		
		if(SteveDriver.snake.getRotationIndex() == SteveDriver.UP_ID || SteveDriver.snake.getRotationIndex() == SteveDriver.DOWN_ID){
			choiceX = 0; 
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.LEFT_ID){
			choiceX = -1;
		}
		else {
			choiceX = 1;
		}
		
		float xPos = 0;
		
		if((choiceX == 0))
			xPos = xPosTopBot;
		else if(choiceX < 0){
			xPos = xPosLeft;
		}
		else
			xPos = xPosRight;
		
		float yPos = 0;
		
		if(choiceX != 0)
			yPos = yPosRightLeft; 
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.UP_ID)
			yPos = yPosBot;
		else 
			yPos = yPosTop;
		
		float deltaY = yPos - snakePosition.y;
		float deltaX = xPos - snakePosition.x;
		
		int rotationChoice = 0;
		for(int i = 0; i < 100; i++){
			rotationChoice = SteveDriver.random.nextInt(4);
			
			if(choiceX != 0 && deltaX < 0 && rotationChoice == SteveDriver.RIGHT_ID){
				this.avatar.setRotation(SteveDriver.UP);
				break;
			}
			else if(deltaY < 0 && rotationChoice == SteveDriver.UP_ID){
				this.avatar.setRotation(SteveDriver.LEFT);
				break;
			}
			else if(choiceX != 0 && deltaX > 0 && rotationChoice == SteveDriver.LEFT_ID){
				this.avatar.setRotation(SteveDriver.DOWN);
				break;
			}
			else if(deltaY > 0 && rotationChoice == SteveDriver.DOWN_ID) {
				this.avatar.setRotation(SteveDriver.RIGHT);
				break;
			}
		}
		
		avatar.setPosition((int)xPos, (int)yPos);
	}
}
