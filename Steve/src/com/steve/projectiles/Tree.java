package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.base.Projectile;

public class Tree extends Projectile {

	public Tree(float x, float y, float dx, float dy) {
		super(x, y, 24, 0, 3, 2, 0.25f, false,
				dx, dy);
		
		speed = 200;
	}
}
