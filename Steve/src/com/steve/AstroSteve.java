package com.steve;

import com.badlogic.gdx.Gdx;

public class AstroSteve extends Snake {
	public AstroSteve(float x, float y) {
		super(x,y);
		xOffSet = 27;
		yOffSet = 10;
		endGame = true;
		drill = false;
		candy = false;
		glue = false;
		matrix = false;
		jet = false;
		nuke = false;
		timeBetweenTurn = 0.05f;
		super.weapons.clear();
		SteveDriver.prefs.putBoolean("astroSteve", true);
		SteveDriver.prefs.putInteger("themeIndex", 0);
		SteveDriver.switchTheme();
	}

	@Override
	public void update()
	{
		super.update();
	}
}
