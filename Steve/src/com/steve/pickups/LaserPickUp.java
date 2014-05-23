package com.steve.pickups;

import com.steve.Snake;
import com.steve.base.Pickup;

public class LaserPickUp extends Pickup {
	public LaserPickUp(float xPos, float yPos){
		super(xPos, yPos, 9 * 16, 0);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(1);
	}
}
