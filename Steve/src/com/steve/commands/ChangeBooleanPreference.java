package com.steve.commands;

import com.steve.Button;
import com.steve.ICommand;
import com.steve.SteveDriver;

public class ChangeBooleanPreference implements ICommand{
	String variable;
	Button myPrecious;
	
	public ChangeBooleanPreference(String var) {
		variable = var;
	}

	@Override
	public void execute() {
		SteveDriver.prefs.putBoolean(variable, !SteveDriver.prefs.getBoolean(variable, false));
		SteveDriver.prefs.flush();
		
		if (myPrecious != null) {
			myPrecious.setStatus((SteveDriver.prefs.getBoolean(variable)) ? 1 : 0);
		}
	}
	
	public void setButton(Button mine) {
		myPrecious = mine;
	}
}