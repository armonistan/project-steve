package com.steve.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.steve.SteveDriver;

public class Store {
	
	public enum UpgradeType {
		SNAKE, TTL, HELMET, LENGTH, WEAPONS, ARMOR, EFFICIENCY;
	}
	
	int[] upgradeTiers;
	int[] upgradePrices;
	
	float cyberPriceMod;
	float mechaPriceMod;
	
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
