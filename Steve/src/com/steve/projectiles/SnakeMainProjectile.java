package com.steve.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Projectile;

public class SnakeMainProjectile extends Projectile {
	private final static float MAX_SPEED = 750;
	private boolean isRocket;
	
	public SnakeMainProjectile(float x, float y, int level, int atlasX, int atlasY) {
		super(x, y, atlasX, atlasY, 1, 1, (40+SteveDriver.snake.getSnakeTier()*SteveDriver.snakeTierWeaponDamageModifier+SteveDriver.snakeTierWeaponDamageModifier) * SteveDriver.constants.get("fireDamage"), true,
				30 * SteveDriver.constants.get("fireRange"));
		speed = 450;
		isRocket = false;
	}
	
	public SnakeMainProjectile(float x, float y, int atlasX, int atlasY) {
		super(x, y, atlasX, atlasY, 1, 1, (40+SteveDriver.snake.getSnakeTier()*SteveDriver.snakeTierWeaponDamageModifier+SteveDriver.snakeTierWeaponDamageModifier) * SteveDriver.constants.get("fireDamage"), true,
				35);
		isRocket = false;
		speed = 900;
		ignoreCollisions = true;
	}
	
	public SnakeMainProjectile(float x, float y, boolean isRocket){
		super(x, y, 16, 0, 1, 1, (40+SteveDriver.snake.getSnakeTier()*SteveDriver.snakeTierWeaponDamageModifier+SteveDriver.snakeTierWeaponDamageModifier) * SteveDriver.constants.get("fireDamage"), true,
				30 * SteveDriver.constants.get("fireRange"));
		
		speed = 250;
		this.isRocket = isRocket;
	}
	
	@Override
	public void update() {
		if(isRocket){
			directionX += (SteveDriver.random.nextFloat() - 0.5f) * 3;
			directionY += (SteveDriver.random.nextFloat() - 0.5f) * 3;
		
			if ((directionX * directionX + directionY * directionY) < MAX_SPEED * MAX_SPEED) {
				directionX *= 1.03f;
				directionY *= 1.03f;
			}
		}
		
		super.update();
	}
}
