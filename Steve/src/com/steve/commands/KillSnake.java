package com.steve.commands;

import com.steve.ICommand;
import com.steve.SteveDriver;

public class KillSnake implements ICommand {

	@Override
	public void execute() {
		SteveDriver.snake.kill();
	}

	@Override
	public void keepExecute() {
		// TODO Auto-generated method stub

	}

}
