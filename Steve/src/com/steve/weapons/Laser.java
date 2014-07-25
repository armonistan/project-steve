package com.steve.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeLaser;

public class Laser extends Weapon{
	private float baseDamage = 30f;
	private float baseShootSpeed = .7f;
	
	public Laser(float x, float y){
		super(x,y, SteveDriver.TEXTURE_SIZE*9, SteveDriver.TEXTURE_SIZE);
		//shoot speed
		float modifier = (SteveDriver.constants.get("fireRate") - 1)/2 + 1;
		shootSpeed = (baseShootSpeed / (modifier));
		//range
		modifier = (SteveDriver.constants.get("fireRange") - 1)/2 + 1;
		range = 450f * modifier;
		range = 1000f;
		//damage
		bulletDamage = baseDamage+SteveDriver.snake.getSnakeTier()*SteveDriver.snakeTierWeaponDamageModifier;
		modifier = (SteveDriver.constants.get("fireDamage") - 1)/2 + 1;
		bulletDamage *= modifier;
		
		shootSound1 = SteveDriver.assets.get("audio/pulseLaser1.ogg", Sound.class);
		shootSound2 = SteveDriver.assets.get("audio/pulseLaser2.ogg", Sound.class);
		shootSound3 = SteveDriver.assets.get("audio/pulseLaser3.ogg", Sound.class);
	}
	
	@Override
	protected void shoot(){
		//different angle
		float deltaY = this.getX() - target.getXPosition();
		float deltaX = this.getY() - target.getYPosition();
				
		float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
		//System.out.println("shoot: " + degrees);
		degrees += 180;
		
		
		SnakeLaser temp = new SnakeLaser(this.getX(), this.getY(), (isUpgraded) ? 1 : 0, bulletDamage);
		temp.setDirection(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
				
		SteveDriver.field.addProjectile(temp);
		int shootSoundInt = -1;
		if(SteveDriver.prefs.getBoolean("sfx", false))
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
		this.shootSpeed -= .1f;
		this.range += 50;
		this.bulletDamage += 10;
		this.setRegion(atlasX, atlasY + SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		//TODO more stuff to upgrade
	}
}
