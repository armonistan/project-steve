package com.steve.bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.helpers.CollisionHelper;
import com.steve.projectiles.Acorn;

public class CarrierTurret extends Enemy{
	
	float shootSpeed = .5f;
	float shootCounter = 0;
	float range;
	float damage;//TODO use it?
	int atlasX;
	int atlasY;
	boolean isAimed;
	boolean isUpgraded;
	//sprivate Sprite base;
	
	Carrier carrier;
	float xOffSet;
	float yOffSet;

	public CarrierTurret(float x, float y, float xOff, float yOff, Carrier c) {
		super(x, y, 9, 5, 1, 1, 0.5f, 0.5f, 1, 0, 125);
		range = 300;//override range if need be
		isAimed = false;
		isUpgraded = false;
		moneyAmount = 0;
		
		carrier = c;
		xOffSet = xOff;
		yOffSet = yOff;
	}
	
	@Override
	public void update() {
		avatar.setPosition(carrier.getX() + (carrier.getRotation() == SteveDriver.UP ? xOffSet *
				SteveDriver.TEXTURE_SIZE : carrier.getWidth() - xOffSet * SteveDriver.TEXTURE_SIZE),
				carrier.getY() + yOffSet * SteveDriver.TEXTURE_SIZE);
		
		super.update();
	}
	
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
			if(Math.abs(deltaPositiveDegrees) < 3)
				this.isAimed = true;
			else{
				this.isAimed = false;
				this.avatar.setRotation(((this.avatar.getRotation() +361)%360));
			}
		}
		else{
			if(Math.abs(deltaNegativeDegrees) < 3)
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
		shootCounter += Gdx.graphics.getRawDeltaTime();
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
		
		Acorn temp = new Acorn(this.avatar.getX(), this.avatar.getY());
		
		SteveDriver.field.addProjectile(temp);
		temp.setDirection(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
		shootCounter = 0;
	}

}
