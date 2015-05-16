package cubesolve.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import cubesolve.CubeSolve;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 4; // 4x MSAA
		config.allowSoftwareMode = true; // Allow software render fallback
		config.foregroundFPS = 10000; // Unlimit fps. Setting to 0 makes it drop to 30 w/ continuous rendering off :(
		config.backgroundFPS = 1; // Limit FPS when not in foreground
		config.vSyncEnabled = true; // Sync w/ vertical refresh

		new LwjglApplication(new CubeSolve(), config);
	}
}
