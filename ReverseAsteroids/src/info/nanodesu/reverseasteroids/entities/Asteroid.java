package info.nanodesu.reverseasteroids.entities;

import info.nanodesu.reverseasteroids.utils.AnimatedSprite;
import info.nanodesu.reverseasteroids.utils.Utls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Asteroid extends AnimatedSprite {
	
	private static float MIN_WAIT = 3;
	
	private static AtlasRegion selectTexture(TextureAtlas atlas) {
		return atlas.findRegion("ingame/asteroid");
	}
	
	private Camera camera;
	
	private float moveTimer = Float.MAX_VALUE;
	
	public Asteroid(TextureAtlas atlas, Camera camera) {
		super(selectTexture(atlas));
		int size = 30;
		this.camera = camera;
		setBounds(Utls.WORLD_WIDTH/2-size/2, Utls.WORLD_HEIGHT/2-size/2, size, size);
		setRotateSpeed(size);
		setOrigin(size/2, size/2);
		setBoxedWorld(true);
	}
	
	@Override
	public void simulate(float dt) {
		moveTimer += Gdx.graphics.getDeltaTime();
		float charge = getMoveCharge();
		setColor(1, charge, charge, 1);
		if (charge >= 1 && Gdx.input.isTouched()) {
			Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			touch.sub(getXByOrgin(), getYByOrigin(), 0);
			setMoveSpeedX(touch.x);
			setMoveSpeedY(touch.y);
			moveTimer = 0;
		}
		super.simulate(dt);
	}
	
	public float getMoveCharge() {
		return MathUtils.clamp(moveTimer / MIN_WAIT, 0, 1);
	}
}
