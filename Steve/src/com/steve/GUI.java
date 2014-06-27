package com.steve;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.commands.ChangeStage;

public class GUI {
	
	private float height;
	private ArrayList<Sprite> guiTextures;
	private Vector2 leftEndPosition;
	private Vector2 rightEndPosition;
	private int healthWidth = 4;
	private int healthColor = 2;
	private float currentHealthPercent;
	
	private float spriteWidth = 3 * SteveDriver.TEXTURE_SIZE;
	
	private Sprite endHP;
	
	public GUI() {
		guiTextures = new ArrayList<Sprite>();
		for (int i = 0; i < 6; i++) {
			this.guiTextures.add(new Sprite(new TextureRegion(SteveDriver.atlas, 15 * SteveDriver.TEXTURE_SIZE -
					(3 * SteveDriver.TEXTURE_SIZE * i), 22 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE, 4 * SteveDriver.TEXTURE_SIZE)));
		}
		height = SteveDriver.constants.get("screenHeight");
		leftEndPosition = new Vector2(3  * SteveDriver.TEXTURE_SIZE * -3, height / 2 - 200);
		rightEndPosition = new Vector2(3  * SteveDriver.TEXTURE_SIZE * 2, height / 2 - 200);
		guiTextures.get(5).setPosition(leftEndPosition.x, leftEndPosition.y);
		guiTextures.get(0).setPosition(rightEndPosition.x, rightEndPosition.y);
		
		endHP = new Sprite(new TextureRegion(SteveDriver.atlas, 0, 22 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE, 4 * SteveDriver.TEXTURE_SIZE));
	}
	
	public void render() {
		currentHealthPercent = 1 - (SteveDriver.snake.GetHungerTimer() / SteveDriver.snake.GetStarveTime());
		
		for (Sprite s : guiTextures) {
			s.setRegion(s.getRegionX(), (22 + (SteveDriver.snake.getLastDamageTimer() > 0 ? 4 : 0)) * SteveDriver.TEXTURE_SIZE,
					s.getRegionWidth(), s.getRegionHeight());
		}

		Sprite temp = guiTextures.get(5);
		if (SteveDriver.snake.segments.size() == 2) {
			if (temp.getRegionY() < (22 + 8) * SteveDriver.TEXTURE_SIZE) {
				temp.setRegion(temp.getRegionX(), temp.getRegionY() + (8) * SteveDriver.TEXTURE_SIZE,
					temp.getRegionWidth(), temp.getRegionHeight());
			}
		}
		else {
			if (temp.getRegionY() >= (22 + 8) * SteveDriver.TEXTURE_SIZE) {
				temp.setRegion(temp.getRegionX(), temp.getRegionY() - (8) * SteveDriver.TEXTURE_SIZE,
					temp.getRegionWidth(), temp.getRegionHeight());
			}
		}
		
		SteveDriver.batch.begin();
		guiTextures.get(0).draw(SteveDriver.batch);
		guiTextures.get(5).draw(SteveDriver.batch);
		for (int i = 0; i < healthWidth; i++) {
			guiTextures.get(1).setPosition(3 * SteveDriver.TEXTURE_SIZE * (i - 2), height / 2 -200);
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
				endHP.setRegionX(15 * SteveDriver.TEXTURE_SIZE - (3  * SteveDriver.TEXTURE_SIZE * healthColor));
				endHP.setRegionWidth(spriteSubSectionWidth);
				endHP.setBounds(3f * SteveDriver.TEXTURE_SIZE * (i - 2f), height / 2f - 200f, spriteSubSectionWidth, 4f * SteveDriver.TEXTURE_SIZE);
				endHP.draw(SteveDriver.batch);
			} else {
				guiTextures.get(healthColor).setPosition(3f * SteveDriver.TEXTURE_SIZE * (i - 2), height/2 - 200);
				guiTextures.get(healthColor).draw(SteveDriver.batch);
			}
		}
		
		SteveDriver.batch.end();
	}
}
