package com.steve;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GUI {
	
	private float height;
	private ArrayList<Sprite> guiTextures;
	private Vector2 leftEndPosition;
	private Vector2 rightEndPosition;
	private int healthWidth = 4;
	private int healthColor = 2;
	private float currentHealthPercent;
	
	private float spriteWidth = 48;
	
	private Sprite endHP;
	
	public GUI() {
		guiTextures = new ArrayList<Sprite>();
		for (int i = 0; i < 6; i++) {
			this.guiTextures.add(new Sprite(new TextureRegion(SteveDriver.atlas, 240-(48*i), 352, 48, 64)));
		}
		height = SteveDriver.constants.get("screenHeight");
		leftEndPosition = new Vector2(48 * -3, height/2 -100);
		rightEndPosition = new Vector2(48 * 2, height/2 -100);
		guiTextures.get(5).setPosition(leftEndPosition.x, leftEndPosition.y);
		guiTextures.get(0).setPosition(rightEndPosition.x, rightEndPosition.y);
		
		endHP = new Sprite(new TextureRegion(SteveDriver.atlas, 0, 352, 48, 64));
	}
	
	public void render() {
		currentHealthPercent = 1 - (SteveDriver.snake.GetHungerTimer() / SteveDriver.snake.GetStarveTime());
		
		SteveDriver.batch.begin();
		guiTextures.get(0).draw(SteveDriver.batch);
		guiTextures.get(5).draw(SteveDriver.batch);
		for (int i = 0; i < healthWidth; i++) {
			guiTextures.get(1).setPosition(48 * (i - 2), height/2 -100);
			guiTextures.get(1).draw(SteveDriver.batch);
		}
		
		int finalPixel = (int) (healthWidth * currentHealthPercent);
		
		for (int i = 0; i < finalPixel + 1; i++) {
			if (currentHealthPercent > .5f) {
				healthColor = 2;
			} else if (currentHealthPercent < .5f && currentHealthPercent > .25f) {
				healthColor = 3;
			} else if (currentHealthPercent < .25f) {
				healthColor = 4;
			}
			
			
			if (i == finalPixel) {
				int spriteSubSectionWidth = (int) (spriteWidth * ((currentHealthPercent * 100) % 25) / 25);
				endHP.setRegionX(240-(48*healthColor));
				endHP.setRegionWidth(spriteSubSectionWidth);
				endHP.setBounds(48f * (i - 2f), height/2f - 100f, spriteSubSectionWidth, 64f);
				endHP.draw(SteveDriver.batch);
			} else {
				guiTextures.get(healthColor).setPosition(48 * (i - 2), height/2 -100);
				guiTextures.get(healthColor).draw(SteveDriver.batch);
			}
		}
		
		SteveDriver.batch.end();
	}
}
