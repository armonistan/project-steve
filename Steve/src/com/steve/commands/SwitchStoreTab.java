package com.steve.commands;

import com.steve.ICommand;
import com.steve.stages.Store;

public class SwitchStoreTab implements ICommand {
	
	private Store store;
	private int index;
	
	public SwitchStoreTab() {
		
	}
	
	public SwitchStoreTab(Store s, int i) {
		store = s;
		index = i;
	}
	
	@Override
	public void execute() {
		store.setStoreTab(index);
	}

	@Override
	public void keepExecute() {
		// TODO Auto-generated method stub
		
	}
}