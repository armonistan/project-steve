package com.steve.stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.steve.Button;
import com.steve.SteveDriver;
import com.steve.TextButton;
import com.steve.commands.QueueUpgrade;
import com.steve.commands.ChangeStage;
import com.steve.commands.ConfirmUpgrade;
import com.steve.commands.SwitchStoreTab;

public class Store {	
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
	private int[] currentTier;
	
	public class Upgrade {
		
		String name;
		String constantName;
		String key;
		String description;
		float value;
		float price;
		int category;
		int tier;
		boolean activated;
		boolean available;
		
		public Upgrade(String displayName, String constantName, String prefsKey, String upgradeDescription, float value, float price, int tier, int category, int xPos, int yPos) {
			activated = false;
			available = true;
			key = prefsKey;
			name = displayName;
			this.constantName = constantName;
			this.price = price;
			this.category = category;
			this.tier = tier;
			this.value = value;
			description = upgradeDescription;
			
			Button b = new Button(SteveDriver.guiHelper.screenToCoordinateSpaceX(xPos), 
						SteveDriver.guiHelper.screenToCoordinateSpaceY(yPos, 4 * 16),
						4, 4, new QueueUpgrade(this));
			
			upgradeButtons.get(category).add(b);

			if (SteveDriver.storePrefs.contains(key)) {
				if (SteveDriver.storePrefs.getBoolean(key)) {
					activated = true;
					currentTier[category] = tier;
					activateUpgrade();
				}
			} else {
				if (currentTier[category] > tier) {
					setUnavailable();
				}
				SteveDriver.storePrefs.putBoolean(key, activated);
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
			currentTier[category] = tier + 1;
			//System.out.println("activating upgrade: " + name + " " + value + " " + constantName);
			SteveDriver.constants.modifyConstant(constantName, value);
			SteveDriver.storePrefs.putBoolean(key, activated);
		}
		
		public void activateTierThreeMainCannonUpgrade() {
			activated = true;
			currentTier[category] = tier + 1;
			//System.out.println("activating upgrade: " + name + " " + value + " " + constantName);
			SteveDriver.constants.modifyConstant(constantName, value);
			SteveDriver.storePrefs.putBoolean(key, activated);
		}
		
		public void setUnavailable() {
			available = false;
		}
		
		public void setPrice(float price) {
			this.price = price;
		}
	}
	
	public Store() {
		upgrades = new ArrayList<Upgrade>();
		upgradeButtons = new HashMap<Integer, ArrayList<Button>>();
		currentTier = new int[6];
		
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
		panelHeight = 3 * screenHeight / 4;
		
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
		setStoreProgress();
	}
	
	public void render() {
		if (selectedUpgrade != null) {
			setDescription();
		}
		renderButtons();
		
		SteveDriver.guiHelper.drawTextCentered("$" + SteveDriver.snake.getMoney(), 
				SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + panelWidth/2),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + panelHeight - 16, 15),
				Color.BLACK);
		
		switch (tabIndex) {
			case 0:
				if (!isUpgradeSelected) {
					description = "Ascend to a higher existence.";
				}
				SteveDriver.guiHelper.drawTextCentered("Snake Upgrades", 
						SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + panelWidth/2),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + panelHeight/10, 15),
						Color.BLACK);
				break;
			case 1:
				if (!isUpgradeSelected) {
					description = "When the field gets tough, Steve gets \ntougher.";
				}
				SteveDriver.guiHelper.drawTextCentered("Durability Upgrades", 
						SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + panelWidth/2),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + panelHeight/10, 15),
						Color.BLACK);
				break;
			case 2:
				if (!isUpgradeSelected) {
					description = "Feeling inadequate? Small? You've\ncome to the right place.";
				}
				SteveDriver.guiHelper.drawTextCentered("Length Upgrades", 
						SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + panelWidth/2),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + panelHeight/10, 15),
						Color.BLACK);
				break;
			case 3:
				if (!isUpgradeSelected) {
					description = "Go from boom to BOOM.";
				}
				SteveDriver.guiHelper.drawTextCentered("Weapon Upgrades", 
						SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + panelWidth/2),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + panelHeight/10, 15),
						Color.BLACK);
				break;
			case 4:
				if (!isUpgradeSelected) {
					description = "Mo' money mo' prolems";
				}
				SteveDriver.guiHelper.drawTextCentered("Cash Upgrades", 
						SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + panelWidth/2),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + panelHeight/10, 15),
						Color.BLACK);
				break;
			case 5:
				if (!isUpgradeSelected) {
					description = "These are secrets. SECRET.";
				}
				SteveDriver.guiHelper.drawTextCentered("Special Upgrades", 
						SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + panelWidth/2),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + panelHeight/10, 15),
						Color.BLACK);
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
		setDescription();
	}
	
	public void setDescription() {
		description = selectedUpgrade.getDescription();
		if (selectedUpgrade.activated) {
			description += "\nPurchased!";
		} else if (!selectedUpgrade.available){
			description += "\nLocked!";
		} else {
			description += "\n$" + selectedUpgrade.getPrice() * SteveDriver.constants.get("priceModifier");
		}
	}
	
	public void purchaseUpgrade() {
		if (selectedUpgrade != null) {
			if (selectedUpgrade.available && !selectedUpgrade.activated && currentTier[selectedUpgrade.category] == selectedUpgrade.tier) {
				if (SteveDriver.snake.spendMoney((int) (selectedUpgrade.price * SteveDriver.constants.get("priceModifier")))) {
					selectedUpgrade.activateUpgrade();
					SteveDriver.snake.addMoney(0);
					for (Upgrade u : this.upgrades) {
						if (u.category == selectedUpgrade.category && u.tier == selectedUpgrade.tier) {
							u.setUnavailable();
						}
					}
					SteveDriver.prefs.flush();
					SteveDriver.storePrefs.flush();
				} else {
					description = "Not enough cash!";
					selectedUpgrade = null;
					return;
				}
			} else if (selectedUpgrade.activated) {
				description = "Upgrade activated!";
				selectedUpgrade = null;
				return;
			} else if (!selectedUpgrade.available) {
				description = "Upgrade not available!";
				selectedUpgrade = null;
				return;
			} else if (currentTier[selectedUpgrade.category] != selectedUpgrade.tier) {
				description = "Too high tier!";
				selectedUpgrade = null;
				return;
			}
		}
	}
	
	public void resetStore() {
		SteveDriver.storePrefs.clear();
		SteveDriver.storePrefs.flush();
	}
	
	public void setStoreProgress() {
		for (int i = 0; i < 6; i++) {
			for (Upgrade u : this.upgrades) {
				if (u.category == i && u.tier < currentTier[i] && !u.activated) {
					u.setUnavailable();
				}
			}
		}
	}
	
	public void setStoreTab(int i) {
		this.tabIndex = i;
		this.isUpgradeSelected = false;
		selectedUpgrade = null;
	}
	
	public void saveStoreProgress() {
		SteveDriver.storePrefs.flush();
	}
	
	private void initializeUpgrades() {
		Upgrade steve = new Upgrade("Regular Steve", 
				"steve",
				"snakeTier0",
				"Steve the cute, normal snake.",
				1.0f, 
				0f, 
				0, 0, 
				panelX - 32 + (panelWidth / 2), 
				panelY + 32 + (3 * panelHeight / 4));
		steve.activateUpgrade();
		upgrades.add(steve);
		
		upgrades.add(new Upgrade("Cyborg Steve", 
				"cyborg",
				"snakeTier1",
				"Turn Steve into a Cyborg",
				1.0f, 
				10000f, 
				1, 0, 
				panelX - 32 + (panelWidth / 2), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Robo Steve", 
				"robot",
				"snakeTier2",
				"Turn Steve into a Robot",
				1.0f, 
				100000f, 
				2, 0, 
				panelX - 32 + (panelWidth / 2), 
				panelY + 32 + (panelHeight / 4)));
		
		upgrades.add(new Upgrade("Increase HP", 
				"hitpoints",
				"effTier1A",
				"Give Steve 25% more hitpoints",
				.25f,
				2500f,
				0, 1,
				panelX - 32 + (panelWidth / 3), 
				panelY + 32 + (3 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Decrease Hunger", 
				"hungerRate",
				"effTier1B",
				"Hunger eats your HP 10% slower",
				.1f, 
				2500f,
				0, 1,
				panelX - 32 + (2 * panelWidth / 3), 
				panelY + 32 + (3 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Increase HP", 
				"hitpoints",
				"effTier2A",
				"Give Steve 25% more hitpoints",
				.25f,
				25000f,
				1, 1,
				panelX - 32 + (panelWidth / 3), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Decrease Hunger", 
				"hungerRate",
				"effTier2B",
				"Hunger eats your HP 10% slower",
				.1f, 
				25000f,
				1, 1,
				panelX - 32 + (2 * panelWidth / 3), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Increase HP", 
				"hitpoints",
				"effTier3A",
				"Give Steve 25% more hitpoints",
				.25f,
				250000f,
				2, 1,
				panelX - 32 + (panelWidth / 3), 
				panelY + 32 + (1 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Decrease Hunger", 
				"hungerRate",
				"effTier3B",
				"Hunger eats your HP 10% slower",
				.1f, 
				250000f,
				2, 1,
				panelX - 32 + (2 * panelWidth / 3), 
				panelY + 32 + (1 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Increase start length", 
				"startLength",
				"survTier1A",
				"Give Steve 2 more segments when the round starts",
				2f,
				2500f,
				0, 2,
				panelX - 32 + (panelWidth / 3), 
				panelY + 32 + (3 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Max Length Increase", 
				"maxLength",
				"survTier1B",
				"Increase the maximum possible length of Steve by 2.",
				2f, 
				2500f,
				0, 2,
				panelX - 32 + (2 * panelWidth / 3), 
				panelY + 32 + (3 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Increase start length", 
				"startLength",
				"survTier2A",
				"Give Steve 2 more segments when the round starts",
				2f,
				25000f,
				1, 2,
				panelX - 32 + (panelWidth / 3), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Max Length Increase", 
				"maxLength",
				"survTier2B",
				"Increase the maximum possible length of Steve by 2.",
				2f, 
				25000f,
				1, 2,
				panelX - 32 + (2 * panelWidth / 3), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Increase start length", 
				"startLength",
				"survTier3A",
				"Give Steve 2 more segments when the round starts",
				2f,
				250000f,
				2, 2,
				panelX - 32 + (panelWidth / 3), 
				panelY + 32 + (1 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Max Length Increase", 
				"maxLength",
				"survTier3B",
				"Increase the maximum possible length of Steve by 2.",
				2f, 
				250000f,
				2, 2,
				panelX - 32 + (2 * panelWidth / 3), 
				panelY + 32 + (1 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Main Gun", 
				"mainGun",
				"wepTier1",
				"Steve gains a main cannon on his first segment.",
				1f,
				2500f,
				0, 3,
				panelX - 32 + (panelWidth / 2), 
				panelY + 32 + (3 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Fire Rate Increase", 
				"fireRate",
				"wepTier2A",
				"Steve gains a 30% fire rate bonus for his main gun,\nand a 15% boost for all other guns.",
				.15f,
				25000f,
				1, 3,
				panelX - 32 + (panelWidth / 2), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		
		upgrades.add(new Upgrade("Fire Rate Increase", 
				"mainCannonType",
				"wepTier3A",
				"Triple firepower!",
				1f,
				250000f,
				2, 3,
				panelX - 32 + (panelWidth / 2), 
				panelY + 32 + (1 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Turret Range Increase", 
				"fireRange",
				"wepTier2B",
				"Steve gains a 20% range bonus for his main gun,\nand a 10% boost for all other guns.",
				.1f,
				25000f,
				1, 3,
				panelX - 32 + (1 * panelWidth / 4), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Turret Range Increase", 
				"mainCannonType",
				"wepTier3B",
				"Turn the main cannon into a \npowerful Gauss Cannon.",
				2f,
				250000f,
				2, 3,
				panelX - 32 + (1 * panelWidth / 4), 
				panelY + 32 + (1 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Damage Increase", 
				"fireDamage",
				"wepTier2C",
				"Steve gains a 40% damage bonus for his main gun,\nand a 20% boost for all other guns.",
				.2f,
				25000f,
				1, 3,
				panelX - 32 + (3 * panelWidth / 4), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		
		//firedamage .2f
		upgrades.add(new Upgrade("Damage Increase", 
				"mainCannonType",
				"wepTier3C",
				"All missles fire!",
				3f,
				250000f,
				2, 3,
				panelX - 32 + (3 * panelWidth / 4), 
				panelY + 32 + (1 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Plentiful Money", 
				"goldModifier",
				"goldTier1A",
				"Sources of gold are worth 15% more.",
				.15f,
				2500f,
				0, 4,
				panelX - 32 + (1 * panelWidth / 3), 
				panelY + 32 + (3 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Plentiful Money", 
				"goldModifier",
				"goldTier2A",
				"Sources of gold are worth 15% more.",
				.15f,
				25000f,
				1, 4,
				panelX - 32 + (1 * panelWidth / 3), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Plentiful Money", 
				"goldModifier",
				"goldTier3A",
				"Sources of gold are worth 15% more.",
				.15f,
				250000f,
				2, 4,
				panelX - 32 + (1 * panelWidth / 3), 
				panelY + 32 + (1 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Cheapskate", 
				"priceModifier",
				"goldTier1B",
				"Upgrades are 10% cheaper.",
				-.1f,
				2500f,
				0, 4,
				panelX - 32 + (2 * panelWidth / 3), 
				panelY + 32 + (3 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Cheapskate", 
				"priceModifier",
				"goldTier2B",
				"Upgrades are 10% cheaper.",
				-.1f,
				25000f,
				1, 4,
				panelX - 32 + (2 * panelWidth / 3), 
				panelY + 32 + (2 * panelHeight / 4)));
		
		upgrades.add(new Upgrade("Cheapskate", 
				"priceModifier",
				"goldTier3B",
				"Upgrades are 10% cheaper.",
				-.1f,
				250000f,
				2, 4,
				panelX - 32 + (2 * panelWidth / 3), 
				panelY + 32 + (1 * panelHeight / 4)));
	}
}
