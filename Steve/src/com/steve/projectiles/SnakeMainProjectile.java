package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeMainProjectile extends Projectile {
	private final static float SPEED = 50;
	private final static float MAX_SPEED = 250;
	
	public SnakeMainProjectile(float x, float y, float dx, float dy, int level) {
		super(x, y, new Vector2(19, 0), new Vector2(1, 1), 100*SteveDriver.constants.get("fireDamage"), true,
				dx * SPEED, dy * SPEED, 100*SteveDriver.constants.get("fireRange")*SteveDriver.constants.get("fireRange"));
	}
	
	public void update() {
		/*direction.x += (SteveDriver.random.nextFloat() - 0.5f) * 3;
		direction.y += (SteveDriver.random.nextFloat() - 0.5f) * 3;
		
		if (direction.len2() < MAX_SPEED * MAX_SPEED) {
			direction.scl(1.05f);
		}*/
		
		super.update();
	}
}
