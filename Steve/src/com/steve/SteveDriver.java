package com.steve;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.steve.helpers.ConstantHelper;
import com.steve.helpers.GUIHelper;
import com.steve.stages.Credits;
import com.steve.stages.EndGame;
import com.steve.stages.Field;
import com.steve.stages.Game;
import com.steve.stages.Loading;
import com.steve.stages.Logo;
import com.steve.stages.Menu;
import com.steve.stages.Store;
import com.steve.stages.Summary;

public class SteveDriver implements ApplicationListener {
	public static Texture atlas;
	public static Texture background;
	public static Texture steveLogo;
	public static Texture space;
	public static Texture emberware;
	public static AssetManager assets;
	
	public static TiledMap demoMap;
	
	public static Snake snake;
	public static Field field;
	public static Random random;
	public static GUIHelper guiHelper;
	public static ConstantHelper constants;
	
	public static Store store;
	public static Summary summary;
	
	public static Preferences prefs;
	public static Preferences storePrefs;
	
	public static final int TEXTURE_SIZE = 32;
	public static int snakeTierWeaponDamageModifier = 10;
	
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
	public static Credits credits;
	public static Logo logo;
	public static Loading loading;
	public static EndGame endGame;
	
	public static int mainThemeIndex = 0;
	public static int spaceThemeIndex = 1;
	
	public static boolean tutorialOn = false;; 
	
	public static boolean carrierDefeated = false;
	public static boolean razorbullDefeated = false;
	public static boolean cyborgBossActivate = false;
	public static boolean robotBossActivate = false;

	public static int numBosses = 2;
	
	private static Music music;
	private static Music mainMusic;
	private static Music spaceMusic;
	
	public static boolean musicPlaying;
	public static IActivityRequestHandler handler;
	
	public static enum STAGE_TYPE {
		MENU,
		CREDITS,
		GAME,
		ENDGAME,
		RESPAWNINGENDGAME,
		PAUSEDENDGAME,
		STORE,
		RESPAWNING,
		SUMMARY,
		PAUSED,
		LOGO,
		LOADING
	}
	
	public SteveDriver(IActivityRequestHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		float verticalSize = 25 * TEXTURE_SIZE;
		float guiVerticalSize = 30 * TEXTURE_SIZE;
		
		camera = new OrthographicCamera(verticalSize * w / h, verticalSize);
		guiCamera = new OrthographicCamera(guiVerticalSize * w / h, guiVerticalSize);
		
		batch = new SpriteBatch();
		random = new Random();
		
		//TODO: Change names
		prefs = Gdx.app.getPreferences("main");
		storePrefs = Gdx.app.getPreferences("store");
		
		constants = new ConstantHelper();
		constants.addToConstants("screenWidth", SteveDriver.guiCamera.viewportWidth);
		constants.addToConstants("screenHeight", SteveDriver.guiCamera.viewportHeight);

		stage = STAGE_TYPE.LOGO;
		
		assets = new AssetManager();
		
		TextureParameter params = new TextureParameter();
		params.magFilter = TextureFilter.MipMapLinearNearest;
		params.minFilter = TextureFilter.Nearest;
		
		//Asset load calls
		assets.load("data/emberware.png", Texture.class, params);
		assets.load("data/SpriteAtlasDouble.png", Texture.class, params);
		assets.load("data/teset-1.png", Texture.class, params);
		assets.load("data/Steve-title.png", Texture.class, params);
		assets.load("data/Space Background.png", Texture.class, params);
		assets.load("data/loading.png", Texture.class, params);
		assets.load("data/nuke.png", Texture.class, params);
		assets.load("data/diedEnemy.png", Texture.class);      
		assets.load("data/diedStarvation.png", Texture.class); 
		assets.load("data/diedBlocker.png", Texture.class);    
		assets.load("data/diedSpace.png", Texture.class);      
		assets.load("data/diedPlayer.png", Texture.class);     
		assets.load("data/diedBackground.png", Texture.class); 
		assets.load("data/stars.png", Texture.class);
		assets.load("data/victoryStars.png", Texture.class);
		assets.load("data/victoryWords.png", Texture.class);
		assets.load("data/space.png", Texture.class);
		
		assets.load("audio/MainV1.ogg", Music.class);
		assets.load("audio/MainSpace.ogg", Music.class);
		
		assets.load("audio/eatApple1.ogg", Sound.class);
		assets.load("audio/eatApple2.ogg", Sound.class);
		assets.load("audio/eatApple3.ogg", Sound.class);
		assets.load("audio/bossSummon.ogg", Sound.class);
		assets.load("audio/enemyDeath.ogg", Sound.class);
		assets.load("audio/gatlingGun1.ogg", Sound.class);
		assets.load("audio/gatlingGun2.ogg", Sound.class);
		assets.load("audio/gatlingGun3.ogg", Sound.class);
		assets.load("audio/pickupGatlingGun1.ogg", Sound.class);
		assets.load("audio/pulseLaser1.ogg", Sound.class);
		assets.load("audio/pulseLaser2.ogg", Sound.class);
		assets.load("audio/pulseLaser3.ogg", Sound.class);
		assets.load("audio/pickupLaser1.ogg", Sound.class);
		assets.load("audio/mainCannon1.ogg", Sound.class);
		assets.load("audio/mainCannon2.ogg", Sound.class);
		assets.load("audio/mainCannon3.ogg", Sound.class);
		assets.load("audio/blockerCollide.ogg", Sound.class);
		assets.load("audio/segmentLoss2.ogg", Sound.class);
		assets.load("audio/specialist1.ogg", Sound.class);
		assets.load("audio/specialist2.ogg", Sound.class);
		assets.load("audio/specialist3.ogg", Sound.class);
		assets.load("audio/specialistPickup.ogg", Sound.class);
		assets.load("audio/storePurchase.ogg", Sound.class);
		assets.load("audio/treasurePickup.ogg", Sound.class);
		assets.load("audio/weaponUpgrade.ogg", Sound.class);
		
		assets.load("fonts/fixedsys48.fnt", BitmapFont.class);
		assets.load("fonts/fixedsys32.fnt", BitmapFont.class);
		
		demoMap = new TmxMapLoader().load("menu.tmx");
		
		logo = new Logo();
	}

	protected void finishLoading() {
		atlas = assets.get("data/SpriteAtlasDouble.png", Texture.class);
		background = assets.get("data/teset-1.png", Texture.class);
		steveLogo = assets.get("data/Steve-title.png", Texture.class);
		space = assets.get("data/Space Background.png", Texture.class);
		
		mainMusic = assets.get("audio/MainV1.ogg", Music.class);
		spaceMusic = assets.get("audio/MainSpace.ogg", Music.class);
		music = mainMusic;
		
		guiHelper = new GUIHelper();
		
		//TODO: Make this better.
		gui = new GUI();
		snake = new Snake(30, 30);
		
		menu = new Menu();
		game = new Game();
		tutorial = new Tutorial();
		store = new Store();
		credits = new Credits();
		loading = new Loading(STAGE_TYPE.GAME);
		endGame = new EndGame();
		
		summary = new Summary();
	}

	@Override
	public void dispose() {
		batch.dispose();
		atlas.dispose();
	}

	@Override
	public void render() {
		if (emberware == null && assets.isLoaded("data/emberware.png")) {
			emberware = assets.get("data/emberware.png", Texture.class);
		}
		else if (menu == null || summary == null) {
			if (assets.update()) {
				finishLoading();
			}
		}
		else {
			if (!prefs.getBoolean("music")) {
				music.stop();
				musicPlaying = false;
			}
			else if (prefs.getBoolean("music") && !musicPlaying) {
				music.setLooping(true);
				music.setVolume(.6f);
				music.play();
				musicPlaying = true;
			}
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		switch (stage) {
		case CREDITS:
			credits.render();
			break;
		case MENU:
			summary.resetSummary();
			menu.render();
			break;
		case RESPAWNING:
			resetTheme();
			store.saveStoreProgress();
			resetField();
			summary.resetSummary();
			stage = STAGE_TYPE.LOADING;
			break;
		case RESPAWNINGENDGAME:
			setSpaceTheme();
			store.saveStoreProgress();
			resetFieldForSpace();
			summary.resetSummary();
			stage = STAGE_TYPE.LOADING;
			break;
		case SUMMARY:
			summary.render();
			break;
		case STORE:
			store.render();
			break;
		case GAME:
			game.render();
			break;
		case PAUSED:
			game.renderPaused();
			break;
		case PAUSEDENDGAME:
			endGame.renderPaused();
			break;
		case LOGO:
			logo.render();
			break;
		case LOADING:
			loading.render();
			break;
		case ENDGAME:
			endGame.render();
			break;
		}

		if (tutorial != null && tutorial.isActive()) {
			tutorial.render();
		}
		
		if (summary != null) {
			if (handler != null && summary.showingAds && stage != STAGE_TYPE.SUMMARY) {
				summary.showingAds = false;
				handler.showAds(false);
			}
			else if (handler != null && !summary.showingAds && stage == STAGE_TYPE.SUMMARY) {
				summary.showingAds = true;
				handler.showAds(true);
			}
		}
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
		loading = new Loading(STAGE_TYPE.GAME);
	}
	
	public static void resetFieldForSpace(){
		int scale = 5;
		
		snake = new AstroSteve(30 * scale, 30 * scale);
		field = new Field(camera, scale);
		loading = new Loading(STAGE_TYPE.ENDGAME);	
		endGame.reset();
	}
	
	public static void setSpaceTheme() {
		if (music != spaceMusic) {
			music.stop();
			music = spaceMusic;
			
			if (musicPlaying) {
				music.play();
			}
		}
	}
	
	public static void resetTheme() {
		if (music != mainMusic) {
			music.stop();
			music = mainMusic;
			
			if (musicPlaying) {
				music.play();
			}
		}
	}
	
	public static void playSound(Sound uh) {
		if(SteveDriver.prefs.getBoolean("sfx", false))
			uh.play();
	}
}