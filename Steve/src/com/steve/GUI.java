package com.steve;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GUI {
	
	private float width, height;
	private ArrayList<Sprite> guiTextures;
	private Vector2 leftEndPosition;
	private Vector2 rightEndPosition;
	private int healthWidth = 4;
	private int healthColor = 2;
	private float currentHealth;
	
	public static BitmapFont font;
	
	private enum guiText {
		LEFTEND, RED, YELLOW, GREEN, DEAD, RIGHTEND;
	}
	
	public GUI() {
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		
		guiTextures = new ArrayList();
		for (int i = 0; i < 6; i++) {
			this.guiTextures.add(new Sprite(new TextureRegion(SteveDriver.atlas, 240-(48*i), 352, 48, 96)));
		}
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		leftEndPosition = new Vector2(48 * -3, height/2 -100);
		rightEndPosition = new Vector2(48 * 2, height/2 -100);
		guiTextures.get(5).setPosition(leftEndPosition.x, leftEndPosition.y);
		guiTextures.get(0).setPosition(rightEndPosition.x, rightEndPosition.y);
	}
	
	public void render() {
		currentHealth = 1 - (SteveDriver.snake.GetHungerTimer() / SteveDriver.snake.GetStarveTime());
		
		font.setColor(Color.BLACK);
		
		SteveDriver.batch.begin();
		guiTextures.get(0).draw(SteveDriver.batch);
		guiTextures.get(5).draw(SteveDriver.batch);
		for (int i = 0; i < healthWidth; i++) {
			guiTextures.get(1).setPosition(48 * (i - 2), height/2 -100);
			guiTextures.get(1).draw(SteveDriver.batch);
		}
		
		for (int i = 0; i < (int) (healthWidth * currentHealth) + 1; i++) {
			if (currentHealth > .5) {
				healthColor = 2;
			} else if (currentHealth < .5 && currentHealth > .25) {
				healthColor = 3;
			} else if (currentHealth < .25) {
				healthColor = 4;
			}
			guiTextures.get(healthColor).setPosition(48 * (i - 2), height/2 -100);
			guiTextures.get(healthColor).draw(SteveDriver.batch);
		}
		
		font.draw(SteveDriver.batch, SteveDriver.snake.getMoney() + "", leftEndPosition.x, leftEndPosition.y);
		
		SteveDriver.batch.end();
	}
}
