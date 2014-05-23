package com.steve.pickups;

import com.badlogic.gdx.math.Rectangle;
import com.steve.Snake;
import com.steve.base.Pickup;

public class Apple extends Pickup {
	public Apple(float x, float y) {
		super(x, y, 4 * 16, 0);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.addBody();
		snake.addMoney(25); //TODO: Temp
	}
}