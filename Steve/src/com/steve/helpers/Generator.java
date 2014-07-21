package com.steve.helpers;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.enemies.Ring;
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
import com.steve.pickups.BossSummon;
import com.steve.pickups.GatlingGunPickUp;
import com.steve.pickups.LaserPickUp;
import com.steve.pickups.SpecialistPickUp;
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
	
	float enemyGenerationTime = 2; 
	float enemyGenerationCounter; 
	
	final float appleGenerationTime = 5; 
	float appleGenerationCounter; 
	
	final float pickUpGenerationTime = 4f; 
	float pickUpGenerationCounter; 
	
	final float upgradeGenerationTime = 6; 
	float upgradeGenerationCounter; 
	
	public Generator(){
		enemyGenerationCounter = 0; 
		appleGenerationCounter = this.appleGenerationTime; 
		pickUpGenerationCounter = 0; 
		upgradeGenerationCounter = 0; 
		SteveDriver.numApples = 0;
		
		
		r = new Random();
	}

	public void update(){
		if(this.enemyGenerationCounter > this.enemyGenerationTime){
			if(generateEnemy())
				this.enemyGenerationCounter = 0;
		}
		else
			enemyGenerationCounter += Gdx.graphics.getRawDeltaTime();
		
		if(this.pickUpGenerationCounter > this.pickUpGenerationTime){
			if(generatePickUp())
				this.pickUpGenerationCounter = 0;
		}
		else
			pickUpGenerationCounter += Gdx.graphics.getRawDeltaTime();
		
		if(SteveDriver.numApples < 1){
			if(generateApple())
				this.appleGenerationCounter = 0;
		}
		else
			appleGenerationCounter += Gdx.graphics.getRawDeltaTime();

		this.enemyGenerationTime = 2 - SteveDriver.snake.getWeapons().size()/(SteveDriver.constants.get("maxLength"));
	}
	
	private boolean generateEnemy(){
		//our center would like to fix
		Vector3 cameraPosition = SteveDriver.camera.position;
		
		//using pixel
		//for the x axis top and bottom
		float xPosTopBot = cameraPosition.x - SteveDriver.constants.get("screenHeight") * .5f +
				r.nextFloat() * SteveDriver.constants.get("screenHeight");
		//for the x axis right
		float xPosRight = cameraPosition.x + SteveDriver.constants.get("screenHeight") * .25f
				+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * .25f;
		//for the x axis left
		float xPosLeft = cameraPosition.x - SteveDriver.constants.get("screenHeight") * .25f
				- r.nextFloat() * SteveDriver.constants.get("screenHeight") * .25f;
		//for the y axis top
		float yPosTop = cameraPosition.y + SteveDriver.constants.get("screenHeight") * .25f
				+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * .25f;
		//for the y axis bot
		float yPosBot = cameraPosition.y - SteveDriver.constants.get("screenHeight") *.25f
				- r.nextFloat() * SteveDriver.constants.get("screenHeight") *.25f;
		//for the y axis right and left
		float yPosRightLeft = cameraPosition.y - r.nextFloat() * SteveDriver.constants.get("screenHeight") * .5f +
				r.nextFloat() * SteveDriver.constants.get("screenHeight");
		
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
		
		int xPos = (int)((choiceX == 0) ? xPosTopBot/SteveDriver.TEXTURE_SIZE : 
			(choiceX < 0) ? xPosLeft/SteveDriver.TEXTURE_SIZE : xPosRight/SteveDriver.TEXTURE_SIZE);
		int yPos = (int)((choiceY == 0) ? yPosRightLeft/SteveDriver.TEXTURE_SIZE : 
			(choiceY < 0) ? yPosBot/SteveDriver.TEXTURE_SIZE : yPosTop/SteveDriver.TEXTURE_SIZE);
	
		int locationID = SteveDriver.field.checkRing(xPos, yPos);
		int enemyType = (locationID == GRASS_ID) ?  r.nextInt(1) :
			(locationID == DESERT_ID) ?  r.nextInt(4) + 1 : r.nextInt(4) + 5;
		
		if (locationID < 3) {
			if(SteveDriver.random.nextInt(100)+1 <= locationID){
				return this.generateRing(xPos, yPos);
			}
	
			switch(enemyType){
			case 0:
				return this.generateSlug(xPos, yPos);
			case 1:
				return this.generateTank(xPos, yPos);
			case 2:
				return this.generateBrute(xPos, yPos);
			case 3:
				return this.generateAntiSpiral(xPos, yPos);
			case 4:
				return this.generateFlyer(xPos, yPos);
			case 5:
				return this.generateTurret(xPos, yPos);
			case 6:
				return this.generateSpiral(xPos, yPos);
			case 7:
				return this.generateHomaHawk(xPos, yPos);
			case 8:
				return this.generateRhino(xPos, yPos);
			}
		}
		
		return true;
	}
	
	public boolean generateEnemyTutorial(){
		Vector3 snakePosition = SteveDriver.snake.getHeadPosition();

		//using pixel
		float xPosTopBot = snakePosition.x - r.nextFloat() * SteveDriver.constants.get("screenHeight") * 2 * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenHeight") * 2 * .5f;
		float xPosRight = snakePosition.x + SteveDriver.constants.get("screenHeight") * 2 * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * 2 * .25f;
		float xPosLeft = snakePosition.x - SteveDriver.constants.get("screenHeight") * 2 * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenHeight") * 2 * .25f;
		float yPosTop = snakePosition.y + SteveDriver.constants.get("screenHeight") * 2 * .5f
				+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * 2 * .25f;
		float yPosBot = snakePosition.y - SteveDriver.constants.get("screenHeight") * 2 * .5f
				- r.nextFloat() * SteveDriver.constants.get("screenHeight") * 2 * .25f;
		float yPosRightLeft = snakePosition.y - r.nextFloat() * SteveDriver.constants.get("screenHeight") * 2 * .25f +
				r.nextFloat() * SteveDriver.constants.get("screenHeight") * 2 * .5f;
		
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
		
		float xPos = ((choiceX == 0) ? xPosTopBot/SteveDriver.TEXTURE_SIZE : 
			(choiceX < 0) ? xPosLeft/SteveDriver.TEXTURE_SIZE : xPosRight/SteveDriver.TEXTURE_SIZE);
		float yPos = ((choiceY == 0) ? yPosRightLeft/SteveDriver.TEXTURE_SIZE : 
			(choiceY < 0) ? yPosBot/SteveDriver.TEXTURE_SIZE : yPosTop/SteveDriver.TEXTURE_SIZE);
	
		return generateSlugTutorial(xPos, yPos);
	}

	public boolean generatePickUp(){
		//our center would like to fix
		Vector3 cameraPosition = SteveDriver.camera.position;
		
		//using pixel
		//for the x axis top and bottom
		float xPosTopBot = cameraPosition.x - SteveDriver.constants.get("screenHeight") * .5f +
				r.nextFloat() * SteveDriver.constants.get("screenHeight");
		//for the x axis right
		float xPosRight = cameraPosition.x + SteveDriver.constants.get("screenHeight") * .25f
				+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * .25f;
		//for the x axis left
		float xPosLeft = cameraPosition.x - SteveDriver.constants.get("screenHeight") * .25f
				- r.nextFloat() * SteveDriver.constants.get("screenHeight") * .25f;
		//for the y axis top
		float yPosTop = cameraPosition.y + SteveDriver.constants.get("screenHeight") * .25f
				+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * .25f;
		//for the y axis bot
		float yPosBot = cameraPosition.y - SteveDriver.constants.get("screenHeight") *.25f
				- r.nextFloat() * SteveDriver.constants.get("screenHeight") *.25f;
		//for the y axis right and left
		float yPosRightLeft = cameraPosition.y - r.nextFloat() * SteveDriver.constants.get("screenHeight") * .5f +
				r.nextFloat() * SteveDriver.constants.get("screenHeight");
		
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
		
		int xPos = (int)((choiceX == 0) ? xPosTopBot/SteveDriver.TEXTURE_SIZE : 
			(choiceX < 0) ? xPosLeft/SteveDriver.TEXTURE_SIZE : xPosRight/SteveDriver.TEXTURE_SIZE);
		int yPos = (int)((choiceY == 0) ? yPosRightLeft/SteveDriver.TEXTURE_SIZE : 
			(choiceY < 0) ? yPosBot/SteveDriver.TEXTURE_SIZE : yPosTop/SteveDriver.TEXTURE_SIZE);
		
		int locationID = SteveDriver.field.checkRing(xPos, yPos);

		//review code. pick up generation may not be satisfactory. aka grass is nothing, chance for weapon in desert...
		int pickUpType = (locationID == GRASS_ID) ?  0 :
			(locationID == DESERT_ID) ?  1 : r.nextInt(2)+1;
			
		float deltaX = Math.abs(xPos - SteveDriver.field.totalRadius/2);
		float deltaY = Math.abs(yPos - SteveDriver.field.totalRadius/2);
		float angle = MathUtils.radiansToDegrees*MathUtils.atan2(deltaX, deltaY);
		float deltaWhole = Math.abs(angle-45);
		float deltaFraction = 1-deltaWhole/45;
		
		if(pickUpType == 1 &&
				Math.sqrt(CollisionHelper.distanceSquared(xPos, yPos, SteveDriver.field.totalRadius/2, SteveDriver.field.totalRadius/2)) < (SteveDriver.field.grassRadius/2+(12+12*deltaFraction))){
			pickUpType = 0;
		}
		else if(pickUpType == 2 &&
				Math.sqrt(CollisionHelper.distanceSquared(xPos, yPos, SteveDriver.field.totalRadius/2, SteveDriver.field.totalRadius/2)) < (SteveDriver.field.desertRadius/2+(12+12*deltaFraction)))
			pickUpType = 0;
		else
			;
			
		if (locationID < 3) {
			switch(pickUpType){
				case 1:
				if(SteveDriver.snake.hasWeaponSpace())
					return this.generateUpgrade(xPos, yPos, r.nextInt(3));
				break;
			
				case 2:
				if(SteveDriver.snake.hasWeaponToUpgrade())
					return this.generateWeaponUpgrade(xPos, yPos);
				break;
			}
		}
		
		return true;
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
	
	public boolean generateRing(float xPos, float yPos){
		Ring r = new Ring(xPos, yPos);
		if(isOccupied(r.getRectangle()))
			SteveDriver.field.enemies.add(r);	
		else
			return false;
		return true;
	}
	
	public boolean generateSlug(float xPos, float yPos){
		Slug s = new Slug(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
		else
			return false;
		return true;
	}
	
	public boolean generateSlugTutorial(float xPos, float yPos){
		Slug s = new Slug(xPos, yPos);
		if(isOccupied(s.getRectangle())){
			SteveDriver.field.enemies.add(s);
			return true;
		}
		
		return false;
	}
	
	public boolean generateBossSummon(int summonType){
		int offset = SteveDriver.field.desertRadius;
		
		float xPosTopBot = SteveDriver.random.nextInt(SteveDriver.field.totalRadius - offset - offset) + offset;
		
		float xPosRight = SteveDriver.random.nextInt(offset) + SteveDriver.field.totalRadius - (2*offset);
	
		float xPosLeft = SteveDriver.random.nextInt(offset) + offset;
	
		float yPosTop = SteveDriver.random.nextInt(offset) + SteveDriver.field.totalRadius - (2*offset);
	
		float yPosBot = SteveDriver.random.nextInt(offset) + offset;
	
		float yPosRightLeft = SteveDriver.random.nextInt(SteveDriver.field.totalRadius - offset - offset) + offset;
		
		if(summonType == 1){
		//using node
			xPosTopBot = SteveDriver.random.nextInt(SteveDriver.field.totalRadius);
		
			xPosRight = SteveDriver.random.nextInt(SteveDriver.field.desertRadius)
					- SteveDriver.field.desertRadius  + SteveDriver.field.totalRadius;
				
			xPosLeft = SteveDriver.random.nextInt(SteveDriver.field.desertRadius);
				
			yPosTop = SteveDriver.random.nextInt(SteveDriver.field.desertRadius)
					- SteveDriver.field.desertRadius  + SteveDriver.field.totalRadius;
				
			yPosBot = SteveDriver.random.nextInt(SteveDriver.field.desertRadius);

			yPosRightLeft = SteveDriver.random.nextInt(SteveDriver.field.totalRadius);
		}
		
		//System.out.println(xPosTopBot + ", " + yPosTop); //y < 500 y > 100  x < 500 x > 100
		
		//x
		//top/bot: 0 left: -1 right: 1
		int choiceX = 0;
		//y
		//right/left: 0 top = 1 bot = -1
		int choiceY = 0;
		int spawnChoice =0;//= SteveDriver.random.nextInt(4);
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
		BossSummon bs = new BossSummon(xPos, yPos, summonType);
		if(SteveDriver.field.isOccupied(bs.getBoundingRectangle())){
			SteveDriver.field.pickups.add(bs);
			return true;
		}
		return false;
	}
	
	public boolean generateBrute(float xPos, float yPos){
		Brute b = new Brute(xPos, yPos);
		if(isOccupied(b.getRectangle()))
			SteveDriver.field.enemies.add(b);
		else
			return false;
		return true;
	}
	
	public boolean generateAntiSpiral(float xPos, float yPos){
		Spring s = new Spring(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
		else
			return false;
		return true;
	}
	
	public boolean generateSpiral(float xPos, float yPos){
		Narwhal s = new Narwhal(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
		else
			return false;
		return true;
	}
	
	public boolean generateHomaHawk(float xPos, float yPos){
		HomaHawk s = new HomaHawk(xPos, yPos);
		if(isOccupied(s.getRectangle()))
			SteveDriver.field.enemies.add(s);
		else
			return false;
		return true;
	}
	
	public boolean generateTank(float xPos, float yPos){
		Tank t = new Tank(xPos, yPos);
		if(isOccupied(t.getRectangle()))
			SteveDriver.field.enemies.add(t);
		else
			return false;
		return true;
	}
	
	public boolean generateFlyer(float xPos, float yPos){
		Flyer f = new Flyer(xPos, yPos);
		if(isOccupied(f.getRectangle()))
			SteveDriver.field.enemies.add(f);
		else
			return false;
		return true;
	}
	
	public boolean generateTurret(float xPos, float yPos){
		Turret t = new Turret(xPos, yPos);
		if(isOccupied(t.getRectangle()))
			SteveDriver.field.enemies.add(t);
		else
			return false;
		return true;
	}
	
	public boolean generateApple(){
		//our center would like to fix
		Vector3 cameraPosition = SteveDriver.camera.position;
		
		//using pixel
		//for the x axis top and bottom
		float xPosTopBot = cameraPosition.x - SteveDriver.constants.get("screenHeight") * .2f +
				r.nextFloat() * SteveDriver.constants.get("screenHeight")*.4f;
		//for the x axis right
		float xPosRight = cameraPosition.x + SteveDriver.constants.get("screenHeight") * .4f
				+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * .05f;
		//for the x axis left
		float xPosLeft = cameraPosition.x - SteveDriver.constants.get("screenHeight") * .4f
				- r.nextFloat() * SteveDriver.constants.get("screenHeight") * .05f;
		//for the y axis top
		float yPosTop = cameraPosition.y + SteveDriver.constants.get("screenHeight") * .4f
				+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * .05f;
		//for the y axis bot
		float yPosBot = cameraPosition.y - SteveDriver.constants.get("screenHeight") *.4f
				- r.nextFloat() * SteveDriver.constants.get("screenHeight") * .05f;
		//for the y axis right and left
		float yPosRightLeft = cameraPosition.y - r.nextFloat() * SteveDriver.constants.get("screenHeight") * .2f +
				r.nextFloat() * SteveDriver.constants.get("screenHeight")*.4f;
		
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
		
		int xPos = (int)((choiceX == 0) ? xPosTopBot/SteveDriver.TEXTURE_SIZE : 
			(choiceX < 0) ? xPosLeft/SteveDriver.TEXTURE_SIZE : xPosRight/SteveDriver.TEXTURE_SIZE);
		int yPos = (int)((choiceY == 0) ? yPosRightLeft/SteveDriver.TEXTURE_SIZE : 
			(choiceY < 0) ? yPosBot/SteveDriver.TEXTURE_SIZE : yPosTop/SteveDriver.TEXTURE_SIZE);
		
		if (SteveDriver.field.checkRing(xPos, yPos) < 3) {
			//System.out.print("Trying: ");
			Apple a = new Apple(xPos, yPos);
			if(isOccupied(a.getRectangle())) {
				Field.pickups.add(a);
				//System.out.println("success");
			}
			else{
				SteveDriver.numApples--;
				//System.out.println("failed");
				return false;
			}
		}
		return true;
	}
	
	public boolean generateRhino(float xPos, float yPos) {
		Rhino r = new Rhino(xPos, yPos);
		
		if (isOccupied(r.getRectangle())) {
			SteveDriver.field.enemies.add(r);
		}
		else
			return false;
		return true;
	}
	
	public boolean generateAppleTutorial (){
		//our center would like to fix
				Vector3 cameraPosition = SteveDriver.camera.position;
				
				//using pixel
				//for the x axis top and bottom
				float xPosTopBot = cameraPosition.x - SteveDriver.constants.get("screenHeight") * .2f +
						r.nextFloat() * SteveDriver.constants.get("screenHeight")*.4f;
				//for the x axis right
				float xPosRight = cameraPosition.x + SteveDriver.constants.get("screenHeight") * .4f
						+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * .05f;
				//for the x axis left
				float xPosLeft = cameraPosition.x - SteveDriver.constants.get("screenHeight") * .4f
						- r.nextFloat() * SteveDriver.constants.get("screenHeight") * .05f;
				//for the y axis top
				float yPosTop = cameraPosition.y + SteveDriver.constants.get("screenHeight") * .4f
						+ r.nextFloat() * SteveDriver.constants.get("screenHeight") * .05f;
				//for the y axis bot
				float yPosBot = cameraPosition.y - SteveDriver.constants.get("screenHeight") *.4f
						- r.nextFloat() * SteveDriver.constants.get("screenHeight") * .05f;
				//for the y axis right and left
				float yPosRightLeft = cameraPosition.y - r.nextFloat() * SteveDriver.constants.get("screenHeight") * .2f +
						r.nextFloat() * SteveDriver.constants.get("screenHeight")*.4f;
				
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
				
				int xPos = (int)((choiceX == 0) ? xPosTopBot/SteveDriver.TEXTURE_SIZE : 
					(choiceX < 0) ? xPosLeft/SteveDriver.TEXTURE_SIZE : xPosRight/SteveDriver.TEXTURE_SIZE);
				int yPos = (int)((choiceY == 0) ? yPosRightLeft/SteveDriver.TEXTURE_SIZE : 
					(choiceY < 0) ? yPosBot/SteveDriver.TEXTURE_SIZE : yPosTop/SteveDriver.TEXTURE_SIZE);
				
				if (SteveDriver.field.checkRing(xPos, yPos) < 3) {
					//System.out.print("Trying: ");
					Apple a = new Apple(xPos, yPos);
					if(isOccupied(a.getRectangle())) {
						Field.pickups.add(a);
						//System.out.println("success");
					}
					else{
						SteveDriver.numApples--;
						//System.out.println("failed");
						return false;
					}
				}
				return true;
	}
	
	public boolean generateUpgrade(float xPos, float yPos, int upgradeType){
		switch(upgradeType){
			case GATLING_GUN_ID:
				GatlingGunPickUp g = new GatlingGunPickUp(xPos, yPos);
				if(isOccupied(g.getRectangle())){
					Field.pickups.add(g);
				}
				else
					return false;
				return true;
			case LASER_ID:
				LaserPickUp l = new LaserPickUp(xPos, yPos);
				if(isOccupied(l.getRectangle()))
					Field.pickups.add(l);
				else
					return false;
				return true;
			case SPECIALIST_ID:
				SpecialistPickUp s = new SpecialistPickUp(xPos, yPos);
				if(isOccupied(s.getRectangle()))
					Field.pickups.add(s);				
				else
					return false;
				return true;
		}
		return true;
	}
	
	public boolean generateUpgradeTutorial(float xPos, float yPos, int upgradeType){
		switch(upgradeType){
			case GATLING_GUN_ID:
				GatlingGunPickUp g = new GatlingGunPickUp(xPos, yPos);
				if(isOccupied(g.getRectangle())){
					Field.pickups.add(g);
					return true;
				}
				break;
			case LASER_ID:
				LaserPickUp l = new LaserPickUp(xPos, yPos);
				if(isOccupied(l.getRectangle())){
					Field.pickups.add(l);			
					return true;
				}
				break;
				
			case SPECIALIST_ID:
				SpecialistPickUp s = new SpecialistPickUp(xPos, yPos);
				if(isOccupied(s.getRectangle())){
					Field.pickups.add(s);
					return true;
				}
				break;
		}
		
		return false;
	}
	
	public boolean generateWeaponUpgrade(float xPos, float yPos){
		WeaponUpgrade wU = new WeaponUpgrade(xPos, yPos);
		if(isOccupied(wU.getRectangle())){
			Field.pickups.add(wU);
		}
		else
			return false;
		return true;
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
