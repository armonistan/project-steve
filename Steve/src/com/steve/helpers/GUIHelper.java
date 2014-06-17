package com.steve.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
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
		
		for (int i = 0; i < message.length() - 1; ) {
			int tempEndIndex = message.indexOf("\n", i);
			tempEndIndex = (tempEndIndex < 0) ? message.length() : tempEndIndex;
			font.draw(SteveDriver.batch, message.substring(i, tempEndIndex), x, y - lineNumber * font.getLineHeight());
			
			i = tempEndIndex + 1;
			lineNumber++;
		}
	}
	
	public void drawTextCentered(String message, float x, float y, Color c) {
		font.setColor(c);
		int lineNumber = 0;
		
		for (int i = 0; i < message.length() - 1; ) {
			int tempEndIndex = message.indexOf("\n", i);
			tempEndIndex = (tempEndIndex < 0) ? message.length() : tempEndIndex;
			font.draw(SteveDriver.batch, message.substring(i, tempEndIndex), x - (font.getBounds(message.substring(i, tempEndIndex)).width/2), y - lineNumber * font.getLineHeight());
			
			i = tempEndIndex + 1;
			lineNumber++;
		}
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
	
	public int screenToCoordinateSpaceX(int inputX) {
		int temp = (int)((inputX - Gdx.graphics.getWidth() / 2) *
			SteveDriver.guiCamera.viewportWidth / Gdx.graphics.getWidth());
		
		System.out.println("X: " + inputX + " " + temp);
		
		return temp;
	}
	
	public int screenToCoordinateSpaceY(int inputY) {
		int temp = -1 * (int)((inputY - Gdx.graphics.getHeight() / 2) * SteveDriver.guiCamera.viewportHeight / Gdx.graphics.getHeight());
		
		System.out.println("Y: " + inputY + " " + temp);
		
		return temp;
	}
	
	public int coordinateToScreenSpaceX(int inputX) {
		return (2 * inputX) + (int)SteveDriver.guiCamera.viewportWidth;
	}
	
	public int coordinateToScreenSpaceY(int inputY, int height) {
		return 2 * ((inputY * -1) + height) + (int)SteveDriver.guiCamera.viewportHeight;
	}
	
	public boolean isOnScreen(float x, float y, float originX, float originY) {
		return (x + originX * 2 >= SteveDriver.camera.position.x - SteveDriver.camera.viewportWidth / 2f &&
				x - originX * 2 < SteveDriver.camera.position.x + SteveDriver.camera.viewportWidth / 2f) &&
				(y + originY >= SteveDriver.camera.position.y - SteveDriver.camera.viewportHeight / 2f &&
				y - originY < SteveDriver.camera.position.y + SteveDriver.camera.viewportHeight / 2f);
	}
}
