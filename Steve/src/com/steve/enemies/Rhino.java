package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.helpers.CollisionHelper;

public class Rhino extends Enemy {
	float rotationPerFrame = MathUtils.PI2;
	float myRotation;
	
	public Rhino(float x, float y) {
		super(x, y, 17, 13, 3, 3,
				.3f, 100, 1, 5, 1350);
		// TODO Auto-generated constructor stub
		moneyAmount = 2100;
		super.knowledgeDistance = 500;
	}

	@Override
	protected void animate() {
		myRotation += rotationPerFrame * MathUtils.radiansToDegrees * Gdx.graphics.getRawDeltaTime();
		
		super.animate();
		
		avatar.setRotation(myRotation);
	}
	
	@Override
	protected Vector2 decideMove() {
		return super.pursuitMoveWithKnowledge();
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
