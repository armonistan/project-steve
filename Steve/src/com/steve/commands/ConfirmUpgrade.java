package com.steve.commands;

import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.stages.Store;

public class ConfirmUpgrade implements ICommand {
	
	Store store;
	
	public ConfirmUpgrade(Store s) {
		store = s;
	}
	
	@Override
	public void execute() {
		store.purchaseUpgrade();
		
		SteveDriver.store.snake.refreshSnakeLoadout(SteveDriver.store.snake.getHeadPosition().x / SteveDriver.TEXTURE_SIZE,
				SteveDriver.store.snake.getHeadPosition().y / SteveDriver.TEXTURE_SIZE);
	}

	@Override
	public void keepExecute() {
		// TODO Auto-generated method stub
		
	}
}
