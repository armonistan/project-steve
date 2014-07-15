package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.steve.SteveDriver;
import com.steve.TextButton;
import com.steve.commands.ChangeStage;
import com.steve.commands.OpenStore;

public class Summary {
	Texture diedEnemy;
	Texture diedStarvation;
	Texture diedBlocker;
	Texture diedSpace;
	Texture background;
	
	Sprite diedEnemySprite;
	Sprite diedStarvationSprite;
	Sprite diedBlockerSprite;
	Sprite diedSpaceSprite;
	Sprite backgroundSprite;
	
	Sprite savingSprite;
	
	public float appleScore;
	public float enemyScore;
	
	private float applePercent;
	private float enemyPercent;
	
	private float savingOpacity;
	private boolean fading;
	private float savingTime = 2.0f;
	private float savingTimer = 0f;
	
	private TextButton continueButton;
	private TextButton spaceButton;
	
	public boolean showingAds;
	
	private WHY_DIED why;
	
	public enum WHY_DIED {
		player,
		blocker,
		enemy,
		starvation,
		space
	}
	
	public Summary() {
		diedEnemy = new Texture(Gdx.files.internal("data/diedEnemy.png"));
		diedStarvation = new Texture(Gdx.files.internal("data/diedStarvation.png"));
		diedBlocker = new Texture(Gdx.files.internal("data/diedBlocker.png"));
		diedSpace = new Texture(Gdx.files.internal("data/diedSpace.png"));
		background = new Texture(Gdx.files.internal("data/diedBackground.png"));
		
		diedEnemySprite = new Sprite(new TextureRegion(diedEnemy, 0f, 0f, 1f, 1f));
		diedEnemySprite.scale(SteveDriver.guiCamera.viewportHeight / diedEnemySprite.getHeight() - 1f);
		diedEnemySprite.setPosition(diedEnemySprite.getWidth() / 2 * -1, diedEnemySprite.getHeight() / 2 * -1);
		diedStarvationSprite = new Sprite(new TextureRegion(diedStarvation, 0f, 0f, 1f, 1f));
		diedStarvationSprite.scale(SteveDriver.guiCamera.viewportHeight / diedStarvationSprite.getHeight() - 1f);
		diedStarvationSprite.setPosition(diedStarvationSprite.getWidth() / 2 * -1, diedStarvationSprite.getHeight() / 2 * -1);
		diedBlockerSprite = new Sprite(new TextureRegion(diedBlocker, 0f, 0f, 1f, 1f));
		diedBlockerSprite.scale(SteveDriver.guiCamera.viewportHeight / diedBlockerSprite.getHeight() - 1f);
		diedBlockerSprite.setPosition(diedBlockerSprite.getWidth() / 2 * -1, diedBlockerSprite.getHeight() / 2 * -1);
		diedSpaceSprite = new Sprite(new TextureRegion(diedSpace, 0f, 0f, 1f, 1f));
		diedSpaceSprite.scale(SteveDriver.guiCamera.viewportWidth / diedSpaceSprite.getWidth() - 1f);
		diedSpaceSprite.setPosition(diedSpaceSprite.getWidth() / 2 * -1, diedSpaceSprite.getHeight() / 2 * -1);
		backgroundSprite = new Sprite(new TextureRegion(background, 0f, 0f, 1f, 1f));
		backgroundSprite.scale(SteveDriver.guiCamera.viewportHeight / diedBlockerSprite.getHeight() - 1f);
		
		savingSprite = new Sprite(new TextureRegion(SteveDriver.atlas, 18 * SteveDriver.TEXTURE_SIZE, 26 * SteveDriver.TEXTURE_SIZE,
				3 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE));

		savingOpacity = 1f;
		fading = true;
		
		appleScore = 0;
		enemyScore = 0;
		
		continueButton = new TextButton(SteveDriver.guiCamera.position.x - 6 * SteveDriver.TEXTURE_SIZE,
				SteveDriver.guiCamera.position.y + 4 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiCamera.viewportHeight / 2, 12, 4,
				new OpenStore(), "To the Store!");
	
		spaceButton = new TextButton(SteveDriver.guiCamera.position.x - 6 * SteveDriver.TEXTURE_SIZE,
					SteveDriver.guiCamera.position.y + 12 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiCamera.viewportHeight / 2, 12, 4,
					new ChangeStage(SteveDriver.STAGE_TYPE.RESPAWNINGENDGAME), "Fufill your destiny");
		
		showingAds = false;
	}
	
	public void resetSummary() {
		appleScore = 0;
		enemyScore = 0;
		
		applePercent = 0.0f;
		enemyPercent = 0.0f;
	}
	
	public void setWhyDied(WHY_DIED died) {
		why = died;
	}
	
	public void render() {
		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);
		
		SteveDriver.batch.begin();
		for (int i = 0; i <= backgroundSprite.getWidth(); i += backgroundSprite.getWidth() * backgroundSprite.getScaleX()) {
			backgroundSprite.setPosition(SteveDriver.guiCamera.viewportWidth * -1 + i, backgroundSprite.getHeight() / 2 * -1);
			
			backgroundSprite.draw(SteveDriver.batch);
		}
		
		savingTimer += Gdx.app.getGraphics().getDeltaTime();
		
		if (savingOpacity >= 1f && savingTimer < savingTime) {
			fading = true;
		} else if (savingOpacity <= 0f || savingTimer > savingTime) {
			fading = false;
		}
		
		if (fading) {
			savingOpacity -= Gdx.app.getGraphics().getDeltaTime();
			if (savingOpacity < 0f) 
				savingOpacity = 0f;
		} else {
			savingOpacity += Gdx.app.getGraphics().getDeltaTime();
			if (savingOpacity > 1f) 
				savingOpacity = 1f;
		}
		
		if (savingTimer > savingTime) {
			savingOpacity = 1f;
			SteveDriver.store.saveStoreProgress();
		}
		
		switch (why) {
		case blocker:
			diedBlockerSprite.draw(SteveDriver.batch);
			break;
		case enemy:
			diedEnemySprite.draw(SteveDriver.batch);
			break;
		case player:
			break;
		case space:
			diedSpaceSprite.draw(SteveDriver.batch);
			break;
		case starvation:
			diedStarvationSprite.draw(SteveDriver.batch);
			break;
		default:
			break;
		
		}
		
		continueButton.update();
		continueButton.render();
		
		if(SteveDriver.prefs.getBoolean("canGoToSpace", false)){
			spaceButton.update();
			spaceButton.render();
		}
		
		if (savingTimer < savingTime) {
			SteveDriver.guiHelper.drawTextCentered("Saving...", 
				SteveDriver.guiCamera.position.x - 15 * SteveDriver.TEXTURE_SIZE, 
				SteveDriver.guiCamera.position.y + 6 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiCamera.viewportHeight / 2,
				Color.BLACK);
		} else {
			SteveDriver.guiHelper.drawTextCentered("Saved!", 
					SteveDriver.guiCamera.position.x - 15 * SteveDriver.TEXTURE_SIZE, 
					SteveDriver.guiCamera.position.y + 6 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiCamera.viewportHeight / 2,
					Color.BLACK);
		}
		
		savingSprite.setPosition(SteveDriver.guiCamera.position.x - 16 * SteveDriver.TEXTURE_SIZE, 
				SteveDriver.guiCamera.position.y + 1 * SteveDriver.TEXTURE_SIZE - SteveDriver.guiCamera.viewportHeight / 2);
		
		savingSprite.draw(SteveDriver.batch, savingOpacity);
		
		SteveDriver.guiHelper.drawTextCentered("Apples: $" + Math.round(appleScore * applePercent), 
				SteveDriver.guiCamera.position.x, 
				SteveDriver.guiCamera.position.y,
				Color.YELLOW);
		
		SteveDriver.guiHelper.drawTextCentered("Enemies: $" + Math.round(enemyScore * enemyPercent), 
				SteveDriver.guiCamera.position.x, 
				SteveDriver.guiCamera.position.y + 3 * SteveDriver.TEXTURE_SIZE,
				Color.YELLOW);

		SteveDriver.batch.end();
		
		updateCounts();
	}
	
	private void updateCounts() {
		if (applePercent < 1f) {
			applePercent += 0.01f;
		} 
		else {
			applePercent = 1f;
		}

		if (enemyPercent < 1f) {
			enemyPercent += 0.01f;
		}
		else {
			enemyPercent = 1f;
		}
	}
}
