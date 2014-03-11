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
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	Texture tiles;
	TileRegion grass, desert, barren;
	Random random;
	
	ArrayList<Pickup> pickups;
	
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
	
	private class CellContainer {
		private TextureRegion[][] tileMap;
		
		public Cell topRight;
		public Cell topLeft;
		public Cell bottomRight;
		public Cell bottomLeft;
		public Cell innerTopRight;
		public Cell innerTopLeft;
		public Cell innerBottomRight;
		public Cell innerBottomLeft;
		
		public Cell leftA;
		public Cell leftB;
		
		public Cell rightA;
		public Cell rightB;
		
		public Cell topA;
		public Cell topB;
		
		public Cell bottomA;
		public Cell bottomB;
		
		public CellContainer(int x, int y, TextureRegion[][] tileMap) {
			this.tileMap = tileMap;
			
			topLeft = new Cell();
			topLeft.setTile(new StaticTiledMapTile(this.tileMap[y][x]));
			
			topRight = new Cell();
			topRight.setTile(new StaticTiledMapTile(this.tileMap[y][x + 2]));
			
			bottomLeft = new Cell();
			bottomLeft.setTile(new StaticTiledMapTile(this.tileMap[y + 2][x]));
			
			bottomRight = new Cell();
			bottomRight.setTile(new StaticTiledMapTile(this.tileMap[y + 2][x + 2]));
			
			innerTopLeft = new Cell();
			innerTopLeft.setTile(new StaticTiledMapTile(this.tileMap[y][x + 3]));
			
			innerTopRight = new Cell();
			innerTopRight.setTile(new StaticTiledMapTile(this.tileMap[y][x + 5]));
			
			innerBottomLeft = new Cell();
			innerBottomLeft.setTile(new StaticTiledMapTile(this.tileMap[y + 2][x]));
			
			innerBottomRight = new Cell();
			innerBottomRight.setTile(new StaticTiledMapTile(this.tileMap[y + 2][x + 5]));
		}
	}

	public Field(OrthographicCamera camera) {
		this.grassRadius = 20;
		this.desertRadius = 10;
		this.barrenRadius = 150;
		
		this.totalRadius = 60;
		this.blockerChains = 50;
		this.maxBlockerLength = 10;
		
		this.random = new Random();
		
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
			randX = random.nextInt(totalRadius);
			randY = random.nextInt(totalRadius);
			
			Cell cell = new Cell();
			cell.setTile(new StaticTiledMapTile(splitTiles[5][1]));
			
			int dx = 0;
			int dy = 0;
			
			for (int j = 0; j < this.maxBlockerLength && random.nextFloat() > .4f; j++) {
				float nextX = random.nextFloat();
				float nextY = random.nextFloat();
				
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
		
		for (int x = 0; x < this.totalRadius; x++) {
			for (int y = 0; y < this.totalRadius; y++) {
				if(blockers.getCell(x, y) != null) {
					
				}
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
	
	public ArrayList<Pickup> getPickups() {
		return pickups;
	}
	
	public TiledMap getTiles() {
		return map;
	}
}