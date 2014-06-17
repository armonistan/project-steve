package com.steve.commands;

import com.badlogic.gdx.Gdx;
import com.steve.SteveDriver;
import com.steve.stages.Store;

public class StartNewGame extends StartNewRound {

	@Override
	public void execute() {
		SteveDriver.storePrefs.clear();
		SteveDriver.storePrefs.flush();
		//System.out.println(SteveDriver.storePrefs.getBoolean("goldTier1B"));
		SteveDriver.prefs.putInteger("money", 0);
		SteveDriver.prefs.flush();
		SteveDriver.snake.spendMoney(SteveDriver.snake.getMoney());
		SteveDriver.constants.initConstants();
		SteveDriver.store = new Store();
		SteveDriver.tutorialOn = true;
		super.execute();
	}

}
