package com.steve;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.steve.helpers.GUIHelper;
import com.steve.stages.Field;
import com.steve.stages.Game;
import com.steve.stages.Menu;

public class SteveDriver implements ApplicationListener {
	public static Texture atlas;
	public static Snake snake;
	public static Field field;
	public static Random random;
	public static GUIHelper guiHelper;
	
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
	
	public static OrthographicCamera camera;
	public static OrthographicCamera guiCamera;
	public static SpriteBatch batch;
	public static GUI gui;
	
	public static STAGE_TYPE stage;
	public static Menu menu;
	public static Game game;
	
	public static enum STAGE_TYPE {
		MENU,
		GAME,
		STORE,
		PAUSED
	}
	
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
		
		guiHelper = new GUIHelper();
		
		//TODO: Make this better.
		resetField();
		gui = new GUI();
		
		stage = STAGE_TYPE.GAME;
		menu = new Menu();
		game = new Game();
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
			menu.render();
			break;
		case STORE:
			guiCamera.position.x = 0;
			guiCamera.position.y = 0;
			guiCamera.update();
			
			batch.setProjectionMatrix(guiCamera.combined);
			
			batch.begin();
			guiHelper.drawBox(-342, 6, 4, 43);
			guiHelper.drawText("This is the store. Hit 3 to get back into the game, yeah.", -330, 0, Color.BLACK);
			batch.end();
			
			if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
				stage = STAGE_TYPE.GAME;
			}
			break;
		case GAME:
			game.render(deltaTime);
			break;
		case PAUSED:
			game.renderPaused();
			break;
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		if (stage == STAGE_TYPE.GAME) {
			stage = STAGE_TYPE.PAUSED;
		}
	}

	@Override
	public void resume() {
		if (stage == STAGE_TYPE.PAUSED) {
			stage = STAGE_TYPE.GAME;
		}
	}
	
	public static void resetField() {
		snake = new Snake(30, 30);
		field = new Field(camera);
	}
}