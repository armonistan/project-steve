package com.steve.pickups;

import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class GatlingGunPickUp extends Pickup{
	public GatlingGunPickUp(float xPos, float yPos){
		super(xPos, yPos, 8 * SteveDriver.TEXTURE_SIZE, 0, 0);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(0);
	}
}
