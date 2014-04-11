package com.steve;

public class SpecialistPickUp extends PickUp {
	SpecialistPickUp(int xPos, int yPos){
		super(xPos, yPos, 10 * 16, 3 * 16);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(2);
	}
}
