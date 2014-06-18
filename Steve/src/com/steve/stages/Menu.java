package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.commands.ChangeBooleanPreference;
import com.steve.commands.ChangeStage;
import com.steve.commands.ExitGame;
import com.steve.commands.StartNewGame;
import com.steve.commands.StartNewRound;
import com.steve.TextButton;

public class Menu {
	Sprite logo;
	
	TextButton newGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, 6 * 16, 10, 4, new StartNewGame(), "New Game");
	TextButton continueGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, 2 * 16, 10, 4, new StartNewRound(), "Continue");
	TextButton credits = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, -2 * 16, 10, 4, new ExitGame(), "Credits");
	TextButton exitGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 - 6 * 16, SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * 16, 6, 4, new ExitGame(), "Exit");
	
	TextButton music = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, SteveDriver.guiCamera.viewportHeight / 2 * -1 + 4 * 16, 6, 4, new ChangeBooleanPreference("music"), "Music");
	
	public Menu() {
		logo = new Sprite(new TextureRegion(SteveDriver.logo, 0f, 0f, 1f, 1f));
		logo.setPosition(logo.getWidth() / 2 * -1, logo.getRegionHeight() / 1.3f /*Why the fuck do I have to do this?*/ * -1 + SteveDriver.guiCamera.viewportHeight / 2);
		logo.scale(-0.5f);
		
		ChangeBooleanPreference temp = (ChangeBooleanPreference)music.getCommand();
		temp.setButton(music);
		music.setStatus((SteveDriver.prefs.getBoolean("music")) ? 1 : 0);
	}
	
	public void render() {
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		
		SteveDriver.batch.begin();
		
		logo.draw(SteveDriver.batch);
		
		newGame.update();
		newGame.render();
		
		exitGame.update();
		exitGame.render();
		
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
		SteveDriver.batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
			SteveDriver.prefs.putInteger("money", 0);
			SteveDriver.prefs.flush();
			SteveDriver.snake.spendMoney(SteveDriver.snake.getMoney());
			
			SteveDriver.store.resetStore();
			
			SteveDriver.resetField();
			
			SteveDriver.stage = STAGE_TYPE.GAME;
		}
		
		if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			if (SteveDriver.snake.getMoney() != 0){
				SteveDriver.stage = STAGE_TYPE.STORE;
			} else {
				SteveDriver.stage = STAGE_TYPE.RESPAWNING;
			}
		}
	}
}
