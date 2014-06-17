package com.steve.pickups;

import com.steve.Snake;
import com.steve.base.Pickup;

public class SpecialistPickup extends Pickup {
	public SpecialistPickup(float xPos, float yPos){
		super(xPos, yPos, 10 * 16, 0);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(2);
	}
}
