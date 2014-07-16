package com.steve.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.steve.SteveDriver;
import com.steve.base.Weapon;
import com.steve.projectiles.SnakeRocket;

public class Specialist extends Weapon{
	public Specialist(float x, float y){
		super(x,y, SteveDriver.TEXTURE_SIZE*10, SteveDriver.TEXTURE_SIZE);
		shootSpeed = 1f / (int)(SteveDriver.constants.get("fireRate"));
		range = 500*SteveDriver.constants.get("fireRange");
		shootSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/specialist1.ogg"));
		shootSound2 = Gdx.audio.newSound(Gdx.files.internal("audio/specialist2.ogg"));
		shootSound3 = Gdx.audio.newSound(Gdx.files.internal("audio/specialist3.ogg"));
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
				int shootSoundInt = -1;
				if(SteveDriver.prefs.getBoolean("sfx", true))
					shootSoundInt = SteveDriver.random.nextInt(3)+1;
				switch(shootSoundInt){
					case 1:
						shootSound1.play(.6f, 1, 0);
					break;
					case 2:
						shootSound2.play(.6f, 1, 0);
					break;
					case 3:
						shootSound3.play(.6f, 1, 0);
					break;
				}
				shootCounter = 0;
	}
	
	@Override
	public void upgrade(){
		super.upgrade();
		this.shootSpeed = 3f - 3f * (int)(SteveDriver.constants.get("fireRate") - 1f);
		this.setRegion(atlasX, atlasY + SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		//TODO more stuff to upgrade
	}
}
