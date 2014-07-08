package com.steve;

import com.badlogic.gdx.Gdx;

public class MenuSnake extends Snake {
	public MenuSnake() {
		super(100, SteveDriver.TEXTURE_SIZE / 2);
		for (int i = 0; i < 5; i++) {
			this.addBody();
			this.mountUpgrade(SteveDriver.random.nextInt(3));
			animate();
		}
	}

	@Override
	public void update()
	{
		//update all the segments.
		if (timer >= timeBetweenTurn) {
			if (headPosition.x > 150 * SteveDriver.TEXTURE_SIZE) {
				if (segments.get(0).getRotation() == SteveDriver.RIGHT) {
					nextRotation = SteveDriver.DOWN;
					nextDirection = SteveDriver.VDOWN;
				}
				else {
					nextRotation = SteveDriver.LEFT;
					nextDirection = SteveDriver.VLEFT;
				}
			}
			else if (headPosition.x < 50 * SteveDriver.TEXTURE_SIZE) {
				if (segments.get(0).getRotation() == SteveDriver.LEFT) {
					nextRotation = SteveDriver.UP;
					nextDirection = SteveDriver.VUP;
				}
				else {
					nextRotation = SteveDriver.RIGHT;
					nextDirection = SteveDriver.VRIGHT;
				}
			}
			
			move();
			
			rotateTail();
			animate();
			timer = 0;
		}
		
		updateWeapons();
		updateTimers(Gdx.graphics.getRawDeltaTime());
	}
}
