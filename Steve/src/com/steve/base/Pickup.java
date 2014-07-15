package com.steve.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.steve.Snake;
import com.steve.SteveDriver;

public class Pickup extends Sprite {
	private boolean active;
	protected int points;
	protected Sound pickupSound;
	
	protected float alphaMod = 1.0f;
	private float lifeTime = 10.0f;
	private float lifeTimer = 10.0f;
	
	public Pickup(float x, float y, int atlasX, int atlasY, int points) {
		super(new TextureRegion(SteveDriver.atlas, atlasX, atlasY, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE));
		this.setPosition(x * SteveDriver.TEXTURE_SIZE, y * SteveDriver.TEXTURE_SIZE);
		active = true;
		this.points = points;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void consume(Snake snake) {
		active = false;
		snake.addMoney(points); //TODO: Temp
	}
	
	public Rectangle getRectangle() {
		return this.getBoundingRectangle();
	}
	
	public float getAlpha() {
		return this.alphaMod;
	}
	
	
	public void update() {
		lifeTimer -= Gdx.app.getGraphics().getDeltaTime();
		if (lifeTimer <= 0) {
			SteveDriver.field.pickupsToRemove.add(this);
		} else {
			this.alphaMod = lifeTimer / lifeTime;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float alpha) {
		if (lifeTimer != 0) {
			super.draw(batch, alphaMod);
		}
	}
}