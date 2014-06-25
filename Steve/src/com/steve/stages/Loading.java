package com.steve.stages;
import com.steve.SteveDriver;
import com.steve.TextButton;
import com.steve.commands.WrapUpLoading;

public class Loading {
	boolean threadFinished;
	
	TextButton startButton;
	
	public Loading() {
		threadFinished = false;
		
		startButton = new TextButton(-5f * SteveDriver.TEXTURE_SIZE,
				SteveDriver.guiCamera.viewportHeight / 2f * -1f + 4f * SteveDriver.TEXTURE_SIZE, 10, 4,
				new WrapUpLoading(), "Generating...");
	}
	
	public void render() {
		if (threadFinished) {
			startButton.setText("Start!");
			startButton.update();
		}
		
		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();
		
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		
		SteveDriver.batch.begin();	
		startButton.render();
		SteveDriver.batch.end();
		
		if (!SteveDriver.field.generatingField.isAlive()) {
			if (!threadFinished) {
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
				}*/
			
				for (int i = 0; i < 100; i++) {
					if(SteveDriver.field.generator.generateEnemyTutorial())
						break;
				}
				
				threadFinished = true;
			}
		}
	}
}
