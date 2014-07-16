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
	
	public MainCannon(float x, float y, int atlasX, int atlasY){
		super(x,y, atlasX, atlasY);
		shootSpeed = .6f + (.6f * (SteveDriver.constants.get("fireRate") - 1f) - ((SteveDriver.constants.get("mainCannonType") == 2) ?  .3f : 0));
		range = 700 * SteveDriver.constants.get("fireRange")*SteveDriver.constants.get("fireRange");
		this.isUpgraded = true;
		alternate = true;
		barrageCounter = 0;
		this.cannonSound = Gdx.audio.newSound(Gdx.files.internal("audio/mainCannon1" + ".ogg"));
		this.gausSound = Gdx.audio.newSound(Gdx.files.internal("audio/mainCannon2" + ".ogg"));
		this.rocketSound = Gdx.audio.newSound(Gdx.files.internal("audio/mainCannon3" + ".ogg"));
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
				
				if (SteveDriver.prefs.getBoolean("sfx", true)) {
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
				
				if (SteveDriver.prefs.getBoolean("sfx", true)) {
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
				
				if (SteveDriver.prefs.getBoolean("sfx", true)) {
					gausSound.play();
				}
				
				shootCounter = 0;
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
				
				if (SteveDriver.prefs.getBoolean("sfx", true)) {
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
