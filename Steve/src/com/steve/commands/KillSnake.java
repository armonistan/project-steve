package com.steve.commands;

import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.stages.Summary.WHY_DIED;

public class KillSnake implements ICommand {

	@Override
	public void execute() {
		SteveDriver.snake.kill(WHY_DIED.player); //Player ended the round.
	}

	@Override
	public void keepExecute() {
		// TODO Auto-generated method stub

	}

}
