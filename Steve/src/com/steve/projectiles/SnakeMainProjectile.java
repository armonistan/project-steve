package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeMainProjectile extends Projectile {
	private final static float SPEED = 150;
	
	public SnakeMainProjectile(float x, float y, int level) {
		super(x, y, 19, 0, 1, 1, 40 * SteveDriver.constants.get("fireDamage"), true,
				100 * SteveDriver.constants.get("fireRange"));
		
		speed = SPEED;
	}
}
