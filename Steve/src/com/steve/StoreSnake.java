package com.steve;

import com.badlogic.gdx.Gdx;

public class StoreSnake extends Snake {
	public StoreSnake() {
		super(100, SteveDriver.TEXTURE_SIZE / 2);
		
		nextDirection = SteveDriver.VRIGHT;
		nextRotation = SteveDriver.RIGHT;
	}

	@Override
	public void update()
	{
		if (bombsAwayTime > bombsAwayTimer) {
			bombsAwayTime = 0f;
			killThemAll();
		}
		
		if (glue) {
			layGlue();
		}
		
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
		//System.out.println("current: " + segments.size());
	}
}
