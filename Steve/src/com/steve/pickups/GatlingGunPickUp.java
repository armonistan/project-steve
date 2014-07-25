package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class GatlingGunPickUp extends Pickup{
	public GatlingGunPickUp(float xPos, float yPos){
		super(xPos, yPos, 8 * SteveDriver.TEXTURE_SIZE, 0, 0);
		pickupSound = SteveDriver.assets.get("audio/pickupGatlingGun1.ogg", Sound.class);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		if(SteveDriver.prefs.getBoolean("sfx", false))
			pickupSound.play();
		snake.mountUpgrade(0);
	}
}
