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
	
	private Rectangle front;
	
	public AntiSpiral(float x, float y) {
		super(x, y, 15, 3, 2, 2, 1.2f, 0.4f, 3, 75);
		sightDistance = 500;//to be refined
		
		moneyAmount = 30;
		fastMoveTime = 0.05f;
		slowMoveTime = super.moveTime;
		slowChangeTime = 3;
		fastChangeTime = 0.5f;
		changeTimer = 0;
		
		front = new Rectangle();
	}

	public void update() {
		//Update front rectangle
		if (avatar.getRotation() == SteveDriver.UP) {
			front.height = SteveDriver.TEXTURE_LENGTH;
			front.width = 2 * SteveDriver.TEXTURE_WIDTH;
			front.x = avatar.getX();
			front.y = avatar.getY() + SteveDriver.TEXTURE_LENGTH;
		}
		else if (avatar.getRotation() == SteveDriver.RIGHT) {
			front.height = 2 * SteveDriver.TEXTURE_LENGTH;
			front.width = SteveDriver.TEXTURE_WIDTH;
			front.x = avatar.getX() + 2 * SteveDriver.TEXTURE_WIDTH;
			front.y = avatar.getY();
		}
		else if (avatar.getRotation() == SteveDriver.DOWN) {
			front.height = SteveDriver.TEXTURE_LENGTH;
			front.width = 2 * SteveDriver.TEXTURE_WIDTH;
			front.x = avatar.getX();
			front.y = avatar.getY() - 2 * SteveDriver.TEXTURE_LENGTH;
		}
		else if (avatar.getRotation() == SteveDriver.LEFT) {
			front.height = 2 * SteveDriver.TEXTURE_LENGTH;
			front.width = SteveDriver.TEXTURE_WIDTH;
			front.x = avatar.getX() - SteveDriver.TEXTURE_WIDTH;
			front.y = avatar.getY();
		}
		
		TiledMapTileLayer blockerLayer = (TiledMapTileLayer)SteveDriver.field.map.getLayers().get(1);
		
		//blockerLayer.getCell(x, y);
		
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
