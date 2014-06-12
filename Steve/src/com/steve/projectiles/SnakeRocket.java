package com.steve.projectiles;

import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeRocket extends Projectile {
	private final static float SPEED = 50;
	private final static float MAX_SPEED = 250;
	
	public SnakeRocket(float x, float y, int level) {
		super(x, y, level == 0 ? 12 : 14, 0, 1, 1, 50*SteveDriver.constants.get("fireDamage"), true, 100*SteveDriver.constants.get("fireRange"));
		speed = SPEED;
	}
	
	@Override
	public void update() {
		directionX += (SteveDriver.random.nextFloat() - 0.5f) * 3;
		directionY += (SteveDriver.random.nextFloat() - 0.5f) * 3;
		
		if ((directionX * directionX + directionY * directionY) < MAX_SPEED * MAX_SPEED) {
			directionX *= 1.03f;
			directionY *= 1.03f;
		}
		
		super.update();
	}
}
