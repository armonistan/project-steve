package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.base.Projectile;

public class Acorn extends Projectile {

	public Acorn(float x, float y) {
		super(x, y, 15, 0, 1, 1, 0.25f, false);
		speed = 100;
	}
}
