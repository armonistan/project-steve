package com.steve.enemies;

import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;

public class Slug extends Enemy {

	public Slug(float x, float y) {
		super(x, y, 11, 1, 2, 2, 0.5f, 0.5f, 2, 80, 50);
		this.knowledgeDistance = 500;
		moneyAmount = 125;
	}
	
	@Override
	protected Vector2 decideMove() {
		if(SteveDriver.random.nextInt(100) < 40)
			return super.pursuitMoveWithKnowledge();
		else{
			return super.randomMove();
		}
	}
}