package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeMainProjectile extends Projectile {
	private final static float SPEED = 100;
	private final static float MAX_SPEED = 550;
	private boolean isRocket;
	
	public SnakeMainProjectile(float x, float y, int level) {
		super(x, y, 16, 0, 1, 1, 40 * SteveDriver.constants.get("fireDamage"), true,
				100 * SteveDriver.constants.get("fireRange"));
		isRocket = false;
		speed = SPEED;
	}
	
	public SnakeMainProjectile(float x, float y) {
		super(x, y, 16, 0, 1, 1, 40 * SteveDriver.constants.get("fireDamage"), true,
				100 * SteveDriver.constants.get("fireRange"));
		isRocket = false;
		speed = SPEED;
		ignoreCollisions = true;
	}
	
	public SnakeMainProjectile(float x, float y, boolean isRocket){
		super(x, y, 16, 0, 1, 1, 40 * SteveDriver.constants.get("fireDamage"), true,
				100 * SteveDriver.constants.get("fireRange"));
		
		speed = SPEED;
		this.isRocket = isRocket;
	}
	
	@Override
	public void update() {
		if(isRocket){
			directionX += (SteveDriver.random.nextFloat() - 0.5f) * 3;
			directionY += (SteveDriver.random.nextFloat() - 0.5f) * 3;
		
			if ((directionX * directionX + directionY * directionY) < MAX_SPEED * MAX_SPEED) {
				directionX *= 1.03f;
				directionY *= 1.03f;
			}
		}
		
		super.update();
	}
}
