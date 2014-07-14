package com.steve.projectiles;

import com.steve.base.Projectile;

public class Tree extends Projectile {

	public Tree(float x, float y) {
		super(x, y, 24, 0, 3, 2, 50, false);
		
		speed = 350;
		CheckBulletTime();
	}
}
