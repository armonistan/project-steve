package com.steve;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SteveDriver implements ApplicationListener {
	public static Texture atlas;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Snake snake;
	private Sprite temp;
	private Field map;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		
		atlas = new Texture(Gdx.files.internal("data/SpriteAtlas.png"));
		atlas.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		snake = new Snake();
		temp = new Sprite(new TextureRegion(atlas, 16, 0, 16, 16));
		temp.setPosition(16,  16);
		
		map = new Field(camera);
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
		
		camera.position.x = snake.getHead().getOriginX() + snake.getHead().getX();
		camera.position.y = snake.getHead().getOriginY() + snake.getHead().getY();
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		map.render(camera);
		
		batch.begin();
		temp.draw(batch);
		snake.render(batch, deltaTime);
		batch.end();
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
