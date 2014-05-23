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
	
	private BitmapFont font;
	private Sprite topLeftBox;
	private Sprite topBox;
	private Sprite topRightBox;
	private Sprite leftBox;
	private Sprite centerBox;
	private Sprite rightBox;
	private Sprite bottomLeftBox;
	private Sprite bottomBox;
	private Sprite bottomRightBox;
	
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
		
		topLeftBox = new Sprite(new TextureRegion(SteveDriver.atlas, 18 * SteveDriver.TEXTURE_WIDTH, 17 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		topBox = new Sprite(new TextureRegion(SteveDriver.atlas, 19 * SteveDriver.TEXTURE_WIDTH, 17 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		topRightBox = new Sprite(new TextureRegion(SteveDriver.atlas, 20 * SteveDriver.TEXTURE_WIDTH, 17 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		leftBox = new Sprite(new TextureRegion(SteveDriver.atlas, 18 * SteveDriver.TEXTURE_WIDTH, 18 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		centerBox = new Sprite(new TextureRegion(SteveDriver.atlas, 19 * SteveDriver.TEXTURE_WIDTH, 18 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		rightBox = new Sprite(new TextureRegion(SteveDriver.atlas, 20 * SteveDriver.TEXTURE_WIDTH, 18 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		bottomLeftBox = new Sprite(new TextureRegion(SteveDriver.atlas, 18 * SteveDriver.TEXTURE_WIDTH, 19 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		bottomBox = new Sprite(new TextureRegion(SteveDriver.atlas, 19 * SteveDriver.TEXTURE_WIDTH, 19 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
		bottomRightBox = new Sprite(new TextureRegion(SteveDriver.atlas, 20 * SteveDriver.TEXTURE_WIDTH, 19 * SteveDriver.TEXTURE_LENGTH,
				SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH));
	}
	
	public void render() {
		currentHealth = 1 - (SteveDriver.snake.GetHungerTimer() / SteveDriver.snake.GetStarveTime());
		
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
		
		drawText(SteveDriver.snake.getMoney() + "", leftEndPosition.x, leftEndPosition.y, Color.BLACK);
		
		SteveDriver.batch.end();
	}
	
	public void drawText(String message, float x, float y, Color cualColorTienes) {
		font.setColor(cualColorTienes);
		font.draw(SteveDriver.batch, message, x, y);
	}
	
	public void drawBox(float x, float y, int length, int width) {
		for (int row = 0; row < length; row++) {
			for (int col = 0; col < width; col++) {
				Sprite temp;
				
				if (row == 0) {
					if (col == 0) {
						temp = topLeftBox;
					}
					else if (col == width - 1) {
						temp = topRightBox;
					}
					else {
						temp = topBox;
					}
				}
				else if (row == length - 1) {
					if (col == 0) {
						temp = bottomLeftBox;
					}
					else if (col == width - 1) {
						temp = bottomRightBox;
					}
					else {
						temp = bottomBox;
					}
				}
				else {
					if (col == 0) {
						temp = leftBox;
					}
					else if (col == width - 1) {
						temp = rightBox;
					}
					else {
						temp = centerBox;
					}
				}
				
				temp.setPosition(x + col * SteveDriver.TEXTURE_WIDTH, y - row * SteveDriver.TEXTURE_LENGTH);
				temp.draw(SteveDriver.batch);
			}
		}
	}
	
	public boolean isTouchInRectangle(float x, float y, float rectX, float rectY, float length, float width) {
		return x > rectX && x < rectX + width && y > rectY && y < rectY + length;
	}
}
