package com.steve.weapons;

import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeBullet;

public class GatlingGun extends Weapon{
	public GatlingGun(float x, float y){
		super(x,y, 16*8, 16);
		shootSpeed = 25 - 25 * (int)(SteveDriver.constants.get("fireRate")-1f);
		range = 400f * SteveDriver.constants.get("fireRange");
	}
	
	@Override
	protected void shoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SnakeBullet temp = new SnakeBullet(this.getX(), this.getY(), (isUpgraded) ? 1 : 0);
				temp.setDirection(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
				
				SteveDriver.field.addProjectile(temp);
				shootCounter = 0;
	}
	
	@Override
	public void upgrade(){
		super.upgrade();
		this.shootSpeed = 5;
		this.setRegion(atlasX, atlasY+16, SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH);
		//TODO more stuff to upgrade
	}
}
