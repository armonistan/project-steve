package com.steve.enemys;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.pickups.Apple;

public class Ring extends Enemy {

	public Ring(float x, float y) {
		super(x, y, new Vector2(11, 7), new Vector2(1, 2), 1f, 1, 10, 0);
		
		moneyAmount = 20;
	}
	
	@Override
	protected Vector2 decideMove() {
		return super.randomMove();
	}
	
	@Override
	public void kill() {
		//TODO: Make this better.
		SteveDriver.field.pickups.add(new Apple(avatar.getX() / 16, avatar.getY() / 16));
		
		super.kill();
	}
}
