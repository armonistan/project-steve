package com.steve;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Turret extends Enemy{
	
	int shootSpeed = 100;
	int shootCounter = 0;
	float range;
	float damage;//TODO use it?
	int atlasX;
	int atlasY;
	boolean isAimed;
	boolean isUpgraded;

	public Turret(float x, float y) {
		super(x, y, new Vector2(15, 1), new Vector2(2, 2), 0.5f, 0.5f, 3);
		range = 300;//override range if need be
		this.avatar.setPosition(x, y);
		isAimed = false;
		isUpgraded = false;}
	
	@Override
	protected void move(){
		//doesnt move but rather turns
		turn();
		behavior();
	}
	
	protected void turn(){
		float deltaY = this.avatar.getX() - SteveDriver.snake.getHeadPosition().x;
		float deltaX = this.avatar.getY() - SteveDriver.snake.getHeadPosition().y;
		
		float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
		degrees += 90;
		
		float deltaPositiveDegrees = (degrees - this.avatar.getRotation() + 360)%360;
		float deltaNegativeDegrees = (this.avatar.getRotation() - degrees + 360)%360;
		
		if(deltaPositiveDegrees < deltaNegativeDegrees){
			if(deltaPositiveDegrees < 3)
				this.isAimed = true;
			else{
				this.isAimed = false;
				this.avatar.setRotation(((this.avatar.getRotation() +361)%360));
			}
		}
		else{
			if(deltaNegativeDegrees < 3)
				this.isAimed = true;
			else{
				this.isAimed = false;
				this.avatar.setRotation(((this.avatar.getRotation() +359)%360));
			}
		}
	}
	
	protected void behavior(){
		if(isInRange()){
			turn();
			if(shootCounter > shootSpeed && isAimed) {
				specialShoot();
			}
		}
		else
			this.avatar.setRotation(((this.avatar.getRotation() +359)%360));
		shootCounter += 1;
	}
	
	protected boolean isInRange(){
		float distance = CollisionHelper.distanceSquared(this.avatar.getX(),this.avatar.getY(), SteveDriver.snake.getHeadPosition().x, SteveDriver.snake.getHeadPosition().y); 
		return distance <= range * range;
	}
	
	protected void specialShoot(){
		//different angle
		float deltaY = this.avatar.getX() - SteveDriver.snake.getHeadPosition().x;
		float deltaX = this.avatar.getY() - SteveDriver.snake.getHeadPosition().y;
		
		float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
		degrees += 180;
		
		SteveDriver.field.projectiles.add(new Acorn(this.avatar.getX(), this.avatar.getY(), 100*MathUtils.cosDeg(degrees), 100*MathUtils.sinDeg(degrees)));
		shootCounter = 0;
	}
	
	

}
