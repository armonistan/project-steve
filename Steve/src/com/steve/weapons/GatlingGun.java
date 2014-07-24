package com.steve.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeBullet;

public class GatlingGun extends Weapon{
	private float baseDamage = 10f;
	private float baseShootSpeed = .5f;
	
	public GatlingGun(float x, float y){
		super(x,y, SteveDriver.TEXTURE_SIZE*8, SteveDriver.TEXTURE_SIZE);
		//shoot speed
		float modifier = (SteveDriver.constants.get("fireRate") - 1)/2 + 1;
		shootSpeed = (baseShootSpeed / (modifier));
		//range
		modifier = (SteveDriver.constants.get("fireRange") - 1)/2 + 1;
		range = 300f * modifier;
		range = 1000f;
		//damage
		bulletDamage = baseDamage+SteveDriver.snake.getSnakeTier()*SteveDriver.snakeTierWeaponDamageModifier;
		modifier = (SteveDriver.constants.get("fireDamage") - 1)/2 + 1;
		bulletDamage *= modifier;
		
		shootSound1 = SteveDriver.assets.get("audio/gatlingGun1.ogg", Sound.class);
		shootSound2 = SteveDriver.assets.get("audio/gatlingGun2.ogg", Sound.class);
		shootSound3 = SteveDriver.assets.get("audio/gatlingGun3.ogg", Sound.class);
	}
	
	@Override
	protected void shoot(){
		//different angle
		float deltaY = this.getX() - target.getXPosition();
		float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SnakeBullet temp = new SnakeBullet(this.getX(), this.getY(), (isUpgraded) ? 1 : 0, bulletDamage);
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
		this.shootSpeed -= .1f;
		this.range += 50;
		this.bulletDamage += 10;
		this.setRegion(atlasX, atlasY + SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		//TODO more stuff to upgrade
	}
}
