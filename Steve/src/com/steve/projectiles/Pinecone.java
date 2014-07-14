package com.steve.projectiles;

import com.steve.base.Projectile;

public class Pinecone extends Projectile {

	public Pinecone(float x, float y) {
		super(x, y, 16, 0, 1, 1, 40, false);
		
		speed = 400;
		CheckBulletTime();
	}

}
