package com.steve.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeBullet;

public class GatlingGun extends Weapon{
	public GatlingGun(float x, float y){
		super(x,y, SteveDriver.TEXTURE_SIZE*8, SteveDriver.TEXTURE_SIZE);
		shootSpeed = .5f - .5f * (int)(SteveDriver.constants.get("fireRate")-1f);
		range = 400f * SteveDriver.constants.get("fireRange");
		shootSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/gatlingGun1.ogg"));
		shootSound2 = Gdx.audio.newSound(Gdx.files.internal("audio/gatlingGun2.ogg"));
		shootSound3 = Gdx.audio.newSound(Gdx.files.internal("audio/gatlingGun3.ogg"));
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
				int shootSoundInt = -1;
				if(SteveDriver.prefs.getBoolean("sfx", true))
					shootSoundInt = SteveDriver.random.nextInt(2)+1;
				switch(shootSoundInt){
					case 1:
						shootSound1.play(.3f, 1, 0);
					break;
					case 2:
						shootSound2.play(.3f, 1, 0);
					break;
					case 3:
						shootSound3.play();
					break;
				}
				shootCounter = 0;
	}
	
	@Override
	public void upgrade(){
		super.upgrade();
		this.shootSpeed = 0.5f - 0.5f * (int)(SteveDriver.constants.get("fireRate")-1f);
		this.setRegion(atlasX, atlasY + SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		//TODO more stuff to upgrade
	}
}
