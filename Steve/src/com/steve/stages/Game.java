package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class Game {
	public void render(float deltaTime) {
		Vector3 test = SteveDriver.camera.position.lerp(SteveDriver.snake.getHeadPosition(), 0.01f);
		SteveDriver.camera.position.x = test.x;
		SteveDriver.camera.position.y = test.y;
		SteveDriver.camera.update();
		
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.camera.combined);
		SteveDriver.field.render();
		
		SteveDriver.batch.begin();
		SteveDriver.snake.render(deltaTime);
		SteveDriver.batch.end();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		SteveDriver.gui.render();
		
		//TODO: Temp
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			SteveDriver.resetField();
		}
		
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			SteveDriver.stage = STAGE_TYPE.MENU;
		}
	}
}
