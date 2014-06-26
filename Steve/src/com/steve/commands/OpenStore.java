package com.steve.commands;

import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;

public class OpenStore extends ChangeStage implements ICommand {

	public OpenStore() {
		super(STAGE_TYPE.STORE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		SteveDriver.constants.initConstants();
		SteveDriver.store.initializeUpgrades();
		
		super.execute();
	}
}