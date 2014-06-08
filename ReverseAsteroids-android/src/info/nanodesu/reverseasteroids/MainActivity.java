package info.nanodesu.reverseasteroids;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class MainActivity extends AndroidApplication implements ActionResolver, GameHelperListener {
	
	private GameHelper ghelp;
	
	public MainActivity() {
		ghelp = new GameHelper(this);
		ghelp.enableDebugLog(true, "GPGS");
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        
        initialize(new ReverseAsteroidsMain(this), cfg);
        ghelp.setup(this);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	ghelp.onStart(this);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	ghelp.onStop();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	ghelp.onActivityResult(requestCode, resultCode, data);
    }
    
	@Override
	public boolean getSignedInGPGS() {
		return ghelp.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					ghelp.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		ghelp.getGamesClient().submitScore(getString(R.string.leaderboard_highscores), score);
	}

	@Override
	public void unlockAchievementGPGS(Achievements achievementId) {
		String key = null;
		switch (achievementId) {
		case DESTRUCTIVE:
			key = getString(R.string.achievement_destructive);
			break;
		case QUITE_A_LOT:
			key = getString(R.string.achievement_quite_a_lot);
			break;
		case SCORE_100K:
			key = getString(R.string.achievement_reach_100000_points);
			break;
		case SCORE_10K:
			key = getString(R.string.achievement_reach_10000_points);
			break;
		case SCORE_1K:
			key = getString(R.string.achievement_reach_1000_points);
			break;
		}
		if (key != null) {
			ghelp.getGamesClient().unlockAchievement(key);			
		}
	}

	@Override
	public void getLeaderboardGPGS() {
		startActivityForResult(ghelp.getGamesClient().getLeaderboardIntent(getString(R.string.leaderboard_highscores)), 100);
	}

	@Override
	public void getAchievementsGPGS() {
		startActivityForResult(ghelp.getGamesClient().getAchievementsIntent(), 101);
	}

	@Override
	public void onSignInFailed() {
		
	}

	@Override
	public void onSignInSucceeded() {
		
	}
}