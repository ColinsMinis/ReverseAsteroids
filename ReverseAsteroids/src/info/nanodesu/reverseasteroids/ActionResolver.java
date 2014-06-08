package info.nanodesu.reverseasteroids;

public interface ActionResolver {
	
	public enum Achievements {
		SCORE_1K,
		SCORE_10K,
		SCORE_100K,
		DESTRUCTIVE,
		QUITE_A_LOT
	}
	
	public boolean getSignedInGPGS();

	public void loginGPGS();

	public void submitScoreGPGS(int score);

	public void unlockAchievementGPGS(Achievements unlock);

	public void getLeaderboardGPGS();

	public void getAchievementsGPGS();
}
