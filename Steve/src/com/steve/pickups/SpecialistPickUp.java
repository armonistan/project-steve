package com.steve.pickups;

import com.steve.Snake;
import com.steve.base.Pickup;

public class SpecialistPickUp extends Pickup {
	public SpecialistPickUp(float xPos, float yPos){
		super(xPos, yPos, 10 * 16, 0, 0);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(2);
	}
}
