package info.nanodesu.reverseasteroids.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class TextBox {
	private BitmapFont font; 
	
	private String text;
	
	private Rectangle bounds;
	
	private Color clr = Color.WHITE;
	
	public TextBox(String text, BitmapFont font) {
		bounds = new Rectangle();
		this.font = font;
		setText(text);
	}
	
	public void setClr(Color clr) {
		this.clr = clr;
	}
	
	public Color getClr() {
		return clr;
	}
	
	public void setX(float x) {
		bounds.x = x;
	}
	
	public void setY(float y) {
		bounds.y = y;
	}
	
	public float getX() {
		return bounds.x;
	}
	
	public float getY() {
		return bounds.y;
	}
	
	/**
	 * width and height are determined by the text
	 */
	public float getWidth() {
		return bounds.width;
	}

	/**
	 * width and height are determined by the text
	 */
	public float getHeight() {
		return bounds.height;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setText(String text) {
		this.text = text;
		TextBounds bnds = font.getBounds(text);
		bounds.width = bnds.width;
		bounds.height = bnds.height;
	}
	
	public String getText() {
		return text;
	}
	
	public void draw(SpriteBatch batch) {
		Color c = font.getColor();
		font.setColor(clr);
		font.drawMultiLine(batch, text, bounds.x, bounds.y+bounds.height);
		font.setColor(c);
	}
}
