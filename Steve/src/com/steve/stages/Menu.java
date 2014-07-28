package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.steve.MenuSnake;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.commands.ChangeBooleanPreference;
import com.steve.commands.ChangeStage;
import com.steve.commands.ExitGame;
import com.steve.commands.OpenWebsite;
import com.steve.commands.StartNewGame;
import com.steve.commands.StartNewRound;
import com.steve.TextButton;

public class Menu {
	Sprite logo;
	
	TextButton newGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, 6 * SteveDriver.TEXTURE_SIZE, 10, 4, new ChangeStage(STAGE_TYPE.AREYOUFUCKINGSURE), "New Game");
	TextButton continueGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, 2 * SteveDriver.TEXTURE_SIZE, 10, 4, new StartNewRound(), "Continue");
	TextButton credits = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, -2 * SteveDriver.TEXTURE_SIZE, 10, 4, new ChangeStage(STAGE_TYPE.CREDITS), "Credits");
	TextButton exitGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 - 6 * SteveDriver.TEXTURE_SIZE,
			SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * SteveDriver.TEXTURE_SIZE, 6, 4, new ExitGame(), "Exit");
	TextButton buyGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 - (6 + 9) * SteveDriver.TEXTURE_SIZE,
		SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * SteveDriver.TEXTURE_SIZE, 9, 4, new OpenWebsite("https://play.google.com/store/apps/details?id=com.steve"), "Buy Game");
	
	TextButton music = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * SteveDriver.TEXTURE_SIZE, 6, 4, new ChangeBooleanPreference("music"), "Music");
	TextButton sfx = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1 + 200, SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * SteveDriver.TEXTURE_SIZE, 6, 4, new ChangeBooleanPreference("sfx"), "SFX");
	
	
	public MenuSnake snake;
	public MenuField field;
	
	public Menu() {
		logo = new Sprite(new TextureRegion(SteveDriver.assets.get("data/Steve-title.png", Texture.class), 0f, 0f, 1f, 1f));
		logo.setPosition(logo.getWidth() / 2 * -1, logo.getRegionHeight() / 1.3f /*Why the fuck do I have to do this?*/ * -1 + SteveDriver.guiCamera.viewportHeight / 2);
		logo.scale(-0.5f);
		
		ChangeBooleanPreference temp = (ChangeBooleanPreference)music.getCommand();
		temp.setButton(music);
		ChangeBooleanPreference temp2 = (ChangeBooleanPreference)sfx.getCommand();
		temp2.setButton(sfx);
		music.setStatus((SteveDriver.prefs.getBoolean("music")) ? 1 : 0);
		sfx.setStatus((SteveDriver.prefs.getBoolean("sfx")) ? 1 : 0);
		

		snake = new MenuSnake();
	}
	
	public void render() {
		drawMenuField();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		SteveDriver.batch.begin();
		
		logo.draw(SteveDriver.batch);
		
		newGame.update();
		newGame.render();
		
		exitGame.update();
		exitGame.render();
		
		if (SteveDriver.FREE) {
			buyGame.update();
			buyGame.render();
		}
		
		if (SteveDriver.prefs.contains("money")) {
			continueGame.update();
			continueGame.setStatus(0);
		}
		else {
			continueGame.setStatus(2);
		}

		continueGame.render();
		
		credits.update();
		credits.render();
		
		music.update();
		music.render();
		
		sfx.update();
		sfx.render();
		
		SteveDriver.batch.end();
	}

	public void drawMenuField() {
		if (field != SteveDriver.field || field == null) {
			field = new MenuField();
			SteveDriver.field = field;
			
			SteveDriver.camera.position.x = snake.getHeadPosition().x;
			SteveDriver.camera.position.y = snake.getHeadPosition().y;
		}
		
		Vector3 test = SteveDriver.camera.position.lerp(snake.getHeadPosition(), 0.01f);
		SteveDriver.camera.position.x = test.x;
		SteveDriver.camera.position.y = test.y;
		SteveDriver.camera.update();
		
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.camera.combined);
		SteveDriver.batch.begin();
		field.update();
		field.drawBelowSnake();
		snake.update();
		snake.draw();
		field.drawAboveSnake();
		SteveDriver.batch.end();
	}
}
