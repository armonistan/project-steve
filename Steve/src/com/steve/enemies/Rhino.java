package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.base.Enemy;

public class Rhino extends Enemy {
	float rotationPerFrame = MathUtils.PI2;
	float myRotation;
	
	public Rhino(float x, float y) {
		super(x, y, 17, 13, 3, 3,
				1.5f, 100, 1, 50);
		// TODO Auto-generated constructor stub
	}

	protected void animate() {
		myRotation += rotationPerFrame * MathUtils.radiansToDegrees * Gdx.graphics.getRawDeltaTime();
		
		super.animate();
		
		avatar.setRotation(myRotation);
	}
	
	protected Vector2 decideMove() {
		return super.pursuitMoveWithSight();
	}
}
