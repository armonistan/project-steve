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
	final int TEXTURE_WIDTH = 16;
	final int TEXTURE_LENGTH = 16;
	private final int MIN_SEGMENTS = 2;
	private final int MAX_SEGMENTS = 5;
	final int BIG_TEXTURE_WIDTH = 32;
	final int BIG_TEXTURE_LENGTH = 32;
	private Vector3 headPosition;
	
	private final float TIME_BETWEEN_TURN = 0.5f;
	private float timer = 0;
	
	private Vector2 nextDirection;
	private Vector2[] dirs;
	private final int RIGHT = 270;
	private final int UP = 0;
	private final int LEFT = 90;
	private final int DOWN = 180;
	private int nextRotation;
	
	public Snake(){
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
		headPosition = new Vector3(0, 0, 0);
	}

	public void render(SpriteBatch batch, float deltaTime){
		getTouch();
		
		//Move all the segments.
		if (timer >= TIME_BETWEEN_TURN) {			
			updateSegmentsAndEat();
			
			checkCollisions();
			
			timer = 0f;
		}
		
		//Draw everything.
		for (Sprite s : segments) {
			s.draw(batch);
		}
		
		timer += deltaTime;
		headPosition.x = segments.get(0).getX() + segments.get(0).getOriginX();
		headPosition.y = segments.get(0).getY() + segments.get(0).getOriginY();
	}

	private void checkCollisions() {
		TiledMapTileLayer layer = (TiledMapTileLayer)SteveDriver.map.getTiles().getLayers().get(1);
		
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				
				//TODO: Clean up
				if (cell != null && CollisionHelper.isCollide(new Rectangle(x * TEXTURE_WIDTH, y * TEXTURE_LENGTH, TEXTURE_WIDTH, TEXTURE_LENGTH), segments.get(0).getBoundingRectangle())) {
					System.out.println("Wat");
				}
			}
		}
	}

	private void updateSegmentsAndEat() {
		//Update the rest of the segments
		for (int i = segments.size() - 1; i > 0; i--) {
			Sprite next = segments.get(i - 1);
			Sprite current = segments.get(i);
			current.setPosition(next.getX(), next.getY());
			current.setRotation(next.getRotation());
			if (i == segments.size() - 1) {
				current.setRegion(new TextureRegion(SteveDriver.atlas, 48, 16, 16, 16));
			}
			else if (i > 1) {
				current.setRegion(new TextureRegion(SteveDriver.atlas, next.getRegionX(), next.getRegionY(), next.getRegionWidth(), next.getRegionHeight()));
			}
		}
		
		if (segments.size() > 2) {
			Sprite afterHead = segments.get(1);
			updateAfterHead(segments.get(0), afterHead, segments.get(2));
		}
		
		segments.get(0).setRotation(nextRotation);
		segments.get(0).translate(segments.get(0).getWidth() * nextDirection.x, segments.get(0).getHeight() * nextDirection.y);

		//Eat pickups
		boolean aboutToEat = false;
		for (Pickup p : SteveDriver.map.getPickups()) {
			if (p.getActive()) {
				if (segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY()) {
					p.consume(this);
				}
				else if (segments.get(0).getX() == p.getX() + TEXTURE_LENGTH && segments.get(0).getY() == p.getY() && nextRotation == LEFT ||
						 segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY() + TEXTURE_LENGTH && nextRotation == DOWN ||
						 segments.get(0).getX() == p.getX() - TEXTURE_LENGTH && segments.get(0).getY() == p.getY() && nextRotation == RIGHT ||
						 segments.get(0).getX() == p.getX() && segments.get(0).getY() == p.getY() - TEXTURE_LENGTH && nextRotation == UP) {
					aboutToEat = true;
				}
			}
		}
		
		if (aboutToEat) {
			segments.get(0).setRegion(new TextureRegion(SteveDriver.atlas, 16, 0, 16, 16));
		}
		else {
			segments.get(0).setRegion(new TextureRegion(SteveDriver.atlas, 0, 0, 16, 16));
		}

		segments.get(segments.size() - 1).setRotation(segments.get(segments.size() - 2).getRotation());
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
			
			newSegment.setPosition(tail.getX() + delta.x * TEXTURE_WIDTH, tail.getY() + delta.y * TEXTURE_LENGTH);
			tail.setRegion(new TextureRegion(SteveDriver.atlas, 0, 16, 16, 16));
			
			segments.add(newSegment);
		}
	}
	
	private void updateAfterHead(Sprite before, Sprite current, Sprite after) {
		int atlasX = 0;
		int atlasY = 16; //Default
		
		//TODO: Determine bends
		
		current.setRegionX(atlasX);
		current.setRegionY(atlasY);
		current.setRegionWidth(16);
		current.setRegionHeight(16);
	}
}

