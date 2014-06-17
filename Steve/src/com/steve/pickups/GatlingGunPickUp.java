package com.steve.pickups;

import com.steve.Snake;
import com.steve.base.Pickup;

public class GatlingGunPickup extends Pickup{
	public GatlingGunPickup(float xPos, float yPos){
		super(xPos, yPos, 8 * 16, 0);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(0);
	}
}
