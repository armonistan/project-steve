package com.steve.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.helpers.CollisionHelper;

public class Narwhal extends Enemy{
	
	public Narwhal(float x, float y) {
		super(x, y, 11, 13, 3, 3, .75f, .2f, 2, 50, 1200);
		knowledgeDistance = 500;//to be refined
		
		destroysBlockers = true;
		ignoresBlockers = true;
		moneyAmount = 1600;
	}
	
	@Override
	public void update(){
		decideMoveSpeed();
		super.update();
		
	}
	@Override
	protected Vector2 decideMove() {
		return super.pursuitMoveWithKnowledge();
	}
	
	private void decideMoveSpeed(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();
		float deltaY = this.avatar.getY() - snakePosition.y;
		float deltaX = this.avatar.getX() - snakePosition.x;
		float distance = (float)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		
		moveTime = distance / knowledgeDistance;
		moveTime = (moveTime > .8f) ? .8f : 
			(moveTime < .4f) ? .4f : moveTime;
		
		//System.out.println("move speed: " + moveTime);
	}
	
	@Override
	protected void checkCollideWithSnake(){
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(avatar.getBoundingRectangle(), s.getBoundingRectangle())) {
				SteveDriver.snake.changeHunger(deathDamage);
				//System.out.println("collide front");
				return;
			}
		}
	}
}
