package info.nanodesu.reverseasteroids;

import java.util.Iterator;

import info.nanodesu.reverseasteroids.entities.Asteroid;
import info.nanodesu.reverseasteroids.entities.Enemy;
import info.nanodesu.reverseasteroids.entities.Ship;
import info.nanodesu.reverseasteroids.utils.SmoothSimulator;
import info.nanodesu.reverseasteroids.utils.TextBox;
import info.nanodesu.reverseasteroids.utils.Utls;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ReverseAsteroidsMain implements ApplicationListener {

	private static final int SHIPS_CNT = 3;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private TextureAtlas textures;
	
	private Asteroid asteroid;
	private AtlasRegion background;
	private Array<Ship> ships;
	
	private Array<Enemy> enemies;
	
	private BitmapFont font;
	private TextBox scoreDisplay;
	
	private float explosionProcessDt = 0;
	private int explosionsToProcess = 0;
	
	private int score;
	
	private SmoothSimulator sim;
	
	private Sound[] explosion;
	private Music music;
	
	private boolean gameOver = false;
	private float dtGameOver = 0;
	
	@Override
	public void create() {
		sim = new SmoothSimulator();
		
		int sizeF = Gdx.graphics.getHeight() > 400 ? 20 : 10;
		font = Utls.getFont(Gdx.graphics.getHeight() / sizeF);
		
		scoreDisplay = new TextBox("", font);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Utls.WORLD_WIDTH, Utls.WORLD_HEIGHT);
		camera.update();
		
		batch = new SpriteBatch();
		
		textures = new TextureAtlas(Gdx.files.internal("gfx/game.atlas"));
		
		ships = new Array<Ship>();
		enemies = new Array<Enemy>();
		
		int expCnt = 9;
		explosion = new Sound[expCnt];
		for (int i = 0; i < expCnt; i++) {
			explosion[i] = Gdx.audio.newSound(Gdx.files.internal("sfx/boom"+(i+1)+".mp3"));
		}
		
		reset();
		
		background = textures.findRegion("ingame/background");
		
		music = Gdx.audio.newMusic(Gdx.files.internal("sfx/space_chase.mp3"));
		music.setLooping(true);
		music.play();
	}
	
	private void spawnEnemy() {
		Enemy e = new Enemy(textures, rndExplo());
		enemies.add(e);
		sim.getSims().add(e);
	}
	
	private void spawnShip() {
		Ship ship = new Ship(textures, rndExplo());
		ships.add(ship);
		sim.getSims().add(ship);
	}

	private Sound rndExplo() {
		return explosion[(int)(Math.random() * explosion.length)];
	}
	
	@Override
	public void dispose() {
		music.dispose();
		batch.dispose();
		textures.dispose();
		font.dispose();
	}
	
	public void reset() {
		setScore(0);
		sim.getSims().clear();
		
		ships.clear();
		enemies.clear();
		
		asteroid = new Asteroid(textures, camera);
		sim.getSims().add(asteroid);
		
		for (int i = 0; i < SHIPS_CNT; i++) {
			spawnShip();
		}
		
		spawnEnemy();
		
		scoreDisplay.setText("");
		scoreDisplay.setX(0);
		scoreDisplay.setY(font.getLineHeight());
		
		gameOver = false;
		dtGameOver = 0;
	}
	
	private Vector2 tmpVec = new Vector2(0, 0);
	private Vector2 tmpVec2 = new Vector2(0, 0);
	
	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		
		if (gameOver) {
			batch.begin();
			scoreDisplay.draw(batch);
			batch.end();
			dtGameOver += Gdx.graphics.getDeltaTime();
			
			if (dtGameOver > 3 && Gdx.input.isTouched()) {
				reset();
			}
		} else {
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
			
			for (Enemy e: enemies) {
				e.draw(batch, e.getInitPercent());
				if (e.getInitPercent() >= 1) {
					tmpVec.x = e.getXByOrgin();
					tmpVec.y = e.getYByOrigin();
					tmpVec2.x = asteroid.getXByOrgin();
					tmpVec2.y = asteroid.getYByOrigin();
					if (tmpVec.sub(tmpVec2).len() < asteroid.getWidth()/2 + e.getWidth()/2) {
						gameOver = true;
						String txt = "Game over: "+score;
						TextBounds b = font.getBounds(txt);
						scoreDisplay.setText(txt);
						scoreDisplay.setX(Utls.WORLD_WIDTH/2-b.width/2);
						scoreDisplay.setY(Utls.WORLD_HEIGHT/2+b.height/2);
					}
				}
			}
			
			asteroid.draw(batch);
			
			scoreDisplay.draw(batch);
			
			batch.end();
		}
	}
	
	private void checkCollisions() {
		int cnt = 0;
		for (Ship s: ships) {
			if (!s.isExploded() && s.getBoundingRectangle().overlaps(asteroid.getBoundingRectangle())) {
				explosionsToProcess++;
				s.explode();
				cnt++;
			}
		}
		
		explosionProcessDt += Gdx.graphics.getDeltaTime();
		if (explosionsToProcess > 0 && explosionProcessDt > 0.25f) {
			incScore();
			explosionsToProcess--;
			explosionProcessDt = 0;
		}
		
		for (int i = 0; i < cnt; i++) {
			spawnShip();
		}
	}
	
	private void setScore(int v) {
		score = v;
		font.setColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
		scoreDisplay.setText("Score: "+score);
		
		int h = score / 10;
		if (score % ((h+1) * 2) == 0) {
			spawnEnemy();
		}
	}
	
	private void incScore() {
		setScore(score + 1);
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
