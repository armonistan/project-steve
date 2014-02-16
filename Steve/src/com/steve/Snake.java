package com.steve;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;
import java.util.*;

public class Snake {
	private ArrayList<Sprite> segments;
	final int TEXTURE_WIDTH = 16;
	final int TEXTURE_LENGTH = 16;
	
	private final float TIME_BETWEEN_TURN = 0.1f;
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
		segments.add(new Sprite(new TextureRegion(SteveDriver.atlas, 0, 16, 16, 16)));
		segments.add(new Sprite(new TextureRegion(SteveDriver.atlas, 0, 16, 16, 16)));
		
		dirs = new Vector2[4];
		dirs[0] = new Vector2(1, 0);
		dirs[1] = new Vector2(0, 1);
		dirs[2] = new Vector2(-1, 0);
		dirs[3] = new Vector2(0, -1);
		
		nextDirection = dirs[0];
		
		nextRotation = RIGHT;
		segments.get(0).setRotation(nextRotation);
	}

	public void render(SpriteBatch batch, float deltaTime){
		if (Gdx.input.isTouched()) {			
			float deltaX = Gdx.input.getX() - Gdx.graphics.getWidth() / 2;
			float deltaY = Gdx.input.getY() - Gdx.graphics.getHeight() / 2;
			
			if(Math.abs(deltaX) > Math.abs(deltaY))
				if(deltaX > 0) {
					nextRotation = RIGHT;
					nextDirection = dirs[0];
				}
				else {
					nextRotation = LEFT;
					nextDirection = dirs[2];
				}
			else
				if(deltaY > 0) {
					nextRotation = DOWN;
					nextDirection = dirs[3];
				}
				else {
					nextRotation = UP;
					nextDirection = dirs[1];
				}
		}
		
		if (timer >= TIME_BETWEEN_TURN) {
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
				afterHead.setRegion(updateAfterHead(segments.get(0), afterHead, segments.get(2)));
			}
			
			segments.get(0).setRotation(nextRotation);
			segments.get(0).translate(segments.get(0).getWidth() * nextDirection.x, segments.get(0).getHeight() * nextDirection.y);
			timer = 0f;
		}
		
		for (Sprite s : segments) {
			s.draw(batch);
		}
		
		timer += deltaTime;
	}
	
	public Sprite getHead() {
		return segments.get(0);
	}
	
	private TextureRegion updateAfterHead(Sprite before, Sprite current, Sprite after) {

		float deltaX = after.getX() - current.getX();
		float deltaY = after.getY() - current.getY();
		
		float atlasX = 0;
		float atlasY = 0;
		
		if(deltaX < 0){
			if(deltaY < 0){
				atlasX = 48;
				atlasY = 48;
			}
			else if(deltaY==0){
				atlasX = 16;
				atlasY = 16;
			}
			else{
				atlasX = 0;
				atlasY = 32;
			}
		}
		else if(deltaX == 0){
			if(deltaY < 0){
				atlasX = 16;
				atlasY = 16;
			}
			else if(deltaY==0){
				atlasX = 16;
				atlasY = 16;
			}
			else{
				atlasX = 16;
				atlasY = 16;
			}
		}
		else{
			if(deltaY < 0){
				atlasX = 48;
				atlasY = 32;
			}
			else if(deltaY==0){
				atlasX = 16;
				atlasY = 16;
			}
			else{
				atlasX = 16;
				atlasY = 48;
			}
		}
			
		
		return new TextureRegion(SteveDriver.atlas,
				atlasX, atlasY, this.TEXTURE_WIDTH, this.TEXTURE_LENGTH); 
	}
}
