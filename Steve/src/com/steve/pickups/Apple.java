package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;
import com.steve.helpers.CollisionHelper;

public class Apple extends Pickup {
	
	
	
	public Apple(float x, float y) {
		super(x, y, 4 * SteveDriver.TEXTURE_SIZE, 0, 175);
		int soundDecider = SteveDriver.random.nextInt(3)+1;
		lifeTime = 9.0f;
		lifeTimer = 9.0f;
		//
		pickupSound = SteveDriver.assets.get("audio/eatApple" + soundDecider + ".ogg", Sound.class);
	}

	
	@Override
	public void update() {
		lifeTimer -= Gdx.app.getGraphics().getDeltaTime();
		if (lifeTimer <= 1) {
			SteveDriver.field.pickupsToRemove.add(this);
		}
		else
			this.alphaMod = (lifeTimer / lifeTime > .5f) ? 1 : (2f*lifeTimer / lifeTime);
		
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.addBody();
		if(SteveDriver.prefs.getBoolean("sfx", false))
			pickupSound.play();
		SteveDriver.summary.appleScore += points * SteveDriver.constants.get("goldModifier");
	}
	

}