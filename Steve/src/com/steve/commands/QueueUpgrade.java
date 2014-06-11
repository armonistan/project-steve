package com.steve.commands;

import com.steve.ICommand;
import com.steve.SteveDriver;
import com.steve.stages.Store.Upgrade;

public class QueueUpgrade implements ICommand {
	
	private Upgrade upgrade;
	
	public QueueUpgrade() {
		
	}
	
	public QueueUpgrade(Upgrade u) {
		upgrade = u;
	}
	
	@Override
	public void execute() {
		SteveDriver.store.queueUpgradePurchase(upgrade);
	}
}
