package com.polydus.threecolor3button.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.polydus.threecolor3button.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		float factor = 2f;

		config.resizable = false;
		config.width = (int) (225f * factor);
		config.height = (int) (400f * factor);


		new LwjglApplication(new Main(), config);
	}
}
