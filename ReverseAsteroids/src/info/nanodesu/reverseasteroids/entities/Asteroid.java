package info.nanodesu.reverseasteroids.entities;

import info.nanodesu.reverseasteroids.utils.AnimatedSprite;
import info.nanodesu.reverseasteroids.utils.Utls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Asteroid extends AnimatedSprite {
	
	private static AtlasRegion selectTexture(TextureAtlas atlas) {
		Array<AtlasRegion> asteroids = atlas.findRegions("ingame/Asteroids");
		return asteroids.get((int)(Math.random() * asteroids.size));
	}
	
	private Camera camera;
	
	public Asteroid(TextureAtlas atlas, Camera camera) {
		super(selectTexture(atlas));
		int size = 50;
		this.camera = camera;
		setBounds(Utls.WORLD_WIDTH/2-size/2, Utls.WORLD_HEIGHT/2-size/2, size, size);
		setRotateSpeed(size);
		setOrigin(size/2, size/2);
		setBoxedWorld(true);
	}
	
	@Override
	public void simulate(float dt) {
		if (Gdx.input.isTouched()) {
			Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			touch.sub(getXByOrgin(), getYByOrigin(), 0);
			setMoveSpeedX(touch.x);
			setMoveSpeedY(touch.y);
		}
		super.simulate(dt);
	}
}
