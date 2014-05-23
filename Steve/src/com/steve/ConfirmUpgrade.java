package com.steve;

import com.steve.stages.Store;

public class ConfirmUpgrade implements ICommand {
	
	Store store;
	
	public ConfirmUpgrade(Store s) {
		store = s;
	}
	
	public void execute() {
		store.purchaseUpgrade();
	}
}
