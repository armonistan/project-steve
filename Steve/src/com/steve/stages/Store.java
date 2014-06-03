package com.steve.stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	public enum UpgradeType{}
	
	public Preferences prefs;
	
	private int tabIndex;
	private TextButton infoBox;
	private TextButton returnToGame;
	
	private ArrayList<Upgrade> upgrades;
	private Map<String, Button> upgradeButtons;
	
	private class Upgrade {
		
		String name;
		String constantName;
		String key;
		String description;
		float value;
		float price;
		boolean activated;
		
		public Upgrade(String displayName, String constantName, String upgradeKey, String upgradeDescription, float value, float price) {
			
			activated = false;
			key = upgradeKey;
			name = displayName;
			this.constantName = constantName;
			this.price = price;
			description = upgradeDescription;
			if (prefs.contains(key)) {
				if (prefs.getBoolean(key)) {
					activated = true;
					activateUpgrade();
				}
			} else {
				prefs.putBoolean(key, activated);
			}
		}
		
		public String getName() {
			return name;
		}
		
		public String getDescription() {
			return description;
		}
		
		public float getPrice() {
			return price;
		}
		
		public boolean isActivated() {
			return activated;
		}
		
		public void activateUpgrade() {
			activated = true;			
			SteveDriver.constants.modifyConstant(constantName, value);
			prefs.putBoolean(key, activated);
		}
	}
	
	public Store() {
		prefs = Gdx.app.getPreferences("store");
		upgrades = new ArrayList<Upgrade>();
		upgradeButtons = new HashMap<String, Button>();
		
		tabIndex = 0;
		int screenWidth = (int) SteveDriver.constants.get("screenWidth");
		int screenHeight = (int) SteveDriver.constants.get("screenHeight");
		System.out.println(SteveDriver.guiHelper.screenToCoordinateSpaceX(3/4 * screenWidth));
		System.out.println(screenWidth);
		//infoBox = new TextButton(1/4 * screenWidth, 3/4 * screenHeight, 3/4 * screenWidth, 1/4 * screenHeight, null, "Item Descriptions here");
		returnToGame = new TextButton(0, 
				0, 
				14, 
				4, 
				new ChangeStage(SteveDriver.STAGE_TYPE.RESPAWNING),
				"Return to game!");
		
		initializeUpgrades();
	}
	
	public void render() {
		//infoBox.render();
		returnToGame.update();
		returnToGame.render();
	}
	
	public void queueUpgradePurchase(UpgradeType upgrade, int upgradeTier, int price) {
		
	}
	
	public void purchaseUpgrade() {
		
	}
	
	public void resetStore() {
		
	}
	
	public void setStoreProgress() {
		
	}
	
	public void saveStoreProgress() {
		
	}
	
	private void initializeUpgrades() {
		
	}
}
