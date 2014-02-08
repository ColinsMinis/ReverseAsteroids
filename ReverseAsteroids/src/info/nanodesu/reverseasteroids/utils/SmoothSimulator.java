package info.nanodesu.reverseasteroids.utils;

import com.badlogic.gdx.utils.Array;

public class SmoothSimulator {
	
	private static float FIX_SIM = 1 / 60f;
	
	private Array<Simulate> sims;
	
	private float timeBag = 0;
	
	public SmoothSimulator() {
		sims = new Array<Simulate>();
	}
	
	public Array<Simulate> getSims() {
		return sims;
	}
	
	public void simulate(float dt) {
		timeBag += dt;
		
		while(timeBag >= FIX_SIM) {
			timeBag -= FIX_SIM;
			for (int i = 0; i < sims.size; i++) {
				sims.get(i).simulate(FIX_SIM);
			}
		}
	}
}
