package com.steve;

import com.badlogic.gdx.Gdx;

public class AstroSteve extends Snake {
	private float timeInSpace;
	
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
		SteveDriver.setSpaceTheme();
		
		timeInSpace = 5;
	}

	@Override
	public void update() {
		if (timeInSpace > 0) {
			if (inSpace) {
				timeInSpace -= Gdx.graphics.getRawDeltaTime();
			}
		}

		super.update();
	}
	
	public float getTimeInSpace() {
		return timeInSpace;
	}
}
