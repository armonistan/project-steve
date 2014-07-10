package com.steve.stages;

import com.badlogic.gdx.graphics.Color;
import com.steve.SteveDriver;
import com.steve.TextButton;
import com.steve.commands.ChangeStage;
import com.steve.helpers.GUIHelper.BoxColors;

public class Credits {
	
	public float appleScore;
	public float enemyScore;
	
	private float applePercent;
	private float enemyPercent;
	
	private TextButton backButton;
	
	public boolean showingAds;
	
	private String[] lines = {"Emberware Team:",
			"Armon Nayeraini",
			"David \"Steak\" Campbell",
			"Daniel Pumford",
			"Riley Turben",
			"Alyssa Draper",
			"James Green",
			"Tyler Ferguson"};
	
	public Credits() {
		appleScore = 0;
		enemyScore = 0;
		
		backButton = new TextButton(SteveDriver.guiCamera.position.x - 6 * SteveDriver.TEXTURE_SIZE,
				SteveDriver.guiCamera.position.y + 4 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiCamera.viewportHeight / 2, 12, 4,
				new ChangeStage(SteveDriver.STAGE_TYPE.MENU), "Back to Menu");
		
		showingAds = false;
	}
	
	public void render() {
		SteveDriver.menu.drawMenuField();

		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		SteveDriver.batch.begin();
		SteveDriver.menu.logo.draw(SteveDriver.batch);
		
		backButton.update();
		backButton.render();
		
		SteveDriver.guiHelper.drawBox(-1 * 15 * SteveDriver.TEXTURE_SIZE / 2, 7 * SteveDriver.TEXTURE_SIZE, 15, 18, BoxColors.GOLD);
		
		for (int i = 0; i < lines.length; i++) {
			SteveDriver.guiHelper.drawTextCentered(lines[i], 
					SteveDriver.guiCamera.position.x + 0 * SteveDriver.TEXTURE_SIZE, 
					SteveDriver.guiCamera.position.y + (lines.length / 2 - i - 1) * 2 * SteveDriver.TEXTURE_SIZE,
					Color.BLACK);
		}

		SteveDriver.batch.end();
	}
}
