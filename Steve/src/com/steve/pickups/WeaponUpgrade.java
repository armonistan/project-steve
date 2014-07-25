package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.steve.Snake;
import com.steve.SteveDriver;
import com.steve.base.Pickup;

public class WeaponUpgrade extends Pickup {
	public WeaponUpgrade(float xPos, float yPos){
		super(xPos, yPos, 9 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE, 0);
		pickupSound = SteveDriver.assets.get("audio/weaponUpgrade.ogg", Sound.class);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		int index = snake.getUpgradableWeapon();
		if(index != -1){
			if(SteveDriver.prefs.getBoolean("sfx", false))
				pickupSound.play();
			snake.getWeapons().get(index).upgrade();
		}
	}
}
