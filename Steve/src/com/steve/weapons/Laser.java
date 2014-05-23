package com.steve.weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeBullet;

public class Laser extends Weapon{
	public Laser(float x, float y, int atlasX, int atlasY){
		super(x,y,atlasX,atlasY);
		shootSpeed = 100;
		range = 500;
	}
	
	@Override
	protected void shoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SteveDriver.field.projectiles.add(new SnakeBullet(this.getX(), this.getY(), 600*MathUtils.cosDeg(degrees), 200*MathUtils.sinDeg(degrees), 0));
				shootCounter = 0;
	}
	
	@Override
	public void upgrade(){
		super.upgrade();
		this.shootSpeed = 50;
		this.setRegion(new TextureRegion(SteveDriver.atlas, atlasX, atlasY+16, SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		//TODO more stuff to upgrade
	}
}
