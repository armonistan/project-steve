package com.steve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.steve.helpers.GUIHelper;

public class Button {
	
	public float positionX;
	public float positionY;
	int width;
	int height;
	int pxWidth;
	int pxHeight;
	ICommand buttonAction;
	
	static boolean clicked;
	int status;
	
	public Button(float posX, float posY, int width, int height, ICommand action) {
		positionX = posX;
		positionY = posY;
		this.width = width;
		this.height = height;
		pxWidth = width * SteveDriver.TEXTURE_SIZE;
		pxHeight = height * SteveDriver.TEXTURE_SIZE;
		buttonAction = action;
		
		clicked = false;
		status = 0;
	}
	
	public void render() {
		switch(status) {
			case 1:
				SteveDriver.guiHelper.drawBox(positionX, positionY - SteveDriver.TEXTURE_SIZE, width, height, GUIHelper.BoxColors.GOLD);
				break;
			case 2:
				SteveDriver.guiHelper.drawBox(positionX, positionY - SteveDriver.TEXTURE_SIZE, width, height, GUIHelper.BoxColors.RED);
				break;
			default:
				SteveDriver.guiHelper.drawBox(positionX, positionY - SteveDriver.TEXTURE_SIZE, width, height, GUIHelper.BoxColors.BLACK);
				break;
		}
	}
	
	public void update() {
		if (Gdx.input.isTouched() || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			if (!clicked && SteveDriver.guiHelper.isTouchInRectangle(
					SteveDriver.guiHelper.screenToCoordinateSpaceX(Gdx.input.getX()), 
					SteveDriver.guiHelper.screenToCoordinateSpaceY(Gdx.input.getY()) + pxHeight, 
					positionX, positionY, pxWidth, pxHeight)) {
				if (buttonAction != null) {
					buttonAction.execute();
					clicked = true;
				}
			}
		}
		else {
			clicked = false;
		}
		
		if (buttonAction != null) {
			buttonAction.keepExecute();
		}
	}
	
	public void setStatus(int newStatus) {
		status = newStatus;
	}
	
	public ICommand getCommand() {
		return buttonAction;
	}
	
	public void setCommand(ICommand com) {
		buttonAction = com;
	}
}
