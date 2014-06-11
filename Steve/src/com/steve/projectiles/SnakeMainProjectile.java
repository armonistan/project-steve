package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeMainProjectile extends Projectile {
	private final static float SPEED = 50;
	private final static float MAX_SPEED = 250;
	
	public SnakeMainProjectile(float x, float y, float dx, float dy, int level) {
		super(x, y, 19, 0, 1, 1, 100*SteveDriver.constants.get("fireDamage"), true,
				100 * SteveDriver.constants.get("fireRange") * SteveDriver.constants.get("fireRange"));
	}
	
	@Override
	public void update() {
		directionX += (SteveDriver.random.nextFloat() - 0.5f) * 3f;
		directionY += (SteveDriver.random.nextFloat() - 0.5f) * 3f;
		
		if ((directionX * directionX + directionY * directionY) < MAX_SPEED * MAX_SPEED) {
			directionX *= 1.05f;
			directionY *= 1.05f;
		}
		
		super.update();
	}
}
