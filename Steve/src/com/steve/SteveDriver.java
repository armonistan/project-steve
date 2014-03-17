package com.steve;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class SteveDriver implements ApplicationListener {
	public static Texture atlas;
	public static Snake snake;
	public static Field field;
	public static Random random;
	public static final int TEXTURE_WIDTH = 16;
	public static final int TEXTURE_LENGTH = 16;
	public static final int BIG_TEXTURE_WIDTH = 32;
	public static final int BIG_TEXTURE_LENGTH = 32;
	
	
	private OrthographicCamera camera;
	private OrthographicCamera guiCamera;
	private SpriteBatch batch;
	private GUI gui;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		guiCamera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		
		random = new Random();
		
		atlas = new Texture(Gdx.files.internal("data/SpriteAtlas.png"));
		atlas.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		snake = new Snake();
		
		field = new Field(camera);
		gui = new GUI();
	}

	@Override
	public void dispose() {
		batch.dispose();
		atlas.dispose();
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getRawDeltaTime();
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Vector3 test = camera.position.lerp(snake.getHeadPosition(), 0.01f);
		
		camera.position.x = test.x;
		camera.position.y = test.y;
		camera.update();
		
		guiCamera.position.x = 0;
		guiCamera.position.y = 0;
		guiCamera.update();
		
		batch.setProjectionMatrix(camera.combined);
		field.render(camera, batch);
		
		batch.begin();
		snake.render(batch, deltaTime);
		batch.end();
		
		batch.setProjectionMatrix(guiCamera.combined);
		gui.render(batch);
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