package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.steve.helpers.CollisionHelper;
import com.steve.stages.Field;
import com.steve.stages.Summary.WHY_DIED;

public class AstroSteve extends Snake {
	private float timeInSpace;
	private boolean inSpace;
	
	public AstroSteve(float x, float y) {
		super(x,y);
		xOffSet = 27;
		yOffSet = 10;
		drill = true;
		candy = false;
		glue = false;
		matrix = false;
		jet = false;
		nuke = false;
		timeBetweenTurn = 0.05f;
		super.weapons.clear();
		
		timeInSpace = 5;
		inSpace = false;
	}

	@Override
	public void update() {
		if (timeInSpace > 0) {
			if (inSpace) {
				timeInSpace -= Gdx.graphics.getRawDeltaTime();
			}
		}
		
		//update all the segments.
		if (timer >= timeBetweenTurn) {
			move();
			
			if (checkCollisions()) {
				return;
			}
			
			animateMouth(false);
				
			rotateTail();
			animate();
			timer = 0;
		}
		
		updateWeapons();
		
		updateTimers(Gdx.graphics.getRawDeltaTime());
	}
	
	@Override
	public void draw() {
		//Draw everything.
		for (Sprite s : segments) {
			s.draw(SteveDriver.batch);
		}
		for (Sprite w : weapons){
			w.draw(SteveDriver.batch);
		}
	}
	
	@Override
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
				}
			}
		}
		
		if ((headPosition.x < 0 || headPosition.x > SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE ||
			headPosition.y < 0 || headPosition.y > SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE)) {
			
			//clear field and prevent anymore spawning
			SteveDriver.field.emptyField();
			SteveDriver.field.disableGenerator();
				
			//slow down
			this.timeBetweenTurn = .5f;
			
			inSpace = true;
			return false;
		}
		
		return false;
	}
	
	public float getTimeInSpace() {
		return timeInSpace;
	}
}
