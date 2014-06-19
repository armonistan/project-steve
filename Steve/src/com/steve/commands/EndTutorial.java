package com.steve.commands;

import com.steve.ICommand;
import com.steve.SteveDriver;

public class EndTutorial implements ICommand {
	@Override
	public void execute() {
		SteveDriver.tutorial.endTutorial();
	}

	@Override
	public void keepExecute() {
		// TODO Auto-generated method stub
		
	}
}
