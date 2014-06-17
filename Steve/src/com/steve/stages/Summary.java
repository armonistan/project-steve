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
				SteveDriver.guiCamera.position.y + 2 * SteveDriver.TEXTURE_LENGTH, 12, 4,
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
		
		SteveDriver.guiHelper.drawText("Apples:", 
				SteveDriver.guiHelper.screenToCoordinateSpaceX((int) (1 * SteveDriver.constants.get("screenWidth") / 4)), 
				SteveDriver.guiHelper.screenToCoordinateSpaceY(2 * (int) SteveDriver.constants.get("screenHeight") / 4),
				Color.BLACK);
		
		SteveDriver.guiHelper.drawText("$" + Math.round(appleScore * applePercent), 
				SteveDriver.guiHelper.screenToCoordinateSpaceX(25 + (int) (2 * SteveDriver.constants.get("screenWidth") / 4)), 
				SteveDriver.guiHelper.screenToCoordinateSpaceY(2 * (int) SteveDriver.constants.get("screenHeight") / 4),
				Color.BLACK);
		
		SteveDriver.guiHelper.drawText("Enemies:", 
				SteveDriver.guiHelper.screenToCoordinateSpaceX((int) (1 * SteveDriver.constants.get("screenWidth") / 4)), 
				SteveDriver.guiHelper.screenToCoordinateSpaceY(3 *(int) SteveDriver.constants.get("screenHeight") / 5),
				Color.BLACK);
		
		SteveDriver.guiHelper.drawText("$" + Math.round(enemyCount), 
				SteveDriver.guiHelper.screenToCoordinateSpaceX(25 + (int) (2 * SteveDriver.constants.get("screenWidth") / 4)), 
				SteveDriver.guiHelper.screenToCoordinateSpaceY(3 * (int) SteveDriver.constants.get("screenHeight") / 5),
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
