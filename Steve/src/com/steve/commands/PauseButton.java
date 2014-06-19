package com.steve.commands;

import com.steve.SteveDriver.STAGE_TYPE;

public class PauseButton extends ChangeStage{

	public PauseButton() {
		super(STAGE_TYPE.PAUSED);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		super.execute();
		
		if (stage == STAGE_TYPE.GAME) {
			stage = STAGE_TYPE.PAUSED;
		}
		else if (stage == STAGE_TYPE.PAUSED) {
			stage = STAGE_TYPE.GAME;
		}
	}
}
