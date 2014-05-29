package com.steve.commands;

import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class ChangeStage implements ICommand {
	STAGE_TYPE stage;
	
	public ChangeStage(STAGE_TYPE s) {
		stage = s;
	}
	
	@Override
	public void execute() {
		SteveDriver.stage = stage;
	}
}
