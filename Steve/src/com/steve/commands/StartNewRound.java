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
			SteveDriver.store.initializeUpgrades();
			
			stage = STAGE_TYPE.STORE;
			
			SteveDriver.camera.position.x = SteveDriver.store.snake.getHeadPosition().x;
			SteveDriver.camera.position.y = SteveDriver.store.snake.getHeadPosition().y;
		}
		
		super.execute();
	}
}
