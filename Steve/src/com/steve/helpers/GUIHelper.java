package com.steve.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.steve.SteveDriver;

public class GUIHelper {
	
	private BitmapFont font48;
	private BitmapFont font32;
	public BitmapFont activeFont;
	private BoxSet blackBox;
	private BoxSet goldBox;
	private BoxSet redBox;
	
	public enum BoxColors {
		BLACK, GOLD, RED;
	}
	
	private class BoxSet {
		public Sprite topLeftBox;
		public Sprite topBox;
		public Sprite topRightBox;
		public Sprite leftBox;
		public Sprite centerBox;
		public Sprite rightBox;
		public Sprite bottomLeftBox;
		public Sprite bottomBox;
		public Sprite bottomRightBox;
		
		public BoxSet(int x, int y) {
			topLeftBox = new Sprite(new TextureRegion(SteveDriver.atlas, x * SteveDriver.TEXTURE_SIZE, y * SteveDriver.TEXTURE_SIZE,
					SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
			topBox = new Sprite(new TextureRegion(SteveDriver.atlas, (x + 1) * SteveDriver.TEXTURE_SIZE, y * SteveDriver.TEXTURE_SIZE,
					SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
			topRightBox = new Sprite(new TextureRegion(SteveDriver.atlas, (x + 2) * SteveDriver.TEXTURE_SIZE, y * SteveDriver.TEXTURE_SIZE,
					SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
			leftBox = new Sprite(new TextureRegion(SteveDriver.atlas, x * SteveDriver.TEXTURE_SIZE, (y + 1) * SteveDriver.TEXTURE_SIZE,
					SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
			centerBox = new Sprite(new TextureRegion(SteveDriver.atlas, (x + 1) * SteveDriver.TEXTURE_SIZE, (y + 1) * SteveDriver.TEXTURE_SIZE,
					SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
			rightBox = new Sprite(new TextureRegion(SteveDriver.atlas, (x + 2) * SteveDriver.TEXTURE_SIZE, (y + 1) * SteveDriver.TEXTURE_SIZE,
					SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
			bottomLeftBox = new Sprite(new TextureRegion(SteveDriver.atlas, x * SteveDriver.TEXTURE_SIZE, (y + 2) * SteveDriver.TEXTURE_SIZE,
					SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
			bottomBox = new Sprite(new TextureRegion(SteveDriver.atlas, (x + 1) * SteveDriver.TEXTURE_SIZE, (y + 2) * SteveDriver.TEXTURE_SIZE,
					SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
			bottomRightBox = new Sprite(new TextureRegion(SteveDriver.atlas, (x + 2) * SteveDriver.TEXTURE_SIZE, (y + 2) * SteveDriver.TEXTURE_SIZE,
					SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
		}
	}
	
	public GUIHelper() {
		font48 = new BitmapFont(Gdx.files.internal("fonts/fixedsys48.fnt"));
		font32 = new BitmapFont(Gdx.files.internal("fonts/fixedsys32.fnt"));
		blackBox = new BoxSet(18, 17);
		goldBox = new BoxSet(18, 20);
		redBox = new BoxSet(18, 23);
		
		setActiveFont48();
	}
	
	private BoxSet getBox(BoxColors whichBox) {
		switch (whichBox) {
			case BLACK:
				return this.blackBox;
			case GOLD:
				return this.goldBox;
			case RED:
				return this.redBox;
		}
		return this.blackBox;
	}
	
	public void setActiveFont48() {
		this.activeFont = font48;
	}
	
	public void setActiveFont32() {
		this.activeFont = font32;
	}
	
	public void drawText(String message, float x, float y, Color cualColorTienes) {
		activeFont.setColor(cualColorTienes);
		int lineNumber = 0;
		
		for (int i = 0; i < message.length() - 1; ) {
			int tempEndIndex = message.indexOf("\n", i);
			tempEndIndex = (tempEndIndex < 0) ? message.length() : tempEndIndex;
			activeFont.draw(SteveDriver.batch, message.substring(i, tempEndIndex), x, y - lineNumber * activeFont.getLineHeight());
			
			i = tempEndIndex + 1;
			lineNumber++;
		}
	}
	
	public void drawTextCentered(String message, float x, float y, Color c) {
		activeFont.setColor(c);
		int lineNumber = 0;
		
		for (int i = 0; i < message.length() - 1; ) {
			int tempEndIndex = message.indexOf("\n", i);
			tempEndIndex = (tempEndIndex < 0) ? message.length() : tempEndIndex;
			activeFont.draw(SteveDriver.batch, message.substring(i, tempEndIndex), x - (activeFont.getBounds(message.substring(i, tempEndIndex)).width/2), 
					(y - lineNumber * activeFont.getLineHeight()));
			
			i = tempEndIndex + 1;
			lineNumber++;
		}
	}
	
	public void drawBox(float x, float y, int width, int height, BoxColors whichBox) {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				Sprite temp;
				
				if (row == 0) {
					if (col == 0) {
						temp = getBox(whichBox).topLeftBox;
					}
					else if (col == width - 1) {
						temp = getBox(whichBox).topRightBox;
					}
					else {
						temp = getBox(whichBox).topBox;
					}
				}
				else if (row == height - 1) {
					if (col == 0) {
						temp = getBox(whichBox).bottomLeftBox;
					}
					else if (col == width - 1) {
						temp = getBox(whichBox).bottomRightBox;
					}
					else {
						temp = getBox(whichBox).bottomBox;
					}
				}
				else {
					if (col == 0) {
						temp = getBox(whichBox).leftBox;
					}
					else if (col == width - 1) {
						temp = getBox(whichBox).rightBox;
					}
					else {
						temp = getBox(whichBox).centerBox;
					}
				}
				
				temp.setPosition(x + col * SteveDriver.TEXTURE_SIZE, y - row * SteveDriver.TEXTURE_SIZE);
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
		return temp;
	}
	
	public int screenToCoordinateSpaceY(int inputY) {
		int temp = -1 * (int)((inputY - Gdx.graphics.getHeight() / 2) * SteveDriver.guiCamera.viewportHeight / Gdx.graphics.getHeight());
		return temp;
	}
	
	public boolean isOnScreen(float x, float y, float originX, float originY) {
		return (x + originX * 2 >= SteveDriver.camera.position.x - SteveDriver.camera.viewportWidth / 2f &&
				x - originX * 2 < SteveDriver.camera.position.x + SteveDriver.camera.viewportWidth / 2f) &&
				(y + originY >= SteveDriver.camera.position.y - SteveDriver.camera.viewportHeight / 2f &&
				y - originY < SteveDriver.camera.position.y + SteveDriver.camera.viewportHeight / 2f);
	}
}
