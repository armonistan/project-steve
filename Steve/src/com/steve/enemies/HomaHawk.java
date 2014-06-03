package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.helpers.CollisionHelper;
import com.steve.projectiles.Acorn;
import com.steve.projectiles.Tree;

public class HomaHawk extends Enemy{
	public HomaHawk(float x, float y) {
		super(x, y, new Vector2(11, 10), new Vector2(3, 3), .5f, 0.4f, 4, 75);
		sightDistance = 500;//to be refined
		knowledgeDistance = 600;
		ignoresBlockers = true;
		moneyAmount = 30;
		shootTime = 0.9f;
	}
	
	public void update(){
		this.checkInField();
		super.update();
		super.airShoot(new Tree(avatar.getX(), avatar.getY() + SteveDriver.TEXTURE_LENGTH, 0f, 0f));
	}

	@Override
	protected Vector2 decideMove() {
		return super.flyMove();
	}
	
	private void checkInField(){
		for(Sprite s: SteveDriver.snake.getSegments()){
			float deltaY = this.avatar.getY() - s.getY();
			float deltaX = this.avatar.getX() - s.getX();
			
			if(Math.abs(deltaX) > Gdx.graphics.getWidth()*1.2 ||
					Math.abs(deltaY) > Gdx.graphics.getHeight()*1.2){
				this.moveToNextBombingRun();
			}
		}
	}
	
	private void moveToNextBombingRun(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();
		
		float xPosTopBot = (snakePosition.x - SteveDriver.random.nextInt((int)(Gdx.graphics.getWidth()*.2)) + SteveDriver.random.nextInt((int)(Gdx.graphics.getWidth()*.2)));
		float xPosLeft = (snakePosition.x + (int)(Gdx.graphics.getWidth()*.5)
				+ SteveDriver.random.nextInt((int)(Gdx.graphics.getWidth()*.25)));
		float xPosRight = (snakePosition.x - (int)(Gdx.graphics.getWidth()*.5)
				- SteveDriver.random.nextInt((int)(Gdx.graphics.getWidth()*.25)));
		float yPosTop = (snakePosition.y + (int)(Gdx.graphics.getHeight()*.5)
				+ SteveDriver.random.nextInt((int)(Gdx.graphics.getHeight()*.25)));
		float yPosBot = (snakePosition.y - (int)(Gdx.graphics.getHeight()*.5)
				- SteveDriver.random.nextInt((int)(Gdx.graphics.getHeight()*.25)));
		float yPosRightLeft = (snakePosition.y - SteveDriver.random.nextInt((int)(Gdx.graphics.getHeight()*.2)) + SteveDriver.random.nextInt((int)(Gdx.graphics.getHeight()*.2)));
		
		int choiceX = 0; 
		
		if(SteveDriver.random.nextBoolean()){
			choiceX = 0; 
		}
		else if(SteveDriver.random.nextBoolean()){
			choiceX = -1;
		}
		else
			choiceX = 1;
		
		int xPos = 0;
		
		if((choiceX == 0))
			xPos = (int)xPosTopBot;
		else if(choiceX < 0){
			xPos = (int)xPosLeft;
		}
		else
			xPos = (int)xPosRight;
		
		int yPos = 0;
		
		if(choiceX != 0)
			yPos = (int)yPosRightLeft; 
		else if(SteveDriver.random.nextBoolean())
			yPos = (int)yPosBot;
		else 
			yPos = (int)yPosTop;
		
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
