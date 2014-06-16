package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.projectiles.Tree;

public class HomaHawk extends Enemy{
	public HomaHawk(float x, float y) {
		super(x, y, 11, 10, 3, 3, .2f, 0.4f, 4, 75, 200);
		sightDistance = 500;//to be refined
		knowledgeDistance = 600;
		ignoresBlockers = true;
		moneyAmount = 30;
		shootTime = 1.6f;
	}
	
	@Override
	public void update(){
		this.checkInField();
		super.update();
		super.decideShootAir();
	}
	
	@Override
	public void shoot(float dx, float dy) {
		super.addProjectile(new Tree(avatar.getX(), avatar.getY() + SteveDriver.TEXTURE_LENGTH / 2), dx, dy);
	}

	@Override
	protected Vector2 decideMove() {
		return super.flyMove();
	}
	
	private void checkInField(){
		for(Sprite s: SteveDriver.snake.getSegments()){
			float deltaY = this.avatar.getY() - s.getY();
			float deltaX = this.avatar.getX() - s.getX();
			
			if(Math.abs(deltaX) > SteveDriver.guiCamera.viewportWidth*1.2f ||
					Math.abs(deltaY) > Gdx.graphics.getHeight()*1.2f){
				this.moveToNextBombingRun();
			}
		}
	}
	
	private void moveToNextBombingRun(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();
		
		float xPosTopBot = snakePosition.x - SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenWidth")/5f +
				SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenWidth")/5f;
		float xPosLeft = snakePosition.x + SteveDriver.constants.get("screenWidth")/2f
				+ SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenWidth")/4f;
		float xPosRight = snakePosition.x - SteveDriver.constants.get("screenWidth")/2f
				- SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenWidth")/4f;
		float yPosTop = snakePosition.y + SteveDriver.constants.get("screenHeight")/2f
				+ SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenHeight")/4f;
		float yPosBot = snakePosition.y - SteveDriver.constants.get("screenHeight")/2f
				- SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenHeight")/4f;
		float yPosRightLeft = snakePosition.y - SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenHeight")/5f +
				SteveDriver.random.nextFloat() * SteveDriver.constants.get("screenHeight")/5f;
		
		int choiceX = 0; 
		
		if(SteveDriver.random.nextBoolean()){
			choiceX = 0; 
		}
		else if(SteveDriver.random.nextBoolean()){
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
		else if(SteveDriver.random.nextBoolean())
			yPos = yPosBot;
		else 
			yPos = yPosTop;
		
		float deltaY = yPos - snakePosition.x;
		float deltaX = xPos - snakePosition.y;
		
		int rotationChoice = 0;
		for(int i = 0; i < 100; i++){
			rotationChoice = SteveDriver.random.nextInt(4);
			
			if(choiceX != 0 && deltaX < 0 && rotationChoice == SteveDriver.RIGHT_ID){
				this.avatar.setRotation(SteveDriver.RIGHT);
				break;
			}
			else if(deltaY < 0 && rotationChoice == SteveDriver.UP_ID){
				this.avatar.setRotation(SteveDriver.UP);
				break;
			}
			else if(choiceX != 0 && deltaX > 0 && rotationChoice == SteveDriver.LEFT_ID){
				this.avatar.setRotation(SteveDriver.LEFT);
				break;
			}
			else if(deltaY > 0 && rotationChoice == SteveDriver.DOWN_ID) {
				this.avatar.setRotation(SteveDriver.DOWN);
				break;
			}
		}
		
		avatar.setPosition(xPos, yPos);
	}
}
