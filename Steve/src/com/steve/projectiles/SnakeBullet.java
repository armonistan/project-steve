package com.steve.projectiles;

import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeBullet extends Projectile {
	private final static float SPEED = 100;
	
	public SnakeBullet(float x, float y, int level) {
		super(x, y, level == 0 ? 11 : 13, 0, 1, 1, 10*SteveDriver.constants.get("fireDamage"), true, 100 * SteveDriver.constants.get("fireRange"));
		speed = SPEED;
	}
}