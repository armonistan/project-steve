package com.steve.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.projectiles.Pinecone;

public class Flyer extends Enemy{
	private Sprite propeller;
	private float spinTime;
	private float spinTimer;
	private int propellerFrame;
	private int numPropellerFrames;
	private int propellerAtlasPositionX;
	private int propellerAtlasPositionY;
	private int propellerAtlasBoundsX;
	private int propellerAtlasBoundsY;

	public Flyer(float x, float y) {
		super(x, y, 11, 5, 2, 2, .3f, 0.5f, 2, 50, 300);
		shootTime = 1f;
		
		propellerAtlasPositionX = 15;
		propellerAtlasPositionY = 5;
		propellerAtlasBoundsX = 2;
		propellerAtlasBoundsY = 2;
		propeller = new Sprite(SteveDriver.atlas, propellerAtlasPositionX * SteveDriver.TEXTURE_SIZE, propellerAtlasPositionY * SteveDriver.TEXTURE_SIZE,
				propellerAtlasBoundsX * SteveDriver.TEXTURE_SIZE, propellerAtlasBoundsY * SteveDriver.TEXTURE_SIZE);
		spinTime = 0.01f;
		numPropellerFrames = 3;
		super.knowledgeDistance = 500;
		super.moneyAmount = 750;			
	}
	
	@Override
	public void draw() {
		super.draw();
		
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
		propeller.draw(SteveDriver.batch);
	}
	
	@Override
	public void update() {
		super.decideShoot();
		super.update();
	}
	
	@Override
	public void shoot(float dx, float dy) {
		super.addProjectile(new Pinecone(avatar.getX() + SteveDriver.TEXTURE_SIZE / 2, avatar.getY() + SteveDriver.TEXTURE_SIZE / 2), dx, dy);
	}
	
	protected void updatePropeller() {
		propeller.setRegion(
				propellerAtlasPositionX * SteveDriver.TEXTURE_SIZE + SteveDriver.TEXTURE_SIZE * propellerFrame * propellerAtlasBoundsX,
				propellerAtlasPositionY * SteveDriver.TEXTURE_SIZE,
				propellerAtlasBoundsX * SteveDriver.TEXTURE_SIZE,
				propellerAtlasBoundsY * SteveDriver.TEXTURE_SIZE);
	}
	
	@Override
	protected Vector2 decideMove() {		
		int sightID = -1;
		
		for(Sprite s: SteveDriver.snake.getSegments()){
			float deltaY = this.avatar.getY() - s.getY();
			float deltaX = this.avatar.getX() - s.getX();
			sightID = super.doesSee(deltaX, deltaY);
			
			if(sightID != -1)
				break;
		}
		
		for(Sprite s: SteveDriver.snake.getSegments()){
			float deltaY = this.avatar.getY() - s.getY();
			float deltaX = this.avatar.getX() - s.getX();
			sightID = super.doesSee(deltaX, deltaY);
			
			if(sightID != -1)
				break;
		}
		
		//if i dont see the fucker find him
		if(sightID == -1)
			return super.pursuitMoveWithKnowledge();
		//i aint moving
		else{
			switch(sightID){
			case SteveDriver.RIGHT_ID:
				this.avatar.setRotation(SteveDriver.RIGHT);
				return SteveDriver.VRIGHT;	
			
			case SteveDriver.UP_ID:
				this.avatar.setRotation(SteveDriver.UP);
				return SteveDriver.VUP;
			
			case SteveDriver.LEFT_ID:
				this.avatar.setRotation(SteveDriver.LEFT);
				return SteveDriver.VLEFT;
			
			case SteveDriver.DOWN_ID:
				this.avatar.setRotation(SteveDriver.DOWN);
				return SteveDriver.VDOWN;
			}
			return null;
		}
	}
}
