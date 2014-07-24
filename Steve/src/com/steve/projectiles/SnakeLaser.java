package com.steve.projectiles;

import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeLaser extends Projectile {
	private final static float SPEED = 700;
	
	public SnakeLaser(float x, float y, int level, float bulletDamage) {
		super(x, y, level == 0 ? 17 : 18, 0, 1, 1, 
				bulletDamage, true, 100*SteveDriver.constants.get("fireRange"));
		speed = SPEED;
	}
}
