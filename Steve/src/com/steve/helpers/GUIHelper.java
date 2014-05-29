package com.steve.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.steve.SteveDriver;

public class GUIHelper {
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
	
	public GUIHelper() {
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		
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
	
	public void drawText(String message, float x, float y, Color cualColorTienes) {
		font.setColor(cualColorTienes);
		int lineNumber = 0;
		
		for (int i = message.length(); i > 0; ) {
			int tempStartIndex = message.lastIndexOf("\n", i - 1);
			tempStartIndex = (tempStartIndex < 0) ? 0 : tempStartIndex;
			
			font.draw(SteveDriver.batch, message.substring(tempStartIndex, i), x, y + lineNumber * font.getLineHeight());
			
			i -= (i - tempStartIndex);
			lineNumber++;
		}
		
		//font.draw(SteveDriver.batch, message, x, y);
	}
	
	public void drawBox(float x, float y, int width, int height) {
		for (int row = 0; row < height; row++) {
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
				else if (row == height - 1) {
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

	public boolean isTouchInRectangle(float x, float y, float rectX, float rectY, float width, float height) {
		return x > rectX && x < rectX + width && y > rectY && y < rectY + height;
	}
}
