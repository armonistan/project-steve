package com.steve.commands;

import com.badlogic.gdx.Gdx;
import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.StoreSnake;

public class ResetStoreChanges implements ICommand {

	@Override
	public void execute() {
		SteveDriver.store.resetChoices();
		SteveDriver.store.snake = new StoreSnake();
	}

	@Override
	public void keepExecute() {
		// TODO Auto-generated method stub

	}

}
