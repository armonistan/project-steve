package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class Treasure extends Pickup {
	public Treasure(float x, float y) {
		super(x, y, 5 * SteveDriver.TEXTURE_SIZE, 0, 0);
		//int soundDecider = SteveDriver.random.nextInt(3)+1;
		pickupSound = SteveDriver.assets.get("audio/treasurePickup.ogg", Sound.class);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.addTreasure(1);
		if(SteveDriver.prefs.getBoolean("sfx", true))
			pickupSound.play();
		SteveDriver.summary.appleScore += points;
	}
}