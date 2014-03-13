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
	int grassRadius;
	int desertRadius;
	int barrenRadius;
	int totalRadius;
	int blockerChains;
	int maxBlockerLength;
	
	public static TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	Texture tiles;
	TileRegion grass, desert, barren;
	
	public static ArrayList<Pickup> pickups;
	
	private class TileRegion {
		int startX, startY, width, length;
		Random rand;
		
		public TileRegion(int startX, int startY, int width, int length) {
			this.startX = startX;
			this.startY = startY;
			this.width = width;
			this.length = length;
			rand = new Random();
		}
		
		public int GetRandomX() {
			return this.rand.nextInt(this.width + 1) + this.startX;
		}
		
		public int GetRandomY() {
			return this.rand.nextInt(this.length + 1) + this.startY;
		}
		
	}
	
	//TODO:Define "circles"

	public Field(OrthographicCamera camera) {
		this.grassRadius = 20;
		this.desertRadius = 10;
		this.barrenRadius = 150;
		
		this.totalRadius = 60;
		this.blockerChains = 50;
		this.maxBlockerLength = 10;
		
		this.grass = new TileRegion(6, 4, 1, 2);
		this.desert = new TileRegion(6, 7, 1, 2);
		this.barren = new TileRegion(6, 10, 1, 2);
		
		this.tiles = new Texture(Gdx.files.internal("data/SpriteAtlas.png"));
		this.map = new TiledMap();
		
		this.RandomizeField();
		
		this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, 1);
		this.mapRenderer.setView(camera);
		
		this.pickups = new ArrayList<Pickup>();
		this.pickups.add(new Apple(0, 0));
		this.pickups.add(new Apple(5, 5));
	}
	
	public void RandomizeField() {
		TextureRegion[][] splitTiles = TextureRegion.split(this.tiles, SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH);
		MapLayers layers = this.map.getLayers();
		TiledMapTileLayer layer = new TiledMapTileLayer(this.totalRadius, this.totalRadius, SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH);
		TiledMapTileLayer blockers = new TiledMapTileLayer(this.totalRadius, this.totalRadius, SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH);
		
		//This is the Background generation	
		//Barren tiles generated
		for (int x = 0; x < this.totalRadius; x++) {
			for (int y = 0; y < this.totalRadius; y++) {
				int ty = this.barren.GetRandomY();
				int tx = this.barren.GetRandomX();
				Cell cell = new Cell();
				cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
				layer.setCell(x, y, cell);
			}
		}
		
		//Desert tiles generated
		for (int x = this.desertRadius; x < this.totalRadius - this.desertRadius; x++) {
			for (int y = this.desertRadius; y < this.totalRadius - this.desertRadius; y++) {
				int ty = this.desert.GetRandomY();
				int tx = this.desert.GetRandomX();
				Cell cell = new Cell();
				cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
				layer.setCell(x, y, cell);
			}
		}
		
		//Grass tiles generated
		for (int x = this.grassRadius; x < this.totalRadius - this.grassRadius; x++) {
			for (int y = this.grassRadius; y < this.totalRadius - this.grassRadius; y++) {
				int ty = this.grass.GetRandomY();
				int tx = this.grass.GetRandomX();
				Cell cell = new Cell();
				cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
				layer.setCell(x, y, cell);
			}
		}
		
		int randX, randY;
		for (int i = 0; i < this.blockerChains; i++) {
			randX = SteveDriver.random.nextInt(totalRadius);
			randY = SteveDriver.random.nextInt(totalRadius);
			
			Cell cell = new Cell();
			cell.setTile(new StaticTiledMapTile(splitTiles[5][1]));
			
			int dx = 0;
			int dy = 0;
			
			for (int j = 0; j < this.maxBlockerLength && SteveDriver.random.nextFloat() > .4f; j++) {
				float nextX = SteveDriver.random.nextFloat();
				float nextY = SteveDriver.random.nextFloat();
				
				if (nextX < .33f) {
					dx = -1;
				} else if (nextX > .66f) {
					dx = 1;
				} else {
					dx = 0;
				}
				
				if (nextY < .33f && dx == 0) {
					dy = -1;
				} else if (nextY > .66f && dx == 0) {
					dy = 1;
				} else {
					dy = 0;
				}
				
				randX = randX + dx;
				randY = randY + dy;
				
				blockers.setCell(randX, randY, cell);
				blockers.setCell(randX+1, randY+1, cell);
				blockers.setCell(randX+1, randY, cell);
				blockers.setCell(randX, randY+1, cell);
			}
		}
		
		layers.add(layer);
		layers.add(blockers);
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
}