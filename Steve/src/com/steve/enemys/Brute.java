package com.steve.enemys;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.base.Enemy;

public class Brute extends Enemy{
	public Brute(float x, float y) {
		super(x, y, new Vector2(11, 3), new Vector2(2, 2), 1f, 0.2f, 5, 75);
		sightDistance = 500;//to be refined
		
		moneyAmount = 30;
	}

	@Override
	protected Vector2 decideMove() {		
		return super.pursuitMoveWithSight();
	}
}
