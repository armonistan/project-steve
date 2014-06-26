package com.steve.commands;

import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class StartNewRound extends ChangeStage {

	public StartNewRound() {
		super(STAGE_TYPE.LOADING);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		if (SteveDriver.snake.getMoney() == 0) {
			SteveDriver.resetField();
		}
		else {
			SteveDriver.constants.initConstants();
			SteveDriver.store.initializeUpgrades();
			
			stage = STAGE_TYPE.STORE;
		}
		
		super.execute();
	}
}
