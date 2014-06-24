package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class Apple extends Pickup {
	
	private Sound eatApple;
	
	public Apple(float x, float y) {
		super(x, y, 4 * SteveDriver.TEXTURE_SIZE, 0, 250000);
		int soundDecider = SteveDriver.random.nextInt(3)+1;
		eatApple = Gdx.audio.newSound(Gdx.files.internal("audio/eatApple" + soundDecider + ".ogg"));
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.addBody();
		eatApple.play();
		SteveDriver.summary.appleScore += points;
	}
}