package com.steve;

public class LaserPickUp extends PickUp {
	LaserPickUp(int xPos, int yPos){
		super(xPos, yPos, 9 * 16, 3 * 16);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(1);
	}
}
