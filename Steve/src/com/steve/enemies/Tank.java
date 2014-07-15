package com.steve.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.projectiles.Acorn;

public class Tank extends Enemy {
	
	private Sprite face;

	public Tank(float x, float y) {
		super(x, y, 15, 1, 2, 2, 0.5f, 0.5f, 3, 50, 300);
		shootTime = 0.5f;
		sightDistance = 600;
		knowledgeDistance = 500;
		
		face = new Sprite(SteveDriver.atlas, 21 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE,
				2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE);
		
		shotCap = 10;
		hasShotCounter = 11;
		
		moneyAmount = 450;
	}
	
	@Override
	public void draw() {
		super.draw();
		
		if (hasShotCounter < shotCap) {
			face.setPosition(avatar.getX(), avatar.getY());
			face.setRotation(avatar.getRotation());
			face.draw(SteveDriver.batch);
			hasShotCounter++;
		}
	}
	
	@Override
	public void update(){
		super.decideShoot();
		super.update();
	}
	
	@Override
	public void shoot(float dx, float dy) {
		super.addProjectile(new Acorn(avatar.getX() + SteveDriver.TEXTURE_SIZE / 2, avatar.getY() + SteveDriver.TEXTURE_SIZE / 2), dx, dy);
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