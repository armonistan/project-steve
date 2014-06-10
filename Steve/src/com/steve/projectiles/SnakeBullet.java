package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeBullet extends Projectile {
	private final static float SPEED = 100;
	
	public SnakeBullet(float x, float y, int level) {
		super(x, y, level == 0 ? new Vector2(11, 0) : new Vector2(13, 0), new Vector2(1, 1), 10*SteveDriver.constants.get("fireDamage"), true,
				dx * SPEED, dy * SPEED, 100*SteveDriver.constants.get("fireRange"));
		speed = SPEED;
	}
}