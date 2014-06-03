package com.steve.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.helpers.CollisionHelper;

public class Weapon extends Sprite{
	
	protected Enemy target;
	protected int shootSpeed = 100;
	protected int shootCounter = 0;
	protected float range;
	float damage;//TODO use it?
	protected int atlasX;
	protected int atlasY;
	boolean isAimed;
	protected boolean isUpgraded;
	
	public Weapon(float x, float y, int atlasX, int atlasY) {
	super(new TextureRegion(SteveDriver.atlas, atlasX, atlasY, SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		this.atlasX = atlasX;
		this.atlasY = atlasY;
		range = 300;//override range if need be
		this.setPosition(x, y);
		isAimed = false;
		isUpgraded = false;
	}
	
	public void update(float x, float y){
		this.setPosition(x, y);
		this.weaponBehavior();
	}
	
	protected void weaponBehavior(){
		//base behavior
		targetEnemy();
		if(target != null && SteveDriver.field.enemies.size() > 0){
			turn();
			if(shootCounter > shootSpeed && isInRange() && isAimed)
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
		
		float deltaPositiveDegrees = (degrees - this.getRotation() + 360)%360;
		float deltaNegativeDegrees = (this.getRotation() - degrees + 360)%360;
		
		if(deltaPositiveDegrees < deltaNegativeDegrees){
			if(deltaPositiveDegrees < 3)
				this.isAimed = true;
			else{
				this.isAimed = false;
				this.setRotation(((this.getRotation() +361)%360));
			}
		}
		else{
			if(deltaNegativeDegrees < 3)
				this.isAimed = true;
			else{
				this.isAimed = false;
				this.setRotation(((this.getRotation() +359)%360));
			}
		}
	}
	
	protected void shoot(){
		//override
	}
	
	public void upgrade(){
		isUpgraded = true;
	}
	
	protected boolean isInRange(){
		float distance = CollisionHelper.distanceSquared(this.getX(),this.getY(), target.getXPosition(), target.getYPosition()); 
		return distance <= range * range;
	}
	
	public boolean isUpgraded() {
		return isUpgraded;
	}

	public void setUpgraded(boolean isUpgraded) {
		this.isUpgraded = isUpgraded;
	}
}
