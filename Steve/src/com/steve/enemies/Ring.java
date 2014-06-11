package com.steve.enemies;

import com.badlogic.gdx.math.Vector2;
import com.steve.base.Enemy;
import com.steve.pickups.Apple;
import com.steve.stages.Field;

public class Ring extends Enemy {

	public Ring(float x, float y) {
		super(x, y, 11, 7, 1, 2, 1f, 1, 10, 0);
		
		moneyAmount = 20;
	}
	
	@Override
	protected Vector2 decideMove() {
		return super.randomMove();
	}
	
	@Override
	public void kill() {
		//TODO: Make this better.
		Field.pickups.add(new Apple(avatar.getX() / 16, avatar.getY() / 16));
		
		super.kill();
	}
}
