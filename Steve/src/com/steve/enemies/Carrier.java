package com.steve.enemies;

import com.badlogic.gdx.math.Vector2;
import com.steve.base.Enemy;

public class Carrier extends Enemy {

	public Carrier(float x, float y) {
		super(x, y, 36, 0, 28, 8, 0.5f, 0.5f, 1, 50, 50);
		
		moneyAmount = 50000;
	}
	
	@Override
	protected Vector2 decideMove() {
		this.moveTimer = 0;
		return super.randomMove();
	}
}