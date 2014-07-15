package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.commands.ChangeBooleanPreference;
import com.steve.commands.ChangeStage;
import com.steve.commands.KillSnake;
import com.steve.commands.PauseButton;
import com.steve.TextButton;

public class Game {
	private boolean pPressed = false;

	private TextButton pause = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 - 7 * SteveDriver.TEXTURE_SIZE,
			SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * SteveDriver.TEXTURE_SIZE, 7, 4, new PauseButton(), "Pause");
	private TextButton music = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1,
			SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * SteveDriver.TEXTURE_SIZE, 6, 4, new ChangeBooleanPreference("music"), "Music");
	private TextButton menu = new TextButton(4 * SteveDriver.TEXTURE_SIZE * -1,
			SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * SteveDriver.TEXTURE_SIZE, 8, 4, new ChangeStage(STAGE_TYPE.MENU), "Menu");
	private TextButton store = new TextButton(4 * SteveDriver.TEXTURE_SIZE * -1,
			SteveDriver.guiCamera.viewportHeight / 2 * -1 + 8 * SteveDriver.TEXTURE_SIZE, 8, 4, new KillSnake(), "Store");
	private TextButton sfx = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1+200,
			SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * SteveDriver.TEXTURE_SIZE, 6, 4, new ChangeBooleanPreference("sfx"), "SFX");

	
	public Game() {
		ChangeBooleanPreference temp = (ChangeBooleanPreference)music.getCommand();
		temp.setButton(music);
		ChangeBooleanPreference temp2 = (ChangeBooleanPreference)sfx.getCommand();
		temp2.setButton(sfx);
		music.setStatus((SteveDriver.prefs.getBoolean("music")) ? 1 : 0);
		sfx.setStatus((SteveDriver.prefs.getBoolean("sfx")) ? 1 : 0);
	}
	
	public void render() {
		Vector3 test = new Vector3();
		test = SteveDriver.camera.position.lerp((SteveDriver.tutorial.isActive()) ? SteveDriver.tutorial.getFocus() :
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
		SteveDriver.field.drawBelowSnake();
		SteveDriver.snake.draw();
		SteveDriver.field.drawAboveSnake();
		SteveDriver.batch.end();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		SteveDriver.gui.render();
		
		if (!SteveDriver.tutorialOn) {
			SteveDriver.batch.begin();
			pause.setStatus(0);
			pause.update();
			pause.render();
			SteveDriver.batch.end();
		}
		
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
		SteveDriver.field.drawBelowSnake();
		SteveDriver.snake.draw();
		SteveDriver.field.drawAboveSnake();
		SteveDriver.batch.end();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		SteveDriver.batch.begin();
		pause.setStatus(1);
		pause.update();
		pause.render();
		music.update();
		music.render();
		sfx.update();
		sfx.render();
		menu.update();
		menu.render();
		store.update();
		store.render();
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
