package com.steve.commands;

import com.badlogic.gdx.Gdx;
import com.steve.SteveDriver;
import com.steve.stages.Store;

public class StartNewGame extends StartNewRound {

	@Override
	public void execute() {
		SteveDriver.store.resetStore();
		//System.out.println(SteveDriver.storePrefs.getBoolean("goldTier1B"));
		SteveDriver.prefs.putInteger("money", 0);
		SteveDriver.prefs.putBoolean("edgeTutorial", false);
		SteveDriver.prefs.putBoolean("cyborgBossTutorial", false);
		SteveDriver.prefs.putBoolean("robotBossTutorial", false);
		
		//reset boss spawning
		SteveDriver.prefs.putBoolean("cyborgBossActivate", false);
		SteveDriver.prefs.putBoolean("carrierDefeated", false);
		SteveDriver.prefs.putBoolean("robotBossActivate", false);
		SteveDriver.prefs.putBoolean("razorbullDefeated", false);
		
		//reset progress in game
		SteveDriver.prefs.putInteger("bossesDefeated", 0);
		SteveDriver.prefs.putBoolean("canGoToSpace",false);
		SteveDriver.prefs.putBoolean("bossDefeatedTutorial", false);
		
		//reset specials
		SteveDriver.storePrefs.putFloat("special1", 0f);
		SteveDriver.storePrefs.putFloat("special2", 0f);
		SteveDriver.storePrefs.putFloat("special3", 0f);
		SteveDriver.storePrefs.putFloat("special4", 0f);
		SteveDriver.storePrefs.putFloat("specail5", 0f);
		SteveDriver.storePrefs.putFloat("special6", 0f);
		
		SteveDriver.storePrefs.clear();
		
		SteveDriver.prefs.flush();
		SteveDriver.snake.setMoney(0);
		SteveDriver.snake.spendTreasure(SteveDriver.snake.getTreasure());
		SteveDriver.snake.addTreasure(5);
		SteveDriver.constants.initConstants();
		SteveDriver.store = new Store();
		SteveDriver.tutorialOn = true;
		super.execute();
	}

}
