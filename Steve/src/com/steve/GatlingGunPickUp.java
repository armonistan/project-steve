package com.steve;

public class GatlingGunPickUp extends PickUp{
	GatlingGunPickUp(int xPos, int yPos){
		super(xPos, yPos, 8 * 16, 3 * 16);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(0);
	}
}
