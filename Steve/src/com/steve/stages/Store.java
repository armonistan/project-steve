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
import com.steve.commands.QueueUpgrade;
import com.steve.commands.ChangeStage;
import com.steve.commands.ConfirmUpgrade;
import com.steve.commands.StartNewRound;
import com.steve.commands.SwitchStoreTab;

public class Store {
	
	public Preferences prefs;
	
	private int tabIndex;
	private String description;
	private TextButton infoBox;
	private TextButton returnToGame;
	private TextButton buyUpgrade;
	
	private boolean isUpgradeSelected;
	private Upgrade selectedUpgrade;
	
	private TextButton snakeTier;
	private TextButton effTier;
	private TextButton lengthTier;
	private TextButton weaponsTier;
	private TextButton cashTier;
	private TextButton specialTier;
	
	private int panelX, panelY, panelWidth, panelHeight;
	
	private ArrayList<Upgrade> upgrades;
	private Map<Integer, ArrayList<Button>> upgradeButtons;
	
	public class Upgrade {
		
		String name;
		String constantName;
		String key;
		String description;
		float value;
		float price;
		boolean activated;
		
		public Upgrade(String displayName, String constantName, String prefsKey, String upgradeDescription, float value, float price, int tier, int xPos, int yPos) {
			activated = false;
			key = prefsKey;
			name = displayName;
			this.constantName = constantName;
			this.price = price;
			description = upgradeDescription;
			
			Button b = new Button(SteveDriver.guiHelper.screenToCoordinateSpaceX(xPos), 
						SteveDriver.guiHelper.screenToCoordinateSpaceY(yPos, 4 * 16),
						4, 4, new QueueUpgrade(this));
			
			upgradeButtons.get(tier).add(b);

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
		upgradeButtons = new HashMap<Integer, ArrayList<Button>>();
		for (int i = 0; i < 6; i++) {
			upgradeButtons.put(i, new ArrayList<Button>());
		}
		
		description = "";
		isUpgradeSelected = false;
		
		tabIndex = 0;
		int screenWidth = (int) SteveDriver.constants.get("screenWidth");
		int screenHeight = (int) SteveDriver.constants.get("screenHeight");
		
		panelX = screenWidth / 4;
		panelY = 0;
		panelWidth = 3 * screenWidth / 4;
		panelHeight = 3 * screenWidth / 4;
		
		infoBox = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(screenWidth/4),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(3 * screenHeight / 4, screenHeight / 64),
				3 * screenWidth / 64, screenHeight / 64, null, description);
		
		returnToGame = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(3 * screenHeight / 4, screenHeight / 64),
				screenWidth / (4 * 16 * 2), screenHeight / 64, new ChangeStage(SteveDriver.STAGE_TYPE.RESPAWNING), "Play!");
		
		buyUpgrade = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(screenWidth / 8),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(3 * screenHeight / 4, screenHeight / 64),
				screenWidth / (4 * 16 * 2), screenHeight / 64, new ConfirmUpgrade(this), "Buy!");
		
		snakeTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(0 * (3 * screenHeight / (4 * 6)), screenHeight / 64),
				screenWidth / (4 * 16), 3 * screenHeight / (4 * 16 * 6), new SwitchStoreTab(this, 0), "Snake");
		
		effTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(1 * (3 * screenHeight / (4 * 6)), screenHeight / 64),
				screenWidth / (4 * 16), 3 * screenHeight / (4 * 16 * 6), new SwitchStoreTab(this, 1), "Efficency");
		
		lengthTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(2 * (3 * screenHeight / (4 * 6)), screenHeight / 64),
				screenWidth / (4 * 16), 3 * screenHeight / (4 * 16 * 6), new SwitchStoreTab(this, 2), "Length");
		
		weaponsTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(3 * (3 * screenHeight / (4 * 6)), screenHeight / 64),
				screenWidth / (4 * 16), 3 * screenHeight / (4 * 16 * 6), new SwitchStoreTab(this, 3), "Weapons");
		
		cashTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(4 * (3 * screenHeight / (4 * 6)), screenHeight / 64),
				screenWidth / (4 * 16), 3 * screenHeight / (4 * 16 * 6), new SwitchStoreTab(this, 4), "Cash");
		
		specialTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(5 * (3 * screenHeight / (4 * 6)), screenHeight / 64),
				screenWidth / (4 * 16), 3 * screenHeight / (4 * 16 * 6), new SwitchStoreTab(this, 5), "Special");
		
		initializeUpgrades();
	}
	
	public void render() {
		renderButtons();
		
		SteveDriver.guiHelper.drawText(SteveDriver.snake.getMoney() + "", panelX + panelWidth/2, panelY + 25, Color.BLACK);
		
		switch (tabIndex) {
			case 0:
				if (!isUpgradeSelected) {
					description = "Ascend to a higher tier.";
				}
				SteveDriver.guiHelper.drawText("Snake Upgrades", 0, 0, Color.BLACK);
				break;
			case 1:
				if (!isUpgradeSelected) {
					description = "All things hunger related";
				}
				SteveDriver.guiHelper.drawText("Efficiency Upgrades", 0, 0, Color.BLACK);
				break;
			case 2:
				if (!isUpgradeSelected) {
					description = "Make the snake longer";
				}
				SteveDriver.guiHelper.drawText("Length Upgrades", 0, 0, Color.BLACK);
				break;
			case 3:
				if (!isUpgradeSelected) {
					description = "Buff up the guns you use.";
				}
				SteveDriver.guiHelper.drawText("Weapon Upgrades", 0, 0, Color.BLACK);
				break;
			case 4:
				if (!isUpgradeSelected) {
					description = "Mo' money mo' prolems";
				}
				SteveDriver.guiHelper.drawText("Cash Upgrades", 0, 0, Color.BLACK);
				break;
			case 5:
				if (!isUpgradeSelected) {
					description = "These are secrets. SECRET.";
				}
				SteveDriver.guiHelper.drawText("Special Upgrades", 0, 0, Color.BLACK);
				break;
			default:
				break;
		}
	}
	
	public void renderButtons() {
		infoBox.setText(description);
		infoBox.update();
		infoBox.render();
		buyUpgrade.update();
		buyUpgrade.render();
		returnToGame.update();
		returnToGame.render();
		snakeTier.update();
		snakeTier.render();
		effTier.update();
		effTier.render();
		lengthTier.update();
		lengthTier.render();
		weaponsTier.update();
		weaponsTier.render();
		cashTier.update();
		cashTier.render();
		specialTier.update();
		specialTier.render();
		
		
		for (Button b : this.upgradeButtons.get(tabIndex)) {
			b.update();
			b.render();
		}
	}
	
	public void queueUpgradePurchase(Upgrade u) {
		this.isUpgradeSelected = true;
		selectedUpgrade = u;
		description = u.getDescription() + " $" + u.getPrice();
	}
	
	public void purchaseUpgrade() {
		if (selectedUpgrade != null) {
			
		}
	}
	
	public void resetStore() {
		prefs.clear();
	}
	
	public void setStoreProgress() {
		
	}
	
	public void setStoreTab(int i) {
		this.tabIndex = i;
		this.isUpgradeSelected = false;
		selectedUpgrade = null;
	}
	
	public void saveStoreProgress() {
		prefs.flush();
	}
	
	private void initializeUpgrades() {
		upgrades.add(new Upgrade("Cyborg Steve", 
				"cyborg",
				"snakeTier1",
				"Turn Steve into a Cyborg",
				1.0f, 
				10000f, 
				0, 
				panelX + (panelWidth / 2), 
				panelY + (3 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Robo Steve", 
				"robot",
				"snakeTier2",
				"Turn Steve into a Robot",
				1.0f, 
				100000f, 
				0, 
				panelX + (panelWidth / 2), 
				panelY + (panelHeight / 4)));
		
		upgrades.add(new Upgrade("Increase HP", 
				"hitpoints",
				"effTier1A",
				"Give Steve 25% more hitpoints",
				1.25f, 
				2500f, 
				1, 
				panelX + (panelWidth / 3), 
				panelY + (3 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Decrease Hunger", 
				"hungerRate",
				"effTier1B",
				"Hunger eats your HP 10% slower",
				.90f, 
				2500f,
				1,
				panelX + (2 * panelWidth / 3), 
				panelY + (3 * panelHeight / 4)));
	}
}
