package com.steve.projectiles;

import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeRocket extends Projectile {
	private final static float SPEED = 120;
	private final static float MAX_SPEED = 650;
	
	public SnakeRocket(float x, float y, int level, float bulletDamage) {
		super(x, y, level == 0 ? 12 : 14, 0, 1, 1, 
				bulletDamage, true, 70*((SteveDriver.constants.get("fireRange") - 1)/2 + 1));
		speed = SPEED;
	}
	
	@Override
	public void update() {
		directionX += (SteveDriver.random.nextFloat() - 0.5f) * 3;
		directionY += (SteveDriver.random.nextFloat() - 0.5f) * 3;
		
		if ((directionX * directionX + directionY * directionY) < MAX_SPEED * MAX_SPEED) {
			directionX *= 1.04f;
			directionY *= 1.04f;
		}
		
		super.update();
	}
}
