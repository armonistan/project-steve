package com.steve;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Steve";
		cfg.useGL20 = false;
		cfg.width = 680;
		cfg.height = 520;
		new LwjglApplication(new SteveDriver(), cfg);
	}
}
