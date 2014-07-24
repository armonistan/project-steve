package com.steve.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.bosses.Carrier;
import com.steve.bosses.Razorbull;
import com.steve.helpers.CollisionHelper;

public class Weapon extends Sprite{
	
	protected Enemy target;
	protected float shootSpeed = 2;
	protected float shootCounter = 0;
	protected float bulletDamage = 0;
	protected float range;
	float damage;//TODO use it?
	protected int atlasX;
	protected int atlasY;
	protected boolean isAimed;
	protected boolean isUpgraded;
	protected Sound shootSound1;
	protected Sound shootSound2;
	protected Sound shootSound3;	
	
	public Weapon(float x, float y, int atlasX, int atlasY) {
	super(new TextureRegion(SteveDriver.atlas, atlasX, atlasY, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
		this.atlasX = atlasX;
		this.atlasY = atlasY;
		range = 300;//override range if need be
		this.setPosition(x, y);
		isAimed = false;
		isUpgraded = false;
	}
	
	public void update(float x, float y){
		shootCounter += Gdx.graphics.getRawDeltaTime();
		this.setPosition(x, y);
		this.weaponBehavior();
	}
	
	protected void weaponBehavior(){
		//base behavior
		targetEnemy();
		if(target != null && SteveDriver.field.enemies.size() > 0){
			turn();
			if(shootCounter > shootSpeed){
				if(isInRange() && isAimed)
					shoot();
				else
					shootCounter = shootSpeed;
			}
		}
	}
	
	protected void targetEnemy(){
		float minDistance = Float.POSITIVE_INFINITY;
		target = null;
		
		for(Enemy e : SteveDriver.field.enemies){		
			float enemyX = e.getXPosition();
			float enemyY = e.getYPosition();
			
			float distance = (CollisionHelper.distanceSquared(this.getX(), this.getY(), enemyX, enemyY));
			
			if (distance < range * range) {
				if (e.getClass() == Carrier.class || e.getClass() == Razorbull.class) {
					target = e;
					break;
				}
				
				if(minDistance > distance && distance < range * range){
					minDistance = distance;
					target = e;
				}
			}
		}
	}
	
	protected void turn(){
		float deltaX = target.getXPosition() - this.getX();
		float deltaY = target.getYPosition() - this.getY();
		
		//System.out.println(MathUtils.radiansToDegrees * MathUtils.atan2(3, 4));
		//System.out.println(MathUtils.radiansToDegrees * MathUtils.atan2(-3, 4));
		//System.out.println(MathUtils.radiansToDegrees * MathUtils.atan2(-3, -4));
		//System.out.println(MathUtils.radiansToDegrees * MathUtils.atan2(3, -4));
		
		int degrees = (int)(MathUtils.radiansToDegrees * MathUtils.atan2(deltaY, deltaX));
		//System.out.println("degrees: " + degrees);
		
		//image offset
		degrees += 270;

		float deltaPositiveDegrees = Math.abs(degrees - this.getRotation() + 360)%360;
		float deltaNegativeDegrees = Math.abs(this.getRotation() - degrees + 360)%360;
	
		if(deltaPositiveDegrees < deltaNegativeDegrees){
			if(deltaPositiveDegrees < 3f)
				this.isAimed = true;
			else{
				this.isAimed = false;
				this.setRotation((this.getRotation() +365)%360);
			}
		}
		else{
			if(deltaNegativeDegrees < 3f){
				this.isAimed = true;
			}
			else{
				this.isAimed = false;
				this.setRotation((this.getRotation() +355)%360);
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
		return distance <= (range*range);
	}
	
	public boolean isUpgraded() {
		return isUpgraded;
	}

	public void setUpgraded(boolean isUpgraded) {
		this.isUpgraded = isUpgraded;
	}
}
