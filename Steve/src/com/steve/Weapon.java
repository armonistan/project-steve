package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Weapon extends Sprite{
	
	Enemy target;
	int shootSpeed = 100;
	int shootCounter = 0;
	
	public Weapon(float x, float y, int atlasX, int atlasY) {
	super(new TextureRegion(SteveDriver.atlas, atlasX, atlasY, 16, 16));
		
		this.setPosition(x, y);
	}
	
	public void update(float x, float y){
		this.setPosition(x, y);
		this.weaponBehavior();
	}
	
	protected void weaponBehavior(){
		//base behavior
		targetEnemy();
		if(target != null){
			turn();
			if(shootCounter > shootSpeed)
				shoot();
		}
		shootCounter += 1;
	}
	
	protected void targetEnemy(){
		
		float minDistance = Float.POSITIVE_INFINITY;
		
		for(Enemy e : SteveDriver.field.enemies){
			float enemyX = e.getXPosition();
			float enemyY = e.getYPosition();
			
			float distance = CollisionHelper.distanceSquared(this.getX(), this.getY(), enemyX, enemyY);
			
			if(minDistance > distance){
				minDistance = distance;
				target = e;
			}
		}
	}
	
	protected void turn(){
		float deltaY = this.getX() - target.getXPosition();
		float deltaX = this.getY() - target.getYPosition();
		
		float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
		degrees += 90;
		
		this.setRotation(degrees);
	}
	
	protected void shoot(){
		
	}
}
