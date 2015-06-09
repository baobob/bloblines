package org.bloblines.desktop;

import org.bloblines.Game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1440;
		config.height = 900;
		// config.fullscreen = true;
		// config.vSyncEnabled = true;
		new LwjglApplication(new Game(), config);
	}
}
