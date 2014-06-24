package com.steve.commands;

import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class GoToCredits extends ChangeStage {

	public GoToCredits() {
		super(STAGE_TYPE.GAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		stage = STAGE_TYPE.CREDITS;

		super.execute();
	}
}
