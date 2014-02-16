package com.steve;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import java.util.*;

public class Field {
	int length;
	int width;
	int t_length;
	int t_width;
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	Texture tiles;
	
	ArrayList<Pickup> pickups;
	
	//TODO:Define "circles"

	public Field(OrthographicCamera camera) {
		length = 500;
		width = 500;
		
		t_length = 16;
		t_width = 16;
		Random rand = new Random();
		
		int grass_y = 4;
		int grass_x = 6;
		int grass_width = 1;
		int grass_height = 2;
		
		//map = new TmxMapLoader().load("data/levels/grass.tmx");
		tiles = new Texture(Gdx.files.internal("data/SpriteAtlas.png"));
		TextureRegion[][] splitTiles = TextureRegion.split(tiles, t_length, t_width);
		map = new TiledMap();
		MapLayers layers = map.getLayers();
		TiledMapTileLayer layer = new TiledMapTileLayer(length, width, t_length, t_width);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < length; y++) {
				int ty = rand.nextInt(grass_height + 1) + grass_y;
				int tx = rand.nextInt(grass_width + 1) + grass_x;
				Cell cell = new Cell();
				cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
				layer.setCell(x, y, cell);
			}
		}
		
		layers.add(layer);
		
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1);
		mapRenderer.setView(camera);
		
		pickups = new ArrayList<Pickup>();
		pickups.add(new Apple(0, 0));
		pickups.add(new Apple(5, 5));
		pickups.add(new Apple(0, 5));
		pickups.add(new Apple(5, 0));
	}
	
	public void render(OrthographicCamera camera, SpriteBatch batch) {
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		batch.begin();
		for (Pickup p : pickups) {
			if (p.getActive()) {
				p.draw(batch);
			}
		}
		batch.end();
	}
	
	public ArrayList<Pickup> getPickups() {
		return pickups;
	}
}