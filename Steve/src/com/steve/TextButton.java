package com.steve;

import com.badlogic.gdx.graphics.Color;

public class TextButton extends Button {
	private String text;
	
	public TextButton(float posX, float posY, int width, int height,
			ICommand action, String t) {
		super(posX, posY, width, height, action);
		text = t;
	}

	@Override
	public void render() {
		super.render();
		SteveDriver.guiHelper.drawText(text, positionX + 15, positionY - 20, Color.BLACK);
	}
	
	public void setText(String t) {
		text = t;
	}
}
