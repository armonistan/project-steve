package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class GatlingGunPickUp extends Pickup{
	public GatlingGunPickUp(float xPos, float yPos){
		super(xPos, yPos, 8 * SteveDriver.TEXTURE_SIZE, 0, 0);
		int soundDecider = 1;
		pickupSound = Gdx.audio.newSound(Gdx.files.internal("audio/pickupGatlingGun" + soundDecider + ".ogg"));
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		pickupSound.play();
		snake.mountUpgrade(0);
	}
}
