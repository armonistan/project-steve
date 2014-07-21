package com.steve.stages;

import com.badlogic.gdx.graphics.Color;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.TextButton;
import com.steve.commands.ChangeStage;
import com.steve.commands.StartNewGame;
import com.steve.helpers.GUIHelper.BoxColors;

public class ConfirmNewGame {
	TextButton backButton;
	TextButton doItButton;
	
	public ConfirmNewGame() {
		backButton = new TextButton(-1 * 6 * SteveDriver.TEXTURE_SIZE, 0 * SteveDriver.TEXTURE_SIZE, 12, 4, new ChangeStage(STAGE_TYPE.MENU), "WAIT, GO BACK!");
		doItButton = new TextButton(-1 * 6 * SteveDriver.TEXTURE_SIZE, -1 * 4 * SteveDriver.TEXTURE_SIZE, 12, 4, new StartNewGame(), "Commence");
	}
	
	public void render() {
		SteveDriver.menu.drawMenuField();

		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		SteveDriver.batch.begin();
		SteveDriver.menu.logo.draw(SteveDriver.batch);
		
		backButton.update();
		backButton.render();
		
		doItButton.update();
		doItButton.render();
		
		SteveDriver.guiHelper.drawBox(-1 * 11 * SteveDriver.TEXTURE_SIZE, 4 * SteveDriver.TEXTURE_SIZE, 22, 4, BoxColors.RED, Color.WHITE);
		SteveDriver.guiHelper.drawTextCentered("Are you sure you want this?", 0, 3 * SteveDriver.TEXTURE_SIZE, Color.BLACK);
		
		SteveDriver.batch.end();
	}
}
