package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.steve.Button;
import com.steve.SteveDriver;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.TextButton;
import com.steve.commands.BuyUpgrade;
import com.steve.commands.ChangeStage;
import com.steve.commands.ConfirmUpgrade;
import com.steve.commands.StartNewRound;

public class Store {
	
	public enum UpgradeType {
		SNAKE (0), 
		TTL (1), 
		HELMET (2), 
		LENGTH (3), 
		WEAPONS (4), 
		ARMOR (5), 
		EFFICIENCY (6);
		
		private final int index;
		private UpgradeType(int index) {
			this.index = index;
		}
		
		public int index() {
			return this.index;
		}
	}
	
	Button[][] upgradeButtons;
	Button confirmButton;
	TextButton startButton;
	
	int[] upgradeTiers;
	int[] upgradePrices;
	
	float cyberPriceMod;
	float mechaPriceMod;
	
	UpgradeType upgradeToPurchase;
	int upgradeToPurchaseTier;
	int upgradeToPurchasePrice;
	
	String purchaseDescription;
	
	Preferences prefs;
	Preferences mainPrefs;
	
	public Store() {
		/*
		 * The tier system for upgrades CURRENTLY works as follows:
		 *  0 - not upgraded
		 *  1 - upgraded for organic steve
		 *  2 - upgraded for cyber steve
		 *  3 - upgraded for mecha steve
		 *  
		 *  snakeTier is 0 for organic steve, 1 for cyber steve, and 2 for mecha steve
		 */
		prefs = Gdx.app.getPreferences("store");
		mainPrefs = Gdx.app.getPreferences("main");
		
		purchaseDescription = "";
		
		upgradeTiers = new int[7];
		upgradePrices = new int[7];
		
		upgradeTiers[0] = prefs.contains("snakeTier") ? prefs.getInteger("snakeTier") : 0;
		upgradeTiers[1] = prefs.contains("timeToLiveTier") ? prefs.getInteger("timeToLiveTier") : 0;
		upgradeTiers[2] = prefs.contains("helmetTier") ? prefs.getInteger("helmetTier") : 0;
		upgradeTiers[3] = prefs.contains("lengthTier") ? prefs.getInteger("lengthTier") : 0;
		upgradeTiers[4] = prefs.contains("weaponsTier") ? prefs.getInteger("weaponsTier") : 0;
		upgradeTiers[5] = prefs.contains("armorTier") ? prefs.getInteger("armorTier") : 0;
		upgradeTiers[6] = prefs.contains("efficiencyTier") ? prefs.getInteger("efficiencyTier") : 0;
		
		upgradePrices[0] = 2000;
		upgradePrices[1] = 500;
		upgradePrices[2] = 1800;
		upgradePrices[3] = 500;
		upgradePrices[4] = 750;
		upgradePrices[5] = 1000;
		upgradePrices[6] = 1200;
		
		cyberPriceMod = 5.0f;
		mechaPriceMod = 12.5f;
		
		confirmButton = new Button(250, 160, 4, 4, new ConfirmUpgrade(this));
		startButton = new TextButton(250, 100, 10, 4, new StartNewRound(), "Start!");
		
		upgradeButtons = new Button[7][7];
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				int price = upgradePrices[i] * (j+1);
				if (j > 1 && j < 4) {
					price *= cyberPriceMod;
				} else if (j > 3 && j < 6) {
					price *= mechaPriceMod;
				} else if (j == 6) {
					price *= 25;
				}
				upgradeButtons[i][j] = new Button(60 * i - 200, 60 * j - 200, 4, 4, 
						new BuyUpgrade(this, UpgradeType.values()[i], j, price));
			}
		}
	}
	
	public void render() {
		SteveDriver.guiHelper.drawBox(-342, 230, 43, 4);
		SteveDriver.guiHelper.drawText(purchaseDescription, -330, 224, Color.BLACK);
		SteveDriver.guiHelper.drawText("$" + SteveDriver.snake.getMoney(), 220, 224, Color.BLACK);
		confirmButton.update();
		confirmButton.render();
		
		startButton.update();
		startButton.render();
		
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (upgradeTiers[i] < j+1) {
					upgradeButtons[i][j].update();
					upgradeButtons[i][j].render();
				}
			}
		}
	}
	
	public void queueUpgradePurchase(UpgradeType upgrade, int upgradeTier, int price) {
		purchaseDescription = upgrade.toString() + " " + upgradeTier + " " + price;
		upgradeToPurchase = upgrade;
		upgradeToPurchaseTier = upgradeTier;
		upgradeToPurchasePrice = price;
	}
	
	public void purchaseUpgrade() {
		if (upgradeToPurchase != null) {
			if (upgradeToPurchaseTier == this.upgradeTiers[upgradeToPurchase.index] && upgradeToPurchaseTier <= this.upgradeTiers[0]) {
				if (SteveDriver.snake.spendMoney(upgradeToPurchasePrice)) {
					upgradeTiers[upgradeToPurchase.index] = upgradeToPurchaseTier + 1;
					saveStoreProgress();
					upgradeToPurchase = null;
				} else {
					purchaseDescription = "You don't have enough money!";
				}
			} else {
				if (upgradeToPurchaseTier > this.upgradeTiers[upgradeToPurchase.index])
					purchaseDescription = "You are not high enough tier!";
			}
		}
	}
	
	public void resetStore() {
		for (int i = 0; i < upgradeTiers.length; i++) {
			upgradeTiers[i] = 0;
		}
		saveStoreProgress();
	}
	
	public void setStoreProgress() {
		upgradeTiers[0] = prefs.getInteger("snakeTier");
		upgradeTiers[1] = prefs.getInteger("timeToLiveTier");
		upgradeTiers[2] = prefs.getInteger("helmetTier");
		upgradeTiers[3] = prefs.getInteger("lengthTier");
		upgradeTiers[4] = prefs.getInteger("weaponsTier");
		upgradeTiers[5] = prefs.getInteger("armorTier");
		upgradeTiers[6] = prefs.getInteger("efficiencyTier");
	}
	
	public void saveStoreProgress() {
		prefs.putInteger("snakeTier", upgradeTiers[0]);
		prefs.putInteger("timeToLiveTier", upgradeTiers[1]);
		prefs.putInteger("helmetTier", upgradeTiers[2]);
		prefs.putInteger("lengthTier", upgradeTiers[3]);
		prefs.putInteger("weaponsTier", upgradeTiers[4]);
		prefs.putInteger("armorTier", upgradeTiers[5]);
		prefs.putInteger("efficiencyTier", upgradeTiers[6]);
		prefs.flush();
		
		mainPrefs.putInteger("money", SteveDriver.snake.getMoney());
		mainPrefs.flush();
	}
}
