package com.steve;

import com.badlogic.gdx.math.Vector2;

public class Pinecone extends Projectile {

	public Pinecone(float x, float y) {
		super(x, y, new Vector2(16, 0), new Vector2(1, 1), 0.0f, false,
				0, 0);
		
		speed = 10;
	}

}
