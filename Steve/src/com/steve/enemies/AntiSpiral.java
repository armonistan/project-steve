package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.base.Enemy;

public class AntiSpiral extends Enemy{
	private float slowMoveTime;
	private float fastMoveTime;
	private float changeTime;
	private float changeTimer;
	
	public AntiSpiral(float x, float y) {
		super(x, y, new Vector2(15, 3), new Vector2(2, 2), 1f, 0.4f, 3, 75);
		sightDistance = 500;//to be refined
		
		moneyAmount = 30;
		fastMoveTime = 0.05f;
		slowMoveTime = super.moveTime;
		changeTime = 3;
		changeTimer = 0;
	}

	public void update() {
		if (changeTimer > changeTime) {
			moveTime = (moveTime == slowMoveTime) ? fastMoveTime : slowMoveTime;
			changeTimer = 0;
		}
		else {
			changeTimer += Gdx.graphics.getRawDeltaTime();
		}
		super.update();
	}
	
	@Override
	protected Vector2 decideMove() {
		//todo give real behavior
		return super.pursuitMoveWithSight();
	}
}
