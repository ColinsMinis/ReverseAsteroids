package info.nanodesu.reverseasteroids;

import info.nanodesu.reverseasteroids.entities.Asteroid;
import info.nanodesu.reverseasteroids.entities.Enemy;
import info.nanodesu.reverseasteroids.entities.Ship;
import info.nanodesu.reverseasteroids.utils.AnimatedSprite;
import info.nanodesu.reverseasteroids.utils.Simulate;
import info.nanodesu.reverseasteroids.utils.SmoothSimulator;
import info.nanodesu.reverseasteroids.utils.TextBox;
import info.nanodesu.reverseasteroids.utils.Utls;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Ingame implements Screen {
	
	private static final int ENEMY_SPAWN_FREQUENCY = 2;

	private static final double KILL_SPAWN_CHANCE = 0.15;

	private static final double KILL_CHANCE = 0.35;

	private static final int SHIPS_CNT = 12;
	
	private TextureAtlas textures;
	
	private Asteroid asteroid;
	private AtlasRegion background;
	private Array<Ship> ships;
	
	private Array<Enemy> enemies;
	
	private AnimatedSprite powerup;
	
	private TextBox txtDisplay;
	
	private float explosionProcessDt = 0;
	private int explosionsToProcess = 0;
	
	private float scoreIncTime = 0;
	private int score;
	private int killCnt;
	
	private SmoothSimulator sim;
	
	private Sound killPower;
	private Sound[] explosion;
	private Music music;
	
	private boolean paused = false;
	
	private boolean gameOver = false;
	private float dtGameOver = 0;
	
	private Vector2 tmpVec = new Vector2(0, 0);
	private Vector2 tmpVec2 = new Vector2(0, 0);
	
	private ReverseAsteroidsMain main;
	
	private InputProcessor inputProc = new InputAdapter() {
		public boolean keyDown(int keycode) {
			if (keycode == 67 || keycode == 4) { // 4 is back on android, 67 is backspace on windows
				main.setScreen(new Menu(main));
				dispose();
			}
			return false;
		}
	};
	
	public Ingame(ReverseAsteroidsMain main) {
		this.main = main;
		sim = new SmoothSimulator();
		
		txtDisplay = new TextBox("", main.getFont());

		textures = new TextureAtlas(Gdx.files.internal("gfx/game.atlas"));
		
		ships = new Array<Ship>();
		enemies = new Array<Enemy>();
		
		int expCnt = 9;
		explosion = new Sound[expCnt];
		for (int i = 0; i < expCnt; i++) {
			explosion[i] = Gdx.audio.newSound(Gdx.files.internal("sfx/boom"+(i+1)+".mp3"));
		}

		killPower = Gdx.audio.newSound(Gdx.files.internal("sfx/kill.mp3"));
		
		reset();
		
		background = textures.findRegion("ingame/background");
		
		music = Gdx.audio.newMusic(Gdx.files.internal("sfx/space_chase.mp3"));
		music.setLooping(true);
		
		Gdx.input.setInputProcessor(inputProc);
		Gdx.input.setCatchBackKey(true);
	}
	
	public void reset(){
		score = 0;
		powerup = null;
		setKillCnt(0);
		sim.getSims().clear();
		initCollisionSim();
		
		ships.clear();
		enemies.clear();
		
		asteroid = new Asteroid(textures, main.getCamera());
		sim.getSims().add(asteroid);
		
		for (int i = 0; i < SHIPS_CNT; i++) {
			spawnShip();
		}
		
		spawnEnemy();
		
		setScoreText();
		setScoreTextPosition();
		
		gameOver = false;
		dtGameOver = 0;
	}

	private void setScoreTextPosition() {
		txtDisplay.setX(0);
		txtDisplay.setY(5);
	}
	
	private void initCollisionSim() {
		sim.getSims().add(new Simulate() {
			@Override
			public void simulate(float dt) {
				checkCollisions(dt);
			}
		});
	}
	
	private void setKillCnt(int v) {
		killCnt = v;
		
		if (killCnt % ENEMY_SPAWN_FREQUENCY == 0) {
			spawnEnemy();
		}
		
		if (Math.random() > 1-KILL_SPAWN_CHANCE) {
			spawnPowerup();
		}
	}

	private String getKString(int n) {
		if (n > 10000) {
			return n/1000+"K";
		}
		if (n > 1E7) {
			return n/1E6+"M";
		}
		return n+"";
	}
	
	private void setScoreText() {
		txtDisplay.setText("Score: "+getKString(score) + " (+"+getKString(getActiveEnemyCount()*killCnt)+")");
	}
	
	private void incKillCnt() {
		setKillCnt(killCnt + 1);
	}
	
	private void spawnEnemy() {
		Enemy e = new Enemy(textures, null);
		enemies.add(e);
		sim.getSims().add(e);
	}
	
	private void spawnPowerup() {
		if (powerup == null) {
			powerup = new AnimatedSprite(textures.findRegion("ingame/kill"));
			powerup.setRndBounds(50, 50);
		}
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
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		main.getCamera().update();
		
		SpriteBatch batch = main.getBatch();
		
		batch.setProjectionMatrix(main.getCamera().combined);
		
		checkUnpause();
		
		if (gameOver) {
			batch.begin();
			txtDisplay.draw(batch);
			batch.end();
			dtGameOver += delta;
			
			if (dtGameOver > 3 && Gdx.input.isTouched()) {
				reset();
			}
		} else {
			if (!paused) {
				sim.simulate(delta);
				checkScoreInc(delta);
			}
			
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
			
			Iterator<Enemy> enemyIter = enemies.iterator();
			while (enemyIter.hasNext()) {
				Enemy e = enemyIter.next();
				if (e.getInitPercent() >= 1) {
					e.setColor(1, .1f, .1f, 1);
				} else {
					e.setColor(1, 1, 1, 1);
				}
				e.draw(batch, e.getInitPercent());
				checkGameOver(e);
				
				if (e.isExploded() && e.getStateTime() > 5) {
					enemyIter.remove();
				}
			}
			
			asteroid.draw(batch);
			
			if (powerup != null) {
				powerup.draw(batch);
			}
			
			txtDisplay.draw(batch);
			
			batch.end();
		}
	}

	private int getActiveEnemyCount() {
		int cnt = 0;
		for (Enemy e: enemies) {
			if (!e.isExploded() && e.getInitPercent() >= 1) {
				cnt++;
			}
		}
		return cnt;
	}
	
	private void checkScoreInc(float dt) {
		scoreIncTime += dt;
		if (scoreIncTime > 1) {
			scoreIncTime = 0;
			int inc = getActiveEnemyCount();
			score += inc*killCnt;
			if (inc > 0) {
				setScoreText();
			}
		}
	}
	
	private void checkUnpause()  {
		if (paused && Gdx.input.isTouched()) {
			setPaused(false);
		}
	}
	
	public void setPaused(boolean paused) {
		if (paused != this.paused) {
			if (paused) {
				showMiddleMessage("Tap to continue");
			} else {
				setScoreTextPosition();
				setScoreText();
			}
		}
		this.paused = paused;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	private void checkGameOver(Enemy e) {
		if (!e.isExploded() && e.getInitPercent() >= 1) {
			tmpVec.x = e.getXByOrgin();
			tmpVec.y = e.getYByOrigin();
			tmpVec2.x = asteroid.getXByOrgin();
			tmpVec2.y = asteroid.getYByOrigin();
			if (tmpVec.sub(tmpVec2).len() < asteroid.getWidth()/2 + e.getWidth()/2) {
				gameOver = true;
				String txt = "Game over\n"+score;
				showMiddleMessage(txt);
				if (main.getActions().getSignedInGPGS()) {
					main.getActions().submitScoreGPGS(score);
				}
			}
		}
	}

	private void showMiddleMessage(String txt) {
		TextBounds b = main.getFont().getBounds(txt);
		txtDisplay.setClr(new Color(1, 1, 1, 1));
		txtDisplay.setText(txt);
		txtDisplay.setX(Utls.WORLD_WIDTH/2-b.width/2);
		txtDisplay.setY(Utls.WORLD_HEIGHT/2);
	}

	private void checkCollisions(float dt) {
		int cnt = 0;
		for (Ship s: ships) {
			if (!s.isExploded() && s.getBoundingRectangle().overlaps(asteroid.getBoundingRectangle())) {
				explosionsToProcess++;
				s.explode();
				cnt++;
			}
		}
		
		if (powerup != null && asteroid.getBoundingRectangle().overlaps(powerup.getBoundingRectangle())) {
			powerup = null;
			if (enemies.size > 0) {
				for (Enemy e: enemies) {
					if (!e.isExploded() && Math.random() > 1-KILL_CHANCE) {
						e.setColor(0, 1, 0, 1);
						e.explode();
					}
				}
				killPower.play();
			}
		}
		
		explosionProcessDt += dt;
		if (explosionsToProcess > 0 && explosionProcessDt > 0.25f) {
			incKillCnt();
			explosionsToProcess--;
			explosionProcessDt = 0;
		}
		
		for (int i = 0; i < cnt; i++) {
			spawnShip();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		music.play();
	}

	@Override
	public void hide() {
		music.pause();
		setPaused(true);
	}

	@Override
	public void pause() {
		music.pause();
		setPaused(true);
	}

	@Override
	public void resume() {
		music.play();
	}

	@Override
	public void dispose() {
		Gdx.input.setCatchBackKey(false);
		Gdx.input.setInputProcessor(null);
		music.dispose();
		textures.dispose();
		killPower.dispose();
		for (Sound e: explosion) {
			e.dispose();
		}
	}
}