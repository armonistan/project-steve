package com.steve.stages;

import com.badlogic.gdx.graphics.Color;
import com.steve.SteveDriver;
import com.steve.TextButton;
import com.steve.commands.ChangeStage;

public class Summary {
	
	public float appleScore;
	public float enemyScore;

	private float appleCount;
	private float enemyCount;
	
	private float applePercent;
	
	private TextButton continueButton;
	
	public Summary() {
		appleScore = 0;
		enemyScore = 0;
		
		appleCount = 0;
		enemyCount = 0;
		
		continueButton = new TextButton(SteveDriver.guiCamera.position.x - 6 * SteveDriver.TEXTURE_WIDTH,
				SteveDriver.guiCamera.position.y + 4 * SteveDriver.TEXTURE_LENGTH - SteveDriver.guiCamera.viewportHeight / 2, 12, 4,
				new ChangeStage(SteveDriver.STAGE_TYPE.STORE), "To the Store!");
	}
	
	public void resetSummary() {
		appleScore = 0;
		enemyScore = 0;
		
		applePercent = 0.0f;
		
		appleCount = 0;
		enemyCount = 0;
	}
	
	public void render() {
		continueButton.update();
		continueButton.render();
		
		SteveDriver.guiHelper.drawTextCentered("Apples: $" + Math.round(appleScore * applePercent), 
				SteveDriver.guiCamera.position.x - 14 * SteveDriver.TEXTURE_WIDTH, 
				SteveDriver.guiCamera.position.y + 3 * SteveDriver.TEXTURE_LENGTH - SteveDriver.guiCamera.viewportHeight / 2,
				Color.BLACK);
		
		SteveDriver.guiHelper.drawTextCentered("Enemies: $" + Math.round(enemyCount), 
				SteveDriver.guiCamera.position.x + 14 * SteveDriver.TEXTURE_WIDTH, 
				SteveDriver.guiCamera.position.y + 3 * SteveDriver.TEXTURE_LENGTH - SteveDriver.guiCamera.viewportHeight / 2,
				Color.BLACK);
		
		updateCounts();
	}
	
	private void updateCounts() {
		if (applePercent < .99f) {
			applePercent += 0.01f;
		} 

		if (enemyCount < enemyScore) {
			enemyCount++;
		}
	}
}
