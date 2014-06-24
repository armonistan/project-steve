package com.steve.stages;

import com.badlogic.gdx.graphics.Color;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class Loading {
	public void render() {
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		
		SteveDriver.batch.begin();	
		SteveDriver.guiHelper.drawTextCentered("Loading", 0, 0, Color.BLACK);
		SteveDriver.batch.end();
		
		if (!SteveDriver.field.generatingField.isAlive()) {
			SteveDriver.field.cleanupSetup();
			
			for (int i = 0; i < 100; i++) {
				if(SteveDriver.field.generator.generateAppleTutorial())
					break;
			}
			
			for (int i = 0; i < 100; i++) {
				if(SteveDriver.field.generator.generatePickUpTutorial(1))
					break;
			}
	/*To Do: add to tutorial about weapon upgrades		
			for (int i = 0; i < 100; i++) {
				if(field.generator.generatePickUpTutorial(2))
					break;
			}
	*/	
			for (int i = 0; i < 100; i++) {
				if(SteveDriver.field.generator.generateEnemyTutorial())
					break;
			}
			
			//TODO: TEMP
			if (SteveDriver.snake.getMoney() == 0 && !SteveDriver.tutorial.isActive()) {
				SteveDriver.tutorial.startTutorial();
			}
			
			SteveDriver.stage = STAGE_TYPE.GAME;
		}
	}
}
