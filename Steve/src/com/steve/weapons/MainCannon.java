package com.steve.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeMainProjectile;

public class MainCannon extends Weapon{
	
	private boolean alternate;
	private int barrageCounter;
	private Sound cannonSound;
	private Sound gausSound;
	private Sound rocketSound;
	private float baseShootSpeed = .9f;
	
	public MainCannon(float x, float y, int atlasX, int atlasY){
		super(x,y, atlasX, atlasY);
		shootSpeed = (baseShootSpeed - SteveDriver.snake.snakeTier/10 - SteveDriver.tierTwoWeaponShootSpeedBuff)
				/ (int)(SteveDriver.constants.get("fireRate")) 
				- ((SteveDriver.constants.get("mainCannonType") == 2) ?  .2f : 0);
		range = (250+SteveDriver.tierTwoWeaponRangeBuff) * SteveDriver.constants.get("fireRange");
		//range = 1000f;
		this.isUpgraded = true;
		alternate = true;
		barrageCounter = 0;
		
		this.cannonSound = SteveDriver.assets.get("audio/mainCannon1.ogg", Sound.class);
		this.gausSound = SteveDriver.assets.get("audio/mainCannon2.ogg", Sound.class);
		this.rocketSound = SteveDriver.assets.get("audio/mainCannon3.ogg", Sound.class);
	}
	
	@Override
	protected void weaponBehavior(){
		//base behavior
		targetEnemy();
		if(target != null && SteveDriver.field.enemies.size() > 0){
			turn();
			if(shootCounter > shootSpeed && isInRange() && isAimed){
				if(SteveDriver.constants.get("mainCannonType") == 0){
					shoot();
				}
				else if(SteveDriver.constants.get("mainCannonType") == 1){
					fireRateShoot();
				}
				else if(SteveDriver.constants.get("mainCannonType") == 2){
					fireRangeShoot();
				}
				else if(SteveDriver.constants.get("mainCannonType") == 3){
					fireDamageShoot();
				}
			}
		}
	}
	
	@Override
	protected void shoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SnakeMainProjectile temp = new SnakeMainProjectile(this.getX(), this.getY(), (isUpgraded) ? 1 : 0, 19, 0);
				
				SteveDriver.field.addProjectile(temp);
				temp.setDirection(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
				
				if (SteveDriver.prefs.getBoolean("sfx", false)) {
					this.cannonSound.play();
				}
				
				shootCounter = 0;
	}
	
	protected void fireRateShoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SnakeMainProjectile temp = new SnakeMainProjectile(this.getX(), this.getY(), (isUpgraded) ? 1 : 0, 19, 0);
				SnakeMainProjectile temp2 = new SnakeMainProjectile(this.getX(), this.getY(), (isUpgraded) ? 1 : 0, 19, 0);
				SnakeMainProjectile temp3 = new SnakeMainProjectile(this.getX(), this.getY(), (isUpgraded) ? 1 : 0, 19, 0);
				
				SteveDriver.field.addProjectile(temp);
				SteveDriver.field.addProjectile(temp2);
				SteveDriver.field.addProjectile(temp3);
				temp.setDirection(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
				temp2.setDirection(MathUtils.cosDeg(degrees+10), MathUtils.sinDeg(degrees+10));
				temp3.setDirection(MathUtils.cosDeg(degrees-10), MathUtils.sinDeg(degrees-10));
				
				if (SteveDriver.prefs.getBoolean("sfx", false)) {
					this.cannonSound.play();
				}
				
				shootCounter = 0;
	}
	
	protected void fireRangeShoot(){
		//different angle
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SnakeMainProjectile temp = new SnakeMainProjectile(this.getX(), this.getY(), 21, 0);
				
				SteveDriver.field.addProjectile(temp);
				temp.setDirection(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
				
				if (SteveDriver.prefs.getBoolean("sfx", false) && barrageCounter == 0) {
					gausSound.play();
				}
				
				if(barrageCounter < 1){
					barrageCounter++;
					shootCounter-=0.1f;
				}
				else{
					barrageCounter = 0;
					shootCounter = 0;
				}
			}
	
	protected void fireDamageShoot(){
		//different angle
				shootCounter = this.shootSpeed;
				float deltaY = this.getX() - target.getXPosition();
				float deltaX = this.getY() - target.getYPosition();
				
				float offset = (alternate) ? 3 : -3;
				
				float degrees = MathUtils.radiansToDegrees * MathUtils.atan2(deltaX, deltaY);
				degrees += 180;
				
				SnakeMainProjectile temp = new SnakeMainProjectile(this.getX()+MathUtils.sinDeg(degrees)*offset, this.getY()+MathUtils.cosDeg(degrees)*offset, (isUpgraded) ? 1 : 0, 20, 0);
				
				SteveDriver.field.addProjectile(temp);
				temp.setDirection(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
				
				if (SteveDriver.prefs.getBoolean("sfx", false)) {
					this.rocketSound.play();
				}
				
				if(barrageCounter < 3){
					barrageCounter++;
					shootCounter-=0.2f;
				}
				else{
					barrageCounter = 0;
					shootCounter = 0;
				}
	}
}
