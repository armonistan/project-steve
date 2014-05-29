package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Button {
	
	float positionX;
	float positionY;
	int width;
	int height;
	int pxWidth;
	int pxHeight;
	ICommand buttonAction;
	
	boolean clicked;
	
	public Button(float posX, float posY, int width, int height, ICommand action) {
		positionX = posX;
		positionY = posY;
		this.width = width;
		this.height = height;
		pxWidth = width * SteveDriver.TEXTURE_WIDTH;
		pxHeight = height * SteveDriver.TEXTURE_LENGTH;
		buttonAction = action;
		
		clicked = false;
	}
	
	public void render() {
		SteveDriver.guiHelper.drawBox(positionX, positionY - SteveDriver.TEXTURE_LENGTH, width, height);
	}
	
	public void update() {
		if (Gdx.input.isTouched() || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			if (!clicked && SteveDriver.guiHelper.isTouchInRectangle(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, -1 * (Gdx.input.getY() - Gdx.graphics.getHeight() / 2 - pxHeight), positionX, positionY, pxWidth, pxHeight)) {
				buttonAction.execute();
				clicked = true;
			}
		}
		else {
			clicked = false;
		}
	}
}
