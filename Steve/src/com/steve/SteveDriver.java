package com.steve;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.helpers.ConstantHelper;
import com.steve.helpers.GUIHelper;
import com.steve.stages.Field;
import com.steve.stages.Game;
import com.steve.stages.Menu;
import com.steve.stages.Store;
import com.steve.stages.Summary;

public class SteveDriver implements ApplicationListener {
	public static Texture atlas;
	public static Texture background;
	public static Snake snake;
	public static Field field;
	public static Random random;
	public static GUIHelper guiHelper;
	public static ConstantHelper constants;
	public static Store store;
	public static Summary summary;
	public static Preferences prefs;
	public static Preferences storePrefs;
	
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
	public static Tutorial tutorial;
	
	public static STAGE_TYPE stage;
	public static Menu menu;
	public static Game game;
	
	public static boolean tutorialOn = false;; 
	
	private Sound music;
	private FPSLogger fpsLogger;
	
	public static enum STAGE_TYPE {
		MENU,
		GAME,
		STORE,
		RESPAWNING,
		SUMMARY,
		PAUSED
	}
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		float verticalSize = 30 * TEXTURE_WIDTH;
		
		camera = new OrthographicCamera(verticalSize * w / h, verticalSize);
		guiCamera = new OrthographicCamera(verticalSize * w / h, verticalSize);
		
		batch = new SpriteBatch();
		random = new Random();
		
		prefs = Gdx.app.getPreferences("main");
		storePrefs = Gdx.app.getPreferences("store");
		
		constants = new ConstantHelper();
		constants.addToConstants("screenWidth", SteveDriver.guiCamera.viewportWidth);
		constants.addToConstants("screenHeight", SteveDriver.guiCamera.viewportHeight);
		
		atlas = new Texture(Gdx.files.internal("data/SpriteAtlas.png"));
		atlas.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Texture(Gdx.files.internal("data/teset-1.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		guiHelper = new GUIHelper();
		
		//TODO: Make this better.
		gui = new GUI();
		snake = new Snake(30, 30);
		
		stage = STAGE_TYPE.MENU;
		menu = new Menu();
		game = new Game();
		tutorial = new Tutorial();
		store = new Store();
		store.setStoreProgress();
		
		summary = new Summary();
		
		music = Gdx.audio.newSound(Gdx.files.internal("audio/MainV1.wav"));
		music.loop();
		
		fpsLogger = new FPSLogger();
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
		case RESPAWNING:
			resetField();
			summary.resetSummary();
			stage = STAGE_TYPE.GAME;
			break;
		case SUMMARY:
			batch.begin();
			summary.render();
			batch.end();
			break;
		case STORE:
			guiCamera.position.x = 0;
			guiCamera.position.y = 0;
			guiCamera.update();
			
			batch.setProjectionMatrix(guiCamera.combined);
			
			batch.begin();
			store.render();
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

		if (tutorial.isActive()) {
			tutorial.render();
		}
		
		//fpsLogger.log();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		if (stage == STAGE_TYPE.GAME && !tutorial.isActive()) {
			stage = STAGE_TYPE.PAUSED;
		}
	}

	@Override
	public void resume() {
	}
	
	public static void resetField() {
		int scale = 5;
		
		snake = new Snake(30 * scale, 30 * scale);
		field = new Field(camera, scale);
		
		for (int i = 0; i < 100; i++) {
			if(field.generator.generateAppleTutorial())
				break;
		}
		
		for (int i = 0; i < 100; i++) {
			if(field.generator.generatePickUpTutorial(1))
				break;
		}
/*To Do: add to tutorial about weapon upgrades		
		for (int i = 0; i < 100; i++) {
			if(field.generator.generatePickUpTutorial(2))
				break;
		}
*/	
		for (int i = 0; i < 100; i++) {
			if(field.generator.generateEnemyTutorial())
				break;
		}
		
		//TODO: TEMP
		if (SteveDriver.snake.getMoney() == 0 && !SteveDriver.tutorial.isActive()) {
			SteveDriver.tutorial.startTutorial();
		}
	}
}