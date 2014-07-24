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
import com.steve.helpers.GUIHelper;
import com.steve.pickups.Apple;
import com.steve.pickups.BossSummon;
import com.steve.pickups.ClassicApple;
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
	
	public enum TUTORIAL_TYPE {
		main,
		edge,
		cyborgBoss,
		robotBoss,
		endGame
	}
	
	private TUTORIAL_STAGE_TYPE stage;
	private TUTORIAL_TYPE type;
	private String description;
	private boolean active;
	
	Sprite grayBox;
	Rectangle noGray;
	Vector3 focus;
	
	TextButton continueButton;
	TextButton exitButton;
	
	public Tutorial() {
		stage = TUTORIAL_STAGE_TYPE.intro;
		type = TUTORIAL_TYPE.main;
		
		grayBox = new Sprite(new TextureRegion(SteveDriver.atlas, 21 * SteveDriver.TEXTURE_SIZE, 17 * SteveDriver.TEXTURE_SIZE,
				SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
		noGray = new Rectangle();
		
		continueButton = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 * -1f,
				SteveDriver.guiCamera.viewportHeight / 4f * -1f + 4f * SteveDriver.TEXTURE_SIZE, 6, 4, new ChangeTutorialStage(), "More");
		exitButton = new TextButton(SteveDriver.guiCamera.viewportWidth / 2 - 6 * SteveDriver.TEXTURE_SIZE,
				SteveDriver.guiCamera.viewportHeight / 4f * -1f + 4f * SteveDriver.TEXTURE_SIZE, 6, 4, new EndTutorial(), "Exit");
		
		focus = new Vector3();
	}
	
	public void render() {
		SteveDriver.batch.begin();
		drawGray();
		
		SteveDriver.guiHelper.drawBox(SteveDriver.guiCamera.viewportWidth / 2f * -1f,
				SteveDriver.guiCamera.viewportHeight / 2f / 2f * -1f - SteveDriver.TEXTURE_SIZE,
				(int)SteveDriver.guiCamera.viewportWidth / SteveDriver.TEXTURE_SIZE,
				(int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4, GUIHelper.BoxColors.BLACK, Color.WHITE);
		
		exitButton.update();
		exitButton.render();
		
		if (type == TUTORIAL_TYPE.main) {
			
			if (stage != TUTORIAL_STAGE_TYPE.goodFuckingLuck) {
				continueButton.update();
				continueButton.render();
			}
		
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
				description = "Move by touching the arrows on the screen.";
				noGray.y = -32;
				noGray.x = -32;
				noGray.width = 32;
				noGray.height = 32;
				focus = SteveDriver.snake.getHeadPosition();
				break;
			case hunger:
				description = "Eat apples to grow your snake.\nEat them to prevent your own destruction.";
				focus = new Vector3(30 * SteveDriver.TEXTURE_SIZE, 40 * SteveDriver.TEXTURE_SIZE, 0);
				noGray.y = -32;
				noGray.x = -32;
				noGray.width = 32;
				noGray.height = 32;
			
				Vector3 tempApple = new Vector3();
				float closestAppleDistance = Float.POSITIVE_INFINITY;
			
				for (Pickup p : Field.pickups) {
					if (p.getClass() == Apple.class || p.getClass() == ClassicApple.class) {
						float tempDist = CollisionHelper.distanceSquared(p.getX(), p.getY(),
							SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_SIZE, SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_SIZE);
						
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
								tempBlocker.x = x * SteveDriver.TEXTURE_SIZE;
								tempBlocker.y = y * SteveDriver.TEXTURE_SIZE;
							}
						}
					}
				}
			
				focus = tempBlocker;
				break;
			case enemies:
				description = "Various enemies roam your world.\nBe wary of them at all times.";
				noGray.y = -32;
				noGray.x = -32;
				noGray.width = 48;
				noGray.height = 48;
			
				Vector3 tempEnemy = new Vector3();
				float closestEnemyDistance = Float.POSITIVE_INFINITY;
			
				for (Enemy e : SteveDriver.field.enemies) {
					if (e.getClass() == Slug.class) {
						float tempDist = CollisionHelper.distanceSquared(e.getXPosition(), e.getYPosition(),
							SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_SIZE, SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_SIZE);
						
						if (tempDist < closestEnemyDistance) {
							closestEnemyDistance = tempDist;
							tempEnemy.x = e.getXPosition();
							tempEnemy.y = e.getYPosition();
						}
					}
				}
				
				focus = tempEnemy;
				break;
			case weapons:
				description = "Arm yourself in order to fight back.";
				focus = new Vector3(30 * SteveDriver.TEXTURE_SIZE, 20 * SteveDriver.TEXTURE_SIZE, 0);
				noGray.y = -32;
				noGray.x = -32;
				noGray.width = 32;
				noGray.height = 32;
			
				Vector3 tempWeapon = new Vector3();
				float closestWeaponDistance = Float.POSITIVE_INFINITY;
			
				for (Pickup p : Field.pickups) {
					if (p.getClass() == GatlingGunPickUp.class || p.getClass() == SpecialistPickUp.class || p.getClass() == LaserPickUp.class) {
						float tempDist = CollisionHelper.distanceSquared(p.getX(), p.getY(),
							SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_SIZE, SteveDriver.field.totalRadius / 2 * SteveDriver.TEXTURE_SIZE);
						
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
		}
		else if (type == TUTORIAL_TYPE.edge) {
			description = "You can't go to space until\nyou have ascended to Astro-Steve.";
			noGray.width = 32;
			noGray.height = 32;
			focus = SteveDriver.snake.getHeadPosition();
			
			float xModify = 0f;
			float yModify = 0f;
			
			if (SteveDriver.snake.getHeadPosition().x > SteveDriver.field.totalRadius / 2f * SteveDriver.TEXTURE_SIZE &&
				SteveDriver.snake.getHeadPosition().y > SteveDriver.field.totalRadius / 2f * SteveDriver.TEXTURE_SIZE) {
				if (SteveDriver.snake.getHeadPosition().x > SteveDriver.snake.getHeadPosition().y) {
					xModify = SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE - SteveDriver.snake.getHeadPosition().x;
				}
				else {
					yModify = SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE - SteveDriver.snake.getHeadPosition().y;
				}
			}
			else if (SteveDriver.snake.getHeadPosition().x < SteveDriver.field.totalRadius / 2f * SteveDriver.TEXTURE_SIZE &&
				SteveDriver.snake.getHeadPosition().y > SteveDriver.field.totalRadius / 2f * SteveDriver.TEXTURE_SIZE) {
				if (SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE - SteveDriver.snake.getHeadPosition().x > SteveDriver.snake.getHeadPosition().y) {
					xModify = (SteveDriver.snake.getHeadPosition().x) * -1f - SteveDriver.TEXTURE_SIZE;
				}
				else {
					yModify = SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE - SteveDriver.snake.getHeadPosition().y;
				}
			}
			else if (SteveDriver.snake.getHeadPosition().x < SteveDriver.field.totalRadius / 2f * SteveDriver.TEXTURE_SIZE &&
				SteveDriver.snake.getHeadPosition().y < SteveDriver.field.totalRadius / 2f * SteveDriver.TEXTURE_SIZE) {
				if (SteveDriver.snake.getHeadPosition().x < SteveDriver.snake.getHeadPosition().y) {
					xModify = (SteveDriver.snake.getHeadPosition().x) * -1f - SteveDriver.TEXTURE_SIZE;
				}
				else {
					yModify = (SteveDriver.snake.getHeadPosition().y) * -1f - SteveDriver.TEXTURE_SIZE;
				}
			}
			else {
				if (SteveDriver.snake.getHeadPosition().x > SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE - SteveDriver.snake.getHeadPosition().y) {
					xModify = SteveDriver.field.totalRadius * SteveDriver.TEXTURE_SIZE - SteveDriver.snake.getHeadPosition().x;
				}
				else {
					yModify = (SteveDriver.snake.getHeadPosition().y) * -1f - SteveDriver.TEXTURE_SIZE;
				}
			}
			
			noGray.x = SteveDriver.guiCamera.position.x + xModify - 1.5f * SteveDriver.TEXTURE_SIZE;
			noGray.y = SteveDriver.guiCamera.position.y + yModify - 1.5f * SteveDriver.TEXTURE_SIZE;
		}
		

		else if (type == TUTORIAL_TYPE.cyborgBoss) {
			description = "There is a disturbance. Investigate by eating this.\nPREPARE YOURSELF.";
			noGray.y = -32;
			noGray.x = -32;
			noGray.width = 32;
			noGray.height = 32;
			focus = new Vector3();
			
			for (Pickup p : Field.pickups) {
				if (p.getClass() == BossSummon.class) {
					focus = new Vector3(p.getX(), p.getY(), 0);
				}
			}	
		}
		
		else if (type == TUTORIAL_TYPE.robotBoss) {
			description = "Another disturbance. Investigate by eating this.\nPREPARE YOURSELF.";
			noGray.y = -32;
			noGray.x = -32;
			noGray.width = 32;
			noGray.height = 32;
			focus = new Vector3();
			
			for (Pickup p : Field.pickups) {
				if (p.getClass() == BossSummon.class) {
					focus = new Vector3(p.getX(), p.getY(), 0);
				}
			}	
		}
		
		else if(type == TUTORIAL_TYPE.endGame){
			description = "This is it. You have defeated every boss.\nGo now, and fulfill your destiny.";
			noGray.y = -32;
			noGray.x = -32;
			noGray.width = 32;
			noGray.height = 32;
			focus = SteveDriver.snake.getHeadPosition();
		}
		
		SteveDriver.guiHelper.drawText(description, SteveDriver.guiCamera.viewportWidth / 2 * -1 + 1 * SteveDriver.TEXTURE_SIZE,
				SteveDriver.guiCamera.viewportHeight / 2 * -1 + 6 * SteveDriver.TEXTURE_SIZE, Color.BLACK);
		
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
		type = TUTORIAL_TYPE.main;
	}
	
	public void endTutorial() {
		SteveDriver.tutorialOn = false;
		active = SteveDriver.tutorialOn;
	}
	
	public void startEdgeTutorial() {
		active = SteveDriver.tutorialOn;
		type = TUTORIAL_TYPE.edge;
	}
	
	public void startCyborgBossTutorial(){
		active = SteveDriver.tutorialOn;
		type = TUTORIAL_TYPE.cyborgBoss;
	}
	
	public void endGameTutorial(){
		active = SteveDriver.tutorialOn;
		type = TUTORIAL_TYPE.endGame;
	}
	
	public void startRobotBossTutorial(){
		active = SteveDriver.tutorialOn;
		type = TUTORIAL_TYPE.robotBoss;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public Vector3 getFocus() {
		return focus;
	}
	
	private void drawGray() {
		for (float x = SteveDriver.guiCamera.viewportWidth / 2 * -1; x < SteveDriver.guiCamera.viewportWidth / 2; x += SteveDriver.TEXTURE_SIZE) {
			for (float y = SteveDriver.guiCamera.viewportHeight / 2 * -1; y < SteveDriver.guiCamera.viewportHeight / 2; y += SteveDriver.TEXTURE_SIZE) {
				if (!(x > noGray.x && x <= noGray.x + SteveDriver.TEXTURE_SIZE + noGray.width && 
					  y > noGray.y && y <= noGray.y + SteveDriver.TEXTURE_SIZE + noGray.height)) {
					grayBox.setPosition(x, y);
					grayBox.draw(SteveDriver.batch);
				}
			}
		}
	}
}
