package com.steve.commands;

import com.badlogic.gdx.Gdx;
import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.stages.Store;

public class StartNewGame extends StartNewRound {

	@Override
	public void execute() {
		Gdx.app.getPreferences("main").putInteger("money", 0);
		Gdx.app.getPreferences("main").flush();
		SteveDriver.snake.spendMoney(SteveDriver.snake.getMoney());
		SteveDriver.store.resetStore();
		SteveDriver.store = new Store();
		super.execute();
	}

}
