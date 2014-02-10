package info.nanodesu.reverseasteroids.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Ship {
	
	private float lifeTime = 0;
	
	private static Animation prepareAnimation(TextureAtlas atlas) {
		Array<AtlasRegion> texs = atlas.findRegions("ingame/alien");
		Animation ani = new Animation(1/20f, texs);
		ani.setPlayMode(Animation.LOOP);
		return ani;
	}
	
	public Enemy(TextureAtlas atlas, Sound explo) {
		super(atlas, prepareAnimation(atlas), explo);
		growByOrigin(getWidth(), getWidth());
	}
	
	@Override
	public void simulate(float dt) {
		super.simulate(dt);
		lifeTime += Gdx.graphics.getDeltaTime();
	}
	
	public float getInitPercent() {
		return MathUtils.clamp(lifeTime / 10, 0, 1);
	}
}
