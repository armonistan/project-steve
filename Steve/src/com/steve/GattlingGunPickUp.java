package com.steve;

public class GattlingGunPickUp extends Pickup {
	public GattlingGunPickUp(float x, float y) {
		super(x, y, 8 * 16, 3 * 16);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.mountUpgrade(0);
	}
}