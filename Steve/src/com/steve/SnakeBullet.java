package com.steve;

import com.badlogic.gdx.math.Vector2;

public class SnakeBullet extends Projectile {

	public SnakeBullet(float x, float y, float dx, float dy, int level) {
		super(x, y, level == 0 ? new Vector2(11, 0) : new Vector2(13, 0), new Vector2(1, 1), 10, true,
				dx, dy);
	}

}
