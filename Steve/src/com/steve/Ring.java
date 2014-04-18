package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Ring extends Enemy {

	public Ring(float x, float y) {
		super(x, y, new Vector2(11, 7), new Vector2(1, 2), 1f, 1, 10);
	}
	
	public void update() {
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(s.getBoundingRectangle(), avatar.getBoundingRectangle())) {
				
				//TODO: Replace with something better.
				kill();
				break;
			}
		}
	}

	protected void decideMove() {
		if (SteveDriver.random.nextBoolean()) {
			move(SteveDriver.random.nextBoolean() ? 1 : -1, 0);
		}
		else {
			move(0, SteveDriver.random.nextBoolean() ? 1 : -1);
		}
	}
	
	public void kill() {
		//TODO: Make this better.
		SteveDriver.field.pickups.add(new Apple(avatar.getX() / 16, avatar.getY() / 16));
		
		super.kill();
	}
}
