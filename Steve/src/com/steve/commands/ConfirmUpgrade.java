package com.steve.commands;

import com.steve.ICommand;
import com.steve.stages.Store;

public class ConfirmUpgrade implements ICommand {
	
	Store store;
	
	public ConfirmUpgrade(Store s) {
		store = s;
	}
	
	@Override
	public void execute() {
		store.purchaseUpgrade();
	}

	@Override
	public void keepExecute() {
		// TODO Auto-generated method stub
		
	}
}
