package com.steve.stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.steve.SpriteButton;
import com.steve.SteveDriver;
import com.steve.StoreSnake;
import com.steve.TextButton;
import com.steve.commands.ChangeStage;
import com.steve.commands.ConfirmUpgrade;
import com.steve.commands.FromStoreToMenu;
import com.steve.commands.QueueUpgrade;
import com.steve.commands.ResetStoreChanges;
import com.steve.commands.SwitchStoreTab;

public class Store {
	private int tabIndex;
	private String description;
	private TextButton infoBox;
	private TextButton returnToGame;
	private TextButton buyUpgrade;
	private TextButton resetChoices;
	private TextButton returnToMenu;

	private StoreField field;
	public StoreSnake snake;

	private boolean isUpgradeSelected;
	private Upgrade selectedUpgrade;

	private TextButton[] buttons;
	private TextButton snakeTier;
	private TextButton effTier;
	private TextButton lengthTier;
	private TextButton weaponsTier;
	private TextButton cashTier;
	private TextButton specialTier;

	private int panelX, panelY, panelWidth, panelHeight;

	private ArrayList<Upgrade> upgrades;
	private Map<Integer, ArrayList<SpriteButton>> upgradeButtons;
	private int[] currentTier;

	private int[] initCurrentTier;
	private int initMoney;
	private int initTreasure;
	
	private Sound purchaseSound;

	public Store() {
		description = "";
		isUpgradeSelected = false;

		tabIndex = 0;
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		panelX = Gdx.graphics.getWidth() / 4;
		panelY = -32;
		panelWidth = (3 * Gdx.graphics.getWidth()) / 4;
		panelHeight = (3 * Gdx.graphics.getHeight()) / 4;

		infoBox = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(screenWidth/4),
				SteveDriver.guiHelper.screenToCoordinateSpaceY((3 * screenHeight) / 4) - (SteveDriver.TEXTURE_SIZE / 2) + (screenHeight / 64),
				(2 * (int)SteveDriver.guiCamera.viewportWidth) / SteveDriver.TEXTURE_SIZE / 4, (int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4, null, description);

		returnToGame = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX((3 * screenWidth) / 4),
				SteveDriver.guiHelper.screenToCoordinateSpaceY((6 * screenHeight) / 8)  - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4 / 2, new ChangeStage(SteveDriver.STAGE_TYPE.RESPAWNING), "Play!");

		returnToMenu = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX((3 * screenWidth) / 4),
				SteveDriver.guiHelper.screenToCoordinateSpaceY((7 * screenHeight) / 8)  - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4 / 2, new FromStoreToMenu(), "Menu");
		
		buyUpgrade = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX((0 * screenWidth) / 4),
				SteveDriver.guiHelper.screenToCoordinateSpaceY((6 * screenHeight) / 8) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4 / 2, new ConfirmUpgrade(this), "Buy!");

		resetChoices = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX((0 * screenWidth) / 4),
				SteveDriver.guiHelper.screenToCoordinateSpaceY((7 * screenHeight) / 8) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4 / 2, new ResetStoreChanges(), "Reset");

		buttons = new TextButton[6];
		snakeTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(0 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 0), "Snake");

		effTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(1 * ((3 * screenHeight) / (4 * 6)))  - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 1), "Durability");

		lengthTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(2 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 2), "Length");

		weaponsTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(3 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 3), "Weapons");

/*		cashTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(4 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 4), "Cash");
*/
		specialTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(4 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 5), "Special");

		buttons[0] = snakeTier;
		buttons[1] = effTier;
		buttons[2] = lengthTier;
		buttons[3] = weaponsTier;
		//buttons[4] = cashTier;
		buttons[5] = specialTier;
		
		purchaseSound = SteveDriver.assets.get("audio/storePurchase.ogg", Sound.class);
		
		initializeUpgrades();

		field = new StoreField();
		snake = new StoreSnake();
	}

	public void resetChoices() {
		for (int i = 0; i < upgrades.size(); i++) {
			upgrades.get(i).resetChoice();
		}

		for (int i = 0; i < currentTier.length; i++) {
			currentTier[i] = initCurrentTier[i];
		}

		SteveDriver.snake.addTreasure(initTreasure - SteveDriver.snake.getTreasure());
		SteveDriver.snake.setMoney(initMoney);
	}

	public void render() {
		if (selectedUpgrade != null) {
			//setDescription();
		}

		for (Upgrade u : upgrades) {
			u.update();
		}
		
		//Update the upgrade category buttons
		for (int i = 0; i < buttons.length; i++) {
			if (tabIndex == i) {
				if(buttons[i] != null)
					buttons[i].setStatus(1);
			}
			else{
				if(buttons[i] != null)
					buttons[i].setStatus(0);
			}
		}

		if ((field != SteveDriver.field) || (field == null)) {
			field = new StoreField();
			snake = new StoreSnake();
			SteveDriver.field = field;
		}

		Vector3 test = SteveDriver.camera.position.lerp(snake.getHeadPosition(), 0.1f);
		SteveDriver.camera.position.x = test.x;
		SteveDriver.camera.position.y = test.y;
		SteveDriver.camera.update();

		SteveDriver.batch.setProjectionMatrix(SteveDriver.camera.combined);
		SteveDriver.batch.begin();
		field.update();
		field.drawBelowSnake();
		snake.update();
		snake.draw();
		SteveDriver.batch.end();

		SteveDriver.guiCamera.position.x = 0;
		SteveDriver.guiCamera.position.y = 0;
		SteveDriver.guiCamera.update();

		SteveDriver.batch.setProjectionMatrix(SteveDriver.guiCamera.combined);

		SteveDriver.batch.begin();
		renderButtons();

		if (tabIndex != 5) {
		SteveDriver.guiHelper.drawText("$" + SteveDriver.snake.getMoney(),
				infoBox.positionX,
				infoBox.positionY + (SteveDriver.TEXTURE_SIZE * 2),
				Color.BLACK);
		} else {
			SteveDriver.guiHelper.drawText("Treasure: " + SteveDriver.snake.getTreasure(),
				infoBox.positionX,
				infoBox.positionY + (SteveDriver.TEXTURE_SIZE * 2),
				Color.BLACK);
		}

		if (!isUpgradeSelected) {
			switch (tabIndex) {
				case 0:
					description = "Ascend to a higher existence. Your \n destiny awaits.";
					SteveDriver.guiHelper.drawTextCentered("Snake Upgrades",
							SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10)) - 15,
							Color.BLACK);

				break;
				case 1:
					description = "When the field gets tough, Steve \ngets tougher.";
					SteveDriver.guiHelper.drawTextCentered("Durability Upgrades",
							SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10)) - 15,
							Color.BLACK);
				break;
				case 2:
					description = "Feeling inadequate? Small? You've\ncome to the right place.";
					SteveDriver.guiHelper.drawTextCentered("Length Upgrades",
							SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10)) - 15,
							Color.BLACK);
				break;
				case 3:
					description = "Go from boom to BOOM.";
					SteveDriver.guiHelper.drawTextCentered("Weapon Upgrades",
							SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10)) - 15,
							Color.BLACK);
				break;/*
				case 4:
					description = "Mo' money mo' prolems";
					SteveDriver.guiHelper.drawTextCentered("Cash Upgrades",
							SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10)) - 15,
							Color.BLACK);
				break;
				*/
				case 5:
					description = "These are secrets. SECRET.\nYou retain these across new games.";
					SteveDriver.guiHelper.drawTextCentered("Special Upgrades",
							SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10))  - 15,
							Color.BLACK);
				break;
				default:
				break;
			}
		}
		else if (selectedUpgrade != null) {
			SteveDriver.guiHelper.drawTextCentered(selectedUpgrade.getName(),
					SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
					SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10)) - 15,
					Color.BLACK);
		}
		SteveDriver.batch.end();
	}

	public void renderButtons() {
		infoBox.setText(description);
		SteveDriver.guiHelper.setActiveFont32();
		infoBox.update();
		infoBox.render();
		SteveDriver.guiHelper.setActiveFont48();
		buyUpgrade.update();
		buyUpgrade.render();
		returnToGame.update();
		returnToGame.render();
		resetChoices.update();
		resetChoices.render();
		snakeTier.update();
		snakeTier.render();
		effTier.update();
		effTier.render();
		lengthTier.update();
		lengthTier.render();
		weaponsTier.update();
		weaponsTier.render();
		/*cashTier.update();
		cashTier.render();
		*/
		specialTier.update();
		specialTier.render();
		returnToMenu.update();
		returnToMenu.render();

		for (SpriteButton b : this.upgradeButtons.get(tabIndex)) {
			b.update();
			b.render();
		}
	}

	public void queueUpgradePurchase(Upgrade u) {
		this.isUpgradeSelected = true;
		selectedUpgrade = u;
		setDescription();
	}

	public void flipSpecial() {
		if (selectedUpgrade.activated) {
			selectedUpgrade.deactivateUpgrade();
			description += "\nInactive!";
		} else {
			selectedUpgrade.activateUpgrade();
			description += "\nActive!";
		}

		SteveDriver.store.snake.refreshSnakeLoadout(SteveDriver.store.snake.getHeadPosition().x / SteveDriver.TEXTURE_SIZE,
				SteveDriver.store.snake.getHeadPosition().y / SteveDriver.TEXTURE_SIZE);
	}

	public void setDescription() {
		description = selectedUpgrade.getDescription();

		if ((selectedUpgrade.category == 5) && selectedUpgrade.available) {
			flipSpecial();
			return;
		}

		if (selectedUpgrade.activated) {
			description += "\nPurchased!";
		} /*else if (!selectedUpgrade.available){
			description += "\nLocked!";
		}*/ else {
			description += selectedUpgrade.getPriceString();
		}
	}

	public void purchaseUpgrade() {
		if (selectedUpgrade != null) {
			int tryBuy = selectedUpgrade.trySpendCurrency();

			if (tryBuy == 0) {
				if(SteveDriver.prefs.getBoolean("sfx", true))
					purchaseSound.play();
				selectedUpgrade.activateUpgrade();
				description = "Upgrade activated!";
			}
			else if (tryBuy == 1) {
				description = "Not enough money!";
			}
			else if (tryBuy == 2) {
				description = "Steve must accend to a\nhigher tier first!";
			}
			else if (tryBuy == 3) {
				description = "You already own this.";
			}
			else if (tryBuy == 4) {
				description = "You may only choose one\nupgrade per a tier! ";
			}
			else if (tryBuy == 5) {
				description = "Not enough treasure!";
			}
			else if (tryBuy == 6) {
				description = "Buy the full game to be able to unlock!";
			}

			selectedUpgrade = null;
		}
	}

	public void resetStore() {		
		for (Upgrade u : upgrades) {
			u.reset();
		}

		saveStoreProgress();

		initializeUpgrades();
	}

	public void setStoreTab(int i) {
		this.tabIndex = i;
		this.isUpgradeSelected = false;
		selectedUpgrade = null;
	}

	public void saveStoreProgress() {
		SteveDriver.storePrefs.flush();
		SteveDriver.prefs.flush();
	}

	public void initializeUpgrades() {
		upgrades = new ArrayList<Upgrade>();
		upgradeButtons = new HashMap<Integer, ArrayList<SpriteButton>>();
		currentTier = new int[6];

		for (int i = 0; i < 6; i++) {
			upgradeButtons.put(i, new ArrayList<SpriteButton>());
		}

		initMoney = SteveDriver.snake.getMoney();
		initTreasure = SteveDriver.snake.getTreasure();
		
		setStoreTab(0);
		
		SteveDriver.constants.initConstants();

		Upgrade steve = new Upgrade("Regular Steve",
				"steve",
				"snakeTier0",
				"Steve the cute, normal snake.\nMakes tier 1 upgrades available.",
				1.0f,
				0f,
				0, 0,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 37 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));
		steve.activateUpgrade();
		upgrades.add(steve);

		upgrades.add(new Upgrade("Cyborg Steve",
				"cyborg",
				"snakeTier1",
				"Stronger, faster, better, and still adorable.\nMakes tier 2 upgrades available.",
				1.0f,
				8000f,
				1, 0,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 39 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Robo Steve",
				"robot",
				"snakeTier2",
				"His body may be solid steel,\nbut his heart is solid gold.\nMakes tier 3 upgrades available.",
				1.0f,
				100000f,
				2, 0,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + (panelHeight / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 41 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Increase HP I",
				"hitpoints",
				"effTier1A",
				"Give Steve 25% more hitpoints",
				.25f,
				1000f,
				0, 1,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 31 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Magic Apples I",
				"hungerRate",
				"effTier1B",
				"Apples heal you for an additional 33% of your hp",
				33f,
				1000f,
				0, 1,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 31 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Increase HP II",
				"hitpoints",
				"effTier2A",
				"Give Steve 25% more hitpoints",
				.25f,
				10000f,
				1, 1,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 33 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Magic Apple II",
				"hungerRate",
				"effTier2B",
				"Apples heal you for an additional 33% of your hp",
				33f,
				10000f,
				1, 1,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 33 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Increase HP III",
				"hitpoints",
				"effTier3A",
				"Give Steve 25% more hitpoints",
				.25f,
				50000f,
				2, 1,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 35 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Magic Apple III",
				"hungerRate",
				"effTier3B",
				"Apples heal you for an additional 33% of your hp",
				33f,
				50000f,
				2, 1,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 35 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Start Length Increase I",
				"startLength",
				"survTier1A",
				"Give Steve 1 more segment\nwhen the round starts",
				1f,
				1500f,
				0, 2,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 45 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Max Length Increase I",
				"maxLength",
				"survTier1B",
				"Increase the maximum possible\nlength of Steve by 2.",
				2f,
				1500f,
				0, 2,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 45 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Start Length Increase II",
				"startLength",
				"survTier2A",
				"Give Steve 1 more segment\nwhen the round starts",
				1f,
				25000f,
				1, 2,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 47 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Max Length Increase II",
				"maxLength",
				"survTier2B",
				"Increase the maximum possible\nlength of Steve by 2.",
				2f,
				25000f,
				1, 2,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 47 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Start Length Increase III",
				"startLength",
				"survTier3A",
				"Give Steve 1 more segment\nwhen the round starts",
				1f,
				75000f,
				2, 2,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 49 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Max Length Increase III",
				"maxLength",
				"survTier3B",
				"Increase the maximum possible\nlength of Steve by 2.",
				2f,
				75000f,
				2, 2,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 49 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("The Slug Slayer",
				"mainGun",
				"wepTier1",
				"Mount the Slug Slayer on Steve.\n The splats of slug will be music to your ears.",
				1f,
				3000f,
				0, 3,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 57 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Tommy the Gun",
				"fireRate",
				"wepTier2A",
				"Replace the Slug Slayer with Tommy the Gun.\nTommy likes bullets. Expect to see a lot of them",
				.75f,
				75000f,
				1, 3,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 59 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("The Sentinel",
				"fireRange",
				"wepTier2B",
				"Replace the Slug Slayer with the Sentinel.\n From a distance it watches.\nFrom a distance it kills.",
				.8f,
				75000f,
				1, 3,
				(panelX - 32) + ((1 * panelWidth) / 4),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 59 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));
		
		upgrades.add(new Upgrade("The Overkill",
				"fireDamage",
				"wepTier2C",
				"Replace the Slug Slayer with the Overkill.\nExpect to hear a lot more splats.",
				.6f,
				75000f,
				1, 3,
				(panelX - 32) + ((3 * panelWidth) / 4),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 57 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Tommy the Gun III",
				"mainCannonType",
				"wepTier3A",
				"Tommy the Gun's grandchild.\nThree times the fun Tommy was.",
				1f,
				150000f,
				2, 3,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 61 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("The Beholder",
				"mainCannonType",
				"wepTier3B",
				"No one can hide from the Beholder.\nNo distance is far enough.\nNo cover is strong enough.",
				2f,
				150000f,
				2, 3,
				(panelX - 32) + ((1 * panelWidth) / 4),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 61 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		//firedamage .2f
		upgrades.add(new Upgrade("The Barrage",
				"mainCannonType",
				"wepTier3C",
				"With such firepower comes responsiblity...\nAnd a lot of destruction!",
				3f,
				150000f,
				2, 3,
				(panelX - 32) + ((3 * panelWidth) / 4),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 57 * SteveDriver.TEXTURE_SIZE,
						21 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));
		/*
		upgrades.add(new Upgrade("Plentiful Money I",
				"goldModifier",
				"goldTier1A",
				"Sources of gold are worth 15% more.",
				.15f,
				10000f,
				0, 4,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 51 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));
		
		upgrades.add(new Upgrade("Cheapskate I",
				"priceModifier",
				"goldTier1B",
				"Upgrades are 10% cheaper.",
				-.1f,
				10000f,
				0, 4,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 51 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Plentiful Money II",
				"goldModifier",
				"goldTier2A",
				"Sources of gold are worth 15% more.",
				.15f,
				100000f,
				1, 4,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 53 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));
		
		upgrades.add(new Upgrade("Cheapskate II",
				"priceModifier",
				"goldTier2B",
				"Upgrades are 10% cheaper.",
				-.1f,
				100000f,
				1, 4,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 53 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Plentiful Money III",
				"goldModifier",
				"goldTier3A",
				"Sources of gold are worth 15% more.",
				.15f,
				500000f,
				2, 4,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 55 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Cheapskate III",
				"priceModifier",
				"goldTier3B",
				"Upgrades are 10% cheaper.",
				-.1f,
				500000f,
				2, 4,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 55 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));
		 */
		upgrades.add(new ToggleUpgrade("Glue Trail",
				"glueTrail",
				"special1",
				"Attach a glue stick to Steve's tail\nResults guaranteed to stick to you.",
				1f,
				8f,
				(panelX - 32) + ((1 * panelWidth) / 5),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 41 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new ToggleUpgrade("Candy Zone",
				"candyZone",
				"special6",
				"Welcome to the Candy Zone",
				1f,
				20f,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 37 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new ToggleUpgrade("Nuke",
				"nuke",
				"special2",
				"What's better than a gun wielding snake?\nA gun wielding snake with a nuke, duh.",
				1f,
				15f,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 37 * SteveDriver.TEXTURE_SIZE,
						21 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new ToggleUpgrade("Hard Hat",
				"drill",
				"special3",
				"Rocks? Cacti? Lava? No problem.\nSteve will plow through it all.",
				1f,
				5f,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 39 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new ToggleUpgrade("Jet Fuel Only",
				"jetFuel",
				"special4",
				"Steve gains the ability to go fast.",
				1f,
				2f,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 43 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new ToggleUpgrade("Bullet Time",
				"bulletTime",
				"special5",
				"There is no snake.\nTime slows around you, defying physics.",
				1f,
				10f,
				(panelX - 32) + ((4 * panelWidth) / 5),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 39 * SteveDriver.TEXTURE_SIZE,
						21 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		initCurrentTier = new int[currentTier.length];
		for (int i = 0; i < currentTier.length; i++) {
			initCurrentTier[i] = currentTier[i];
		}
	}

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
		boolean initActivated;
		boolean available;
		float initConstantValue;

		SpriteButton b;

		public Upgrade(String displayName, String constantName, String prefsKey, String upgradeDescription, float value, float price, int tier, int category, int xPos, int yPos, Sprite buttonIcon) {
			available = true;

			initMetaData(displayName, constantName, prefsKey,
					upgradeDescription, value, price, tier, category, xPos,
					yPos, buttonIcon);

			//Initial value when opening store.
			activated = SteveDriver.storePrefs.getBoolean(key, false);
			if (activated) {
				activateUpgrade();
			}

			initInits();
		}
		
		public Upgrade() {
			//Empty and sad.
		}

		protected void initInits() {
			initActivated = activated;
			initConstantValue = SteveDriver.constants.get(constantName);
		}

		protected void initMetaData(String displayName, String constantName,
				String prefsKey, String upgradeDescription, float value,
				float price, int tier, int category, int xPos, int yPos,
				Sprite buttonIcon) {
			key = prefsKey;
			name = displayName;
			this.constantName = constantName;

			this.price = price;
			this.category = category;
			this.tier = tier;
			this.value = value;

			description = upgradeDescription;

			b = new SpriteButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(xPos),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(yPos),
						4, 4, new QueueUpgrade(this), buttonIcon);

			upgradeButtons.get(category).add(b);
		}

		public void update() {
			available = ((SteveDriver.snake.getMoney() >= (int)(price * SteveDriver.constants.get("priceModifier")) && (currentTier[category] == tier) && (tier < currentTier[0] || category == 0))) || activated;
			
			b.setStatus(available ? (activated ? 1 : 0) : 2);
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
		
		public String getPriceString() {
			return "\n$" + String.format("%10.2f", getPrice() * SteveDriver.constants.get("priceModifier"));
		}

		public boolean isActivated() {
			return activated;
		}

		public void activateUpgrade() {
			if (available) {
				activated = true;
				currentTier[category] = tier + 1;
				
				if (constantName == "cyborg") {
					SteveDriver.prefs.putBoolean("cyborgBossActivate", (!SteveDriver.prefs.getBoolean("razorbullDefeated", true)));
					SteveDriver.prefs.putBoolean("razorbullDefeated", (SteveDriver.prefs.getBoolean("razorbullDefeated", false)));
				}
				else if(constantName == "robot"){
					SteveDriver.prefs.putBoolean("robotBossActivate", (!SteveDriver.prefs.getBoolean("carrierDefeated", true)));
					SteveDriver.prefs.putBoolean("carrierDefeated", (SteveDriver.prefs.getBoolean("carrierDefeated", false)));
				}

				SteveDriver.constants.modifyConstant(constantName, value);
				SteveDriver.storePrefs.putBoolean(key, activated);
			}
		}

		public void deactivateUpgrade() {
			activated = false;
			currentTier[category] = tier;

			SteveDriver.constants.modifyConstant(constantName, -1 * value);
			SteveDriver.storePrefs.putBoolean(key, activated);
		}

		public void reset() {
			activated = false;
			available = true;
			SteveDriver.storePrefs.putBoolean(key, activated);
		}

		public void setPrice(float price) {
			this.price = (int) price;
		}

		public void resetChoice() {
			SteveDriver.storePrefs.putBoolean(key, initActivated);

			SteveDriver.constants.addToConstants(constantName, initConstantValue);
			activated = initActivated;
			
			if (constantName == "cyborg" && !initActivated) {
				SteveDriver.prefs.putBoolean("cyborgBossActivate", false);
				SteveDriver.prefs.putBoolean("carrierDefeated", false);
			}
			else if(constantName == "robot" && !initActivated){
				SteveDriver.prefs.putBoolean("robotBossActivate", false);
				SteveDriver.prefs.putBoolean("razorbullDefeated", false);
			}
		}

		public int trySpendCurrency() {
			if ((currentTier[0] > tier) || (category == 0)) {
				if (!activated) {
					if (SteveDriver.snake.spendMoney((int) (price * SteveDriver.constants.get("priceModifier")))) {
						return 0; //Success
					}
					else {
						return 1; //Insufficient funds
					}
				}
				else {
					return 3; //You already own it
				}
			}
			else {
				return 2; //Insufficient tier
			}
		}
	}

	public class ToggleUpgrade extends Upgrade {
		boolean initAvailable;

		public ToggleUpgrade(String displayName, String constantName,
				String prefsKey, String upgradeDescription, float value,
				float price, int xPos, int yPos,
				Sprite buttonIcon) {
			initMetaData(displayName, constantName, prefsKey, upgradeDescription, value, price, 0, 5, xPos, yPos, buttonIcon);

			available = (!SteveDriver.FREE || this.price <= 5f) ? SteveDriver.storePrefs.getBoolean(key, false) : false;
			activated = (!SteveDriver.FREE || this.price <= 5f) ? SteveDriver.storePrefs.getBoolean(key + "ACT", false) : false;
			initAvailable = available;
			
			if (activated) {
				activateUpgrade();
			}
			
			initInits();
		}

		@Override
		public void update() {
			b.setStatus(available ? (activated ? 1 : 2) : 0);
		}

		@Override
		public void activateUpgrade() {
			if (!available) {
				available = true;
			}

			activated = true;

			SteveDriver.constants.modifyConstant(constantName, value);
			SteveDriver.storePrefs.putBoolean(key, available);

			SteveDriver.storePrefs.putBoolean(key + "ACT", activated);
		}

		@Override
		public void deactivateUpgrade() {
			activated = false;
			SteveDriver.storePrefs.putBoolean(key + "ACT", activated);
			SteveDriver.constants.modifyConstant(constantName, -1 * value);
		}

		@Override
		public void resetChoice() {
			available = initAvailable;

			SteveDriver.storePrefs.putBoolean(key + "ACT", initActivated);
			SteveDriver.storePrefs.putBoolean(key, initAvailable);

			SteveDriver.constants.addToConstants(constantName, initConstantValue);
			activated = initActivated;
		}
		
		@Override
		public void reset() {
			//Do fucking nothing
		}

		@Override
		public int trySpendCurrency() {
			if (!SteveDriver.FREE || this.price <= 5f) {
				if (!available) {
					if (SteveDriver.snake.spendTreasure((int) price)) {
						return 0;
					}
					else {
						return 5; //Not enough treasure.
					}
				}
				else {
					return 3;
				}
			}
			else {
				return 6; //Need to buy full game.
			}
		}
		
		@Override
		public String getPriceString() {
			return "\nTreasure: " + (int)getPrice();
		}
	}
}
