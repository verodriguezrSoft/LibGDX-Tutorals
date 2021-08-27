package com.kilobolt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kilobolt.ZBGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Zommbe flappee bird";
		config.width = 1080 / 3;
		config.height = 1920 / 3;
		new LwjglApplication(new ZBGame(), config);
	}
}
