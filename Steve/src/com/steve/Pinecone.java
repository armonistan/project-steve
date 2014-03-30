package com.steve;

import com.badlogic.gdx.math.Vector2;

public class Pinecone extends Projectile {

	public Pinecone(Vector2 position, Vector2 direction) {
		super(position, new Vector2(16, 0), new Vector2(1, 1), 0.0f, false,
				direction);
	}

}
