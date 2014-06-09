package info.nanodesu.reverseasteroids;

import info.nanodesu.reverseasteroids.utils.TextBox;
import info.nanodesu.reverseasteroids.utils.Utls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Menu implements Screen {
	
	private ReverseAsteroidsMain main;
	
	private TextBox startBox;
	private TextBox scoresBox;
	private TextBox creditsBox;
	private TextBox exitBox;
	
	public Menu(ReverseAsteroidsMain main) {
		this.main = main;
		initTextBoxes(main);
	}

	private void initTextBoxes(ReverseAsteroidsMain main) {
		startBox = new TextBox("START", main.getFont());
		scoresBox = new TextBox("HIGHSCORE", main.getFont());
		creditsBox = new TextBox("CREDITS", main.getFont());
		exitBox = new TextBox("EXIT", main.getFont());
		repositionBoxes();
	}
	
	private void handleHighscores() {
		main.getActions().getLeaderboardGPGS();
	}
	
	private void repositionBoxes() {
		placeBoxes(exitBox, creditsBox, scoresBox, startBox);
	}
	
	private void placeBoxes(TextBox... boxes) {
		int h = Utls.WORLD_HEIGHT / boxes.length;
		for (int i = 0; i < boxes.length; i++) {
			boxes[i].setY(h*i + 30);
			boxes[i].setX(Math.abs(Utls.WORLD_WIDTH-boxes[i].getWidth())/2);
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		SpriteBatch batch = main.getBatch();
		batch.setProjectionMatrix(main.getCamera().combined);
		batch.begin();
		startBox.draw(batch);
		scoresBox.draw(batch);
		creditsBox.draw(batch);
		exitBox.draw(batch);
		batch.end();
		processInput();
	}
	
	private void processInput() {
		if (Gdx.input.isTouched()) {
			Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			main.getCamera().unproject(touch);
			
			if (exitBox.getBounds().contains(touch.x, touch.y)) {
				Gdx.app.exit();
			}
			
			if (creditsBox.getBounds().contains(touch.x, touch.y)) {
				Gdx.app.getNet().openURI("https://github.com/OpenSourcedMinis/ReverseAsteroids/blob/master/credits.md");
			}
			
			if (scoresBox.getBounds().contains(touch.x, touch.y)) {
				handleHighscores();
			}
			
			if (startBox.getBounds().contains(touch.x, touch.y)) {
				main.setScreen(new Ingame(main));
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		repositionBoxes();
	}

	@Override
	public void show() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}
}