package com.steve.commands;

import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class WrapUpLoading extends ChangeStage implements ICommand {

	public WrapUpLoading(STAGE_TYPE type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		//TODO: TEMP
		if (SteveDriver.snake.getMoney() == 0 && !SteveDriver.tutorial.isActive()) {
			SteveDriver.tutorial.startTutorial();
		}
		
		SteveDriver.handler.showAds(false);
		
		super.execute();
	}
}
