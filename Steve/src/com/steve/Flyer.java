package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Flyer extends Enemy{
	private float shootTime;
	private float shootTimer;
	
	private Sprite propeller;
	private float spinTime;
	private float spinTimer;
	private int propellerFrame;
	private int numPropellerFrames;
	private Vector2 propellerAtlasPosition;
	private Vector2 propellerAtlasBounds;

	public Flyer(Vector2 position) {
		super(position, new Vector2(11, 5), new Vector2(2, 2), 1f, 0.5f, 2);
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
		if (shootTimer >= shootTime) {
			SteveDriver.field.projectiles.add(new Pinecone(new Vector2(avatar.getX() + SteveDriver.TEXTURE_WIDTH / 2, avatar.getY() + SteveDriver.TEXTURE_LENGTH / 2),
					CollisionHelper.directionVectorFromAngle(avatar.getRotation() - 270).scl(100)));
			
			shootTimer = 0;
		}
		else {
			shootTimer += Gdx.graphics.getRawDeltaTime();
		}
	}

	public void decideMove() {
		followSteve();
	}
	
	protected void updatePropeller() {
		propeller.setRegion(new TextureRegion(SteveDriver.atlas,
				(int)propellerAtlasPosition.x * SteveDriver.TEXTURE_WIDTH + SteveDriver.TEXTURE_WIDTH * propellerFrame * (int)propellerAtlasBounds.x,
				(int)propellerAtlasPosition.y * SteveDriver.TEXTURE_LENGTH,
				(int)propellerAtlasBounds.x* SteveDriver.TEXTURE_WIDTH,
				(int)propellerAtlasBounds.y * SteveDriver.TEXTURE_LENGTH));
	}
}
