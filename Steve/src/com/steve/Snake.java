package com.steve;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.*;
import java.util.*;

public class Snake {
	private ArrayList<Sprite> segments;
	private final int MAX_SEGMENTS = 10;
	private Vector3 headPosition;
	
	private final float TIME_BETWEEN_TURN = 0.5f;
	private final float TIME_TILL_STARVE = 50000f; //arbitary number
	private float timer = 0;
	private float hungerTimer = 0;
	
	
	private Vector2 nextDirection;
	private Vector2[] dirs;
	private final int RIGHT = 270;
	private final int UP = 0;
	private final int LEFT = 90;
	private final int DOWN = 180;
	private int nextRotation;
	
	public Snake(Vector2 position){
		segments = new ArrayList<Sprite>();
		segments.add(new Sprite(new TextureRegion(SteveDriver.atlas, 0, 0, 16, 16)));
		segments.add(new Sprite(new TextureRegion(SteveDriver.atlas, 0, 16, 16, 16)));
		
		dirs = new Vector2[4];
		dirs[0] = new Vector2(1, 0);
		dirs[1] = new Vector2(0, 1);
		dirs[2] = new Vector2(-1, 0);
		dirs[3] = new Vector2(0, -1);
		
		nextDirection = dirs[0];
		
		nextRotation = RIGHT;
		segments.get(0).setRotation(nextRotation);
		headPosition = new Vector3(position.x * SteveDriver.TEXTURE_WIDTH, position.y * SteveDriver.TEXTURE_LENGTH, 0);
		segments.get(0).setPosition(headPosition.x, headPosition.y);
	}

	public void render(SpriteBatch batch, float deltaTime){
		getTouch();
		
		//update all the segments.
		if (timer >= TIME_BETWEEN_TURN) {			
			move();
			animate();
			boolean aboutToEat = checkEat();
			animateMouth(aboutToEat);
			rotateTail();
			checkCollisions();
			timer = 0;
		}
		
		updateStarvation();
		updateTimers(deltaTime);
		
		//Draw everything.
		for (Sprite s : segments) {
			s.draw(batch);
		}		
	}

	private void checkCollisions() {
		TiledMapTileLayer layer = (TiledMapTileLayer)SteveDriver.field.map.getLayers().get(1);
		
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				
				//TODO: Clean up
				if (cell != null && CollisionHelper.isCollide(new Rectangle(x * SteveDriver.TEXTURE_WIDTH, y * SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH), segments.get(0).getBoundingRectangle())) {
					System.out.println("Wat");
				}
			}
		}
		
		//TODO: DO NOT LEAVE THIS IN
		for (Enemy e : SteveDriver.field.enemies) {
			if (CollisionHelper.isCollide(segments.get(0).getBoundingRectangle(), e.avatar.getBoundingRectangle())) {
				e.kill();
			}
		}
	}

	private void getTouch() {
		if (Gdx.input.isTouched()) {			
			float deltaX = Gdx.input.getX() - Gdx.graphics.getWidth() / 2;
			float deltaY = Gdx.input.getY() - Gdx.graphics.getHeight() / 2;
			
			if(Math.abs(deltaX) > Math.abs(deltaY)) {
				if(deltaX > 0 && segments.get(0).getRotation() != LEFT) {
					nextRotation = RIGHT;
					nextDirection = dirs[0];
				}
				else if (segments.get(0).getRotation() != RIGHT){
					nextRotation = LEFT;
					nextDirection = dirs[2];
				}
			}
			else {
				if(deltaY > 0 && segments.get(0).getRotation() != UP) {
					nextRotation = DOWN;
					nextDirection = dirs[3];
				}
				else if (segments.get(0).getRotation() != DOWN){
					nextRotation = UP;
					nextDirection = dirs[1];
				}
			}
		} else {
			if ((Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) && segments.get(0).getRotation() != UP) {
				nextRotation = DOWN;
				nextDirection = dirs[3];
			}
			if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) && segments.get(0).getRotation() != DOWN) {
				nextRotation = UP;
				nextDirection = dirs[1];
			}
			if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) && segments.get(0).getRotation() != RIGHT) {
				nextRotation = LEFT;
				nextDirection = dirs[2];
			}
			if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) && segments.get(0).getRotation() != LEFT) {
				nextRotation = RIGHT;
				nextDirection = dirs[0];
			}
		}
	}
	
	public Vector3 getHeadPosition() {
		return headPosition;
	}
	
	public void addBody() {
		hungerTimer = 0;
		
		if (segments.size() < MAX_SEGMENTS) {
			Sprite newSegment = new Sprite(new TextureRegion(SteveDriver.atlas, 48, 16, 16, 16));
			Sprite tail = segments.get(segments.size() - 1);
			
			Vector2 delta = dirs[0];
			
			if (tail.getRotation() ==  DOWN) {
				delta = dirs[1];
			}
			else if (tail.getRotation() ==  RIGHT) {
				delta = dirs[2];
			}
			else if (tail.getRotation() ==  UP) {
				delta = dirs[3];
			}
			
			newSegment.setPosition(tail.getX() + delta.x * SteveDriver.TEXTURE_WIDTH, tail.getY() + delta.y * SteveDriver.TEXTURE_LENGTH);
			tail.setRegion(new TextureRegion(SteveDriver.atlas, 0, 16, 16, 16));
			
			segments.add(newSegment);
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
			else if (i > 0) {
				current.setRegion(new TextureRegion(SteveDriver.atlas, next.getRegionX(), next.getRegionY(), next.getRegionWidth(), next.getRegionHeight()));
			}
		}
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
				else if (segments.get(0).getX() == p.getX() + SteveDriver.TEXTURE_WIDTH && segments.get(0).getY() == p.getY() && nextRotation == LEFT ||
						 segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY() + SteveDriver.TEXTURE_LENGTH && nextRotation == DOWN ||
						 segments.get(0).getX() == p.getX() - SteveDriver.TEXTURE_WIDTH && segments.get(0).getY() == p.getY() && nextRotation == RIGHT ||
						 segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY() - SteveDriver.TEXTURE_LENGTH && nextRotation == UP) {
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
			tail.setRotation(270);
		}
		else if(deltaX < 0)
			tail.setRotation(90);
		else if(deltaY > 0){
			tail.setRotation(0);
		}
		else if(deltaY < 0){
			tail.setRotation(180);
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

	private void updateStarvation(){
		if(hungerTimer > TIME_TILL_STARVE){
			segments.remove(segments.size() - 1);
			hungerTimer = 0;
		}
	}
	
	private void updateTimers(float deltaTime){
		timer += deltaTime;
		hungerTimer += deltaTime;
		headPosition.x = segments.get(0).getX() + segments.get(0).getOriginX();
		headPosition.y = segments.get(0).getY() + segments.get(0).getOriginY();
	}
}

