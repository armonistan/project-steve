package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class Apple extends Pickup {
	
	public Apple(float x, float y) {
		super(x, y, 4 * SteveDriver.TEXTURE_SIZE, 0, 175);
		int soundDecider = SteveDriver.random.nextInt(3)+1;
		
		pickupSound = SteveDriver.assets.get("audio/eatApple" + soundDecider + ".ogg", Sound.class);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.addBody();
		if(SteveDriver.prefs.getBoolean("sfx", true))
			pickupSound.play();
		SteveDriver.summary.appleScore += points * SteveDriver.constants.get("goldModifier");
	}
	

}