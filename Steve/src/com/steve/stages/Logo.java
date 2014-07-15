package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class Logo {
	private Sprite emberware;
	private float showTimer;
	private float showTime;
	
	public Logo() {
		emberware = new Sprite(new TextureRegion(SteveDriver.emberware, 0f, 0f, 1f, 1f));
		emberware.setPosition(emberware.getWidth() / 2 * -1, emberware.getHeight() / 2 * -1);
		
		showTimer = 0f;
		showTime = 3f;
	}
	
	public void render() {
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		
		SteveDriver.batch.begin();		
		emberware.draw(SteveDriver.batch);
		SteveDriver.batch.end();
		
		if (showTimer >= showTime) {
			SteveDriver.stage = STAGE_TYPE.MENU;
		}
		else {
			showTimer += Gdx.graphics.getRawDeltaTime();
		}
	}
}
