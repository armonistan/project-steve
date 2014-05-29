package com.steve.commands;

import com.steve.ICommand;
import com.steve.SteveDriver;

public class ChangeTutorialStage implements ICommand {
	@Override
	public void execute() {
		SteveDriver.tutorial.goToNextStage();
	}
}
