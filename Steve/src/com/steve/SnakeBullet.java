package com.steve;

import com.badlogic.gdx.math.Vector2;

public class SnakeBullet extends Projectile {

	public SnakeBullet(Vector2 position, Vector2 direction, int level) {
		super(position, level == 0 ? new Vector2(11, 0) : new Vector2(13, 0), new Vector2(1, 1), 10, true,
				direction);
	}

}
