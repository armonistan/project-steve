package com.steve.helpers;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.enemies.AntiSpiral;
import com.steve.enemies.Brute;
import com.steve.enemies.Flyer;
import com.steve.enemies.HomaHawk;
import com.steve.enemies.Rhino;
import com.steve.enemies.Snail;
import com.steve.enemies.Spiral;
import com.steve.enemies.Tank;
import com.steve.enemies.Turret;
import com.steve.pickups.Apple;
import com.steve.pickups.GatlingGunPickUp;
import com.steve.pickups.LaserPickUp;
import com.steve.pickups.SpecialistPickUp;
import com.steve.pickups.WeaponUpgrade;

public class Generator {
	private final int GATLING_GUN_ID = 0;
	private final int LASER_ID = 1;
	private final int SPECIALIST_ID = 2;
	
	private final int GRASS_ID = 0;
	private final int DESERT_ID = 1;
	private final int BARREN_ID = 2;
	
	private Random r;
	
	final float enemyGenerationTime = 5; 
	float enemyGenerationCounter; 
	
	final float appleGenerationTime = 3; 
	float appleGenerationCounter; 
	
	final float pickUpGenerationTime = 10; 
	float pickUpGenerationCounter; 
	
	final float upgradeGenerationTime = 20; 
	float upgradeGenerationCounter; 
	
	public Generator(){
		enemyGenerationCounter = 0; 
		appleGenerationCounter = 0; 
		pickUpGenerationCounter = 0; 
		upgradeGenerationCounter = 0; 
		
		r = new Random();
	}

	public void update(){
		if(this.enemyGenerationCounter > this.enemyGenerationTime){
			generateEnemy();
			this.enemyGenerationCounter = 0;
		}
		else
			enemyGenerationCounter += Gdx.graphics.getRawDeltaTime();
		
		if(this.pickUpGenerationCounter > this.pickUpGenerationTime){
			generatePickUp(true);
			this.pickUpGenerationCounter = 0;
		}
		else
			pickUpGenerationCounter += Gdx.graphics.getRawDeltaTime();
		
		if(this.appleGenerationCounter > this.appleGenerationTime){
			generateApple(true);
			this.appleGenerationCounter = 0;
		}
		else
			appleGenerationCounter += Gdx.graphics.getRawDeltaTime();
	}
	
	private void generateEnemy(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();
		
		float xPosTopBot = (snakePosition.x - r.nextInt((int)(Gdx.graphics.getWidth()*.5)) + r.nextInt((int)(Gdx.graphics.getWidth())));
		float xPosLeft = (snakePosition.x + (int)(Gdx.graphics.getWidth()*.5)
				+ r.nextInt((int)(Gdx.graphics.getWidth()*.25)));
		float xPosRight = (snakePosition.x - (int)(Gdx.graphics.getWidth()*.5)
				- r.nextInt((int)(Gdx.graphics.getWidth()*.25)));
		float yPosTop = (snakePosition.y + (int)(Gdx.graphics.getHeight()*.5)
				+ r.nextInt((int)(Gdx.graphics.getHeight()*.25)));
		float yPosBot = (snakePosition.y - (int)(Gdx.graphics.getHeight()*.5)
				- r.nextInt((int)(Gdx.graphics.getHeight()*.25)));
		float yPosRightLeft = (snakePosition.y - r.nextInt((int)(Gdx.graphics.getHeight()*.5)) + r.nextInt((int)(Gdx.graphics.getHeight())));
		
		int choiceX = (r.nextInt(2)==0) ? 0 : 
				(r.nextInt(2)==0) ? -1 : 1;
		int choiceY = (choiceX == 0) ? (r.nextInt(2)==0) ? 1: -1: 0;
		
		int xPos = (int)((choiceX == 0) ? xPosTopBot/16 : 
			(choiceX < 0) ? xPosLeft/16 : xPosRight/16);
		int yPos = (int)((choiceY == 0) ? yPosRightLeft/16 : 
			(choiceY < 0) ? yPosBot/16 : yPosTop/16);
	
		int locationID = SteveDriver.field.checkRing((int)xPos, (int)yPos);
		int enemyType = (locationID == GRASS_ID) ?  r.nextInt(1) :
			(locationID == DESERT_ID) ?  r.nextInt(4) + 1 : r.nextInt(3) + 5;
	
		switch(enemyType){
			case 0:
				this.generateSlug(xPos, yPos);
				break;
			case 1:
				this.generateTank(xPos, yPos);
				break;
			case 2:
				this.generateBrute(xPos, yPos);
				break;
			case 3:
				this.generateAntiSpiral(xPos, yPos);
				break;
			case 4:
				this.generateFlyer(xPos, yPos);
				break;
			case 5:
				this.generateTurret(xPos, yPos);
				break;
			case 6:
				this.generateSpiral(xPos, yPos);
				break;
			case 7:
				this.generateHomaHawk(xPos, yPos);
				break;
			case 8:
				this.generateRhino(xPos, yPos);
				break;
		}
	}
	
	public void generatePickUp(boolean offScreen){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();
		int xPos = 0;
		int yPos = 0;
		
		if (offScreen) {
			float xPosTopBot = (snakePosition.x - r.nextInt((int)(Gdx.graphics.getWidth()*.5))
				+ r.nextInt((int)(Gdx.graphics.getWidth())));
			float xPosLeft = (snakePosition.x + (int)(Gdx.graphics.getWidth()*.5)
					+ r.nextInt((int)(Gdx.graphics.getWidth()*.25)));
			float xPosRight = (snakePosition.x - (int)(Gdx.graphics.getWidth()*.5)
					- r.nextInt((int)(Gdx.graphics.getWidth()*.25)));
			float yPosTop = (snakePosition.y + (int)(Gdx.graphics.getHeight()*.5)
					+ r.nextInt((int)(Gdx.graphics.getHeight()*.25)));
			float yPosBot = (snakePosition.y - (int)(Gdx.graphics.getHeight()*.5)
					- r.nextInt((int)(Gdx.graphics.getHeight()*.25)));
			float yPosRightLeft = (snakePosition.y - r.nextInt((int)(Gdx.graphics.getHeight()*.5))
					+ r.nextInt((int)(Gdx.graphics.getHeight())));
			
			int choiceX = (r.nextInt(2)==0) ? 0 : 
					(r.nextInt(2)==0) ? -1 : 1;
			int choiceY = (choiceX == 0) ? (r.nextInt(2)==0) ? 1: -1: 0;
			
			xPos = (int)((choiceX == 0) ? xPosTopBot/16 : 
				(choiceX < 0) ? xPosLeft/16 : xPosRight/16);
			yPos = (int)((choiceY == 0) ? yPosRightLeft/16 : 
				(choiceY < 0) ? yPosBot/16 : yPosTop/16);
		}
		else {
			xPos = (int)(snakePosition.x / SteveDriver.TEXTURE_WIDTH) +
					r.nextInt((int)Gdx.graphics.getWidth() / SteveDriver.TEXTURE_WIDTH) - 
					(int)Gdx.graphics.getWidth() / SteveDriver.TEXTURE_WIDTH / 2;
			yPos = (int)(snakePosition.y / SteveDriver.TEXTURE_LENGTH) +
					r.nextInt((int)Gdx.graphics.getHeight() / SteveDriver.TEXTURE_LENGTH) -
					(int)Gdx.graphics.getHeight() / SteveDriver.TEXTURE_LENGTH / 2;
		}
	
		int locationID = SteveDriver.field.checkRing(xPos, yPos);

		//review code. pick up generation may not be satisfactory. aka grass is nothing, chance for weapon in desert...
		int pickUpType = (locationID == GRASS_ID && offScreen) ?  0 :
			(locationID == DESERT_ID || !offScreen) ?  r.nextInt(2) + ((!offScreen) ? 1 :0) : r.nextInt(2)+1;

		switch(pickUpType){
			case 1:
				if(SteveDriver.snake.hasWeaponSpace())
					this.generateUpgrade(xPos, yPos, r.nextInt(3));
			break;
			
			case 2:
				if(SteveDriver.snake.hasWeaponToUpgrade())
					this.generateWeaponUpgrade(xPos, yPos);
			break;
		}
	}
	
	public void generateApple(boolean offScreen){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();
		int xPos = 0;
		int yPos = 0;
		
		if (offScreen) {
			float xPosTopBot = (snakePosition.x - r.nextInt((int)(Gdx.graphics.getWidth()*.5))
				+ r.nextInt((int)(Gdx.graphics.getWidth())));
			float xPosLeft = (snakePosition.x + (int)(Gdx.graphics.getWidth()*.5)
				+ r.nextInt((int)(Gdx.graphics.getWidth()*.25)));
			float xPosRight = (snakePosition.x - (int)(Gdx.graphics.getWidth()*.5)
				- r.nextInt((int)(Gdx.graphics.getWidth()*.25)));
			float yPosTop = (snakePosition.y + (int)(Gdx.graphics.getHeight()*.5)
				+ r.nextInt((int)(Gdx.graphics.getHeight()*.25)));
			float yPosBot = (snakePosition.y - (int)(Gdx.graphics.getHeight()*.5)
				- r.nextInt((int)(Gdx.graphics.getHeight()*.25)));
			float yPosRightLeft = (snakePosition.y - r.nextInt((int)(Gdx.graphics.getHeight()*.5))
				+ r.nextInt((int)(Gdx.graphics.getHeight())));
		
			int choiceX = (r.nextInt(2)==0) ? 0 : 
				(r.nextInt(2)==0) ? -1 : 1;
			int choiceY = (choiceX == 0) ? (r.nextInt(2)==0) ? 1: -1: 0;
		
			xPos = (int)((choiceX == 0) ? xPosTopBot/16 : 
				(choiceX < 0) ? xPosLeft/16 : xPosRight/16);
			yPos = (int)((choiceY == 0) ? yPosRightLeft/16 : 
				(choiceY < 0) ? yPosBot/16 : yPosTop/16);
		}
		else {
			xPos = (int)(snakePosition.x / SteveDriver.TEXTURE_WIDTH) +
					r.nextInt((int)Gdx.graphics.getWidth() / SteveDriver.TEXTURE_WIDTH) - 
					(int)Gdx.graphics.getWidth() / SteveDriver.TEXTURE_WIDTH / 2;
			yPos = (int)(snakePosition.y / SteveDriver.TEXTURE_LENGTH) +
					r.nextInt((int)Gdx.graphics.getHeight() / SteveDriver.TEXTURE_LENGTH) -
					(int)Gdx.graphics.getHeight() / SteveDriver.TEXTURE_LENGTH / 2;
		}
		
		Apple a = new Apple(xPos, yPos);
		
		if(isOccupied(a.getRectangle())){
			SteveDriver.field.pickups.add(a);
		}
	}
		
	public void generateSlug(float xPos, float yPos){
		Snail s = new Snail(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
	}
	
	public void generateBrute(float xPos, float yPos){
		Brute b = new Brute(xPos, yPos);
		if(isOccupied(b.getRectangle()))
			SteveDriver.field.enemies.add(b);
	}
	
	public void generateAntiSpiral(float xPos, float yPos){
		AntiSpiral s = new AntiSpiral(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
	}
	
	public void generateSpiral(float xPos, float yPos){
		Spiral s = new Spiral(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
	}
	
	public void generateHomaHawk(float xPos, float yPos){
		HomaHawk s = new HomaHawk(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
	}
	
	public void generateTank(float xPos, float yPos){
		Tank t = new Tank(xPos, yPos);
		if(isOccupied(t.getRectangle()))
			SteveDriver.field.enemies.add(t);
	}
	
	public void generateFlyer(float xPos, float yPos){
		Flyer f = new Flyer(xPos, yPos);
		if(isOccupied(f.getRectangle()))
			SteveDriver.field.enemies.add(f);
	}
	
	public void generateTurret(float xPos, float yPos){
		Turret t = new Turret(xPos, yPos);
		if(isOccupied(t.getRectangle()))
			SteveDriver.field.enemies.add(t);
	}
	
	public void generateApple(float xPos, float yPos){
		Apple a = new Apple(xPos, yPos);
		if(isOccupied(a.getRectangle()))
			SteveDriver.field.pickups.add(a);	
	}
	
	public void generateRhino(float xPos, float yPos) {
		Rhino r = new Rhino(xPos, yPos);
		
		if (isOccupied(r.getRectangle())) {
			SteveDriver.field.enemies.add(r);
		}
	}
	
	public void generateUpgrade(float xPos, float yPos, int upgradeType){
		switch(upgradeType){
			case GATLING_GUN_ID:
				GatlingGunPickUp g = new GatlingGunPickUp(xPos, yPos);
				if(isOccupied(g.getRectangle()))
					SteveDriver.field.pickups.add(g);				
				break;
			case LASER_ID:
				LaserPickUp l = new LaserPickUp(xPos, yPos);
				if(isOccupied(l.getRectangle()))
					SteveDriver.field.pickups.add(l);				
				break;
			case SPECIALIST_ID:
				SpecialistPickUp s = new SpecialistPickUp(xPos, yPos);
				if(isOccupied(s.getRectangle()))
					SteveDriver.field.pickups.add(s);				
				break;
		}
	}
	
	public void generateWeaponUpgrade(float xPos, float yPos){
		WeaponUpgrade wU = new WeaponUpgrade(xPos, yPos);
		if(isOccupied(wU.getRectangle())){
			SteveDriver.field.pickups.add(wU);
		}
	}
	
	private boolean isOccupied(Rectangle newObject){
		return SteveDriver.field.isOccupied(newObject);
	}

}
