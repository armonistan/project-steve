package com.steve;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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

	public static final float RIGHT = MathUtils.PI * 3f / 2f * MathUtils.radiansToDegrees;
	public static final float UP = 0;
	public static final float LEFT = MathUtils.PI / 2f * MathUtils.radiansToDegrees;
	public static final float DOWN = MathUtils.PI * MathUtils.radiansToDegrees;
	public static final Vector2 VRIGHT = new Vector2(1, 0);
	public static final Vector2 VUP = new Vector2(0, 1);
	public static final Vector2 VLEFT = new Vector2(-1, 0);
	public static final Vector2 VDOWN = new Vector2(0, -1);
	
	public static final int RIGHT_ID = 0;
	public static final int UP_ID = 1;
	public static final int LEFT_ID = 2;
	public static final int DOWN_ID = 3;
	
	private static OrthographicCamera camera;
	private static OrthographicCamera guiCamera;
	private static SpriteBatch batch;
	private static GUI gui;
	
	private static int stage;
	private static final int MENU = 0;
	private static final int GAME = 1;
	
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
		
		//TODO: Make this better.
		resetField();
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
		
		switch (stage) {
		case MENU:
			if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
				stage = GAME;
			}
			break;
		case GAME:
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
			
			//TODO: Temp
			if (Gdx.input.isKeyPressed(Keys.SPACE)) {
				resetField();
			}
			
			if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
				stage = MENU;
			}
			break;
		}
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
	
	public static void resetField() {
		snake = new Snake(30, 30);
		field = new Field(camera);
	}
}