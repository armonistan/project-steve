package com.steve;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.*;
import com.steve.base.Enemy;
import com.steve.base.Pickup;
import com.steve.base.Projectile;
import com.steve.base.Weapon;
import com.steve.helpers.CollisionHelper;
import com.steve.stages.Field;
import com.steve.stages.Summary.WHY_DIED;
import com.steve.weapons.GatlingGun;
import com.steve.weapons.Laser;
import com.steve.weapons.MainCannon;
import com.steve.weapons.Specialist;

import java.util.*;

public class Snake {
	protected ArrayList<Segment> segments;
	protected ArrayList<Weapon> weapons;
	
	protected Vector3 headPosition;
	
	protected float timeBetweenTurn = 0.4f;
	protected float timer = 0;
	
	protected float bombsAwayTimer = 60f;
	protected float bombsAwayTime = 0f;
	
	private float lastDamageTimer;
	
	public float getLastDamageTimer() {
		return lastDamageTimer;
	}

	protected Vector2 nextDirection;
	protected float nextRotation;
	
	private int money;
	private int treasure;
	public int snakeTier;
	
	protected int xOffSet;
	protected int yOffSet;
	
	protected boolean drill;
	protected boolean glue;
	protected boolean nuke;
	protected boolean jet;
	protected boolean matrix;
	protected boolean candy;
	
	protected Sound blockerCollide;
	private Sound loseSegment;
	
	Sprite helmet;
	Sprite glueStick;
	Rectangle tempCollider;
	
	Texture nukeExplosion;
	Sprite nukeExplosionSprite;
	private float nukeOpacity;
	
	//health related stuff
	private float currentHealth = 60f; //what is modified in damage units
	private float maxHealth = 60f; //starts out here in damage units
	private float hungerTime = .05f; //time till next hunger damage in secs
	private float hungerTimer = 0f; //counter that tell when to hit with hunger damage
	private float hungerDamage = .73f; //amount of damage hunger hits per tick
	
	public Snake(float x, float y){
		//create the structure to hold the body
		segments = new ArrayList<Segment>();
		//pick up where we left off
		gatherTier();
		
		nextDirection = SteveDriver.VUP;
		nextRotation = SteveDriver.UP;

		nukeExplosion = SteveDriver.assets.get("data/nuke.png", Texture.class);
		nukeExplosion.setFilter(TextureFilter.Nearest, TextureFilter.MipMapLinearNearest);
		
		nukeExplosionSprite = new Sprite(new TextureRegion(nukeExplosion, 0f, 0f, 1f, 1f));
		
		nukeOpacity = 0f;
		
		blockerCollide = SteveDriver.assets.get("audio/blockerCollide.ogg", Sound.class);
		loseSegment = SteveDriver.assets.get("audio/segmentLoss2.ogg", Sound.class);
		
		refreshSnakeLoadout(x, y);

		tempCollider = new Rectangle();
	}
	
	private void initializeBody(float x, float y){
		if(segments.size() == 0){
			//create the head and the first body segment
			segments.add(new Segment(0, 0, xOffSet, yOffSet));	
			segments.add(new Segment(0, 1, xOffSet, yOffSet));
			//set the rotation
			segments.get(0).setRotation(0);
			segments.get(1).setRotation(0);
			//set its position
			headPosition = new Vector3(x * SteveDriver.TEXTURE_SIZE, y * SteveDriver.TEXTURE_SIZE, 0);
			segments.get(0).setPosition(headPosition.x, headPosition.y);
			segments.get(1).setPosition(headPosition.x, headPosition.y-SteveDriver.TEXTURE_SIZE);
		}
		else{
			segments.get(0).setRegion(new TextureRegion(SteveDriver.atlas, 0 + xOffSet * SteveDriver.TEXTURE_SIZE, 0 + yOffSet * SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));	
		}
			
		//hard code it to be body segments
		int atlasX = SteveDriver.TEXTURE_SIZE*this.xOffSet;
		int atlasY = SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.yOffSet;
		
		//System.out.println("begin intialize: ");
		//System.out.println("size: " + segments.size());
		
		
		//loop till you have one slot left. that will be for the tail
		for(; segments.size() < SteveDriver.constants.get("startLength")-1;) {
			Segment newSegment = new Segment(0, 1, xOffSet, yOffSet);
			Sprite secondToLast = segments.get(segments.size() - 2);
			Sprite tail = segments.get(segments.size()-1);
			
			Vector2 delta = SteveDriver.VRIGHT;
			
			if(secondToLast.getRegionX() == SteveDriver.TEXTURE_SIZE + SteveDriver.TEXTURE_SIZE * this.xOffSet && 
					secondToLast.getRegionY() == 3 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.yOffSet){
				delta = SteveDriver.VLEFT;
			}
			else if(secondToLast.getRegionX() == 0 * this.xOffSet && 
					secondToLast.getRegionY() == 2 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.yOffSet){
				delta = SteveDriver.VRIGHT;
			}
			else if(secondToLast.getRegionX() == 3 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.xOffSet && 
					secondToLast.getRegionY() == 2 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.yOffSet){
				delta = SteveDriver.VLEFT;
			}
			else if(secondToLast.getRegionX() == 2 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.xOffSet && 
					secondToLast.getRegionY() == 3 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.yOffSet){
				delta = SteveDriver.VRIGHT;
			}
			else if(secondToLast.getRegionX() == 0*this.xOffSet && 
					secondToLast.getRegionY() == 3 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.yOffSet){
				delta = SteveDriver.VDOWN;
			}
			else if(secondToLast.getRegionX() == SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.xOffSet && 
					secondToLast.getRegionY() == 2 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.yOffSet){
				delta = SteveDriver.VDOWN;				
			}
			else if(secondToLast.getRegionX() == 2 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.xOffSet && 
					secondToLast.getRegionY() == 2 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.yOffSet){
				delta = SteveDriver.VRIGHT;
			}
			else if(secondToLast.getRegionX() == 3 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.xOffSet && 
					secondToLast.getRegionY() == 3 * SteveDriver.TEXTURE_SIZE+SteveDriver.TEXTURE_SIZE*this.yOffSet){
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
			
			newSegment.setPosition(tail.getX() + delta.x * SteveDriver.TEXTURE_SIZE, 
					tail.getY() + delta.y * SteveDriver.TEXTURE_SIZE);
			segments.add(newSegment);
			//System.out.println("intialize: ");
			//System.out.println("size: " + segments.size());
		}
				
		//then add the damned tail
		if(segments.size() < (SteveDriver.constants.get("startLength"))){
			//System.out.println("intialize before tail ");
			//System.out.println("size: " + segments.size());
			this.addBody();	
		}
		
		//System.out.println("maxstart: " + SteveDriver.constants.get("startLength"));
		//System.out.println("intialize: " + segments.size());
	}

	public void refreshSnakeLoadout(float x, float y) {
		weapons = new ArrayList<Weapon>();

		if (SteveDriver.constants.get("drill") != 0f) {
			drill = true;
		} else {
			drill = false;
		}
		
		if (SteveDriver.constants.get("glueTrail") != 0f) {
			glue = true;
			glueStick = new Sprite(new TextureRegion(SteveDriver.atlas,
					2 * SteveDriver.TEXTURE_SIZE, 0 * SteveDriver.TEXTURE_SIZE,
					1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE));
		} else {
			glue = false;
			glueStick = null;
		}
		if (SteveDriver.constants.get("nuke") != 0f){
			nuke = true;
		}
		else{
			nuke = false;
		}
		if(SteveDriver.constants.get("jetFuel") != 0f){
			jet = true;
		}
		else{
			jet = false;
		}
		if(SteveDriver.constants.get("bulletTime") != 0f){
			matrix = true;
		}
		else{
			matrix = false;
		}
		if(SteveDriver.constants.get("candyZone") != 0f){
			candy = true;
		}
		else{
			candy = false;
		}
		if(SteveDriver.constants.get("drill") != 0f){
			helmet = new Sprite(new TextureRegion(SteveDriver.atlas,
					3 * SteveDriver.TEXTURE_SIZE, 0 * SteveDriver.TEXTURE_SIZE,
					1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE));
		}
		else
			helmet = null;
		
		gatherTier();
		this.initializeBody(x,y);
		animateMouth(false);
		animate();
		
		if (SteveDriver.constants.get("mainGun") == 1.0f) {
			this.mountUpgrade(3);
		}
		//tier modifier
		timeBetweenTurn = .4f - (snakeTier*.05f);
		
		
		//tier modifier hunger
		float modifiedHungerDamage = (snakeTier == 1)? hungerDamage : 
			(snakeTier == 2) ? .4f : .33f;
		hungerDamage = modifiedHungerDamage;
		
		//tier modifier hp
		maxHealth = maxHealth+(snakeTier * (40f)) + ((snakeTier == 3) ? 25f : 0);
		
		//upgrade modifier hp
		maxHealth *= SteveDriver.constants.get("hitpoints");
		
		//set hp
		currentHealth = maxHealth;
		
		//tier modifier speed
		timeBetweenTurn = .4f - (snakeTier*.05f);
		
		//modify hunger
		//hungerTime /= SteveDriver.constants.get("hungerRate"); 
		
		if(SteveDriver.constants.get("fireDamage") > 1){
			SteveDriver.tierTwoWeaponDamageBuff = 40f;
			SteveDriver.tierTwoWeaponRangeBuff = 150f;
			SteveDriver.tierTwoWeaponShootSpeedBuff = .1f;
		}
		else if(SteveDriver.constants.get("fireRate") > 1){
			SteveDriver.tierTwoWeaponDamageBuff = 20f;
			SteveDriver.tierTwoWeaponRangeBuff = 150f;
			SteveDriver.tierTwoWeaponShootSpeedBuff = .3f;
		}
		else if(SteveDriver.constants.get("fireRange") > 1){
			SteveDriver.tierTwoWeaponDamageBuff = 20f;
			SteveDriver.tierTwoWeaponRangeBuff = 200f;
			SteveDriver.tierTwoWeaponShootSpeedBuff = .1f;
		}
		else{
			SteveDriver.tierTwoWeaponDamageBuff = 0f;
			SteveDriver.tierTwoWeaponRangeBuff = 0f;
			SteveDriver.tierTwoWeaponShootSpeedBuff = 0f;		
		}
		
		
		if (jet) {
			timeBetweenTurn = 0.1f;
		}
	}

	private void gatherTier() {
		//TODO: Make this better
		money = ((SteveDriver.prefs.contains("money")) ? SteveDriver.prefs.getInteger("money") : 0);
		treasure = ((SteveDriver.prefs.contains("treasure")) ? SteveDriver.prefs.getInteger("treasure") : 10);
		snakeTier = ((SteveDriver.constants.get("steve") != 0) ? 1 : 1);
		snakeTier = ((SteveDriver.constants.get("cyborg") != 0) ? 2 : snakeTier);
		snakeTier = ((SteveDriver.constants.get("robot") != 0) ? 3 : snakeTier);
		
		xOffSet = (snakeTier > 1) ? 27 : 0;
		yOffSet = (snakeTier > 2) ? 5 : 0;
	}
	
	public int getMoney() {
		return money;
	}
	
	public int getTreasure() {
		return treasure;
	}
	
	public void addTreasure (int amount){
		treasure += amount;
		SteveDriver.prefs.putInteger("treasure", treasure);
		SteveDriver.prefs.flush();
	}
	
	public boolean spendTreasure(int amount) {
		if (treasure >= amount) {
			addTreasure(-1 * amount);
			return true;
		} else {
			return false;
		}
	}
	
	public void addMoney(int amount) {
		money += amount * SteveDriver.constants.get("goldModifier"); //TODO: CHANGE BACK
		
		//TODO: Make this only save when needed.
		SteveDriver.prefs.putInteger("money", money);
		SteveDriver.prefs.flush();
	}
	
	public void setMoney(int amount) {
		money = amount;
		
		//TODO: Make this only save when needed.
		SteveDriver.prefs.putInteger("money", money);
		SteveDriver.prefs.flush();
	}
	
	public boolean spendMoney(int amount) {
		if (money >= amount) {
			money -= amount;
			SteveDriver.prefs.putInteger("money", money);
			return true;
		}
		else {
			return false;
		}
	}
	
	public float GetHungerTimer() {
		return this.hungerTimer;
	}
	
	public float getCurrentHealth(){
		return this.currentHealth;
	}
	
	public float getMaxHealth(){
		return this.maxHealth;
	}

	public void update(){
		getTouch();
	
		checkProjectiles();
		
		if (bombsAwayTime > bombsAwayTimer) {
			bombsAwayTime = 0f;
			killThemAll();
		}
		
		if (glue) {
			layGlue();
		}
		
		//update all the segments.
		if (timer >= timeBetweenTurn) {
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
		
		if (updateHealth()) {
			return;
		}
		
		if (updateHunger()){
			;
		}
		
		updateWeapons();
		
		updateTimers(Gdx.graphics.getRawDeltaTime());
	}
	
	private boolean updateHunger(){
		if(hungerTimer > hungerTime){
			currentHealth -= hungerDamage;
			
			if (hungerTimer < 0f) {
				hungerTimer = 0f;
			}
			hungerTimer = 0f;
		}
		
		return true;
	}
	
	public void draw() {
		//Draw everything.
		for (Sprite s : segments) {
			s.draw(SteveDriver.batch);
		}
		for (Sprite w : weapons){
			w.draw(SteveDriver.batch);
		}
		
		drawUpgradeImages();
	}

	protected boolean checkCollisions() {
		TiledMapTileLayer layer = Field.blockers;
		
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				
				tempCollider.x = x * SteveDriver.TEXTURE_SIZE;
				tempCollider.y = y * SteveDriver.TEXTURE_SIZE;
				tempCollider.width = SteveDriver.TEXTURE_SIZE;
				tempCollider.height = SteveDriver.TEXTURE_SIZE;
				
				if (cell != null && CollisionHelper.isCollide(tempCollider, segments.get(0).getBoundingRectangle())) {
					if (drill) {
						SteveDriver.field.destroyBlocker(x, y);
						return false;
					}
					if(SteveDriver.prefs.getBoolean("sfx", true))
						blockerCollide.play();
					kill(WHY_DIED.blocker); //Collide with wall death
					return true;
				}
			}
		}
		
		//TODO: ALWAYS KILLS YOU WHEN OUTSIDE RHELM
		if (!SteveDriver.prefs.getBoolean("edgeTutorial", false) && (headPosition.x < 6 * SteveDriver.TEXTURE_SIZE ||
				headPosition.x >= SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE - 6 * SteveDriver.TEXTURE_SIZE ||
				headPosition.y < 6 * SteveDriver.TEXTURE_SIZE ||
				headPosition.y >= SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE - 6 * SteveDriver.TEXTURE_SIZE)) {
			SteveDriver.prefs.putBoolean("edgeTutorial", true);
			SteveDriver.prefs.flush();
			SteveDriver.tutorialOn = true;
			SteveDriver.tutorial.startEdgeTutorial();
		}
		else if ((headPosition.x < 0 || headPosition.x > SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE ||
				headPosition.y < 0 || headPosition.y > SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE)) {

			kill(WHY_DIED.space); //Tried to go to space death
			
			return true;
		}
		
		return false;
	}
	
	private void checkProjectiles() {
		for (Sprite s : segments) {
			for (Projectile p : SteveDriver.field.getProjectiles()) {
				if (!p.getFriendly() && p.getAlive()) {
					if (CollisionHelper.isCollide(s.getBoundingRectangle(), p.getAvatar().getBoundingRectangle())) {
						changeHunger(p.getDamage());
						
						p.kill();
					}
				}
			}
		}
	}
	
	protected void getTouch() {
		if (Gdx.input.isTouched() && !Button.clicked) {
			float x = Gdx.input.getX() - Gdx.graphics.getWidth() / 2;
			float y = Gdx.input.getY() * -1 + Gdx.graphics.getHeight() / 2;
			float m = (float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
			
			boolean xGtY1 = y < m * x;
			boolean xGtY2 = y < -1 * m * x;
			
			if (xGtY1) {
				if (xGtY2 && segments.get(0).getRotation() != SteveDriver.UP) {
					nextRotation = SteveDriver.DOWN;
					nextDirection = SteveDriver.VDOWN;
				}
				else if (segments.get(0).getRotation() != SteveDriver.LEFT){
					nextRotation = SteveDriver.RIGHT;
					nextDirection = SteveDriver.VRIGHT;
				}
			}
			else {
				if (xGtY2 && segments.get(0).getRotation() != SteveDriver.RIGHT) {
					nextRotation = SteveDriver.LEFT;
					nextDirection = SteveDriver.VLEFT;
				}
				else if (segments.get(0).getRotation() != SteveDriver.DOWN){
					nextRotation = SteveDriver.UP;
					nextDirection = SteveDriver.VUP;
				}
			}
			
			
			
			/*float deltaX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2);
			float deltaY = (Gdx.input.getY() - Gdx.graphics.getHeight() / 2);
			
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
			}*/
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
		currentHealth += SteveDriver.constants.get("hungerRate")/100 * maxHealth;
		if(currentHealth > maxHealth)
			currentHealth = maxHealth;
		//System.out.println("add body: " + atlasX/SteveDriver.TEXTURE_SIZE + ", " + atlasY/SteveDriver.TEXTURE_SIZE);
		//System.out.println("in body" + "\n");
		if (segments.size() < (SteveDriver.constants.get("maxLength"))) {
			Segment newSegment = new Segment(3, 1, xOffSet, yOffSet);
			Segment secondToLast = segments.get(segments.size() - 2);
			Segment tail = segments.get(segments.size()-1);
			
			Vector2 delta = SteveDriver.VRIGHT;
			float rotation = SteveDriver.LEFT;
			
			if(secondToLast.getBaseAtlasX() == 1 && 
				secondToLast.getBaseAtlasY() == 3){
				delta = SteveDriver.VLEFT;
				rotation = SteveDriver.RIGHT;
			}
			else if(secondToLast.getBaseAtlasX() == 0 && 
					secondToLast.getBaseAtlasY() == 2){
				delta = SteveDriver.VRIGHT;
				rotation = SteveDriver.LEFT;
			}
			else if(secondToLast.getBaseAtlasX() == 3 && 
					secondToLast.getBaseAtlasY() == 2){
				delta = SteveDriver.VLEFT;
				rotation = SteveDriver.RIGHT;
			}
			else if(secondToLast.getBaseAtlasX() == 2 && 
					secondToLast.getBaseAtlasY() == 3){
				delta = SteveDriver.VRIGHT;
				rotation = SteveDriver.LEFT;
			}
			else if(secondToLast.getBaseAtlasX() == 0 && 
					secondToLast.getBaseAtlasY() == 3){
				delta = SteveDriver.VDOWN;
				rotation = SteveDriver.UP;
			}
			else if(secondToLast.getBaseAtlasX() == 1 && 
					secondToLast.getBaseAtlasY() == 2){
				delta = SteveDriver.VDOWN;	
				rotation = SteveDriver.UP;
			}
			else if(secondToLast.getBaseAtlasX() == 2 && 
					secondToLast.getBaseAtlasY() == 2){
				delta = SteveDriver.VRIGHT;
				rotation = SteveDriver.LEFT;
			}
			else if(secondToLast.getBaseAtlasX() == 3 && 
					secondToLast.getBaseAtlasY() == 3){
				delta = SteveDriver.VUP;
				rotation = SteveDriver.DOWN;
			}
			else if (tail.getRotation() ==  SteveDriver.DOWN) {
				delta = SteveDriver.VUP;
				rotation = SteveDriver.DOWN;
			}
			else if (tail.getRotation() ==  SteveDriver.RIGHT) {
				delta = SteveDriver.VLEFT;
				rotation = SteveDriver.RIGHT;
			}
			else if (tail.getRotation() ==  SteveDriver.UP) {
				delta = SteveDriver.VDOWN;
				rotation = SteveDriver.UP;
			}
			
			newSegment.setPosition(tail.getX() + delta.x * SteveDriver.TEXTURE_SIZE, 
					tail.getY() + delta.y * SteveDriver.TEXTURE_SIZE);
			newSegment.setRotation(rotation);
			segments.add(newSegment);
		}
	}
	
	public void changeHunger(float amount) {
		currentHealth -= amount;
		
		if (hungerTimer < 0f) {
			hungerTimer = 0f;
		}
		
		lastDamageTimer = 0.5f;
	}

	private void updateBody(){
		Segment head = segments.get(0);
		Segment next = segments.get(2);
		Segment current = segments.get(1);
		
		float deltaHeadX = head.getX() - current.getX();
		float deltaHeadY = head.getY() - current.getY();
		
		float deltaNextX = current.getX() - next.getX();
		float deltaNextY = current.getY() - next.getY();
		
		float degrees = head.getRotation();
		
		int atlasX = 0;
		int atlasY = 1;
		
		boolean rightUp = (deltaHeadY > 0) && (deltaNextX > 0);
		boolean leftUp  = (deltaHeadY > 0) && (deltaNextX < 0);
		
		boolean rightDown = (deltaHeadY < 0) && (deltaNextX > 0);
		boolean leftDown = (deltaHeadY < 0) && (deltaNextX < 0);
		
		boolean upRight = (deltaHeadX > 0) && (deltaNextY > 0);
		boolean upLeft = (deltaHeadX < 0) && (deltaNextY > 0);
		
		boolean downRight = (deltaHeadX > 0) && (deltaNextY < 0);
		boolean downLeft = (deltaHeadX < 0) && (deltaNextY < 0);
		
		if(rightUp){			
			atlasX = 1;
			atlasY = 3;
			degrees = 0;
		}
		else if(leftUp){
			atlasX = 0;
			atlasY = 2;
			degrees = 0;
		}
		else if(rightDown){
			atlasX = 3;
			atlasY = 2;
			degrees = 0;
		}
		else if(leftDown){
			atlasX = 2;
			atlasY = 3;
			degrees = 0;
		}
		else if(upRight){
			atlasX = 0;
			atlasY = 3;
			degrees = 0;
		}
		else if(upLeft){
			atlasX = 1;
			atlasY = 2;
			degrees = 0;
		}
		else if(downRight){
			atlasX = 2;
			atlasY = 2;
			degrees = 0;
		}
		else if(downLeft){
			atlasX = 3;
			atlasY = 3;
			degrees = 0;
		}
		
		current.setBaseAtlasX(atlasX);
		current.setBaseAtlasY(atlasY);
		current.setRotation(degrees);
	}
	
	protected void animate(){
		for (int i = segments.size() - 1; i > 0; i--) {
			Segment next = segments.get(i - 1);
			Segment current = segments.get(i);
			if (i == segments.size() - 1) {				
				current.setBaseAtlasX(3);
				current.setBaseAtlasY(1);
			}
			else {
				if(i == 1){
					updateBody(); //TODO: Remove
				}
				else {
					current.setBaseAtlasX(next.getBaseAtlasX());
					current.setBaseAtlasY(next.getBaseAtlasY());
				}
			}
		}
		
		for (int i = 1; i < segments.size(); i++) {
			segments.get(i).update(i <= weapons.size(), xOffSet, yOffSet);
		}
	}

	protected void move(){
		//Update the rest of the segments
		for (int i = segments.size() - 1; i > 0; i--) {
			Segment next = segments.get(i - 1);
			Segment current = segments.get(i);
			current.setPosition(next.getX(), next.getY());
			current.setRotation(next.getRotation());
		}
		
		segments.get(0).setRotation(nextRotation);
		segments.get(0).translate(segments.get(0).getWidth() * nextDirection.x, segments.get(0).getHeight() * nextDirection.y);
	}
	
	private boolean checkEat(){
		boolean aboutToEat = false;
		for (Pickup p : Field.pickups) {
			if (p.getActive()) {
				if (segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY()) {
					p.consume(this);
				}
				else if (segments.get(0).getX() == p.getX() + SteveDriver.TEXTURE_SIZE && segments.get(0).getY() == p.getY() && nextRotation == SteveDriver.LEFT ||
						 segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY() + SteveDriver.TEXTURE_SIZE && nextRotation == SteveDriver.DOWN ||
						 segments.get(0).getX() == p.getX() - SteveDriver.TEXTURE_SIZE && segments.get(0).getY() == p.getY() && nextRotation == SteveDriver.RIGHT ||
						 segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY() - SteveDriver.TEXTURE_SIZE && nextRotation == SteveDriver.UP) {
					aboutToEat = true;
				}
			}
		}
		
		return aboutToEat;
	}
	
	protected void rotateTail(){
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
	
	protected void animateMouth(boolean aboutToEat){
		if (aboutToEat) {
			segments.get(0).setRegion(SteveDriver.TEXTURE_SIZE + (xOffSet * SteveDriver.TEXTURE_SIZE), 0 + (yOffSet * SteveDriver.TEXTURE_SIZE), SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		}
		else if(segments.size() > 0){
			segments.get(0).setRegion(0 + (xOffSet * SteveDriver.TEXTURE_SIZE), 0 + (yOffSet * SteveDriver.TEXTURE_SIZE), SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		}
	}

	private boolean updateHealth(){
		if(currentHealth <= 0){
			if(SteveDriver.prefs.getBoolean("sfx", true))
				loseSegment.play();
			
			if (segments.size() <= 2) {
				kill((lastDamageTimer > 0) ? WHY_DIED.enemy : WHY_DIED.starvation); //Either starved, or was clobbered.
				return true;
			}
			
			segments.remove(segments.size() - 1);
			
			if(this.segments.size()-2 < weapons.size()){
				weapons.remove(weapons.size() - 1);
			}
			
			currentHealth = maxHealth;
			hungerTimer = 0f;
		}
		
		return false;
	}

	public void kill(WHY_DIED why) {
		//TODO: Make this better.
		SteveDriver.prefs.flush();
		SteveDriver.stage = SteveDriver.STAGE_TYPE.SUMMARY;
		SteveDriver.summary.setWhyDied(why);
		SteveDriver.store.initializeUpgrades();
	}
	
	public ArrayList<Segment> getSegments() {
		return segments;
	}
	
	public ArrayList<Weapon> getWeapons(){
		return weapons;
	}
	
	protected void updateTimers(float deltaTime){
		timer += deltaTime;
		
		hungerTimer += deltaTime;
		
		headPosition.x = segments.get(0).getX() + segments.get(0).getOriginX();
		headPosition.y = segments.get(0).getY() + segments.get(0).getOriginY();
		
		if (nuke) {
			bombsAwayTime += deltaTime;
		}
		
		if (bombsAwayTimer - bombsAwayTime < 0.25f) {
			nukeOpacity = 1f - (bombsAwayTimer - bombsAwayTime) * 4f;
		}
		else if (nukeOpacity > 0f) {
			nukeOpacity -= deltaTime;
		}
		
		if (lastDamageTimer > 0) {
			lastDamageTimer -= deltaTime;
		}
	}
	
	protected void updateWeapons(){
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
				int atlasX = SteveDriver.TEXTURE_SIZE*23;
				int atlasY = SteveDriver.TEXTURE_SIZE*5;
				
				if((SteveDriver.constants.get("mainCannonType") == 0)){
					if (SteveDriver.constants.get("fireRange") > 1f){
						atlasX = SteveDriver.TEXTURE_SIZE*24;
					}
					else if(SteveDriver.constants.get("fireRate") > 1f){
						atlasX = SteveDriver.TEXTURE_SIZE*24;
						atlasY = SteveDriver.TEXTURE_SIZE*4;
					}
					else if(SteveDriver.constants.get("fireDamage") > 1f){
						atlasX = SteveDriver.TEXTURE_SIZE*24;
						atlasY = SteveDriver.TEXTURE_SIZE*6;
					}
				}
				else if(SteveDriver.constants.get("mainCannonType") == 1){
					atlasX = SteveDriver.TEXTURE_SIZE*25;
					atlasY = SteveDriver.TEXTURE_SIZE*4;
				}
				else if(SteveDriver.constants.get("mainCannonType") == 2){
					atlasX = SteveDriver.TEXTURE_SIZE*25;
					atlasY = SteveDriver.TEXTURE_SIZE*5;
				}
				else if(SteveDriver.constants.get("mainCannonType") == 3){
					atlasX = SteveDriver.TEXTURE_SIZE*25;
					atlasY = SteveDriver.TEXTURE_SIZE*6;
				}
					
				
				weapons.add(new MainCannon(this.segments.get(this.weapons.size()+1).getX(), 
					this.segments.get(this.weapons.size()+1).getY(), atlasX, atlasY));
			}
		}
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
	
	public int getOppositeRotationIndex(){
		if(this.segments.get(0).getRotation() == SteveDriver.RIGHT)
			return SteveDriver.LEFT_ID;
		else if(this.segments.get(0).getRotation() == SteveDriver.UP)
			return SteveDriver.DOWN_ID;
		else if(this.segments.get(0).getRotation() == SteveDriver.LEFT)
			return SteveDriver.RIGHT_ID;
		else
			return SteveDriver.UP_ID;
	}
	
	public void killThemAll() {
		int explodeDistance = 300 * 300;
		int x = (int) this.segments.get(0).getX();
		int y = (int) this.segments.get(0).getY();
		
		nukeOpacity = 1f;
		
		for (Enemy e : SteveDriver.field.enemies) {
			if (CollisionHelper.distanceSquared(x, y, e.getXPosition(), e.getYPosition()) < explodeDistance) {
				e.setHealth(0f);
			}
		}
		SteveDriver.field.destroyBlockersRadius(15, x/SteveDriver.TEXTURE_SIZE, y/SteveDriver.TEXTURE_SIZE);
	}
	
	public void layGlue() {
		int x = (int) this.segments.get(this.segments.size() - 1).getX() / SteveDriver.TEXTURE_SIZE;
		int y = (int) this.segments.get(this.segments.size() - 1).getY() / SteveDriver.TEXTURE_SIZE;
		
		SteveDriver.field.setGlueTile(x, y);		
	}
	
	private void drawUpgradeImages(){
		if(drill && helmet != null){
			helmet.setPosition(segments.get(0).getX(), segments.get(0).getY());
			helmet.setRotation(segments.get(0).getRotation());
			helmet.draw(SteveDriver.batch);
		}
		if(nuke){
			if (nukeOpacity > 0f) {
				nukeExplosionSprite.setPosition(segments.get(0).getX() - 1000, segments.get(0).getY() - 1000);
				nukeExplosionSprite.setColor(1, 1, 1, nukeOpacity);
				nukeExplosionSprite.draw(SteveDriver.batch);
			}
		}
		if(glue && glueStick != null){
			glueStick.setPosition(segments.get(segments.size()-1).getX(), segments.get(segments.size()-1).getY());
			glueStick.setRotation(segments.get(segments.size()-1).getRotation());
			glueStick.draw(SteveDriver.batch);
		}
		if(jet){
			
		}
		if(matrix){
			
		}
		if(candy){
			
		}
	}

	protected class Segment extends Sprite {
		private int baseAtlasX;
		private int baseAtlasY;
		
		public Segment(int baseX, int baseY, int xOff, int yOff) {
			super(new TextureRegion(SteveDriver.atlas, 0, 0, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
			
			baseAtlasX = baseX;
			baseAtlasY = baseY;
			
			update(false, xOff, yOff);
		}
		
		public void update(boolean weaponized, int xOff, int yOff) {			
			this.setRegion(baseAtlasX * SteveDriver.TEXTURE_SIZE + xOff * SteveDriver.TEXTURE_SIZE + ((weaponized) ? 4 * SteveDriver.TEXTURE_SIZE : 0),
					baseAtlasY * SteveDriver.TEXTURE_SIZE + yOff * SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		}
		
		public int getBaseAtlasX() {
			return baseAtlasX;
		}
		
		public int getBaseAtlasY() {
			return baseAtlasY;
		}
		
		public void setBaseAtlasX(int x) {
			baseAtlasX = x;
		}
		
		public void setBaseAtlasY(int y) {
			baseAtlasY = y;
		}
	}
}

