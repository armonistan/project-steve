package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeLaser extends Projectile {
	private final static float SPEED = 500;
	
	public SnakeLaser(float x, float y, int level) {
		super(x, y, level == 0 ? 17 : 18, 0, 1, 1, 30*SteveDriver.constants.get("fireDamage"), true);
		speed = SPEED*SteveDriver.constants.get("fireRange");
	}
}
