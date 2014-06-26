package com.steve.stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
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

	private StoreField field;
	public StoreSnake snake;

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
	private Map<Integer, ArrayList<SpriteButton>> upgradeButtons;
	private int[] currentTier;

	private int[] initCurrentTier;
	private int initMoney;
	private int initTreasure;

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
				SteveDriver.guiHelper.screenToCoordinateSpaceY((3 * screenHeight) / 4) - (SteveDriver.TEXTURE_SIZE / 2),
				(2 * (int)SteveDriver.guiCamera.viewportWidth) / SteveDriver.TEXTURE_SIZE / 4, (int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4, null, description);

		returnToGame = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY((6 * screenHeight) / 8)  - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4, new ChangeStage(SteveDriver.STAGE_TYPE.RESPAWNING), "Play!");

		buyUpgrade = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX((6 * screenWidth) / 8),
				SteveDriver.guiHelper.screenToCoordinateSpaceY((6 * screenHeight) / 8) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4 / 2, new ConfirmUpgrade(this), "Buy!");

		resetChoices = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX((6 * screenWidth) / 8),
				SteveDriver.guiHelper.screenToCoordinateSpaceY((7 * screenHeight) / 8) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (int)SteveDriver.guiCamera.viewportHeight / SteveDriver.TEXTURE_SIZE / 4 / 2, new ResetStoreChanges(), "Reset");

		snakeTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(0 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 0), "Snake");

		effTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(1 * ((3 * screenHeight) / (4 * 6)))  - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 1), "Efficency");

		lengthTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(2 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 2), "Length");

		weaponsTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(3 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 3), "Weapons");

		cashTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(4 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 4), "Cash");

		specialTier = new TextButton(SteveDriver.guiHelper.screenToCoordinateSpaceX(0),
				SteveDriver.guiHelper.screenToCoordinateSpaceY(5 * ((3 * screenHeight) / (4 * 6))) - (screenHeight / 64),
				(int)SteveDriver.guiCamera.viewportWidth / (4 * SteveDriver.TEXTURE_SIZE), (3 * (int)SteveDriver.guiCamera.viewportHeight) / (4 * SteveDriver.TEXTURE_SIZE * 6), new SwitchStoreTab(this, 5), "Special");

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
		SteveDriver.snake.addMoney(initMoney - SteveDriver.snake.getMoney());
	}

	public void render() {
		if (selectedUpgrade != null) {
			//setDescription();
		}

		for (Upgrade u : upgrades) {
			u.update();
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
					description = "Ascend to a higher existence.";
					SteveDriver.guiHelper.drawTextCentered("Snake Upgrades",
							SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10)) - 15,
							Color.BLACK);

				break;
				case 1:
					description = "When the field gets tough, Steve gets \ntougher.";
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
				break;
				case 4:
					description = "Mo' money mo' prolems";
					SteveDriver.guiHelper.drawTextCentered("Cash Upgrades",
							SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
						SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10)) - 15,
							Color.BLACK);
				break;
				case 5:
					description = "These are secrets. SECRET.";
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
			SteveDriver.guiHelper.drawTextCentered(selectedUpgrade.name,
					SteveDriver.guiHelper.screenToCoordinateSpaceX(panelX + (panelWidth/2)),
					SteveDriver.guiHelper.screenToCoordinateSpaceY(panelY + (panelHeight/10)),
					Color.BLACK);
		}
		SteveDriver.batch.end();
	}

	public void renderButtons() {
		infoBox.setText(description);
		infoBox.update();
		infoBox.render();
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
		cashTier.update();
		cashTier.render();
		specialTier.update();
		specialTier.render();

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
		} else if (!selectedUpgrade.available){
			description += "\nLocked!";
		} else {
			description += "\n$" + (selectedUpgrade.getPrice() * SteveDriver.constants.get("priceModifier"));
		}
	}

	public void purchaseUpgrade() {
		if (selectedUpgrade != null) {
			int tryBuy = selectedUpgrade.trySpendCurrency();

			if (tryBuy == 0) {
				selectedUpgrade.activateUpgrade();
				description = "Upgrade activated!";
			}
			else if (tryBuy == 1) {
				description = "Not enough cash!";
			}
			else if (tryBuy == 2) {
				description = "Steve must accend to a higher tier first!";
			}
			else if (tryBuy == 3) {
				description = "You already own this.";
			}
			else if (tryBuy == 4) {
				description = "Upgrade not available!";
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

		Upgrade steve = new Upgrade("Regular Steve",
				"steve",
				"snakeTier0",
				"Steve the cute, normal snake.",
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
				"Turn Steve into a Cyborg",
				1.0f,
				10000f,
				1, 0,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 39 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Robo Steve",
				"robot",
				"snakeTier2",
				"Turn Steve into a Robot",
				1.0f,
				100000f,
				2, 0,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + (panelHeight / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 41 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Increase HP",
				"hitpoints",
				"effTier1A",
				"Give Steve 25% more hitpoints",
				.25f,
				2500f,
				0, 1,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 31 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Decrease Hunger",
				"hungerRate",
				"effTier1B",
				"Hunger eats your HP 10% slower",
				.1f,
				2500f,
				0, 1,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 31 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Increase HP",
				"hitpoints",
				"effTier2A",
				"Give Steve 25% more hitpoints",
				.25f,
				25000f,
				1, 1,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 33 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Decrease Hunger",
				"hungerRate",
				"effTier2B",
				"Hunger eats your HP 10% slower",
				.1f,
				25000f,
				1, 1,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 33 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Increase HP",
				"hitpoints",
				"effTier3A",
				"Give Steve 25% more hitpoints",
				.25f,
				250000f,
				2, 1,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 35 * SteveDriver.TEXTURE_SIZE,
						19 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Decrease Hunger",
				"hungerRate",
				"effTier3B",
				"Hunger eats your HP 10% slower",
				.1f,
				250000f,
				2, 1,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(SteveDriver.atlas, 35 * SteveDriver.TEXTURE_SIZE,
						17 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE, 2 * SteveDriver.TEXTURE_SIZE)));

		upgrades.add(new Upgrade("Increase start length",
				"startLength",
				"survTier1A",
				"Give Steve 2 more segments when the round starts",
				2f,
				2500f,
				0, 2,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Max Length Increase",
				"maxLength",
				"survTier1B",
				"Increase the maximum possible length of Steve by 2.",
				2f,
				2500f,
				0, 2,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Increase start length",
				"startLength",
				"survTier2A",
				"Give Steve 2 more segments when the round starts",
				2f,
				25000f,
				1, 2,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Max Length Increase",
				"maxLength",
				"survTier2B",
				"Increase the maximum possible length of Steve by 2.",
				2f,
				25000f,
				1, 2,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Increase start length",
				"startLength",
				"survTier3A",
				"Give Steve 2 more segments when the round starts",
				2f,
				250000f,
				2, 2,
				(panelX - 32) + (panelWidth / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Max Length Increase",
				"maxLength",
				"survTier3B",
				"Increase the maximum possible length of Steve by 2.",
				2f,
				250000f,
				2, 2,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Main Gun",
				"mainGun",
				"wepTier1",
				"Steve gains a main cannon on his first segment.",
				1f,
				2500f,
				0, 3,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Fire Rate Increase",
				"fireRate",
				"wepTier2A",
				"Steve gains a 30% fire rate bonus for his main gun,\nand a 15% boost for all other guns.",
				.15f,
				25000f,
				1, 3,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));


		upgrades.add(new Upgrade("Fire Rate Increase",
				"mainCannonType",
				"wepTier3A",
				"Triple firepower!",
				1f,
				250000f,
				2, 3,
				(panelX - 32) + (panelWidth / 2),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Turret Range Increase",
				"fireRange",
				"wepTier2B",
				"Steve gains a 20% range bonus for his main gun,\nand a 10% boost for all other guns.",
				.1f,
				25000f,
				1, 3,
				(panelX - 32) + ((1 * panelWidth) / 4),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Turret Range Increase",
				"mainCannonType",
				"wepTier3B",
				"Turn the main cannon into a \npowerful Gauss Cannon.",
				2f,
				250000f,
				2, 3,
				(panelX - 32) + ((1 * panelWidth) / 4),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Damage Increase",
				"fireDamage",
				"wepTier2C",
				"Steve gains a 40% damage bonus for his main gun,\nand a 20% boost for all other guns.",
				.2f,
				25000f,
				1, 3,
				(panelX - 32) + ((3 * panelWidth) / 4),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));


		//firedamage .2f
		upgrades.add(new Upgrade("Damage Increase",
				"mainCannonType",
				"wepTier3C",
				"All missles fire!",
				3f,
				250000f,
				2, 3,
				(panelX - 32) + ((3 * panelWidth) / 4),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Plentiful Money",
				"goldModifier",
				"goldTier1A",
				"Sources of gold are worth 15% more.",
				.15f,
				2500f,
				0, 4,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Plentiful Money",
				"goldModifier",
				"goldTier2A",
				"Sources of gold are worth 15% more.",
				.15f,
				25000f,
				1, 4,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Plentiful Money",
				"goldModifier",
				"goldTier3A",
				"Sources of gold are worth 15% more.",
				.15f,
				250000f,
				2, 4,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Cheapskate",
				"priceModifier",
				"goldTier1B",
				"Upgrades are 10% cheaper.",
				-.1f,
				2500f,
				0, 4,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Cheapskate",
				"priceModifier",
				"goldTier2B",
				"Upgrades are 10% cheaper.",
				-.1f,
				25000f,
				1, 4,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new Upgrade("Cheapskate",
				"priceModifier",
				"goldTier3B",
				"Upgrades are 10% cheaper.",
				-.1f,
				250000f,
				2, 4,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new ToggleUpgrade("Glue Trail",
				"glueTrail",
				"special1",
				"Steve lays down a trail of glue behind him.",
				1f,
				1f,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new ToggleUpgrade("Candy Zone",
				"candyZone",
				"special6",
				"???",
				1f,
				1f,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((3 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new ToggleUpgrade("Nuke",
				"nuke",
				"special2",
				"Steve sets off a detonation every\n60 seconds.",
				1f,
				1f,
				(panelX - 32) + ((1 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new ToggleUpgrade("Drill Helmet",
				"drill",
				"special3",
				"You are the snake that will pierce\nthe heavens.",
				1f,
				1f,
				(panelX - 32) + ((2 * panelWidth) / 3),
				panelY + 32 + ((1 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new ToggleUpgrade("Jet Fuel Only",
				"jetFuel",
				"special4",
				"Steve gains the ability to go fast.",
				1f,
				1f,
				(panelX - 32) + ((4 * panelWidth) / 5),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

		upgrades.add(new ToggleUpgrade("Bullet Time",
				"bulletTime",
				"special5",
				"There is no snake.",
				1f,
				1f,
				(panelX - 32) + ((1 * panelWidth) / 5),
				panelY + 32 + ((2 * panelHeight) / 4),
				new Sprite(new TextureRegion(SteveDriver.atlas, 0 * SteveDriver.TEXTURE_SIZE,
						0 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE, 1 * SteveDriver.TEXTURE_SIZE))));

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

			//Initial value when opening store.
			activated = SteveDriver.storePrefs.getBoolean(key, false);
			if (activated) {
				activateUpgrade();
			}

			initActivated = activated;
			initConstantValue = SteveDriver.constants.get(constantName);
		}

		public void update() {
			available = (currentTier[category] == tier) || activated;

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

		public boolean isActivated() {
			return activated;
		}

		public void activateUpgrade() {
			if (available) {
				activated = true;
				currentTier[category] = tier + 1;

				if (constantName == "mainGun") {
					System.out.println();
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
		}

		public int trySpendCurrency() {
			if (available) {
				if (!activated) {
					if ((currentTier[0] > tier) || (category == 0)) {
						if (SteveDriver.snake.spendMoney((int) (price * SteveDriver.constants.get("priceModifier")))) {
							return 0; //Success
						}
						else {
							return 1; //Insufficient funds
						}
					}
					else {
						return 2; //Insufficient tier
					}
				}
				else {
					return 3; //You already own it
				}
			}
			else {
				return 4; //You can't buy it
			}
		}
	}

	public class ToggleUpgrade extends Upgrade {
		boolean initAvailable;

		public ToggleUpgrade(String displayName, String constantName,
				String prefsKey, String upgradeDescription, float value,
				float price, int xPos, int yPos,
				Sprite buttonIcon) {
			super(displayName, constantName, prefsKey, upgradeDescription, value, price,
					0, 5, xPos, yPos, buttonIcon);

			available = SteveDriver.storePrefs.getBoolean(key, false);
			initAvailable = available;
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

			super.activateUpgrade();
		}

		@Override
		public void deactivateUpgrade() {
			activated = false;
			SteveDriver.constants.modifyConstant(constantName, -1 * value);
		}

		@Override
		public void resetChoice() {
			available = initAvailable;
			super.resetChoice();
		}

		@Override
		public int trySpendCurrency() {
			if (!available) {
				if (SteveDriver.snake.spendTreasure((int) price)) {
					return 0;
				}
				else {
					return 1;
				}
			}
			else {
				return 3;
			}
		}
	}
}
