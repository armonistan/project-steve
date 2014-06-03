package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.base.Projectile;

public class SnakeLaser extends Projectile {
	private final static float SPEED = 500;
	
	public SnakeLaser(float x, float y, float dx, float dy, int level) {
		super(x, y, level == 0 ? new Vector2(17, 0) : new Vector2(18, 0), new Vector2(1, 1), 30, true,
				dx * SPEED, dy * SPEED);
	}
}
