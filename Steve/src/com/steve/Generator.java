package com.steve;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Generator {
	
	private final int MIN_DISTANCE = 100;
	private final int GATLING_GUN_ID = 0;
	private final int LASER_ID = 1;
	private final int SPECIALIST_ID = 2;
	
	private final int GRASS_ID = 0;
	private final int DESERT_ID = 1;
	private final int BARREN_ID = 2;
	
	private Random r;
	
	final float enemyGenerationTime = 5; 
	float enemyGenerationCounter; 
	
	final float appleGenerationTime = 5; 
	float appleGenerationCounter; 
	
	final float pickUpGenerationTime = 5; 
	float pickUpGenerationCounter; 
	
	final float upgradeGenerationTime = 5; 
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
		
		appleGenerationCounter += Gdx.graphics.getRawDeltaTime();
		pickUpGenerationCounter += Gdx.graphics.getRawDeltaTime();
		upgradeGenerationCounter += Gdx.graphics.getRawDeltaTime();
	}
	
	private void generateEnemy(){
		float xPos = r.nextInt(20);
		float yPos = r.nextInt(20);
		int enemyRange = (SteveDriver.field.checkRing((int)xPos, (int)yPos) == GRASS_ID) ?  0 :
			(SteveDriver.field.checkRing((int)xPos, (int)yPos) == GRASS_ID) ?  2 : 3;
		
		int enemyType = r.nextInt(enemyRange+2)+enemyRange;
		//r.next
		switch(enemyType){
			case 0:
				this.generateSlug(xPos, yPos);
				break;
			case 1:
				this.genrateBrute(xPos, yPos);
				break;
			case 2:
				this.generateTank(xPos, yPos);
				break;
		}
	}
	
	private void generateBasePickUp(){
		int xPos = 0;
		int yPos = 0;
		int temper = 0;
		switch(temper){
		case 0:
			if(SteveDriver.snake.hasWeaponSpace())
				this.generateUpgrade(xPos, yPos, temper);
			break;
		case 1:
			this.generateApple(xPos, yPos);
			break;
		}
	}
	
	public void generateSlug(float xPos, float yPos){
		Snail s = new Snail(xPos, yPos);
		if(isOccupied(s.getRectangle()) && isPlayerSafe(xPos,yPos) 
				&& )
			SteveDriver.field.enemies.add(s);
	}
	
	public void genrateBrute(float xPos, float yPos){
		Brute b = new Brute(xPos, yPos);
		if(isOccupied(b.getRectangle()) && isPlayerSafe(xPos,yPos)
				&& SteveDriver.field.checkRing((int)xPos, (int)yPos) == DESERT_ID)
			SteveDriver.field.enemies.add(b);
	}
	
	public void generateTank(float xPos, float yPos){
		Tank t = new Tank(xPos, yPos);
		if(isOccupied(t.getRectangle()) && isPlayerSafe(xPos,yPos) 
				&& SteveDriver.field.checkRing((int)xPos, (int)yPos) == DESERT_ID)
			SteveDriver.field.enemies.add(t);	
	}
	
	public void generateApple(float xPos, float yPos){
		Apple a = new Apple(xPos, yPos);
		if(isOccupied(a.getRectangle()) && isPlayerSafe(xPos,yPos))
			SteveDriver.field.pickups.add(a);	
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
	
	private boolean isOccupied(Rectangle newObject){
		return SteveDriver.field.isOccupied(newObject);
	}
	
	private boolean isPlayerSafe(float xPos, float yPos){
		float distance = 10000;
		Vector2 proposedPosition = new Vector2(xPos, yPos); 
		
		for(Sprite s: SteveDriver.snake.getSegments()){
			distance = (distance > proposedPosition.dst(s.getX(), s.getY())) ?  (proposedPosition.dst(s.getX(), s.getY())):(distance);
		}
		
		return distance > this.MIN_DISTANCE;
	}
}
