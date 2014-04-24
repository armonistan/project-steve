package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Brute extends Enemy{
	public Brute(Vector2 position) {
		super(position, new Vector2(11, 3), new Vector2(2, 2), 1f, 0.2f, 5);
		sightDistance = 500;//to be refined
	}

	@Override
	protected Vector2 decideMove() {		
		return super.pursuitMoveWithSight();
	}
}
