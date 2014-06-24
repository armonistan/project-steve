package com.steve.projectiles;

import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeLaser extends Projectile {
	private final static float SPEED = 500;
	
	public SnakeLaser(float x, float y, int level) {
		super(x, y, level == 0 ? 17 : 18, 0, 1, 1, (30+SteveDriver.snake.getSnakeTier()*SteveDriver.snakeTierWeaponDamageModifier)*SteveDriver.constants.get("fireDamage"), true, 100*SteveDriver.constants.get("fireRange"));
		speed = SPEED;
	}
}
