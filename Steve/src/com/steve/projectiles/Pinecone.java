package com.steve.projectiles;

import com.steve.base.Projectile;

public class Pinecone extends Projectile {

	public Pinecone(float x, float y) {
		super(x, y, 16, 0, 1, 1, 20, false);
		
		speed = 300;
	}

}
