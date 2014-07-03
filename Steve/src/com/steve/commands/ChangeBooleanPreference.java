package com.steve.commands;


import com.steve.Button;
import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.TextButton;

public class ChangeBooleanPreference implements ICommand{
	String variable;
	Button myPrecious;
	
	public ChangeBooleanPreference(String var) {
		variable = var;
	}

	@Override
	public void execute() {
		SteveDriver.prefs.putBoolean(variable, !SteveDriver.prefs.getBoolean(variable, false));
		if(variable == "music")
			SteveDriver.switchTheme();
		SteveDriver.prefs.flush();
	}
	
	public void setButton(Button mine) {
		myPrecious = mine;
	}

	@Override
	public void keepExecute() {		
		if (myPrecious != null) {
			myPrecious.setStatus((SteveDriver.prefs.getBoolean(variable)) ? 1 : 0);
		}
	}
}
