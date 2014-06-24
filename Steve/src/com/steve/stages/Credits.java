package com.steve.stages;

import com.badlogic.gdx.graphics.Color;
import com.steve.SteveDriver;
import com.steve.TextButton;
import com.steve.commands.ChangeStage;

public class Credits {
	
	public float appleScore;
	public float enemyScore;
	
	private float applePercent;
	private float enemyPercent;
	
	private TextButton backButton;
	
	public boolean showingAds;
	
	public Credits() {
		appleScore = 0;
		enemyScore = 0;
		
		backButton = new TextButton(SteveDriver.guiCamera.position.x - 6 * SteveDriver.TEXTURE_WIDTH,
				SteveDriver.guiCamera.position.y + 4 * SteveDriver.TEXTURE_LENGTH - SteveDriver.guiCamera.viewportHeight / 2, 12, 4,
				new ChangeStage(SteveDriver.STAGE_TYPE.MENU), "Back to Menu");
		
		showingAds = false;
	}
	
	public void render() {
		backButton.update();
		backButton.render();
		
		SteveDriver.guiHelper.drawTextCentered("Armon", 
				SteveDriver.guiCamera.position.x + 1 * SteveDriver.TEXTURE_WIDTH, 
				SteveDriver.guiCamera.position.y + 4 * SteveDriver.TEXTURE_LENGTH,
				Color.BLACK);
		
		SteveDriver.guiHelper.drawTextCentered("Meatspin?", 
				SteveDriver.guiCamera.position.x + 1 * SteveDriver.TEXTURE_WIDTH, 
				SteveDriver.guiCamera.position.y + 1 * SteveDriver.TEXTURE_LENGTH,
				Color.BLACK);
	}
}
