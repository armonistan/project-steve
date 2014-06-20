package com.steve.projectiles;

import com.steve.base.Projectile;

public class Acorn extends Projectile {

	public Acorn(float x, float y) {
		super(x, y, 15, 0, 1, 1, 10, false);
		speed = 100;
		CheckBulletTime();
	}
}
