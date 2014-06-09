package com.steve.weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeBullet;

public class GatlingGun extends Weapon{
	public GatlingGun(float x, float y){
		super(x,y, 16*8, 16);
		shootSpeed = 25;
	}
	
	@Override
	protected void shoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SteveDriver.field.addProjectile(new SnakeBullet(this.getX(), this.getY(), MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees), (isUpgraded) ? 1 : 0));
				shootCounter = 0;
	}
	
	@Override
	public void upgrade(){
		super.upgrade();
		this.shootSpeed = 5;
		this.setRegion(new TextureRegion(SteveDriver.atlas, atlasX, atlasY+16, SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		//TODO more stuff to upgrade
	}
}
