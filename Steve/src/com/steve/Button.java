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
	
	public Button(float posX, float posY, int width, int height, ICommand action) {
		positionX = posX;
		positionY = posY;
		this.width = width;
		this.height = height;
		pxWidth = width * SteveDriver.TEXTURE_WIDTH;
		pxHeight = height * SteveDriver.TEXTURE_LENGTH;
		buttonAction = action;
	}
	
	public void render() {
		SteveDriver.gui.drawBox(positionX, positionY, width, height);
	}
	
	public void update() {
		if (Gdx.input.isTouched() || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			if (SteveDriver.gui.isTouchInRectangle(Gdx.input.getX() - 340, Gdx.input.getY() - 260, positionX, positionY, pxWidth, pxHeight)) {
				buttonAction.execute();
			}
		}
	}
}
