package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.commands.ExitGame;
import com.steve.commands.StartNewGame;
import com.steve.commands.StartNewRound;
import com.steve.TextButton;

public class Menu {
	TextButton newGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, SteveDriver.guiCamera.viewportHeight / 2, 10, 4, new StartNewGame(), "New Game");
	TextButton continueGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, SteveDriver.guiCamera.viewportHeight / 2 - 100, 14, 4, new StartNewRound(), "Continue Game");
	TextButton exitGame = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1, SteveDriver.guiCamera.viewportHeight / 2 - 200, 10, 4, new ExitGame(), "Exit Game");
	
	public void render() {
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		
		SteveDriver.batch.begin();
		newGame.update();
		newGame.render();
		
		exitGame.update();
		exitGame.render();
		
		if (Gdx.app.getPreferences("main").contains("money")) {
			continueGame.update();
			continueGame.render();
		}
		SteveDriver.batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
			Gdx.app.getPreferences("main").putInteger("money", 0);
			Gdx.app.getPreferences("main").flush();
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
