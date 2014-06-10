package com.steve.weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeBullet;
import com.steve.projectiles.SnakeRocket;

public class Specialist extends Weapon{
	public Specialist(float x, float y){
		super(x,y, 16*10, 16);
		shootSpeed = 200;
		range = 1000;
	}
	
	@Override
	protected void shoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SnakeRocket temp = new SnakeRocket(this.getX(), this.getY(), (isUpgraded) ? 1 : 0);
				temp.setDirection(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
				
				SteveDriver.field.addProjectile(temp);
				shootCounter = 0;
	}
	
	@Override
	public void upgrade(){
		super.upgrade();
		this.shootSpeed = 100;
		this.setRegion(atlasX, atlasY+16, SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH);
		//TODO more stuff to upgrade
	}
}
