package com.steve;

public class SpecialistPickUp extends Pickup {
	SpecialistPickUp(float xPos, float yPos){
		super(xPos, yPos, 10 * 16, 0);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(2);
	}
}
