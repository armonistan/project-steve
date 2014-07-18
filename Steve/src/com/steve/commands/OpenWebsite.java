package com.steve.commands;

import com.badlogic.gdx.Gdx;
import com.steve.ICommand;

public class OpenWebsite implements ICommand {
	String web;
	
	public OpenWebsite(String site) {
		web = site;
	}
	
	@Override
	public void execute() {
		Gdx.net.openURI(web);
	}

	@Override
	public void keepExecute() {
		// TODO Auto-generated method stub

	}
}
