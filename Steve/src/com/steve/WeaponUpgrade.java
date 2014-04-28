package com.steve;

public class WeaponUpgrade extends PickUp {
	WeaponUpgrade(float xPos, float yPos){
		super(xPos, yPos, 9 * 16, 3 * 16);
	}
	
	public void consume(Snake snake) {
		super.consume(snake);
		int index = snake.getUpgradableWeapon();
		if(index != -1)
			snake.getWeapons().get(index).upgrade();
	}
}
