package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.base.Enemy;

public class Spiral extends Enemy{
	
	public Spiral(float x, float y) {
		super(x, y, new Vector2(11, 13), new Vector2(3, 3), .75f, .2f, 2, 75);
		knowledgeDistance = 500;//to be refined
		
		destroysBlockers = true;
		moneyAmount = 30;
	}
	
	public void update() {
		super.update();
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
		float distance = (float)Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY, 2));
		
		moveTime = distance/knowledgeDistance;
		moveTime = (moveTime > .8) ? .8f : 
			(moveTime < .4) ? .4f : moveTime;
		
		System.out.println("move speed: " + moveTime);
	}
}
