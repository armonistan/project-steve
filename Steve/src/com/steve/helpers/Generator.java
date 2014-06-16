package com.steve.helpers;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.enemies.Spring;
import com.steve.enemies.Brute;
import com.steve.enemies.Flyer;
import com.steve.enemies.HomaHawk;
import com.steve.enemies.Rhino;
import com.steve.enemies.Slug;
import com.steve.enemies.Narwhal;
import com.steve.enemies.Tank;
import com.steve.enemies.Turret;
import com.steve.pickups.Apple;
import com.steve.pickups.GatlingGunPickup;
import com.steve.pickups.LaserPickup;
import com.steve.pickups.SpecialistPickup;
import com.steve.pickups.WeaponUpgrade;
import com.steve.stages.Field;

public class Generator {
	private final int GATLING_GUN_ID = 0;
	private final int LASER_ID = 1;
	private final int SPECIALIST_ID = 2;
	
	private final int GRASS_ID = 0;
	private final int DESERT_ID = 1;
	private final int BARREN_ID = 2;
	
	private Random r;
	
	final float enemyGenerationTime = 2; 
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
			generatePickUp();
			this.pickUpGenerationCounter = 0;
		}
		else
			pickUpGenerationCounter += Gdx.graphics.getRawDeltaTime();
		
		if(this.appleGenerationCounter > this.appleGenerationTime){
			generateApple();
			this.appleGenerationCounter = 0;
		}
		else
			appleGenerationCounter += Gdx.graphics.getRawDeltaTime();
	}
	
	private void generateEnemy(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();

		//using pixel
		float xPosTopBot = snakePosition.x - r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenWidth") * .5f;
		float xPosRight = snakePosition.x + SteveDriver.constants.get("screenWidth") * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float xPosLeft = snakePosition.x - SteveDriver.constants.get("screenWidth") * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosTop = snakePosition.y + SteveDriver.constants.get("screenWidth") * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosBot = snakePosition.y - SteveDriver.constants.get("screenWidth") * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosRightLeft = snakePosition.y - r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenWidth") * .5f;
		
		//x
		//top/bot: 0 left: -1 right: 1
		int choiceX = 0;
		//y
		//right/left: 0 top = 1 bot = -1
		int choiceY = 0;
		if(SteveDriver.snake.getRotationIndex() == SteveDriver.RIGHT_ID){
			choiceX = 1;
			choiceY = 0;
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.UP_ID){
			choiceX = 0;
			choiceY = 1;
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.LEFT_ID){
			choiceX = -1;
			choiceY = 0;
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.DOWN_ID){
			choiceX = 0;
			choiceY = -1;
		}
		
		int xPos = (int)((choiceX == 0) ? xPosTopBot/SteveDriver.TEXTURE_WIDTH : 
			(choiceX < 0) ? xPosLeft/SteveDriver.TEXTURE_WIDTH : xPosRight/SteveDriver.TEXTURE_WIDTH);
		int yPos = (int)((choiceY == 0) ? yPosRightLeft/SteveDriver.TEXTURE_LENGTH : 
			(choiceY < 0) ? yPosBot/SteveDriver.TEXTURE_LENGTH : yPosTop/SteveDriver.TEXTURE_LENGTH);
	
		int locationID = SteveDriver.field.checkRing(xPos, yPos);
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
	
	public boolean generateEnemyTutorial(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();

		//using pixel
		float xPosTopBot = snakePosition.x - r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenWidth") * .5f;
		float xPosRight = snakePosition.x + SteveDriver.constants.get("screenWidth") * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float xPosLeft = snakePosition.x - SteveDriver.constants.get("screenWidth") * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosTop = snakePosition.y + SteveDriver.constants.get("screenWidth") * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosBot = snakePosition.y - SteveDriver.constants.get("screenWidth") * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosRightLeft = snakePosition.y - r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenWidth") * .5f;
		
		//x
		//top/bot: 0 left: -1 right: 1
		int choiceX = 0;
		//y
		//right/left: 0 top = 1 bot = -1
		int choiceY = 0;
		int spawnChoice = SteveDriver.random.nextInt(4);
		if(spawnChoice == SteveDriver.RIGHT_ID){
			choiceX = 1;
			choiceY = 0;
		}
		else if(spawnChoice == SteveDriver.UP_ID){
			choiceX = 0;
			choiceY = 1;
		}
		else if(spawnChoice == SteveDriver.LEFT_ID){
			choiceX = -1;
			choiceY = 0;
		}
		else if(spawnChoice == SteveDriver.DOWN_ID){
			choiceX = 0;
			choiceY = -1;
		}
		
		float xPos = ((choiceX == 0) ? xPosTopBot/SteveDriver.TEXTURE_WIDTH : 
			(choiceX < 0) ? xPosLeft/SteveDriver.TEXTURE_WIDTH : xPosRight/SteveDriver.TEXTURE_WIDTH);
		float yPos = ((choiceY == 0) ? yPosRightLeft/SteveDriver.TEXTURE_LENGTH : 
			(choiceY < 0) ? yPosBot/SteveDriver.TEXTURE_LENGTH : yPosTop/SteveDriver.TEXTURE_LENGTH);
	
		return generateSlugTutorial(xPos, yPos);
	}
	
	public void generatePickUp(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();

		//using pixel
		float xPosTopBot = snakePosition.x - r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenWidth") * .5f;
		float xPosRight = snakePosition.x + SteveDriver.constants.get("screenWidth") * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float xPosLeft = snakePosition.x - SteveDriver.constants.get("screenWidth") * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosTop = snakePosition.y + SteveDriver.constants.get("screenWidth") * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosBot = snakePosition.y - SteveDriver.constants.get("screenWidth") * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosRightLeft = snakePosition.y - r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenWidth") * .5f;
		
		//x
		//top/bot: 0 left: -1 right: 1
		int choiceX = 0;
		//y
		//right/left: 0 top = 1 bot = -1
		int choiceY = 0;
		if(SteveDriver.snake.getRotationIndex() == SteveDriver.RIGHT_ID){
			choiceX = 1;
			choiceY = 0;
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.UP_ID){
			choiceX = 0;
			choiceY = 1;
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.LEFT_ID){
			choiceX = -1;
			choiceY = 0;
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.DOWN_ID){
			choiceX = 0;
			choiceY = -1;
		}
		
		int xPos = (int)((choiceX == 0) ? xPosTopBot/SteveDriver.TEXTURE_WIDTH : 
			(choiceX < 0) ? xPosLeft/SteveDriver.TEXTURE_WIDTH : xPosRight/SteveDriver.TEXTURE_WIDTH);
		int yPos = (int)((choiceY == 0) ? yPosRightLeft/SteveDriver.TEXTURE_LENGTH : 
			(choiceY < 0) ? yPosBot/SteveDriver.TEXTURE_LENGTH : yPosTop/SteveDriver.TEXTURE_LENGTH);
		
		int locationID = SteveDriver.field.checkRing(xPos, yPos);

		//review code. pick up generation may not be satisfactory. aka grass is nothing, chance for weapon in desert...
		int pickUpType = (locationID == GRASS_ID) ?  0 :
			(locationID == DESERT_ID) ?  r.nextInt(2) : r.nextInt(2)+1;

			
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
		
	public boolean generatePickUpTutorial(int type){
		//using node
		float xPosTopBot = SteveDriver.random.nextInt(SteveDriver.field.totalRadius - SteveDriver.field.desertRadius - SteveDriver.field.desertRadius) + SteveDriver.field.desertRadius;
		
		float xPosRight = SteveDriver.random.nextInt(SteveDriver.field.desertRadius) + SteveDriver.field.totalRadius - (2*SteveDriver.field.desertRadius);
		
		float xPosLeft = SteveDriver.random.nextInt(SteveDriver.field.desertRadius) + SteveDriver.field.desertRadius;
		
		float yPosTop = SteveDriver.random.nextInt(SteveDriver.field.desertRadius) + SteveDriver.field.totalRadius - (2*SteveDriver.field.desertRadius);
		
		float yPosBot = SteveDriver.random.nextInt(SteveDriver.field.desertRadius) + SteveDriver.field.desertRadius;
		
		float yPosRightLeft = SteveDriver.random.nextInt(SteveDriver.field.totalRadius - SteveDriver.field.desertRadius - SteveDriver.field.desertRadius) + SteveDriver.field.desertRadius;

		//System.out.println(xPosTopBot + ", " + yPosTop); //y < 500 y > 100  x < 500 x > 100
		
		//x
		//top/bot: 0 left: -1 right: 1
		int choiceX = 0;
		//y
		//right/left: 0 top = 1 bot = -1
		int choiceY = 0;
		int spawnChoice = SteveDriver.random.nextInt(4);
		if(spawnChoice == SteveDriver.RIGHT_ID){
			choiceX = 1;
			choiceY = 0;
		}
		else if(spawnChoice == SteveDriver.UP_ID){
			choiceX = 0;
			choiceY = 1;
		}
		else if(spawnChoice == SteveDriver.LEFT_ID){
			choiceX = -1;
			choiceY = 0;
		}
		else if(spawnChoice == SteveDriver.DOWN_ID){
			choiceX = 0;
			choiceY = -1;
		}
		
		float xPos = ((choiceX == 0) ? xPosTopBot : 
			(choiceX < 0) ? xPosLeft : xPosRight);
		float yPos = ((choiceY == 0) ? yPosRightLeft : 
			(choiceY < 0) ? yPosBot : yPosTop);
	
		int pickUpType = type;
		
		switch(pickUpType){
		case 1:
				return this.generateUpgradeTutorial(xPos, yPos, r.nextInt(3));
		case 2:
				return this.generateWeaponUpgradeTutorial(xPos, yPos);
		}
	
		return false;
	}
	
	public void generateSlug(float xPos, float yPos){
		Slug s = new Slug(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
	}
	
	public boolean generateSlugTutorial(float xPos, float yPos){
		Slug s = new Slug(xPos, yPos);
		if(isOccupied(s.getRectangle())){
			SteveDriver.field.enemies.add(s);
			return true;
		}
		
		return false;
	}
	
	public void generateBrute(float xPos, float yPos){
		Brute b = new Brute(xPos, yPos);
		if(isOccupied(b.getRectangle()))
			SteveDriver.field.enemies.add(b);
	}
	
	public void generateAntiSpiral(float xPos, float yPos){
		Spring s = new Spring(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
	}
	
	public void generateSpiral(float xPos, float yPos){
		Narwhal s = new Narwhal(xPos, yPos);
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
	
	public void generateApple(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();

		//using pixel
		float xPosTopBot = snakePosition.x - r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenWidth") * .5f;
		float xPosRight = snakePosition.x + SteveDriver.constants.get("screenWidth") * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float xPosLeft = snakePosition.x - SteveDriver.constants.get("screenWidth") * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosTop = snakePosition.y + SteveDriver.constants.get("screenWidth") * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosBot = snakePosition.y - SteveDriver.constants.get("screenWidth") * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f;
		float yPosRightLeft = snakePosition.y - r.nextFloat() * SteveDriver.constants.get("screenWidth") * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenWidth") * .5f;
		
		//x
		//top/bot: 0 left: -1 right: 1
		int choiceX = 0;
		//y
		//right/left: 0 top = 1 bot = -1
		int choiceY = 0;
		if(SteveDriver.snake.getRotationIndex() == SteveDriver.RIGHT_ID){
			choiceX = 1;
			choiceY = 0;
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.UP_ID){
			choiceX = 0;
			choiceY = 1;
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.LEFT_ID){
			choiceX = -1;
			choiceY = 0;
		}
		else if(SteveDriver.snake.getRotationIndex() == SteveDriver.DOWN_ID){
			choiceX = 0;
			choiceY = -1;
		}
		
		int xPos = (int)((choiceX == 0) ? xPosTopBot/SteveDriver.TEXTURE_WIDTH : 
			(choiceX < 0) ? xPosLeft/16 : xPosRight/SteveDriver.TEXTURE_WIDTH);
		int yPos = (int)((choiceY == 0) ? yPosRightLeft/SteveDriver.TEXTURE_LENGTH : 
			(choiceY < 0) ? yPosBot/16 : yPosTop/SteveDriver.TEXTURE_LENGTH);
		
		
		Apple a = new Apple(xPos, yPos);
		if(isOccupied(a.getRectangle()))
			Field.pickups.add(a);	
	}
	
	public void generateRhino(float xPos, float yPos) {
		Rhino r = new Rhino(xPos, yPos);
		
		if (isOccupied(r.getRectangle())) {
			SteveDriver.field.enemies.add(r);
		}
	}
	
	public boolean generateAppleTutorial (){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();
		int xPos = 0;
		int yPos = 0;
		
		xPos = (int)(snakePosition.x / SteveDriver.TEXTURE_WIDTH) +
					r.nextInt((int)SteveDriver.guiCamera.viewportWidth / SteveDriver.TEXTURE_WIDTH) - 
					(int)SteveDriver.guiCamera.viewportWidth / SteveDriver.TEXTURE_WIDTH / 2;
		yPos = (int)(snakePosition.y / SteveDriver.TEXTURE_LENGTH) +
					r.nextInt((int)SteveDriver.guiCamera.viewportWidth / SteveDriver.TEXTURE_LENGTH) -
					(int)SteveDriver.guiCamera.viewportWidth / SteveDriver.TEXTURE_LENGTH / 2;
		
		Apple a = new Apple(xPos, yPos);
		
		if(isOccupied(a.getRectangle())){
			Field.pickups.add(a);
			return true;
		}
		return false;
	}
	
	public void generateUpgrade(float xPos, float yPos, int upgradeType){
		switch(upgradeType){
			case GATLING_GUN_ID:
				GatlingGunPickup g = new GatlingGunPickup(xPos, yPos);
				if(isOccupied(g.getRectangle())){
					Field.pickups.add(g);
				}
				break;
			case LASER_ID:
				LaserPickup l = new LaserPickup(xPos, yPos);
				if(isOccupied(l.getRectangle()))
					Field.pickups.add(l);				
				break;
			case SPECIALIST_ID:
				SpecialistPickup s = new SpecialistPickup(xPos, yPos);
				if(isOccupied(s.getRectangle()))
					Field.pickups.add(s);				
				break;
		}
	}
	
	public boolean generateUpgradeTutorial(float xPos, float yPos, int upgradeType){
		switch(upgradeType){
			case GATLING_GUN_ID:
				GatlingGunPickup g = new GatlingGunPickup(xPos, yPos);
				if(isOccupied(g.getRectangle())){
					Field.pickups.add(g);
					return true;
				}
				break;
			case LASER_ID:
				LaserPickup l = new LaserPickup(xPos, yPos);
				if(isOccupied(l.getRectangle())){
					Field.pickups.add(l);			
					return true;
				}
				break;
				
			case SPECIALIST_ID:
				SpecialistPickup s = new SpecialistPickup(xPos, yPos);
				if(isOccupied(s.getRectangle())){
					Field.pickups.add(s);
					return true;
				}
				break;
		}
		
		return false;
	}
	
	public void generateWeaponUpgrade(float xPos, float yPos){
		WeaponUpgrade wU = new WeaponUpgrade(xPos, yPos);
		if(isOccupied(wU.getRectangle())){
			Field.pickups.add(wU);
		}
	}
	
	public boolean generateWeaponUpgradeTutorial(float xPos, float yPos){
		WeaponUpgrade wU = new WeaponUpgrade(xPos, yPos);
		if(isOccupied(wU.getRectangle())){
			Field.pickups.add(wU);
			return true;
		}
		return false;
	}
	
	private boolean isOccupied(Rectangle newObject){
		return SteveDriver.field.isOccupied(newObject);
	}

}
