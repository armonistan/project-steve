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
		SteveDriver.prefs.putBoolean("canGoToSpace", false);
		SteveDriver.prefs.putBoolean("bossDefeatedTutorial", false);
		
		//reset specials
		boolean sp1 = SteveDriver.storePrefs.getBoolean("special1");
		boolean sp2 = SteveDriver.storePrefs.getBoolean("special2");
		boolean sp3 = SteveDriver.storePrefs.getBoolean("special3");
		boolean sp4 = SteveDriver.storePrefs.getBoolean("special4");
		boolean sp5 = SteveDriver.storePrefs.getBoolean("special5");
		boolean sp6 = SteveDriver.storePrefs.getBoolean("special6");
		
		SteveDriver.storePrefs.clear();
		
		SteveDriver.storePrefs.putBoolean("special1", sp1);
		SteveDriver.storePrefs.putBoolean("special2", sp2);
		SteveDriver.storePrefs.putBoolean("special3", sp3);
		SteveDriver.storePrefs.putBoolean("special4", sp4);
		SteveDriver.storePrefs.putBoolean("special5", sp5);
		SteveDriver.storePrefs.putBoolean("special6", sp6);
		
		SteveDriver.store.saveStoreProgress();
		
		SteveDriver.prefs.flush();
		SteveDriver.snake.setMoney(0);
		//SteveDriver.snake.spendTreasure(SteveDriver.snake.getTreasure());
		//SteveDriver.snake.addTreasure(10);
		SteveDriver.snake.addMoney(0);
		SteveDriver.constants.initConstants();
		SteveDriver.store = new Store();
		SteveDriver.tutorialOn = true;
		super.execute();
	}

}
