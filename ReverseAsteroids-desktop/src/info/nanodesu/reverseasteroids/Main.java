package info.nanodesu.reverseasteroids;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ReverseAsteroids";
		cfg.useGL20 = false;
		cfg.width = 480 * 1;
		cfg.height = 320 * 1;
		
//		packTextures();
		
		new LwjglApplication(new ReverseAsteroidsMain(), cfg);
	}
	
	public static void packTextures() {
		Settings packSettings = new Settings();
		TexturePacker2.process(packSettings, "../asset_src/to_pack", "../ReverseAsteroids-android/assets/gfx", "game");		
	}
}
