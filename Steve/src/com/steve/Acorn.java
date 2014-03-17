package com.steve;

import com.badlogic.gdx.math.Vector2;

public class Acorn extends Projectile {

	public Acorn(Vector2 position, Vector2 direction) {
		super(position, new Vector2(15, 0), new Vector2(1, 1), 0.25f, false,
				direction);
	}

}
