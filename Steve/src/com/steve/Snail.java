package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Snail extends Enemy {

	
	public Snail(Vector2 position) {
		super(position, new Vector2(11, 1), new Vector2(2, 2), 0.5f, 0.5f, 2);
	}
	
	@Override
	protected Vector2 decideMove() {
		return super.randomMove();
	}
}