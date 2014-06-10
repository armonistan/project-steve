package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;

public class AntiSpiral extends Enemy{
	private float slowMoveTime;
	private float fastMoveTime;
	private float slowChangeTime;
	private float fastChangeTime;
	private float changeTimer;
	
	public AntiSpiral(float x, float y) {
		super(x, y, 15, 3, 2, 2, 1.2f, 0.4f, 3, 75);
		sightDistance = 500;//to be refined
		
		moneyAmount = 30;
		fastMoveTime = 0.05f;
		slowMoveTime = super.moveTime;
		slowChangeTime = 3;
		fastChangeTime = 0.5f;
		changeTimer = 0;
	}

	public void update() {
		
		if (changeTimer > ((moveTime == slowMoveTime) ? slowChangeTime : fastChangeTime)) {
			moveTime = (moveTime == slowMoveTime) ? fastMoveTime : slowMoveTime;
			changeTimer = 0;
		}
		else {
			changeTimer += Gdx.graphics.getRawDeltaTime();
		}
		super.update();
	}
	
	@Override
	protected Vector2 decideMove() {
		if (moveTime == slowMoveTime) {
			return super.pursuitMoveWithSight();
		}
		else {
			return super.flyMove();
		}
	}
}
