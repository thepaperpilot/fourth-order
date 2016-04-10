package thepaperpilot.order.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import thepaperpilot.order.Main;

class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "The Fourth Order";
		config.vSyncEnabled = false;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Main(), config);
	}
}
