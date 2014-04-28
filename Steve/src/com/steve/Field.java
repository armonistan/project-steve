package com.steve;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class Field {
	int grassRadius;
	int desertRadius;
	int barrenRadius;
	int totalRadius;
	int blockerChains;
	int maxBlockerLength;
	
	float pickUpTimer = 0;
	
	public static TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	Texture tiles;
	TileRegion grass, desert, barren;
	
	public static ArrayList<PickUp> pickups;
	
	public ArrayList<Enemy> enemies;
	public LinkedList<Enemy> enemiesToRemove;
	
	public ArrayList<Projectile> projectiles;
	public LinkedList<Projectile> projectilesToRemove;
	
	private Generator generator;
	
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
		
		public Cell middle;
		
		public Cell leftA;
		public Cell leftB;
		
		public Cell rightA;
		public Cell rightB;
		
		public Cell topA;
		public Cell topB;
		
		public Cell bottomA;
		public Cell bottomB;
		
		Random rand;
		
		public CellContainer(int x, int y, TextureRegion[][] tileMap, Random rand) {
			this.tileMap = tileMap;
			this.rand = rand;
			
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
			
			middle = new Cell();
			middle.setTile(new StaticTiledMapTile(this.tileMap[y + 1][x + 1]));
			
			leftA = new Cell();
			leftA.setTile(new StaticTiledMapTile(this.tileMap[y + 1][x]));
			
			leftB = new Cell();
			leftB.setTile(new StaticTiledMapTile(this.tileMap[y + 1][x + 5]));
			
			rightA = new Cell();
			rightA.setTile(new StaticTiledMapTile(this.tileMap[y + 1][x + 2]));
			
			rightB = new Cell();
			rightB.setTile(new StaticTiledMapTile(this.tileMap[y + 1][x + 3]));
			
			topA = new Cell();
			topA.setTile(new StaticTiledMapTile(this.tileMap[y][x + 1]));
			
			topB = new Cell();
			topB.setTile(new StaticTiledMapTile(this.tileMap[y + 2][x + 4]));
			
			bottomA = new Cell();
			bottomA.setTile(new StaticTiledMapTile(this.tileMap[y + 2][x + 1]));
			
			bottomB = new Cell();
			bottomB.setTile(new StaticTiledMapTile(this.tileMap[y + 2][x + 4]));
		}
		
		public Cell top() {
			return rand.nextFloat() > .5 ? this.topA : this.topB; 
		}
		
		public Cell bottom() {
			return rand.nextFloat() > .5 ? this.bottomA : this.bottomB; 
		}
		
		public Cell left() {
			return rand.nextFloat() > .5 ? this.leftA : this.leftB; 
		}
		
		public Cell right() {
			return rand.nextFloat() > .5 ? this.rightA : this.rightB; 
		}
	}
	
	public Field(OrthographicCamera camera) {
		this.grassRadius = 20;
		this.desertRadius = 10;
		this.barrenRadius = 60;
		
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
		
		this.pickups = new ArrayList<PickUp>();
		this.pickups.add(new GatlingGunPickUp(30,35));
		this.pickups.add(new SpecialistPickUp(29,35));
		this.pickups.add(new LaserPickUp(28,35));
		this.pickups.add(new Apple(33, 33));
		this.pickups.add(new Apple(34, 34));
		this.pickups.add(new Apple(35, 35));
		this.pickups.add(new Apple(36, 36));
		this.pickups.add(new Apple(37, 37));
		
		this.pickups.add(new Apple(38, 38));
		this.pickups.add(new Apple(39, 39));
		this.pickups.add(new Apple(40, 40));
		this.pickups.add(new Apple(41, 41));
		this.pickups.add(new WeaponUpgrade(42, 42));
		
		this.enemies = new ArrayList<Enemy>();
		this.enemiesToRemove = new LinkedList<Enemy>();
		enemies.add(new Snail(50, 30));
		enemies.add(new Ring(20, 30));
		enemies.add(new Brute(40, 30));
		enemies.add(new Tank(30, 20));
		
		this.projectiles = new ArrayList<Projectile>();
		this.projectilesToRemove =  new LinkedList<Projectile>();
		this.generator = new Generator();
	}
	
	public int checkRing(int x, int y) {
		if (x >= this.grassRadius && x < this.totalRadius - this.grassRadius && y >= this.grassRadius && y < this.totalRadius - this.grassRadius) {
			return 0;
		} else if (x >= this.desertRadius && x < this.totalRadius - this.desertRadius && y >= this.desertRadius && y < this.totalRadius - this.desertRadius) {
			return 1;
		} else {
			return 2;
		}
	}
	
	public void RandomizeField() {
		TextureRegion[][] splitTiles = TextureRegion.split(this.tiles, SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH);
		MapLayers layers = this.map.getLayers();
		TiledMapTileLayer layer = new TiledMapTileLayer(this.totalRadius, this.totalRadius, SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH);
		TiledMapTileLayer blockers = new TiledMapTileLayer(this.totalRadius, this.totalRadius, SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH);
		ArrayList<CellContainer> blockerTiles = new ArrayList<CellContainer>();
		blockerTiles.add(new CellContainer(0, 4, splitTiles, SteveDriver.random));
		blockerTiles.add(new CellContainer(0, 7, splitTiles, SteveDriver.random));
		blockerTiles.add(new CellContainer(0, 10, splitTiles, SteveDriver.random));
		
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
		//this code sets up the positions for the blockers on the grid
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
				
				if(this.checkRing(randX, randY) == this.checkRing(randX+1, randY+1)){
					//ensures that there is always a tileable set of blockers
					blockers.setCell(randX, randY, cell);
					blockers.setCell(randX+1, randY+1, cell);
					blockers.setCell(randX+1, randY, cell);
					blockers.setCell(randX, randY+1, cell);
				}
			}
		}
		boolean left, right, top, bottom;
		int tileRad = 0;
		//code to set the blockers to the correct images
		for (int x = 0; x < this.totalRadius; x++) {
			for (int y = 0; y < this.totalRadius; y++) {
				if(blockers.getCell(x, y) != null) {
					tileRad = this.checkRing(x, y);
					left = (blockers.getCell(x-1, y) == null) || (tileRad != this.checkRing(x - 1, y));
					right = (blockers.getCell(x + 1, y) == null) || (tileRad != this.checkRing(x + 1, y));
					top = (blockers.getCell(x, y + 1) == null) || (tileRad != this.checkRing(x, y + 1));
					bottom = (blockers.getCell(x, y - 1) == null) || (tileRad != this.checkRing(x, y - 1));
					
					blockers.setCell(x, y, blockerTiles.get(tileRad).middle);
					//set the actual tile image
					if (left) {
						if (top) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).topLeft);
						} else if (bottom) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).bottomLeft);
						} else {
							blockers.setCell(x, y, blockerTiles.get(tileRad).left());
						}
					} else if (right) {
						if (top) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).topRight);
						} else if (bottom) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).bottomRight);
						} else {
							blockers.setCell(x, y, blockerTiles.get(tileRad).right());
						}
					} else if (bottom) {
						if (left) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).bottomLeft);
						} else if (right) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).bottomRight);
						} else {
							blockers.setCell(x, y, blockerTiles.get(tileRad).bottomA);
						}
					} else if (top) {
						if (left) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).topLeft);
						} else if (right) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).topRight);
						} else {
							blockers.setCell(x, y, blockerTiles.get(tileRad).top());
						}
					}
				}
			}
		}
		
		layers.add(layer);
		layers.add(blockers);
	}
	
	public void render(OrthographicCamera camera, SpriteBatch batch) {
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		this.generator.update();
		
		batch.begin();
		for (PickUp p : pickups) {
			if (p.getActive()) {
				p.draw(batch);
			}
		}
		
		for (Enemy e : enemies) {
			e.render(batch);
		}
		for (Enemy e : enemiesToRemove) {
			enemies.remove(e);
		}
		enemiesToRemove.clear();
		
		for (Projectile p : projectiles) {
			p.render(batch);
		}
		for (Projectile p : projectilesToRemove) {
			projectiles.remove(p);
		}
		projectilesToRemove.clear();
		batch.end();
	}
	
	public boolean isOccupied(Rectangle newObject){
		for(Enemy e : enemies){
			if(CollisionHelper.isCollide(newObject, e.getRectangle())){
				return false;
			}
		}
		for(Sprite s : SteveDriver.snake.getSnakeSprites()){
			if(CollisionHelper.isCollide(newObject, s.getBoundingRectangle())){
				return false;
			}
		}
		return true;
	}
	
	public int getFieldRadius(){
		return this.totalRadius;
	}
}