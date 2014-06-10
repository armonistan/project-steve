package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.base.Projectile;

public class Pinecone extends Projectile {

	public Pinecone(float x, float y) {
		super(x, y, 16, 0, 1, 1, 0.0f, false);
		
		speed = 300;
	}

}
