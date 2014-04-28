package com.steve;

import java.util.Random;

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
	//temp
	private int tempCounter;
	private int tempCounter2;
	
	public Generator(){
		tempCounter = 0;
		r = new Random();
	}

	public void update(){
		int temper = 2;//r.nextInt(3);
		int temper2 = r.nextInt(2);
		int xPos = r.nextInt(SteveDriver.field.getFieldRadius());
		int yPos = r.nextInt(SteveDriver.field.getFieldRadius());
		if(tempCounter%50==0)
			switch(temper){
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
		if(tempCounter2%1000==0)
			switch(temper2){
			case 0:
				if(SteveDriver.snake.hasWeaponSpace())
					this.generateUpgrade(xPos, yPos, temper);
				break;
			case 1:
				this.generateApple(xPos, yPos);
				break;
			}
		tempCounter++;
		tempCounter2++;
	}
	
	public void generateSlug(float xPos, float yPos){
		Snail s = new Snail(new Vector2(xPos, yPos));
		if(isOccupied(s.getRectangle()) && isPlayerSafe(xPos,yPos) 
				&& SteveDriver.field.checkRing((int)xPos, (int)yPos) == GRASS_ID)
			SteveDriver.field.enemies.add(s);
	}
	
	public void genrateBrute(float xPos, float yPos){
		Brute b = new Brute(new Vector2(xPos, yPos));
		if(isOccupied(b.getRectangle()) && isPlayerSafe(xPos,yPos)
				&& SteveDriver.field.checkRing((int)xPos, (int)yPos) == DESERT_ID)
			SteveDriver.field.enemies.add(b);
	}
	
	public void generateTank(float xPos, float yPos){
		Tank t = new Tank(new Vector2(xPos, yPos));
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
