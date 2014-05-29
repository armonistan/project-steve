package com.steve.commands;

import com.badlogic.gdx.Gdx;
import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class StartNewGame extends StartNewRound {

	@Override
	public void execute() {
		Gdx.app.getPreferences("main").putInteger("money", 0);
		Gdx.app.getPreferences("main").flush();
		int test = Gdx.app.getPreferences("main").getInteger("money");
		SteveDriver.snake.spendMoney(SteveDriver.snake.getMoney());
		SteveDriver.store.resetStore();
		super.execute();
	}

}
