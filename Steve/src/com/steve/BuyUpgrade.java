package com.steve;

import com.steve.Store.UpgradeType;

public class BuyUpgrade implements ICommand {
	
	private Store store;
	private UpgradeType upgrade;
	private int upgradeTier;
	
	public BuyUpgrade() {
		
	}
	
	public BuyUpgrade(Store s, UpgradeType upgradeType, int upgradeTier) {
		store = s;
		upgrade = upgradeType;
		this.upgradeTier = upgradeTier;
	}
	
	public void execute() {
		store.queueUpgradePurchase(upgrade, upgradeTier);
	}
}
