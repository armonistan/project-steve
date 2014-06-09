package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.*;
import com.steve.base.Pickup;
import com.steve.base.Projectile;
import com.steve.base.Weapon;
import com.steve.helpers.CollisionHelper;
import com.steve.weapons.GatlingGun;
import com.steve.weapons.Laser;
import com.steve.weapons.MainCannon;
import com.steve.weapons.Specialist;

import java.util.*;

public class Snake {
	private ArrayList<Sprite> segments;
	private ArrayList<Weapon> weapons;
	private int maxSegments = 10;
	
	private final int beltImageOffset = 64;
	private final int TILE_WIDTH = 16;
	
	private Vector3 headPosition;
	
	private final float TIME_BETWEEN_TURN = 0.5f;
	private float timeTillStarve = 1000f; //unit is seconds
	private float timer = 0;
	private float hungerTimer = 0;
	
	private Vector2 nextDirection;
	private float nextRotation;
	
	private int money;
	private int snakeTier;
	
	private int helmetTier;
	private int helmetHits;
	
	public Snake(float x, float y){
		segments = new ArrayList<Sprite>();
		weapons = new ArrayList<Weapon>();
		segments.add(new Sprite(new TextureRegion(SteveDriver.atlas, 0, 0, 16, 16)));
		segments.add(new Sprite(new TextureRegion(SteveDriver.atlas, 0, 16, 16, 16)));
		
		nextDirection = SteveDriver.VRIGHT;
		nextRotation = SteveDriver.RIGHT;
		segments.get(0).setRotation(0);
		headPosition = new Vector3(x * SteveDriver.TEXTURE_WIDTH, y * SteveDriver.TEXTURE_LENGTH, 0);
		segments.get(0).setPosition(headPosition.x, headPosition.y);
		segments.get(1).setPosition(headPosition.x, headPosition.y-SteveDriver.TEXTURE_WIDTH);

		//TODO: Make this better
		money = ((SteveDriver.prefs.contains("money")) ? SteveDriver.prefs.getInteger("money") : 0);
		
		for (int i = 0; i < SteveDriver.constants.get("startLength"); i++) {
			this.addBody();
			animate();
		}
		
		if (SteveDriver.constants.get("mainGun") == 1.0f) {
			this.addBody();
			this.mountUpgrade(3);
		}
		
		timeTillStarve *= SteveDriver.constants.get("hitpoints");
		
		snakeTier = 1;
		//will be used to save which tier of helmet it is
		helmetTier = SteveDriver.storePrefs.contains("helmetTier") ? SteveDriver.storePrefs.getInteger("helmetTier") : 0;
		//need to add logic for the different helmet tiers. for instance, drill helmet won't break.
		helmetHits = helmetTier;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void addMoney(int amount) {
		money += amount * SteveDriver.constants.get("goldModifier");
		
		//TODO: Make this only save when needed.
		SteveDriver.prefs.putInteger("money", money);
	}
	
	public boolean spendMoney(int amount) {
		if (money >= amount) {
			money -= amount;
			return true;
		}
		else {
			return false;
		}
	}
	
	public float GetHungerTimer() {
		return this.hungerTimer;
	}
	
	public float GetStarveTime() {
		return this.timeTillStarve;
	}

	public void update(float deltaTime){
		getTouch();
		checkProjectiles();
		
		//update all the segments.
		if (timer >= TIME_BETWEEN_TURN) {
			move();
			
			if (checkCollisions()) {
				return;
			}
			
			boolean aboutToEat = checkEat();
			animateMouth(aboutToEat);
			rotateTail();
			animate();
			timer = 0;
		}
		
		if (updateStarvation()) {
			return;
		}
		
		updateWeapons();
		updateTimers(deltaTime);
	}
	
	public void draw() {
		//Draw everything.
		for (Sprite s : segments) {
			s.draw(SteveDriver.batch);
		}
		for (Sprite w : weapons){
			w.draw(SteveDriver.batch);
		}
	}

	private boolean checkCollisions() {
		TiledMapTileLayer layer = (TiledMapTileLayer)SteveDriver.field.map.getLayers().get(1);
		
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				
				if (cell != null && CollisionHelper.isCollide(new Rectangle(x * SteveDriver.TEXTURE_WIDTH, y * SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH), segments.get(0).getBoundingRectangle())) {
					if (helmetHits > 0) {
						helmetHits -= 1;
					}
					
					kill();
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void checkProjectiles() {
		for (Sprite s : segments) {
			for (Projectile p : SteveDriver.field.getProjectiles()) {
				if (!p.getFriendly() && p.getAlive()) {
					if (CollisionHelper.isCollide(s.getBoundingRectangle(), p.getAvatar().getBoundingRectangle())) {
						changeHungerByPercent(p.getPercentDamage());
						
						p.kill();
					}
				}
			}
		}
	}

	private void getTouch() {
		if (Gdx.input.isTouched()) {			
			float deltaX = Gdx.input.getX() - Gdx.graphics.getWidth() / 2;
			float deltaY = Gdx.input.getY() - Gdx.graphics.getHeight() / 2;
			
			if(Math.abs(deltaX) > Math.abs(deltaY)) {
				if(deltaX > 0 && segments.get(0).getRotation() != SteveDriver.LEFT) {
					nextRotation = SteveDriver.RIGHT;
					nextDirection = SteveDriver.VRIGHT;
				}
				else if (segments.get(0).getRotation() != SteveDriver.RIGHT){
					nextRotation = SteveDriver.LEFT;
					nextDirection = SteveDriver.VLEFT;
				}
			}
			else {
				if(deltaY > 0 && segments.get(0).getRotation() != SteveDriver.UP) {
					nextRotation = SteveDriver.DOWN;
					nextDirection = SteveDriver.VDOWN;
				}
				else if (segments.get(0).getRotation() != SteveDriver.DOWN){
					nextRotation = SteveDriver.UP;
					nextDirection = SteveDriver.VUP;
				}
			}
		} else {
			if ((Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) && segments.get(0).getRotation() != SteveDriver.UP) {
				nextRotation = SteveDriver.DOWN;
				nextDirection = SteveDriver.VDOWN;
			}
			if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) && segments.get(0).getRotation() != SteveDriver.DOWN) {
				nextRotation = SteveDriver.UP;
				nextDirection = SteveDriver.VUP;
			}
			if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) && segments.get(0).getRotation() != SteveDriver.RIGHT) {
				nextRotation = SteveDriver.LEFT;
				nextDirection = SteveDriver.VLEFT;
			}
			if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) && segments.get(0).getRotation() != SteveDriver.LEFT) {
				nextRotation = SteveDriver.RIGHT;
				nextDirection = SteveDriver.VRIGHT;
			}
		}
	}
	
	public Vector3 getHeadPosition() {
		return headPosition;
	}
	
	public void addBody() {
		hungerTimer = 0;
		
		if (segments.size() < maxSegments) {
			if(segments.size() == 4){
				hungerTimer = 0;
			}
			
			Sprite newSegment = new Sprite(new TextureRegion(SteveDriver.atlas, 48, 16, 16, 16));
			Sprite secondToLast = segments.get(segments.size() - 2);
			Sprite tail = segments.get(segments.size()-1);
			
			Vector2 delta = SteveDriver.VRIGHT;
			
			if(secondToLast.getRegionX() == 16 && 
					secondToLast.getRegionY() == 48){
				delta = SteveDriver.VLEFT;
			}
			else if(secondToLast.getRegionX() == 0 && 
					secondToLast.getRegionY() == 32){
				delta = SteveDriver.VRIGHT;
			}
			else if(secondToLast.getRegionX() == 48 && 
					secondToLast.getRegionY() == 32){
				delta = SteveDriver.VLEFT;
			}
			else if(secondToLast.getRegionX() == 32 && 
					secondToLast.getRegionY() == 48){
				delta = SteveDriver.VRIGHT;
			}
			else if(secondToLast.getRegionX() == 0 && 
					secondToLast.getRegionY() == 48){
				delta = SteveDriver.VDOWN;
			}
			else if(secondToLast.getRegionX() == 16 && 
					secondToLast.getRegionY() == 32){
				delta = SteveDriver.VDOWN;				
			}
			else if(secondToLast.getRegionX() == 32 && 
					secondToLast.getRegionY() == 32){
				delta = SteveDriver.VRIGHT;
			}
			else if(secondToLast.getRegionX() == 48 && 
					secondToLast.getRegionY() == 48){
				delta = SteveDriver.VUP;
			}
			else if (tail.getRotation() ==  SteveDriver.DOWN) {
				delta = SteveDriver.VUP;
			}
			else if (tail.getRotation() ==  SteveDriver.RIGHT) {
				delta = SteveDriver.VLEFT;
			}
			else if (tail.getRotation() ==  SteveDriver.UP) {
				delta = SteveDriver.VDOWN;
			}
			
			newSegment.setPosition(tail.getX() + delta.x * SteveDriver.TEXTURE_WIDTH, 
					tail.getY() + delta.y * SteveDriver.TEXTURE_LENGTH);
			segments.add(newSegment);
		}
	}
	
	public void changeHungerByPercent(float percent) {
		if (percent >= 0 && percent <= 100) {
			hungerTimer += (timeTillStarve * percent/100);
			
			if (hungerTimer < 0) {
				hungerTimer = 0;
			}
		}
	}

	private void updateBody(){
		Sprite head = segments.get(0);
		Sprite next = segments.get(2);
		Sprite current = segments.get(1);
		
		float deltaHeadX = head.getX() - current.getX();
		float deltaHeadY = head.getY() - current.getY();
		
		float deltaNextX = current.getX() - next.getX();
		float deltaNextY = current.getY() - next.getY();
		
		float degrees = head.getRotation();
		
		int atlasX = 0;
		int atlasY = 16;
		
		boolean rightUp = (deltaHeadY > 0) && (deltaNextX > 0);
		boolean leftUp  = (deltaHeadY > 0) && (deltaNextX < 0);
		
		boolean rightDown = (deltaHeadY < 0) && (deltaNextX > 0);
		boolean leftDown = (deltaHeadY < 0) && (deltaNextX < 0);
		
		boolean upRight = (deltaHeadX > 0) && (deltaNextY > 0);
		boolean upLeft = (deltaHeadX < 0) && (deltaNextY > 0);
		
		boolean downRight = (deltaHeadX > 0) && (deltaNextY < 0);
		boolean downLeft = (deltaHeadX < 0) && (deltaNextY < 0);
		
		if(rightUp){			
			atlasX = 16;
			atlasY = 48;
			degrees = 0;
		}
		else if(leftUp){
			atlasX = 0;
			atlasY = 32;
			degrees = 0;
		}
		else if(rightDown){
			atlasX = 48;
			atlasY = 32;
			degrees = 0;
		}
		else if(leftDown){
			atlasX = 32;
			atlasY = 48;
			degrees = 0;
		}
		else if(upRight){
			atlasX = 0;
			atlasY = 48;
			degrees = 0;
		}
		else if(upLeft){
			atlasX = 16;
			atlasY = 32;
			degrees = 0;
		}
		else if(downRight){
			atlasX = 32;
			atlasY = 32;
			degrees = 0;
		}
		else if(downLeft){
			atlasX = 48;
			atlasY = 48;
			degrees = 0;
		}
		
		//System.out.println(deltaX + "," + deltaY);
		//System.out.println(atlasX + "|" + atlasY);
		
		if(weapons.size() > 0){
			atlasX += this.beltImageOffset;
		}
		
		segments.get(1).setRegion(new TextureRegion(SteveDriver.atlas, atlasX, atlasY, 16, 16));
		segments.get(1).setRotation(degrees);
	}
	
	private void animate(){
		for (int i = segments.size() - 1; i > 0; i--) {
			Sprite next = segments.get(i - 1);
			Sprite current = segments.get(i);
			if (i == segments.size() - 1) {
				current.setRegion(new TextureRegion(SteveDriver.atlas, 48, 16, 16, 16));
			}
			else if(i == 1){
				updateBody();
			}
			else {
				if (i < weapons.size() + 1) {
					current.setRegion(new TextureRegion(SteveDriver.atlas, next.getRegionX(), next.getRegionY(), next.getRegionWidth(), next.getRegionHeight()));
				}
				else if (i == weapons.size() + 1 && (next.getRegionX()/TILE_WIDTH > 3)) {
					current.setRegion(new TextureRegion(SteveDriver.atlas, next.getRegionX() - this.beltImageOffset, next.getRegionY(), next.getRegionWidth(), next.getRegionHeight()));
				}
				else {
					current.setRegion(new TextureRegion(SteveDriver.atlas, next.getRegionX(), next.getRegionY(), next.getRegionWidth(), next.getRegionHeight()));
				}
			}
		}
		
		int i = 0;
	}

	private void move(){
		//Update the rest of the segments
		for (int i = segments.size() - 1; i > 0; i--) {
			Sprite next = segments.get(i - 1);
			Sprite current = segments.get(i);
			current.setPosition(next.getX(), next.getY());
			current.setRotation(next.getRotation());
		}
		
		segments.get(0).setRotation(nextRotation);
		segments.get(0).translate(segments.get(0).getWidth() * nextDirection.x, segments.get(0).getHeight() * nextDirection.y);
	}
	
	private boolean checkEat(){
		boolean aboutToEat = false;
		for (Pickup p : SteveDriver.field.pickups) {
			if (p.getActive()) {
				if (segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY()) {
					p.consume(this);
				}
				else if (segments.get(0).getX() == p.getX() + SteveDriver.TEXTURE_WIDTH && segments.get(0).getY() == p.getY() && nextRotation == SteveDriver.LEFT ||
						 segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY() + SteveDriver.TEXTURE_LENGTH && nextRotation == SteveDriver.DOWN ||
						 segments.get(0).getX() == p.getX() - SteveDriver.TEXTURE_WIDTH && segments.get(0).getY() == p.getY() && nextRotation == SteveDriver.RIGHT ||
						 segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY() - SteveDriver.TEXTURE_LENGTH && nextRotation == SteveDriver.UP) {
					aboutToEat = true;
				}
			}
		}
		
		return aboutToEat;
	}
	
	private void rotateTail(){
		Sprite tail = segments.get(segments.size()-1);
		Sprite next = segments.get(segments.size()-2);
		
		float deltaX = next.getX() - tail.getX();
		float deltaY = next.getY() - tail.getY();
		
		if(deltaX > 0){
			tail.setRotation(SteveDriver.RIGHT);
		}
		else if(deltaX < 0)
			tail.setRotation(SteveDriver.LEFT);
		else if(deltaY > 0){
			tail.setRotation(SteveDriver.UP);
		}
		else if(deltaY < 0){
			tail.setRotation(SteveDriver.DOWN);
		}
	}
	
	private void animateMouth(boolean aboutToEat){
		if (aboutToEat) {
			segments.get(0).setRegion(new TextureRegion(SteveDriver.atlas, 16, 0, 16, 16));
		}
		else {
			segments.get(0).setRegion(new TextureRegion(SteveDriver.atlas, 0, 0, 16, 16));
		}
	}

	private boolean updateStarvation(){
		if(hungerTimer > timeTillStarve){
			if (segments.size() <= 2) {
				kill();
				return true;
			}
			
			segments.remove(segments.size() - 1);
			if(this.segments.size()-2 < weapons.size()){
				weapons.remove(weapons.size() - 1);
			}
			hungerTimer = 0;
		}
		
		return false;
	}

	private void kill() {
		//TODO: Make this better.
		System.out.println("You suck.");
		SteveDriver.prefs.flush();
		SteveDriver.stage = SteveDriver.STAGE_TYPE.SUMMARY;
	}
	
	public ArrayList<Sprite> getSegments() {
		return segments;
	}
	
	public ArrayList<Weapon> getWeapons(){
		return weapons;
	}
	
	private void updateTimers(float deltaTime){
		timer += deltaTime;
		hungerTimer += deltaTime/SteveDriver.constants.get("hungerRate");
		headPosition.x = segments.get(0).getX() + segments.get(0).getOriginX();
		headPosition.y = segments.get(0).getY() + segments.get(0).getOriginY();
	}
	
	private void updateWeapons(){
		for(int i = 0; i < weapons.size(); i++){
			weapons.get(i).update(segments.get(i+1).getX(), segments.get(i+1).getY());
		}
	}
	
	public void mountUpgrade(int upgradeType){
		if(this.segments.size() - 2 > this.weapons.size()) {
			switch(upgradeType){
			case 0:
				weapons.add(new GatlingGun(this.segments.get(this.weapons.size()+1).getX(), 
					this.segments.get(this.weapons.size()+1).getY()));
				break;
			case 1:
				weapons.add(new Laser(this.segments.get(this.weapons.size()+1).getX(), 
					this.segments.get(this.weapons.size()+1).getY()));
				break;
			case 2:
				weapons.add(new Specialist(this.segments.get(this.weapons.size()+1).getX(), 
					this.segments.get(this.weapons.size()+1).getY()));
				break;
			case 3:
				weapons.add(new MainCannon(this.segments.get(this.weapons.size()+1).getX(), 
					this.segments.get(this.weapons.size()+1).getY()));
			}
		}
	}
	
	public ArrayList<Sprite> getSnakeSprites(){
		return this.segments;
	}
	
	public boolean hasWeaponSpace(){
		return this.weapons.size() < this.segments.size()-1;
	}
	
	public int getUpgradableWeapon(){
		int weaponIndex = -1;
		int indexCounter = 0;
		for(Weapon w: weapons){
			if(!w.isUpgraded()){
				weaponIndex = indexCounter;
				break;
			}
			indexCounter++;
		}
		
		return weaponIndex;
	}
	
	public boolean hasWeaponToUpgrade(){
		for(Weapon w: weapons){
			if(!w.isUpgraded()){
				return true;
			}
		}
		
		return false;
	}
	
	public int getSnakeTier(){
		return snakeTier;
	}
	
	public int getSnakeArmor() {
		return SteveDriver.storePrefs.getInteger("armorTier") + 1;
	}
	
	public int getRotationIndex(){
		if(this.segments.get(0).getRotation() == SteveDriver.RIGHT)
			return SteveDriver.RIGHT_ID;
		else if(this.segments.get(0).getRotation() == SteveDriver.UP)
			return SteveDriver.UP_ID;
		else if(this.segments.get(0).getRotation() == SteveDriver.LEFT)
			return SteveDriver.LEFT_ID;
		else
			return SteveDriver.DOWN_ID;
	}
}

