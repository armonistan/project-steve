package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.steve.commands.ChangeTutorialStage;
import com.steve.commands.EndTutorial;

public class Tutorial {
	public enum TUTORIAL_STAGE_TYPE {
		intro,
		movement,
		hunger,
		blockers,
		enemies,
		weapons,
		goodFuckingLuck
	}
	
	private TUTORIAL_STAGE_TYPE stage;
	private String description;
	private boolean active;
	
	boolean centered;
	Sprite grayBox;
	Rectangle noGray;
	
	TextButton continueButton;
	TextButton exitButton;
	
	public Tutorial() {
		stage = TUTORIAL_STAGE_TYPE.intro;
		
		centered = true;
		grayBox = new Sprite(new TextureRegion(SteveDriver.atlas, 21 * SteveDriver.TEXTURE_WIDTH, 17 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		noGray = new Rectangle();
		
		continueButton = new TextButton(Gdx.graphics.getWidth() / 2 - 160, 0, 9, 4, new ChangeTutorialStage(), "Continue");
		exitButton = new TextButton(Gdx.graphics.getWidth() / 2 - 160 - 16, -100, 10, 4, new EndTutorial(), "We Get It");
	}
	
	public void render() {
		SteveDriver.batch.begin();
		drawGray();
		
		continueButton.update();
		continueButton.render();
		exitButton.update();
		exitButton.render();
		
		switch (stage) {
		case intro:
			description = "This is you. You are a snake named Steve.\nYou want to get to Space.";
			noGray.y = 20;
			noGray.x = -16;
			noGray.width = 160;
			noGray.height = 64;
			break;
		case movement:
			description = "You move by touching the screen.";
			break;
		case hunger:
			description = "Eat apples to grow your snake.\nEat them to prevent your own destruction.";
			break;
		case blockers:
			description = "Don't run into these rocks!";
			break;
		case enemies:
			description = "Various enemies roam your world.\nAttack them with your face.";
			break;
		case weapons:
			description = "Actually, don't use your face.\nTry eating these first.";
			break;
		case goodFuckingLuck:
			description = "Kill all the stuff!\nKeep moving away from the movie.";
			break;
		}
		
		SteveDriver.guiHelper.drawText(description, Gdx.graphics.getWidth() / 2 * -0.9f, Gdx.graphics.getHeight() / 2 * -0.85f, Color.BLACK);
		
		SteveDriver.batch.end();
	}
	
	public void goToNextStage() {
		if (stage.ordinal() < TUTORIAL_STAGE_TYPE.values().length - 1) {
			stage = TUTORIAL_STAGE_TYPE.values()[stage.ordinal() + 1];
		}
		else {
			stage = TUTORIAL_STAGE_TYPE.values()[0];
		}
	}
	
	public void startTutorial() {
		stage = TUTORIAL_STAGE_TYPE.values()[0];
		active = true;
	}
	
	public void endTutorial() {
		active = false;
	}
	
	public boolean isActive() {
		return active;
	}
	
	private void drawGray() {
		for (float x = Gdx.graphics.getWidth() / 2 * -1; x < Gdx.graphics.getWidth() / 2; x += SteveDriver.TEXTURE_WIDTH) {
			for (float y = Gdx.graphics.getHeight() / 2 * -1; y < Gdx.graphics.getHeight() / 2; y += SteveDriver.TEXTURE_LENGTH) {
				if (!(x > noGray.x && x <= noGray.x + SteveDriver.TEXTURE_WIDTH + noGray.width && 
					  y > noGray.y && y <= noGray.y + SteveDriver.TEXTURE_LENGTH + noGray.height)) {
					grayBox.setPosition(x, y);
					grayBox.draw(SteveDriver.batch);
				}
			}
		}
	}
}
