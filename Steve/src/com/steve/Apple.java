package com.steve;

public class Apple extends PickUp {
	public Apple(float x, float y) {
		super(x, y, 4 * 16, 0);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		snake.addBody();
	}
}