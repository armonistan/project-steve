package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.steve.base.Enemy;

public class Spring extends Enemy{
	private float slowMoveTime;
	private float fastMoveTime;
	private float slowChangeTime;
	private float fastChangeTime;
	private float changeTimer;
	
	public Spring(float x, float y) {
		super(x, y, 15, 3, 2, 2, 10f, 0.4f, 3, 75, 50);
		sightDistance = 500;//to be refined
		
		moneyAmount = 150;
		fastMoveTime = 0.05f;
		slowMoveTime = super.moveTime;
		slowChangeTime = 3;
		fastChangeTime = 0.5f;
		changeTimer = 0;
	}

	@Override
	public void update() {
		
		if (changeTimer > ((moveTime == slowMoveTime) ? slowChangeTime : fastChangeTime)) {
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
		if (moveTime == slowMoveTime) {
			return super.pursuitMoveWithSight();
		}
		else {
			return super.flyMove();
		}
	}
}
