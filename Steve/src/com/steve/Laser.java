package com.steve;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Laser extends Weapon{
	public Laser(float x, float y, int atlasX, int atlasY){
		super(x,y,atlasX,atlasY);
		shootSpeed = 100;
	}
	
	@Override
	protected void shoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SteveDriver.field.projectiles.add(new SnakeBullet(new Vector2(this.getX(), this.getY()), new Vector2(600*MathUtils.cosDeg(degrees), 200*MathUtils.sinDeg(degrees)), 0));
				shootCounter = 0;
	}
}
