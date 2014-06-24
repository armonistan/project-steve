package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteButton extends Button {
	private Sprite sprite;
	
	public SpriteButton(float posX, float posY, int width, int height,
			ICommand action, Sprite s) {
		super(posX, posY, width, height, action);
		sprite = s;
		s.setPosition(posX+24, posY-40);
	}

	@Override
	public void render() {
		
		super.render();
		sprite.draw(SteveDriver.batch);
	}
}
