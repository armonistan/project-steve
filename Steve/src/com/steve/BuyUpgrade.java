package com.steve;

import com.steve.stages.Store;
import com.steve.stages.Store.UpgradeType;

public class BuyUpgrade implements ICommand {
	
	private Store store;
	private UpgradeType upgrade;
	private int upgradeTier;
	private int price;
	
	public BuyUpgrade() {
		
	}
	
	public BuyUpgrade(Store s, UpgradeType upgradeType, int upgradeTier, int price) {
		store = s;
		upgrade = upgradeType;
		this.upgradeTier = upgradeTier;
		this.price = price;
	}
	
	public void execute() {
		store.queueUpgradePurchase(upgrade, upgradeTier, price);
	}
}
