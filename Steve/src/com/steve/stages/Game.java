package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class Game {
	private boolean pPressed = false;
	
	public void render(float deltaTime) {
		//TODO: TEMP
		if (SteveDriver.snake.getMoney() == 0) {
			SteveDriver.tutorial.startTutorial();
			SteveDriver.stage = STAGE_TYPE.PAUSED;
		}
		
		Vector3 test = SteveDriver.camera.position.lerp(SteveDriver.snake.getHeadPosition(), 0.01f);
		SteveDriver.camera.position.x = test.x;
		SteveDriver.camera.position.y = test.y;
		SteveDriver.camera.update();
		
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.camera.combined);
		SteveDriver.field.update();
		SteveDriver.field.draw();
		
		SteveDriver.batch.begin();
		SteveDriver.snake.update(deltaTime);
		SteveDriver.snake.draw();
		SteveDriver.batch.end();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		SteveDriver.gui.render();
		
		if (Gdx.input.isKeyPressed(Keys.P)) {
			if (!pPressed) {
				SteveDriver.stage = STAGE_TYPE.PAUSED;
				pPressed = true;
			}
		}
		else {
			pPressed = false;
		}
	}
	
	public void renderPaused() {
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.camera.combined);
		SteveDriver.field.draw();
		
		SteveDriver.batch.begin();
		SteveDriver.snake.draw();
		SteveDriver.batch.end();
		
		//TODO: Temp
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			SteveDriver.resetField();
			SteveDriver.stage = STAGE_TYPE.GAME;
		}
		
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			SteveDriver.stage = STAGE_TYPE.MENU;
		}
		
		if (Gdx.input.isKeyPressed(Keys.P)) {
			if (!pPressed) {
				SteveDriver.stage = STAGE_TYPE.GAME;
				pPressed = true;
			}
		}
		else {
			pPressed = false;
		}
	}
}
