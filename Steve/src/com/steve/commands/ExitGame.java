package com.steve.commands;

import com.badlogic.gdx.Gdx;
import com.steve.ICommand;

public class ExitGame implements ICommand {

	@Override
	public void execute() {
		Gdx.app.exit();
	}

	@Override
	public void keepExecute() {
		// TODO Auto-generated method stub
		
	}

}
