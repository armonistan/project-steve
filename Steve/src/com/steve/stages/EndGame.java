package com.steve.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.steve.AstroSteve;
import com.steve.SteveDriver;
import com.steve.TextButton;
import com.steve.commands.ChangeStage;
import com.steve.commands.KillSnake;
import com.steve.commands.OpenStore;
import com.steve.helpers.GUIHelper.BoxColors;

public class EndGame extends Game {
	private AstroSteve astro;
	
	private TextButton returnToStore;
	private TextButton returnToMenu;
	
	public EndGame() {
		super();
		
		returnToStore = new TextButton(-1 * 5 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 10, 4, new KillSnake(), "Store");
		returnToMenu = new TextButton(-1 * 5 * SteveDriver.TEXTURE_SIZE, -1 * 2 * SteveDriver.TEXTURE_SIZE, 10, 4, new ChangeStage(SteveDriver.STAGE_TYPE.MENU), "Menu");
	}
	
	@Override
	public void render() {
		astro = (AstroSteve)SteveDriver.snake;
		
		if (astro.getTimeInSpace() > 0) {
			super.render();
		}
		else {
			SteveDriver.camera.update();
			
			SteveDriver.guiCamera.position.x = 0;
			SteveDriver.guiCamera.position.y = 0;
			SteveDriver.guiCamera.update();
			
			SteveDriver.batch.setProjectionMatrix(SteveDriver.camera.combined);
			SteveDriver.batch.begin();
			SteveDriver.field.space.draw(SteveDriver.batch);
			SteveDriver.snake.update();
			SteveDriver.snake.draw();
			SteveDriver.batch.end();
			
			SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
			SteveDriver.batch.begin();
			SteveDriver.guiHelper.drawBox(-1 * 15 * SteveDriver.TEXTURE_SIZE, 10 * SteveDriver.TEXTURE_SIZE, 30, 4, BoxColors.GOLD);
			SteveDriver.guiHelper.drawTextCentered("Thanks For Playing!", 0, 10 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiHelper.activeFont.getLineHeight() / 2, Color.BLACK);
			returnToStore.update();
			returnToStore.render();
			returnToMenu.update();
			returnToMenu.render();
			SteveDriver.batch.end();
		}
	}
}
