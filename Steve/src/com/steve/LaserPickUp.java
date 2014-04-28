package com.steve;

public class LaserPickUp extends PickUp {
	LaserPickUp(float xPos, float yPos){
		super(xPos, yPos, 9 * 16, 0);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(1);
	}
}
