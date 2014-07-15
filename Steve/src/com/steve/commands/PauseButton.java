package com.steve.commands;

import com.steve.SteveDriver.STAGE_TYPE;

public class PauseButton extends ChangeStage{
	STAGE_TYPE gameType;
	STAGE_TYPE pausedType;

	public PauseButton(STAGE_TYPE type, STAGE_TYPE paused) {
		super(paused);

		gameType = type;
		pausedType = paused;
	}

	@Override
	public void execute() {
		super.execute();
		
		if (stage == gameType) {
			stage = pausedType;
		}
		else if (stage == pausedType) {
			stage = gameType;
		}
	}
}
