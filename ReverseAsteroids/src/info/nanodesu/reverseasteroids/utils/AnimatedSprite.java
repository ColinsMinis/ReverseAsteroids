package info.nanodesu.reverseasteroids.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class AnimatedSprite extends Sprite implements Simulate {
	private Animation ani;
	private float stateTime;

	private float rotateSpeed = 0;
	private float moveSpeedX = 0;
	private float moveSpeedY = 0;
	
	private boolean boxedWorld;
	
	public AnimatedSprite(TextureRegion texRegion) {
		super(texRegion);
	}
	
	public AnimatedSprite(Animation ani) {
		this.ani = ani;
		this.stateTime = 0;
	}
	
	public void setRndBounds(int width, int height) {
		int startX = (int) (Math.random() * Utls.WORLD_WIDTH);
		int startY = (int) (Math.random() * Utls.WORLD_HEIGHT);
		setBounds(startX, startY, width, height);
		setOrigin(getWidth()/2, getHeight()/2);
	}
	
	public void growByOrigin(float newWidth, float newHeight) {
		float diffW = newWidth - getWidth();
		float diffH = newHeight - getHeight();
		
		setSize(newWidth, newHeight);
		setOrigin(getWidth()/2, getHeight()/2);
		translate(-diffW/2, -diffH/2);
	}
	
	public void setAnimation(Animation ani) {
		resetAnimState();
		this.ani = ani;
	}
	
	public float getStateTime() {
		return stateTime;
	}
	
	public Animation getAnimation() {
		return ani;
	}
	
	public void setBoxedWorld(boolean boxedWorld) {
		this.boxedWorld = boxedWorld;
	}
	
	public boolean isBoxedWorld() {
		return boxedWorld;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		if (ani != null) {
			TextureRegion keyFrame = ani.getKeyFrame(stateTime);
			setRegion(keyFrame);
		}
		super.draw(spriteBatch);
	}
	
	public void simulate(float dt) {
		if (ani != null) {
			animate(dt);
		}
		
		move(dt);
		if (boxedWorld) {
			boxWorld();
		}
	}
	
	private void boxWorld() {
		if (getX() + getWidth() < 0) {
			setX(Utls.WORLD_WIDTH + getWidth());
		}
		
		if (getX() - getWidth() > Utls.WORLD_WIDTH) {
			setX(-getWidth());
		}
		
		if (getY() + getHeight() < 0) {
			setY(Utls.WORLD_HEIGHT + getHeight());
		}
		
		if (getY() - getHeight() > Utls.WORLD_HEIGHT) {
			setY(-getHeight());
		}
	}
	
	private void animate(float dt) {
		stateTime += dt;
	}

	private void move(float dt) {
		float rDiff = rotateSpeed * dt; 
		float xDiff = moveSpeedX * dt;
		float yDiff = moveSpeedY * dt;
		
		rotate(rDiff);
		translate(xDiff, yDiff);
	}
	
	public void rotateTowards(float x, float y) {
		setRotation(MathUtils.atan2(y, x) * MathUtils.radiansToDegrees);
	}
	
	public float getXByOrgin() {
		return getX() + getOriginX();
	}
	
	public float getYByOrigin() {
		return getY() + getOriginY();
	}
	
	public void resetAnimState() {
		stateTime = 0;
	}

	public float getRotateSpeed() {
		return rotateSpeed;
	}

	public void setRotateSpeed(float rotateSpeed) {
		this.rotateSpeed = rotateSpeed;
	}

	public float getMoveSpeedX() {
		return moveSpeedX;
	}

	public void setMoveSpeedX(float moveSpeedX) {
		this.moveSpeedX = moveSpeedX;
	}

	public float getMoveSpeedY() {
		return moveSpeedY;
	}

	public void setMoveSpeedY(float moveSpeedY) {
		this.moveSpeedY = moveSpeedY;
	}
}