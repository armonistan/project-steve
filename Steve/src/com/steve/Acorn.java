package com.steve;

import com.badlogic.gdx.math.Vector2;

public class Acorn extends Projectile {

	public Acorn(float x, float y, float dx, float dy) {
		super(x, y, new Vector2(15, 0), new Vector2(1, 1), 0.25f, false,
				dx, dy);
		
		speed = 100;
	}
}
