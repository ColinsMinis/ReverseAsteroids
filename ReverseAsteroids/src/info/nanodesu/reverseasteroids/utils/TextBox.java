package info.nanodesu.reverseasteroids.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

public class TextBox{
	private BitmapFont font; 
	private TextureRegion back;
	
	private String text;
	private float x;
	private float y;
	private float width;
	private float height;

	public TextBox(String text, BitmapFont font) {
		this(text, font, null);
	}
	
	public TextBox(String text, BitmapFont font, TextureRegion back) {
		this.font = font;
		this.back = back;
		setText(text);
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	/**
	 * width and height are determined by the text
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * width and height are determined by the text
	 */
	public float getHeight() {
		return height;
	}
	
	public void setText(String text) {
		this.text = text;
		TextBounds bnds = font.getBounds(text);
		width = bnds.width;
		height = bnds.height;
	}
	
	public String getText() {
		return text;
	}
	
	public void draw(SpriteBatch batch) {
		if (back != null) {
			batch.draw(back, x, y, width, height);
		}
		font.draw(batch, text, x, y);
	}
}
