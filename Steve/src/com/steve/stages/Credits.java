package com.steve.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.steve.SpriteButton;
import com.steve.SteveDriver;
import com.steve.TextButton;
import com.steve.commands.ChangeStage;
import com.steve.commands.OpenWebsite;
import com.steve.helpers.GUIHelper.BoxColors;

public class Credits {
	
	private TextButton backButton;
	private SpriteButton facebookButton;
	private SpriteButton twitterButton;
	
	private String[] lines = {"Emberware Team:",
			"Armon Nayeraini",
			"David \"Steak\" Campbell",
			"Daniel Pumford",
			"Riley Turben",
			"Alyssa Draper",
			"James Green",
			"Tyler Ferguson"};
	
	public Credits() {
		backButton = new TextButton(SteveDriver.guiCamera.position.x - 6 * SteveDriver.TEXTURE_SIZE,
				SteveDriver.guiCamera.position.y + 4 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiCamera.viewportHeight / 2, 12, 4,
				new ChangeStage(SteveDriver.STAGE_TYPE.MENU), "Back to Menu");
		
		facebookButton = new SpriteButton(SteveDriver.guiCamera.position.x + SteveDriver.guiCamera.viewportWidth / 2 - 4 * SteveDriver.TEXTURE_SIZE,
				SteveDriver.guiCamera.position.y + 4 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiCamera.viewportHeight / 2, 4, 4,
				new OpenWebsite("https://www.facebook.com/pages/Steve-Snake-Evolved/532055363584445"), new Sprite(SteveDriver.assets.get("data/facebook_logo.png", Texture.class)));
		
		twitterButton = new SpriteButton(SteveDriver.guiCamera.position.x + SteveDriver.guiCamera.viewportWidth / 2 - 8 * SteveDriver.TEXTURE_SIZE,
				SteveDriver.guiCamera.position.y + 4 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiCamera.viewportHeight / 2, 4, 4,
				new OpenWebsite("http://www.twitter.com/emberwareDev"), new Sprite(SteveDriver.assets.get("data/twitter_logo.png", Texture.class)));
	}
	
	public void render() {
		SteveDriver.menu.drawMenuField();

		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		SteveDriver.batch.begin();
		SteveDriver.menu.logo.draw(SteveDriver.batch);
		
		backButton.update();
		backButton.render();
		
		facebookButton.update();
		facebookButton.render();
		
		twitterButton.update();
		twitterButton.render();
		
		SteveDriver.guiHelper.drawBox(-1 * 18 * SteveDriver.TEXTURE_SIZE / 2, 7 * SteveDriver.TEXTURE_SIZE, 18, 18, BoxColors.GOLD, Color.WHITE);
		
		for (int i = 0; i < lines.length; i++) {
			SteveDriver.guiHelper.drawTextCentered(lines[i], 
					SteveDriver.guiCamera.position.x + 0 * SteveDriver.TEXTURE_SIZE, 
					SteveDriver.guiCamera.position.y + (lines.length / 2 - i - 1) * 2 * SteveDriver.TEXTURE_SIZE,
					Color.BLACK);
		}

		SteveDriver.batch.end();
	}
}
