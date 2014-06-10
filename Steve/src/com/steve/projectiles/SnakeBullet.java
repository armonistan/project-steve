package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.base.Projectile;

public class SnakeBullet extends Projectile {
	private final static float SPEED = 100;
	
	public SnakeBullet(float x, float y, int level) {
		super(x, y, level == 0 ? 11 : 13, 0, 1, 1, 10, true);
		
		speed = SPEED;
	}
}