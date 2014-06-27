package com.steve.enemies;

import com.badlogic.gdx.math.Vector2;
import com.steve.base.Enemy;

public class TutorialSlug extends Enemy {

	public TutorialSlug(float x, float y) {
		super(x, y, 11, 1, 2, 2, 0.5f, 0.5f, 2, 50, 50);
		
		moneyAmount = 0;
	}
	
	@Override
	protected Vector2 decideMove() {
		return super.randomMove();
	}
}