package info.nanodesu.reverseasteroids;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main implements ActionResolver {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ReverseAsteroids";
		cfg.useGL20 = false;
		cfg.width = 480 * 1;
		cfg.height = 320 * 1;
		
//		packTextures();
		
		new LwjglApplication(new ReverseAsteroidsMain(new Main()), cfg);
	}
	
	public static void packTextures() {
		Settings packSettings = new Settings();
		TexturePacker2.process(packSettings, "../asset_src/to_pack", "../ReverseAsteroids-android/assets/gfx", "game");		
	}

	@Override
	public boolean getSignedInGPGS() {
		return false;
	}

	@Override
	public void loginGPGS() {
		System.out.println("login google");
	}

	@Override
	public void submitScoreGPGS(int score) {
		System.out.println("Submit score: "+score);
	}

	@Override
	public void unlockAchievementGPGS(Achievements unlock) {
		System.out.println("Unlock achievement: "+unlock);
	}

	@Override
	public void getLeaderboardGPGS() {
		System.out.println("getleaderboard");
	}

	@Override
	public void getAchievementsGPGS() {
		System.out.println("getachievements");
	}
}