package com.steve;

import com.badlogic.gdx.math.Vector2;

public class Ring extends Enemy {

	public Ring(Vector2 position) {
		super(position, new Vector2(11, 7), new Vector2(1, 2), 1f, 1, 10);
	}

	protected void decideMove() {
		if (SteveDriver.random.nextBoolean()) {
			move(new Vector2(SteveDriver.random.nextBoolean() ? 1 : -1, 0));
		}
		else {
			move(new Vector2(0, SteveDriver.random.nextBoolean() ? 1 : -1));
		}
	}
	
	public void kill() {
		//TODO: Make this better.
		SteveDriver.field.pickups.add(new Apple(avatar.getX() / 16, avatar.getY() / 16));
		
		super.kill();
	}
}
