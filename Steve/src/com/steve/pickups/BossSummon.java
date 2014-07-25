package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;
import com.steve.bosses.Carrier;
import com.steve.bosses.Razorbull;

public class BossSummon extends Pickup {
	
	int type;
	
	public BossSummon(float x, float y, int type) {
		super(x, y, 8 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE, 0);
		pickupSound = SteveDriver.assets.get("audio/bossSummon.ogg", Sound.class);
		this.type = type;
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		
		switch(type){
			case 1:
				Carrier c = new Carrier(this.getX()/SteveDriver.TEXTURE_SIZE, this.getY()/SteveDriver.TEXTURE_SIZE+3);
				SteveDriver.field.enemies.add(c);
				c.intialize();
			break;
			
			case 0:
				SteveDriver.field.enemies.add(new Razorbull(this.getX()/SteveDriver.TEXTURE_SIZE, this.getY()/SteveDriver.TEXTURE_SIZE+3));
			break;
		}
		if(SteveDriver.prefs.getBoolean("sfx", false))
			pickupSound.play();
	}
	
	@Override
	public void update() {
		//Nothing
	}
}