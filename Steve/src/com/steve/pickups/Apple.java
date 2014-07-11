package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class Apple extends Pickup {
	
	private float lifeTime = 15.0f;
	private float lifeTimer = 15.0f;
	
	public Apple(float x, float y) {
		super(x, y, 4 * SteveDriver.TEXTURE_SIZE, 0, 175);
		int soundDecider = SteveDriver.random.nextInt(3)+1;
		pickupSound = Gdx.audio.newSound(Gdx.files.internal("audio/eatApple" + soundDecider + ".ogg"));
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		snake.addBody();
		if(SteveDriver.prefs.getBoolean("sfx", true))
			pickupSound.play();
		SteveDriver.summary.appleScore += points;
	}
	
	@Override
	public void update() {
		lifeTimer -= Gdx.app.getGraphics().getDeltaTime();
		if (lifeTimer <= 0) {
			SteveDriver.field.pickupsToRemove.add(this);
		} else {
			this.alphaMod = lifeTimer / lifeTime;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float alpha) {
		if (lifeTimer != 0) {
			super.draw(batch, alphaMod);
		}
	}
}