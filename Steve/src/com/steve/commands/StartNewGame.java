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
		SteveDriver.storePrefs.putBoolean("special1", false);
		SteveDriver.storePrefs.putBoolean("special2", false);
		SteveDriver.storePrefs.putBoolean("special3", false);
		SteveDriver.storePrefs.putBoolean("special4", false);
		SteveDriver.storePrefs.putBoolean("specail5", false);
		SteveDriver.storePrefs.putBoolean("special6", false);
		
		SteveDriver.storePrefs.clear();
		
		SteveDriver.prefs.flush();
		SteveDriver.snake.setMoney(0);
		SteveDriver.snake.spendTreasure(SteveDriver.snake.getTreasure());
		//SteveDriver.snake.addTreasure(5);
		SteveDriver.constants.initConstants();
		SteveDriver.store = new Store();
		SteveDriver.tutorialOn = true;
		super.execute();
	}

}
