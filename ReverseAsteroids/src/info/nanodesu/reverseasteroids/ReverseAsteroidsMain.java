package info.nanodesu.reverseasteroids;

import info.nanodesu.reverseasteroids.utils.Utls;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ReverseAsteroidsMain extends Game {
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	
	@Override
	public void create() {
		initCamera();
		initFont();
		initBatch();
		
		setScreen(new Ingame(this));
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public BitmapFont getFont() {
		return font;
	}
	
	private void initBatch() {
		batch = new SpriteBatch();
	}

	private void initFont() {
		int sizeF = Gdx.graphics.getHeight() > 400 ? 20 : 10;
		font = Utls.getFont(Gdx.graphics.getHeight() / sizeF);
	}

	private void initCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Utls.WORLD_WIDTH, Utls.WORLD_HEIGHT);
		camera.update();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}