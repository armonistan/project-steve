package com.steve.commands;

import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class FromGameToMenu extends ChangeStage {

	public FromGameToMenu() {
		super(STAGE_TYPE.MENU);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		super.execute();
		SteveDriver.resetTheme();
	}
}
