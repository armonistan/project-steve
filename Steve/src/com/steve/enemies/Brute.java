package com.steve.enemies;

import com.badlogic.gdx.math.Vector2;
import com.steve.base.Enemy;

public class Brute extends Enemy{
	public Brute(float x, float y) {
		super(x, y, 11, 3, 2, 2, 0.1f, 0.2f, 2, 50, 250);
		sightDistance = 500;//to be refined
		
		moneyAmount = 150;
	}

	@Override
	protected Vector2 decideMove() {		
		return super.pursuitMoveWithSight();
	}
}
