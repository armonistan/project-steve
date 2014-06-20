package com.steve.bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.enemies.Flyer;
import com.steve.helpers.CollisionHelper;
import com.steve.projectiles.Pinecone;
import com.steve.stages.Field;

public class Carrier extends Enemy {

	Rectangle front, middle, back;
	
	public Carrier(float x, float y) {
		super(x, y, 36, 0, 28, 8, 0.5f, 0.5f, 1, 50, 5000);
		front = new Rectangle(this.avatar.getX(), this.avatar.getY()+this.avatar.getBoundingRectangle().height/3, 100,50);
		middle = new Rectangle(this.avatar.getX()+this.avatar.getBoundingRectangle().width/4, this.avatar.getY(), 280,120);
		back = new Rectangle(this.avatar.getX()+(.85f*this.avatar.getBoundingRectangle().width), this.avatar.getY()+this.avatar.getBoundingRectangle().height/3, 48,64);
		moneyAmount = 50000;
		shootTime = 5f;
		
	}
	
	@Override
	public void update() {
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
				//SteveDriver.snake.changeHunger(deathDamage);
				System.out.println("collide front");
				return;
			}
		}
		
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(middle, s.getBoundingRectangle())) {
				//SteveDriver.snake.changeHunger(deathDamage);
				System.out.println("collide mid");
				return;
			}
		}
		
		for (Sprite s : SteveDriver.snake.getSegments()) {
			if (CollisionHelper.isCollide(back, s.getBoundingRectangle())) {
				//SteveDriver.snake.changeHunger(deathDamage);
				System.out.println("collide back");
				return;
			}
		}
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
	protected void checkProjectiles(){
		
	}
	
	@Override
	protected void move() {
	}
	

	public void shoot() {
		SteveDriver.field.enemiesToAdd.add(new Flyer(this.avatar.getX()/16, this.avatar.getY()/16));
	}
}