package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.steve.Button;
import com.steve.ChangeStage;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class Menu {
	Button newGame = new Button(-300, 70, 10, 4, new ChangeStage(STAGE_TYPE.GAME));
	
	public void render() {
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		
		SteveDriver.batch.begin();
		newGame.update();
		newGame.render();
		SteveDriver.guiHelper.drawText("This is the menu. Hit 2 to continue the game, yeah.", -300, 70, Color.BLACK);
		SteveDriver.guiHelper.drawText("Hit 3 to start a new game.", -200, 0, Color.BLACK);
		SteveDriver.batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
			Gdx.app.getPreferences("main").putInteger("money", 0);
			Gdx.app.getPreferences("main").flush();
			SteveDriver.snake.spendMoney(SteveDriver.snake.getMoney());
			
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
