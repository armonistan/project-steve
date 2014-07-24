package com.steve.bosses;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.enemies.Flyer;
import com.steve.enemies.HomaHawk;
import com.steve.helpers.CollisionHelper;
import com.steve.projectiles.Pinecone;
import com.steve.stages.Field;

public class Carrier extends Enemy {

	Rectangle front, middle, back;
	ArrayList <CarrierTurret> turrets = new ArrayList<CarrierTurret>();
	Admiral swiggins;
	int threshHold;
	float startX;
	float startY;
	static int startHealth = 5000;
	float numTurrets = 10;
	
	public Carrier(float x, float y) {
		super(x, y, 36, 0, 28, 8, 0.5f, 0.5f, 1, 50, startHealth);
		front = new Rectangle(this.avatar.getX(), this.avatar.getY()+this.avatar.getBoundingRectangle().height/3, SteveDriver.TEXTURE_SIZE*5,SteveDriver.TEXTURE_SIZE*2);
		middle = new Rectangle(this.avatar.getX()+this.avatar.getBoundingRectangle().width/4, this.avatar.getY(), SteveDriver.TEXTURE_SIZE*19,SteveDriver.TEXTURE_SIZE*8);
		back = new Rectangle(this.avatar.getX()+(.85f*this.avatar.getBoundingRectangle().width), this.avatar.getY()+this.avatar.getBoundingRectangle().height/3, SteveDriver.TEXTURE_SIZE*3,SteveDriver.TEXTURE_SIZE*4);
		moneyAmount = 50000;
		shootTime = 2f;
		startX = x;
		startY = y;
		SteveDriver.disableSpawnsRobot = true;
		SteveDriver.gui.carrierAlive(this);
	}
	
	public void intialize(){
		float x = startX;
		float y = startY;
		
		for(int counter = 0; counter < numTurrets/2; counter++){
			float xOffset = 8+4*counter;
			float yOffset = 1;
			
			turrets.add(new CarrierTurret(x+xOffset,y+yOffset));
		}
		
		for(int counter = 0; counter < numTurrets/2; counter++){
			float xOffset = 8+4*counter;
			float yOffset = 6;
			
			turrets.add(new CarrierTurret(x+xOffset,y+yOffset));
		}
		
		swiggins = new Admiral(x+20,y+3);
		SteveDriver.field.enemies.add(swiggins);
		
		for(CarrierTurret c : turrets){
			SteveDriver.field.enemies.add(c);
		}
		
		threshHold = startHealth/turrets.size();
	}
	
	@Override
	public void update() {
		if(this.health < (threshHold*turrets.size()-1))
			loseTurret();
		
		decideShoot();
		checkCollideWithSnake();
		checkProjectiles();
		checkIsDead();
		move();
		animate();
	}
	
	@Override
	protected void checkCollideWithSnake(){
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(front, s.getBoundingRectangle())) {
				SteveDriver.snake.changeHunger(deathDamage);
				//System.out.println("collide front");
				return;
			}
		}
		
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(middle, s.getBoundingRectangle())) {
				SteveDriver.snake.changeHunger(deathDamage);
				//System.out.println("collide mid");
				return;
			}
		}
		
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(back, s.getBoundingRectangle())) {
				SteveDriver.snake.changeHunger(deathDamage);
				//System.out.println("collide back");
				return;
			}
		}
	}
	
	@Override
	public void kill(){
		SteveDriver.disableSpawnsRobot = false;
		swiggins.kill();
		for(CarrierTurret c : this.turrets)
			c.kill();
		SteveDriver.prefs.putBoolean("carrierDefeated", true);
		SteveDriver.prefs.putBoolean("robotBossActivate", false);
		SteveDriver.prefs.putInteger("bossesDefeated", SteveDriver.prefs.getInteger("bossesDefeated")+1);
		SteveDriver.prefs.flush();
		SteveDriver.robotBossActivate = SteveDriver.prefs.getBoolean("robotBossActivate", false);
		SteveDriver.gui.carrierDead();
		super.kill();
	}
	
	protected void decideShoot(){
		if(shootTimer> shootTime){
			shoot();
			shootTimer = 0;
		}
		
		else {
			shootTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
	
	@Override
	protected void move() {
	}
	
	protected void loseTurret(){
		int index = SteveDriver.random.nextInt(turrets.size());
		
		SteveDriver.field.enemiesToRemove.add(turrets.get(index));
		turrets.remove(index);
	}
	

	public void shoot() {
		SteveDriver.field.enemiesToAdd.add(new HomaHawk(this.avatar.getX()/16+8, this.avatar.getY()/16+3, this.avatar.getRotation()));
	}
}