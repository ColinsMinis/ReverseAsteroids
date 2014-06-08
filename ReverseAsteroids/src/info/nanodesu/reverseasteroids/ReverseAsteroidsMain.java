package info.nanodesu.reverseasteroids;

import info.nanodesu.reverseasteroids.utils.Utls;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ReverseAsteroidsMain extends Game {
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	
	private ActionResolver actions;
	
	public ReverseAsteroidsMain(ActionResolver resolver) {
		this.actions = resolver;
	}
	
	@Override
	public void create() {
		initCamera();
		initResources();
		
		setScreen(new Menu(this));
	}
	
	public ActionResolver getActions() {
		return actions;
	}
	
	public void initResources() {
		initFont();
		initBatch();
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
		font = Utls.getFont();
	}
	
	private void initCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Utls.WORLD_WIDTH, Utls.WORLD_HEIGHT);
		camera.update();
	}
	
	@Override
	public void dispose() {
		if (getScreen() != null) {
			getScreen().dispose();			
		}
		batch.dispose();
		font.dispose();
	}
}