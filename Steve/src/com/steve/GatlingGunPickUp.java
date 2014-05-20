package com.steve;

public class GatlingGunPickUp extends Pickup{
	GatlingGunPickUp(float xPos, float yPos){
		super(xPos, yPos, 8 * 16, 0);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(0);
	}
}
