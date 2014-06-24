package com.steve.commands;

import com.badlogic.gdx.Gdx;
import com.steve.SteveDriver;
import com.steve.stages.Store;

public class StartNewGame extends StartNewRound {

	@Override
	public void execute() {
		SteveDriver.store.resetStore();
		//System.out.println(SteveDriver.storePrefs.getBoolean("goldTier1B"));
		SteveDriver.prefs.putInteger("money", 0);
		SteveDriver.prefs.putBoolean("edgeTutorial", false);
		SteveDriver.prefs.flush();
		SteveDriver.snake.spendMoney(SteveDriver.snake.getMoney());
		SteveDriver.snake.spendTreasure(SteveDriver.snake.getTreasure());
		SteveDriver.snake.addTreasure(5);
		SteveDriver.constants.initConstants();
		SteveDriver.store = new Store();
		SteveDriver.tutorialOn = true;
		super.execute();
	}

}
