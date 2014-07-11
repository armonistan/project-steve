package com.steve.commands;

import com.steve.SteveDriver;
import com.steve.StoreSnake;
import com.steve.SteveDriver.STAGE_TYPE;

public class FromStoreToMenu extends ChangeStage {

	public FromStoreToMenu() {
		super(STAGE_TYPE.MENU);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		super.execute();
		
		SteveDriver.store.resetChoices();
		SteveDriver.store.snake = new StoreSnake();
	}
}
