package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class Game {
	private boolean pPressed = false;
	
	public void render(float deltaTime) {		
		Vector3 test = SteveDriver.camera.position.lerp((SteveDriver.tutorial.isActive()) ? SteveDriver.tutorial.getFocus() :
			SteveDriver.snake.getHeadPosition(), (SteveDriver.tutorial.isActive()) ? 0.1f : 0.05f);
		SteveDriver.camera.position.x = test.x;
		SteveDriver.camera.position.y = test.y;
		SteveDriver.camera.update();
		
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.camera.combined);
		if (!SteveDriver.tutorial.isActive()) {
			SteveDriver.field.update();
			SteveDriver.snake.update();
		}
		
		SteveDriver.batch.begin();
		SteveDriver.field.draw();
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
		
		SteveDriver.batch.begin();
		SteveDriver.field.draw();
		SteveDriver.snake.draw();
		SteveDriver.batch.end();
		
		//TODO: Temp
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			SteveDriver.resetField();
			SteveDriver.stage = STAGE_TYPE.GAME;
		}
		
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			SteveDriver.tutorial.endTutorial();
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
