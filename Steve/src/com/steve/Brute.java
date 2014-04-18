package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Brute extends Enemy{

	public Brute(float x, float y) {
		super(x, y, new Vector2(11, 3), new Vector2(2, 2), 1f, 0.2f, 5);
	}

	protected void decideMove() {
		super.followSnake();
	}
	
	protected void update() {
		super.update();
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(s.getBoundingRectangle(), avatar.getBoundingRectangle())) {
				SteveDriver.snake.changeHungerByPercent(1f);
				
				//TODO: Replace with something better.
				kill();
				break;
			}
		}
	}
}
