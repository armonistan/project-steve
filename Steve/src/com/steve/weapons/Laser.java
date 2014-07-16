package com.steve.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeLaser;

public class Laser extends Weapon{
	public Laser(float x, float y){
		super(x,y, SteveDriver.TEXTURE_SIZE*9, SteveDriver.TEXTURE_SIZE);
		shootSpeed = .7f / (int)(SteveDriver.constants.get("fireRate"));
		range = 650*SteveDriver.constants.get("fireRange");
		shootSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/pulseLaser1.ogg"));
		shootSound2 = Gdx.audio.newSound(Gdx.files.internal("audio/pulseLaser2.ogg"));
		shootSound3 = Gdx.audio.newSound(Gdx.files.internal("audio/pulseLaser3.ogg"));
		
		


	}
	
	@Override
	protected void shoot(){
		//different angle
		float deltaY = this.getX() - target.getXPosition();
		float deltaX = this.getY() - target.getYPosition();
				
		float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
		//System.out.println("shoot: " + degrees);
		degrees += 180;
		
		
		SnakeLaser temp = new SnakeLaser(this.getX(), this.getY(), (isUpgraded) ? 1 : 0);
		temp.setDirection(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
				
		SteveDriver.field.addProjectile(temp);
		int shootSoundInt = -1;
		if(SteveDriver.prefs.getBoolean("sfx", true))
			shootSoundInt = SteveDriver.random.nextInt(3)+1;
		switch(shootSoundInt){
			case 1:
				shootSound1.play(1f, 1, 0);
			break;
			case 2:
				shootSound2.play(1f, 1, 0);
			break;
			case 3:
				shootSound3.play(1f, 1, 0);
			break;
		}
		shootCounter = 0;
	}
	
	@Override
	public void upgrade(){
		super.upgrade();
		this.shootSpeed *= .75;
		this.setRegion(atlasX, atlasY + SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		//TODO more stuff to upgrade
	}
}
