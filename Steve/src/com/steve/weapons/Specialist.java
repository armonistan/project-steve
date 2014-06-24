package com.steve.weapons;

import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeRocket;

public class Specialist extends Weapon{
	public Specialist(float x, float y){
		super(x,y, SteveDriver.TEXTURE_SIZE*10, SteveDriver.TEXTURE_SIZE);
		shootSpeed = 1f - 1f * (int)(SteveDriver.constants.get("fireRate") - 1f);
		range = 700*SteveDriver.constants.get("fireRange");
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
		this.shootSpeed = 3f - 3f * (int)(SteveDriver.constants.get("fireRate") - 1f);
		this.setRegion(atlasX, atlasY+16, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		//TODO more stuff to upgrade
	}
}
