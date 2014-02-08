package info.nanodesu.reverseasteroids;

import info.nanodesu.reverseasteroids.utils.AnimatedSprite;
import info.nanodesu.reverseasteroids.utils.SmoothSimulator;
import info.nanodesu.reverseasteroids.utils.Utls;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ReverseAsteroidsMain implements ApplicationListener {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private TextureAtlas textures;
	
	private AnimatedSprite asteroid;
	private AtlasRegion background;
	
	private SmoothSimulator sim;
	
	private Music music; 
	
	@Override
	public void create() {
		sim = new SmoothSimulator();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Utls.WORLD_WIDTH, Utls.WORLD_HEIGHT);
		camera.update();
		
		batch = new SpriteBatch();
		
		textures = new TextureAtlas(Gdx.files.internal("gfx/game.atlas"));
		
		Array<AtlasRegion> asteroids = textures.findRegions("ingame/Asteroids");
		AtlasRegion a = asteroids.get((int)(Math.random() * asteroids.size));
		asteroid = new AnimatedSprite(a);
		asteroid.setBounds(375, 215, 50, 50);
		asteroid.setRotateSpeed(40);
		asteroid.setOrigin(25, 25);
		asteroid.setMoveSpeedX(100);
		asteroid.setMoveSpeedY(30);
		asteroid.setBoxedWorld(true);
		
		sim.getSims().add(asteroid);
		
		background = textures.findRegion("ingame/background");
		
		music = Gdx.audio.newMusic(Gdx.files.internal("sfx/space_chase.mp3"));
		music.setLooping(true);
		music.play();
	}

	@Override
	public void dispose() {
		music.dispose();
		batch.dispose();
		textures.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		
		processInput();
		
		sim.simulate(Gdx.graphics.getDeltaTime());
		
		batch.begin();
		batch.draw(background, 0, 0);
		asteroid.draw(batch);
		batch.end();
	}
	
	private void processInput() {
		if (Gdx.input.isTouched()) {
			Vector3 touch = new Vector3();
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			touch.sub(asteroid.getXByOrgin(), asteroid.getYByOrigin(), 0);
			
			asteroid.setMoveSpeedX(touch.x);
			asteroid.setMoveSpeedY(touch.y);
		}
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
