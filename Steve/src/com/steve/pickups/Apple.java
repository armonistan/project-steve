package com.steve.pickups;

import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class Apple extends Pickup {
	public Apple(float x, float y) {
		super(x, y, 4 * 16, 0);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.addBody();
		snake.addMoney(2500000); //TODO: Temp
		SteveDriver.summary.appleScore += 2500;
	}
}