package com.steve.commands;

import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class StartNewRound extends ChangeStage {

	public StartNewRound() {
		super(STAGE_TYPE.GAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		SteveDriver.resetField();
		
		super.execute();
	}
}
