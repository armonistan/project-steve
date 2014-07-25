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
	static int startHealth = 20000;
	float numTurrets = 10;
	int beginNumEnemies = 0;
	int numActiveHomahawks = 5;
	int stopSpawnNum = 0;
	int numSpawnedHawks = 0;
	
	public Carrier(float x, float y) {
		super(x, y, 36, 0, 28, 8, .7f, 0.5f, 1, 50, startHealth);
		front = new Rectangle(this.avatar.getX(), this.avatar.getY()+this.avatar.getBoundingRectangle().height/3, SteveDriver.TEXTURE_SIZE*5,SteveDriver.TEXTURE_SIZE*2);
		middle = new Rectangle(this.avatar.getX()+this.avatar.getBoundingRectangle().width/4, this.avatar.getY(), SteveDriver.TEXTURE_SIZE*19,SteveDriver.TEXTURE_SIZE*8);
		back = new Rectangle(this.avatar.getX()+(.85f*this.avatar.getBoundingRectangle().width), this.avatar.getY()+this.avatar.getBoundingRectangle().height/3, SteveDriver.TEXTURE_SIZE*3,SteveDriver.TEXTURE_SIZE*4);
		moneyAmount = 50000;
		shootTime = 10f;
		shootTimer = 7f;
		startX = x;
		startY = y;
		ignoresBlockers = true;
		destroysBlockers = true;
		SteveDriver.disableSpawnsRobot = true;
		SteveDriver.gui.carrierAlive(this);
	}
	
	public void intialize(){
		float x = startX;
		float y = startY;
		
		for(int counter = 0; counter < numTurrets/2; counter++){
			float xOffset = 8+4*counter;
			float yOffset = 1;
			
			turrets.add(new CarrierTurret(x, y, xOffset,yOffset, this));
		}
		
		for(int counter = 0; counter < numTurrets/2; counter++){
			float xOffset = 8+4*counter;
			float yOffset = 6;
			
			turrets.add(new CarrierTurret(x, y, xOffset, yOffset, this));
		}
		
		swiggins = new Admiral(x, y, 20, 3, this);
		SteveDriver.field.enemies.add(swiggins);
		
		for(CarrierTurret c : turrets){
			SteveDriver.field.enemies.add(c);
		}
		
		threshHold = startHealth/turrets.size();
		
		this.beginNumEnemies = SteveDriver.field.enemies.size();
		this.stopSpawnNum = this.beginNumEnemies + this.numActiveHomahawks;
	}
	
	@Override
	public void update() {
		if(this.health < (threshHold*turrets.size()-1)){
			loseTurret();
			speedUp();
		}
		
		decideShoot();
		checkCollideWithSnake();
		checkProjectiles();
		checkIsDead();
		move();
		animate();
	}
	
	private void speedUp(){
		this.moveTime-=.025f;
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
			if(this.numSpawnedHawks < this.numActiveHomahawks){
				shoot();
				shootTimer -= .5f;
				numSpawnedHawks++;
			}
			else{
				shootTimer = 0;
				numSpawnedHawks = 0;
			}
		}
		
		else {
			shootTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
	
	@Override
	protected Vector2 decideMove() {
		if (avatar.getRotation() == SteveDriver.UP) {
			if (avatar.getX() <= 0f) {
				avatar.setRotation(SteveDriver.DOWN);
			}
		}
		else {
			if (avatar.getX() + avatar.getWidth() >= SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE) {
				avatar.setRotation(SteveDriver.UP);
			}
		}
		
		if (avatar.getRotation() == SteveDriver.UP) {
			return SteveDriver.VLEFT;
		}
		else {
			return SteveDriver.VRIGHT;
		}
	}
	
	protected void loseTurret(){
		int index = SteveDriver.random.nextInt(turrets.size());
		
		SteveDriver.field.enemiesToRemove.add(turrets.get(index));
		turrets.remove(index);
		this.stopSpawnNum--;
	}
	

	public void shoot() {
		SteveDriver.field.enemiesToAdd.add(new HomaHawk(this.avatar.getX()/16+8, this.avatar.getY()/16+3, this.avatar.getRotation()));
	}
	
	public float getX() {
		return avatar.getX();
	}
	
	public float getY() {
		return avatar.getY();
	}
	
	public float getRotation() {
		return avatar.getRotation();
	}
	
	public float getWidth() {
		return avatar.getWidth();
	}
}