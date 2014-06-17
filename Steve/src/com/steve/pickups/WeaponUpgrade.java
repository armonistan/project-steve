package com.steve.pickups;

import com.steve.Snake;
import com.steve.base.Pickup;

public class WeaponUpgrade extends Pickup {
	public WeaponUpgrade(float xPos, float yPos){
		super(xPos, yPos, 9 * 16, 3 * 16, 0);
	}
	
	@Override
	public void consume(Snake snake) {
		super.consume(snake);
		int index = snake.getUpgradableWeapon();
		if(index != -1)
			snake.getWeapons().get(index).upgrade();
	}
}
