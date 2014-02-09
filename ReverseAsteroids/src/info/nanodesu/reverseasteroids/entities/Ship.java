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
		initExplosionTexture(atlas);
		this.explosionSound = explosionSound;
		
		int size = 60;
		int startX = (int) (Math.random() * Utls.WORLD_WIDTH);
		int startY = (int) (Math.random() * Utls.WORLD_HEIGHT);
		setBounds(startX, startY, size/2, size);
		setOrigin(getWidth()/2, getHeight()/2);
		setBoxedWorld(true);
		
		int moveX = (int) (Math.random() * 120)-60;
		int moveY = (int) (Math.random() * 120)-60;
		rotateTowards(moveX, moveY);
		setRotation(getRotation() - 90);
		setMoveSpeedX(moveX);
		setMoveSpeedY(moveY);
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
		explosionSound.play();
		isExploded = true;
		growExplosion();
	}
	
	private void growExplosion() {
		float newWidth = getWidth() * 4;
		float newHeight = getHeight() * 4;
		
		float diffW = newWidth - getWidth();
		float diffH = newHeight - getHeight();
		
		setSize(newWidth, newHeight);
		setOrigin(getWidth()/2, getHeight()/2);
		translate(-diffW/2, -diffH/2);
	}
	
	public boolean isExploded() {
		return isExploded;
	}
}