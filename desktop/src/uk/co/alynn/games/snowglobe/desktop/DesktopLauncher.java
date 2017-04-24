package uk.co.alynn.games.snowglobe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uk.co.alynn.games.snowglobe.Snowglobe;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 1024;
		config.height = 640;

		config.title = "Arctic Memories";
		config.vSyncEnabled = true;
		config.resizable = true;

		config.samples = 4;

		config.useHDPI = true;

		new LwjglApplication(new Snowglobe(), config);
	}
}
