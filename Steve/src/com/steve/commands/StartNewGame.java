package com.steve.commands;

import com.badlogic.gdx.Gdx;
import com.steve.SteveDriver;
import com.steve.stages.Store;

public class StartNewGame extends StartNewRound {

	@Override
	public void execute() {
		SteveDriver.storePrefs.clear();
		SteveDriver.storePrefs.flush();
		System.out.println(SteveDriver.storePrefs.getBoolean("goldTier1B"));
		Gdx.app.getPreferences("main").putInteger("money", 0);
		Gdx.app.getPreferences("main").flush();
		SteveDriver.snake.spendMoney(SteveDriver.snake.getMoney());
		SteveDriver.constants.initConstants();
		SteveDriver.store = new Store();
		super.execute();
	}

}
