package com.steve;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Specialist extends Weapon{
	public Specialist(float x, float y, int atlasX, int atlasY){
		super(x,y,atlasX,atlasY);
		shootSpeed = 200;
	}
	
	@Override
	protected void shoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SteveDriver.field.projectiles.add(new SnakeBullet(this.getX(), this.getY(), 300*MathUtils.cosDeg(degrees), 300*MathUtils.sinDeg(degrees), 0));
				shootCounter = 0;
	}
}
