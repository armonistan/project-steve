package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.steve.base.Enemy;
import com.steve.base.Pickup;
import com.steve.commands.ChangeTutorialStage;
import com.steve.commands.EndTutorial;
import com.steve.enemies.Slug;
import com.steve.helpers.CollisionHelper;
import com.steve.pickups.Apple;
import com.steve.pickups.GatlingGunPickUp;
import com.steve.pickups.LaserPickUp;
import com.steve.pickups.SpecialistPickUp;
import com.steve.stages.Field;

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
	
	Sprite grayBox;
	Rectangle noGray;
	Vector3 focus;
	
	TextButton continueButton;
	TextButton exitButton;
	
	public Tutorial() {
		stage = TUTORIAL_STAGE_TYPE.intro;
		
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
			noGray.y = -32;
			noGray.x = -32;
			noGray.width = 32;
			noGray.height = 32;
			focus = SteveDriver.snake.getHeadPosition();
			break;
		case movement:
			description = "You move by touching the screen.";
			noGray.y = -32;
			noGray.x = -32;
			noGray.width = 32;
			noGray.height = 32;
			focus = SteveDriver.snake.getHeadPosition();
			break;
		case hunger:
			description = "Eat apples to grow your snake.\nEat them to prevent your own destruction.";
			focus = new Vector3(30 * SteveDriver.TEXTURE_WIDTH, 40 * SteveDriver.TEXTURE_LENGTH, 0);
			noGray.y = -32;
			noGray.x = -32;
			noGray.width = 32;
			noGray.height = 32;
			
			Vector3 tempApple = new Vector3();
			float closestAppleDistance = Float.POSITIVE_INFINITY;
			
			for (Pickup p : Field.pickups) {
				if (p.getClass() == Apple.class) {
					float tempDist = CollisionHelper.distanceSquared(p.getX(), p.getY(),
							SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_WIDTH, SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_LENGTH);
						
					if (tempDist < closestAppleDistance) {
						closestAppleDistance = tempDist;
						tempApple.x = p.getX();
						tempApple.y = p.getY();
					}
				}
			}
			
			focus = tempApple;
			
			break;
		case blockers:
			description = "Don't run into these rocks!";
			noGray.y = -48;
			noGray.x = -48;
			noGray.width = 48;
			noGray.height = 48;
			
			TiledMapTileLayer blockerLayer = Field.blockers;
			
			Vector3 tempBlocker = new Vector3();
			float closestDistance = Float.POSITIVE_INFINITY;
			
			for (int x = 0; x < blockerLayer.getWidth(); x++) {
				for (int y = 0; y < blockerLayer.getHeight(); y++) {
					Cell temp = blockerLayer.getCell(x, y);
					
					if (temp != null) {
						float tempDist = CollisionHelper.distanceSquared(x, y, SteveDriver.field.totalRadius / 2, SteveDriver.field.totalRadius / 2);
						
						if (tempDist < closestDistance) {
							closestDistance = tempDist;
							tempBlocker.x = x * SteveDriver.TEXTURE_WIDTH;
							tempBlocker.y = y * SteveDriver.TEXTURE_LENGTH;
						}
					}
				}
			}
			
			focus = tempBlocker;
			break;
		case enemies:
			description = "Various enemies roam your world.\nAttack them with your face.";
			noGray.y = -32;
			noGray.x = -32;
			noGray.width = 48;
			noGray.height = 48;
			
			Vector3 tempEnemy = new Vector3();
			float closestEnemyDistance = Float.POSITIVE_INFINITY;
			
			for (Enemy e : SteveDriver.field.enemies) {
				if (e.getClass() == Slug.class) {
					float tempDist = CollisionHelper.distanceSquared(e.avatar.getX(), e.avatar.getY(),
							SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_WIDTH, SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_LENGTH);
						
					if (tempDist < closestEnemyDistance) {
						closestEnemyDistance = tempDist;
						tempEnemy.x = e.avatar.getX();
						tempEnemy.y = e.avatar.getY();
					}
				}
			}
			
			focus = tempEnemy;
			break;
		case weapons:
			description = "Actually, don't use your face.\nTry eating these first.";
			focus = new Vector3(30 * SteveDriver.TEXTURE_WIDTH, 20 * SteveDriver.TEXTURE_LENGTH, 0);
			noGray.y = -32;
			noGray.x = -32;
			noGray.width = 32;
			noGray.height = 32;
			
			Vector3 tempWeapon = new Vector3();
			float closestWeaponDistance = Float.POSITIVE_INFINITY;
			
			for (Pickup p : Field.pickups) {
				if (p.getClass() == GatlingGunPickUp.class || p.getClass() == SpecialistPickUp.class || p.getClass() == LaserPickUp.class) {
					float tempDist = CollisionHelper.distanceSquared(p.getX(), p.getY(),
							SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_WIDTH, SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_LENGTH);
						
					if (tempDist < closestWeaponDistance) {
						closestWeaponDistance = tempDist;
						tempWeapon.x = p.getX();
						tempWeapon.y = p.getY();
					}
				}
			}
			
			focus = tempWeapon;
			
			break;
		case goodFuckingLuck:
			description = "Kill all the stuff!\nKeep moving away from the middle.";
			noGray.y = -32;
			noGray.x = -32;
			noGray.width = 32;
			noGray.height = 32;
			focus = SteveDriver.snake.getHeadPosition();
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
		active = SteveDriver.tutorialOn;
	}
	
	public void endTutorial() {
		SteveDriver.tutorialOn = false;
		active = SteveDriver.tutorialOn;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public Vector3 getFocus() {
		return focus;
	}
	
	private void drawGray() {
		for (float x = SteveDriver.guiCamera.viewportWidth / 2 * -1; x < SteveDriver.guiCamera.viewportWidth / 2; x += SteveDriver.TEXTURE_WIDTH) {
			for (float y = SteveDriver.guiCamera.viewportHeight / 2 * -1; y < SteveDriver.guiCamera.viewportHeight / 2; y += SteveDriver.TEXTURE_LENGTH) {
				if (!(x > noGray.x && x <= noGray.x + SteveDriver.TEXTURE_WIDTH + noGray.width && 
					  y > noGray.y && y <= noGray.y + SteveDriver.TEXTURE_LENGTH + noGray.height)) {
					grayBox.setPosition(x, y);
					grayBox.draw(SteveDriver.batch);
				}
			}
		}
	}
}
