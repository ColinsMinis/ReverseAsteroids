package info.nanodesu.reverseasteroids.entities;

import info.nanodesu.reverseasteroids.utils.AnimatedSprite;
import info.nanodesu.reverseasteroids.utils.Utls;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Ship extends AnimatedSprite {
	private TextureRegion[] explosions;
	private Sound explosionSound;
	
	private boolean isExploded = false;
	
	private static AtlasRegion selectTexture(TextureAtlas atlas) {
		Array<AtlasRegion> ships = atlas.findRegions("ingame/ship");
		return ships.get((int) (Math.random() * ships.size));
	}

	public Ship(TextureAtlas atlas, Sound explosionSound) {
		super(selectTexture(atlas));
		init(atlas, explosionSound);
	}

	private void init(TextureAtlas atlas, Sound explosionSound) {
		initExplosionTexture(atlas);
		this.explosionSound = explosionSound;
		
		int size = 40 + (int) (Math.random() * 30);
		setRndBounds(size/2, size);
		setBoxedWorld(true);
		
		int moveX = (int) (Math.random() * 120)-60;
		int moveY = (int) (Math.random() * 120)-60;
		rotateTowards(moveX, moveY);
		rotate(-90);
		setMoveSpeedX(moveX);
		setMoveSpeedY(moveY);
	}

	public Ship(TextureAtlas atlas, Animation ani, Sound exploSound) {
		super(ani);
		init(atlas, exploSound);
		setRotation(0);
	}
	
	private void initExplosionTexture(TextureAtlas atlas) {
		explosions = Utls.flatten(atlas.findRegion("ingame/explosions").split(64, 64));
		int exIndex = (int)(Math.random() * 4);
		int start = exIndex * 16;
		TextureRegion[] tmp = new TextureRegion[16];
		System.arraycopy(explosions, start, tmp, 0, 16);
		explosions = tmp;
	}
	
	public void explode() {
		Animation ani = new Animation(1/20f, explosions);
		setAnimation(ani);
		if (explosionSound != null) {
			explosionSound.play();
		}
		
		isExploded = true;
		growExplosion();
	}
	
	private void growExplosion() {
		float newWidth = getWidth() * 4;
		float newHeight = getHeight() * 4;
		
		growByOrigin(newWidth, newHeight);
	}
	
	public boolean isExploded() {
		return isExploded;
	}
}