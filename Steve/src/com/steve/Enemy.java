package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
	protected Sprite avatar;
	protected Vector2 mapPosition;
	
	public Enemy(Vector2 position, Vector2 atlasPosition, Vector2 atlasBounds) {
		mapPosition = position;
		
		avatar.setRegion(new TextureRegion(SteveDriver.atlas, (int)atlasPosition.x, (int)atlasPosition.y, (int)atlasBounds.x, (int)atlasBounds.y));
		avatar.setPosition(mapPosition.x * SteveDriver.TEXTURE_WIDTH, mapPosition.y * SteveDriver.TEXTURE_LENGTH);
	}
	
	public void render(SpriteBatch batch) {
		update();
		
		avatar.draw(batch);
	}
	
	public void update() {
		//TODO: Define basic update behavior
		//Should override
	}
	
	public void kill() {
		SteveDriver.field.enemiesToRemove.add(this);
	}
}
