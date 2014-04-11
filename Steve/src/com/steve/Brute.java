package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Brute extends Enemy{

	public Brute(Vector2 position) {
		super(position, new Vector2(11, 3), new Vector2(2, 2), 1f, 0.2f, 5);
	}

	protected void decideMove() {
		//TODO: This can be more sophisticated.
		/*if (avatar.getRotation() == SteveDriver.UP || avatar.getRotation() == SteveDriver.DOWN) {
			move(new Vector2(SteveDriver.random.nextBoolean() ? 1 : -1, 0));
		}
		else {
			move(new Vector2(0, SteveDriver.random.nextBoolean() ? 1 : -1));
		}*/
		
		Vector2 directionToSnake = new Vector2(avatar.getX() + avatar.getOriginX(), avatar.getY() + avatar.getOriginY())
			.sub(new Vector2(SteveDriver.snake.getHeadPosition().x, SteveDriver.snake.getHeadPosition().y));
		float angleToSnake = (float)Math.atan2(directionToSnake.y, directionToSnake.x) / (float)Math.PI * 180 + 180;
		
		if (angleToSnake > 45 && angleToSnake <= 135) {
			move(new Vector2(0, 1));
		}
		else if (angleToSnake > 135 && angleToSnake <= 225) {
			move(new Vector2(-1, 0));
		}
		else if (angleToSnake > 225 && angleToSnake <= 315) {
			move(new Vector2(0, -1));
		}
		else {
			move(new Vector2(1, 0));
		}
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
