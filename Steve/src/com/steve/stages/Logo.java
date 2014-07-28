package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class Logo {
	private Sprite emberware;
	private float showTimer;
	private float showTime;
	
	public Logo() {		
		showTimer = 0f;
		showTime = 3f;
	}
	
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		
		if (SteveDriver.assets.isLoaded("data/emberware.png")) {
			if (emberware == null) {
				emberware = new Sprite(new TextureRegion(SteveDriver.assets.get("data/emberware.png", Texture.class), 0f, 0f, 1f, 1f));
				emberware.scale(SteveDriver.guiCamera.viewportWidth / emberware.getWidth() - 1f);
				emberware.setPosition(emberware.getWidth() / 2 * -1, emberware.getHeight() / 2 * -1);
			}
			
			SteveDriver.batch.begin();		
			emberware.draw(SteveDriver.batch);
			SteveDriver.batch.end();
		}
		
		if (showTimer >= showTime && SteveDriver.assets.getQueuedAssets() == 0) {
			SteveDriver.stage = STAGE_TYPE.MENU;
		}
		else {
			showTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
}
