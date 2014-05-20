package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Flyer extends Enemy{
	private Sprite propeller;
	private float spinTime;
	private float spinTimer;
	private int propellerFrame;
	private int numPropellerFrames;
	private Vector2 propellerAtlasPosition;
	private Vector2 propellerAtlasBounds;

	public Flyer(float x, float y) {
		super(x, y, new Vector2(11, 5), new Vector2(2, 2), 1f, 0.5f, 2);
		shootTime = 1f;
		
		propellerAtlasPosition = new Vector2(15, 5);
		propellerAtlasBounds = new Vector2(2, 2);
		propeller = new Sprite(SteveDriver.atlas, (int)propellerAtlasPosition.x * SteveDriver.TEXTURE_WIDTH, (int)propellerAtlasPosition.y * SteveDriver.TEXTURE_LENGTH,
				(int)propellerAtlasBounds.x * SteveDriver.TEXTURE_WIDTH, (int)propellerAtlasBounds.x * SteveDriver.TEXTURE_LENGTH);
		spinTime = 0.01f;
		numPropellerFrames = 3;
	}
	
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		if (spinTimer >= spinTime) {
			propellerFrame = (propellerFrame + 1) % numPropellerFrames;
			updatePropeller();
			
			spinTimer = 0;
		}
		else {
			spinTimer += Gdx.graphics.getRawDeltaTime();
		}
		
		propeller.setPosition(avatar.getX(), avatar.getY());
		propeller.setRotation(avatar.getRotation());
		propeller.draw(batch);
	}
	
	public void update() {
		super.shoot();
		super.update();
	}
	
	protected void updatePropeller() {
		propeller.setRegion(new TextureRegion(SteveDriver.atlas,
				(int)propellerAtlasPosition.x * SteveDriver.TEXTURE_WIDTH + SteveDriver.TEXTURE_WIDTH * propellerFrame * (int)propellerAtlasBounds.x,
				(int)propellerAtlasPosition.y * SteveDriver.TEXTURE_LENGTH,
				(int)propellerAtlasBounds.x* SteveDriver.TEXTURE_WIDTH,
				(int)propellerAtlasBounds.y * SteveDriver.TEXTURE_LENGTH));
	}
	
	@Override
	protected Vector2 decideMove() {		
		return super.pursuitMoveWithSight();
	}
}
