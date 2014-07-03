package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class LaserPickUp extends Pickup {
	public LaserPickUp(float xPos, float yPos){
		super(xPos, yPos, 9 * SteveDriver.TEXTURE_SIZE, 0, 0);
		int soundDecider = 1;
		pickupSound = Gdx.audio.newSound(Gdx.files.internal("audio/pickupLaser" + soundDecider + ".ogg"));
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		if(SteveDriver.prefs.getBoolean("sfx", true))
			pickupSound.play();
		snake.mountUpgrade(1);
	}
}
