package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class Treasure extends Pickup {
	public Treasure(float x, float y) {
		super(x, y, 5 * SteveDriver.TEXTURE_SIZE, 0, 0);
		//int soundDecider = SteveDriver.random.nextInt(3)+1;
		pickupSound = Gdx.audio.newSound(Gdx.files.internal("audio/treasurePickup" + ".ogg"));
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