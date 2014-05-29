package com.steve.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.base.Enemy;

public class HomaHawk extends Enemy{
	public HomaHawk(float x, float y) {
		super(x, y, new Vector2(11, 10), new Vector2(3, 3), 1f, 0.4f, 4, 75);
		sightDistance = 500;//to be refined
		
		moneyAmount = 30;
	}

	@Override
	protected Vector2 decideMove() {
		//todo give real behavior
		return super.pursuitMoveWithSight();
	}
}
