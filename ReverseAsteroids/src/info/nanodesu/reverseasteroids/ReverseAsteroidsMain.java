package info.nanodesu.reverseasteroids;

import java.util.Iterator;

import info.nanodesu.reverseasteroids.entities.Asteroid;
import info.nanodesu.reverseasteroids.entities.Ship;
import info.nanodesu.reverseasteroids.utils.SmoothSimulator;
import info.nanodesu.reverseasteroids.utils.Utls;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class ReverseAsteroidsMain implements ApplicationListener {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private TextureAtlas textures;
	
	private Asteroid asteroid;
	private AtlasRegion background;
	
	private Array<Ship> ships;
	
	private SmoothSimulator sim;
	
	private Sound[] explosion;
	private Music music;
	
	@Override
	public void create() {
		sim = new SmoothSimulator();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Utls.WORLD_WIDTH, Utls.WORLD_HEIGHT);
		camera.update();
		
		batch = new SpriteBatch();
		
		textures = new TextureAtlas(Gdx.files.internal("gfx/game.atlas"));
		
		asteroid = new Asteroid(textures, camera);
		
		ships = new Array<Ship>();
		
		int expCnt = 9;
		explosion = new Sound[expCnt];
		for (int i = 0; i < expCnt; i++) {
			explosion[i] = Gdx.audio.newSound(Gdx.files.internal("sfx/boom"+(i+1)+".mp3"));
		}
		
		for (int i = 0; i < 10; i++) {
			spawnShip();
		}
		
		sim.getSims().add(asteroid);
		
		background = textures.findRegion("ingame/background");
		
		music = Gdx.audio.newMusic(Gdx.files.internal("sfx/space_chase.mp3"));
		music.setLooping(true);
		music.play();
	}
	
	private void spawnShip() {
		Ship ship = new Ship(textures, explosion[(int)(Math.random() * explosion.length)]);
		ships.add(ship);
		sim.getSims().add(ship);
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
		
		sim.simulate(Gdx.graphics.getDeltaTime());
		
		checkCollisions();
		
		batch.begin();
		batch.draw(background, 0, 0);
		
		Iterator<Ship> iter = ships.iterator();
		while(iter.hasNext()) {
			Ship s = iter.next();
			s.draw(batch);
			if (s.isExploded() && s.getStateTime() > 5) {
				iter.remove();
			}
		}
		
		asteroid.draw(batch);
		batch.end();
	}
	
	private void checkCollisions() {
		int cnt = 0;
		for (Ship s: ships) {
			if (!s.isExploded() && s.getBoundingRectangle().overlaps(asteroid.getBoundingRectangle())) {
				s.explode();
				cnt++;
			}
		}
		for (int i = 0; i < cnt; i++) {
			spawnShip();
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
