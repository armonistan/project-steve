package com.steve.weapons;

import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeMainProjectile;

public class MainCannon extends Weapon{
	public MainCannon(float x, float y){
		super(x,y, 16*8, 16*5);
		shootSpeed = 75 - (75 * (int)(SteveDriver.constants.get("fireRate") - 1f)) * 2;
		range = 700*SteveDriver.constants.get("fireRange")*SteveDriver.constants.get("fireRange");
		this.isUpgraded = true;
	}
	
	@Override
	protected void shoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SteveDriver.field.addProjectile(new SnakeMainProjectile(this.getX(), this.getY(), MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees), (isUpgraded) ? 1 : 0));
				shootCounter = 0;
	}
	

}