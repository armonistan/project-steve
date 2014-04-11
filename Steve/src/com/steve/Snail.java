package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Snail extends Enemy {

	public Snail(Vector2 position) {
		super(position, new Vector2(11, 1), new Vector2(2, 2), 0.5f, 0.5f, 2);
	}
	
	protected void decideMove() {
		super.moveRandomly();
	}

	protected void update() {
		super.update();
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(s.getBoundingRectangle(), avatar.getBoundingRectangle())) {
				SteveDriver.snake.changeHungerByPercent(0.25f);
				
				//TODO: Replace with something better.
				kill();
				break;
			}
		}
	}
}