package com.steve.pickups;

import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class SpecialistPickUp extends Pickup {
	public SpecialistPickUp(float xPos, float yPos){
		super(xPos, yPos, 10 * SteveDriver.TEXTURE_SIZE, 0, 0);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(2);
	}
}
