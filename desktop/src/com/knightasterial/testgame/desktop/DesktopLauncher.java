package com.knightasterial.testgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.knightasterial.testgame.MyTestGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Drop - Sample Game";
		config.width = 800;
		config.height = 480;
		
		new LwjglApplication(new MyTestGame(), config);
		
		
	}
}
