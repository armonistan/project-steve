package com.steve.stages;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.base.Pickup;
import com.steve.base.Projectile;
import com.steve.enemies.AntiSpiral;
import com.steve.enemies.Brute;
import com.steve.enemies.Flyer;
import com.steve.enemies.HomaHawk;
import com.steve.enemies.Ring;
import com.steve.enemies.Snail;
import com.steve.enemies.Spiral;
import com.steve.enemies.Tank;
import com.steve.enemies.Turret;
import com.steve.helpers.CollisionHelper;
import com.steve.helpers.Generator;
import com.steve.pickups.Apple;
import com.steve.pickups.GatlingGunPickUp;
import com.steve.pickups.LaserPickUp;
import com.steve.pickups.SpecialistPickUp;
import com.steve.pickups.WeaponUpgrade;

import java.util.*;

public class Field {
	public int grassRadius;
	public int desertRadius;
	public int barrenRadius;
	public int totalRadius;
	int blockerChains;
	int maxBlockerLength;
	
	float pickUpTimer = 0;
	
	public static TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	Texture tiles;
	TileRegion grass, desert, barren;
	
	public static LinkedList<Pickup> pickups;
	public static LinkedList<Pickup> pickupsToRemove;
	
	public ArrayList<Enemy> enemies;
	public LinkedList<Enemy> enemiesToRemove;
	
	public ArrayList<Projectile> projectiles;
	public LinkedList<Projectile> projectilesToRemove;
	
	public Generator generator;
	
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
		
		public int getCornerX(boolean isLeft){
			return (isLeft) ? this.startX+2 : this.startX+4;
		}
		
		public int getSideX(int position){
			return (position == 1) ? this.startX+2 : 
				(position == 2) ? this.startX+3 :
					this.startX+4;
		}

		public int getCornerY(boolean isBot){
			return (isBot) ? this.startY+5 : this.startY+3;
		}
		
		public int getSideY(int position){
			return (position == 1) ? this.startY+3 : 
				(position == 2) ? this.startY+4 :
					this.startY+5;}
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
			innerBottomLeft.setTile(new StaticTiledMapTile(this.tileMap[y + 2][x + 3]));
			
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
	
	public Field(OrthographicCamera camera, int scale) {
		this.grassRadius = 20 * scale;
		this.desertRadius = 10 * scale;
		this.barrenRadius = 60 * scale;
		
		this.totalRadius = 60 * scale;
		this.blockerChains = 100 * scale * scale;
		this.maxBlockerLength = 10;
		
		this.grass = new TileRegion(6, 4, 1, 2);
		this.desert = new TileRegion(6, 7, 1, 2);
		this.barren = new TileRegion(6, 10, 1, 2);
		
		this.tiles = new Texture(Gdx.files.internal("data/SpriteAtlas.png"));
		this.map = new TiledMap();
		
		this.RandomizeField();
		
		this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, 1);
		this.mapRenderer.setView(camera);
		
		this.pickups = new LinkedList<Pickup>();
		this.pickupsToRemove = new LinkedList<Pickup>();
		
		this.enemies = new ArrayList<Enemy>();
		this.enemiesToRemove = new LinkedList<Enemy>();

		
		this.projectiles = new ArrayList<Projectile>();
		this.projectilesToRemove =  new LinkedList<Projectile>();
		this.generator = new Generator();
		
		SteveDriver.camera.position.x = SteveDriver.snake.getHeadPosition().x;
		SteveDriver.camera.position.y = SteveDriver.snake.getHeadPosition().y;
	}
	
	public void destroyBlocker(int xPos, int yPos){
		TiledMapTileLayer blockers = (TiledMapTileLayer)SteveDriver.field.map.getLayers().get(1);	
		blockers.setCell(xPos, yPos, null);
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
	
	private void placeTile(int x, int y, TextureRegion[][] splitTiles, TiledMapTileLayer layer, int tier){
		int xType = (tier == 1) ? 
						(x == this.desertRadius) ? 1 :
							(x == this.totalRadius - this.desertRadius - 1) ? 2 
									: 3 
							:(x == this.grassRadius) ? 1 :
						(x == this.totalRadius - this.grassRadius - 1) ? 2 
							: 3
					;
		int yType = (tier == 1) ? 
				(y == this.desertRadius) ? 1 :
					(y == this.totalRadius - this.desertRadius - 1) ? 2 : 3 :
				(y == this.grassRadius) ? 1 :
						(y == this.totalRadius - this.grassRadius - 1) ? 2 : 3
				;
		int tx = 0;
		int ty = 0;
		
		switch(xType){
			case 1:
				switch(yType){
					case 1:
						tx = (tier == 1) ? this.desert.getCornerX(true) : this.grass.getCornerX(true);
						ty = (tier == 1) ? this.desert.getCornerY(true) : this.grass.getCornerY(true);
					break;
					
					case 2:
						tx = (tier == 1) ? this.desert.getCornerX(true) : this.grass.getCornerX(true);
						ty = (tier == 1) ? this.desert.getCornerY(false) : this.grass.getCornerY(false);
					break;
					
					case 3:
						tx = (tier == 1) ? this.desert.getSideX(1) : this.grass.getSideX(1);
						ty = (tier == 1) ? this.desert.getSideY(2) : this.grass.getSideY(2);
					break;
				}
			break;
			
			case 2:
				switch(yType){
					case 1:
						tx = (tier == 1) ? this.desert.getCornerX(false) : this.grass.getCornerX(false);
						ty = (tier == 1) ? this.desert.getCornerY(true) : this.grass.getCornerY(true);
					break;
					
					case 2:
						tx = (tier == 1) ? this.desert.getCornerX(false) : this.grass.getCornerX(false);
						ty = (tier == 1) ? this.desert.getCornerY(false) : this.grass.getCornerY(false);
					break;
					
					case 3:
						tx = (tier == 1) ? this.desert.getSideX(3) : this.grass.getSideX(3);
						ty = (tier == 1) ? this.desert.getSideY(2) : this.grass.getSideY(2);
					break;
				}
			break;
			
			case 3:
				switch(yType){
					case 1:
						tx = (tier == 1) ? this.desert.getSideX(2) : this.grass.getSideX(2);
						ty = (tier == 1) ? this.desert.getSideY(3) : this.grass.getSideY(3);
					break;
					
					case 2:
						tx = (tier == 1) ? this.desert.getSideX(2) : this.grass.getSideX(2);
						ty = (tier == 1) ? this.desert.getSideY(1) : this.grass.getSideY(1);
					break;
					
					case 3:
						tx = (tier == 1) ? this.desert.GetRandomX() : this.grass.GetRandomX();
						ty = (tier == 1) ? this.desert.GetRandomY() : this.grass.GetRandomY();
					break;
				}
			break;
		}
		
		Cell cell = new Cell();
		cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
		layer.setCell(x, y, cell);
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
				this.placeTile(x, y, splitTiles, layer, 1);
			}
		}
		
		//Grass tiles generated
		for (int x = this.grassRadius; x < this.totalRadius - this.grassRadius; x++) {
			for (int y = this.grassRadius; y < this.totalRadius - this.grassRadius; y++) {
				this.placeTile(x, y, splitTiles, layer, 0);
			}
		}
		
		int randX, randY;
		//this code sets up the positions for the blockers on the grid
		for (int i = 0; i < this.blockerChains; ) {
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
				
				if(this.checkRing(randX, randY) == this.checkRing(randX+2, randY+2) && this.checkRing(randX, randY) == this.checkRing(randX-1, randY-1)){
					//ensures that there is always a tileable set of blockers
					i++;
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
					} else {
						boolean topLeft = (blockers.getCell(x-1, y + 1) == null) || (tileRad != this.checkRing(x - 1, y + 1));
						boolean topRight = (blockers.getCell(x + 1, y + 1) == null) || (tileRad != this.checkRing(x + 1, y + 1));
						boolean bottomLeft = (blockers.getCell(x - 1, y - 1) == null) || (tileRad != this.checkRing(x - 1, y - 1));
						boolean bottomRight = (blockers.getCell(x + 1, y - 1) == null) || (tileRad != this.checkRing(x + 1, y - 1));
						if (topLeft) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).innerBottomRight);
						} else if (topRight) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).innerBottomLeft);
						} else if (bottomLeft) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).innerTopRight);
						} else if (bottomRight) {
							blockers.setCell(x, y, blockerTiles.get(tileRad).innerTopLeft);
						}
					}
				}
			}
		}
		
		layers.add(layer);
		layers.add(blockers);
	}
	
	public void update() {
		this.generator.update();
		
		for (Enemy e : enemies) {
			e.update();
		}
		while (enemiesToRemove.size() > 0) {
			enemies.remove(enemiesToRemove.remove());
		}
		
		for (Projectile p : projectiles) {
			p.update();
		}
		while (projectilesToRemove.size() > 0) {
			projectiles.remove(projectilesToRemove.remove());
		}
	}
	
	public void draw() {
		//mapRenderer.setView(SteveDriver.camera);
		//mapRenderer.render();
		
		TiledMapTileLayer background = (TiledMapTileLayer)map.getLayers().get(0);
		TiledMapTileLayer blockers = (TiledMapTileLayer)map.getLayers().get(1);
		
		SteveDriver.batch.begin();
		for (int x = 0; x < totalRadius; x++) {
			for (int y = 0; y < totalRadius; y++) {
				if ((x + 1) * 16 >= SteveDriver.camera.position.x - SteveDriver.camera.viewportWidth / 2 &&
					x * 16 < SteveDriver.camera.position.x + SteveDriver.camera.viewportWidth / 2 &&
					(y + 1) * 16 >= SteveDriver.camera.position.y - SteveDriver.camera.viewportHeight / 2&
					y * 16 < SteveDriver.camera.position.y + SteveDriver.camera.viewportHeight / 2) {
					SteveDriver.batch.draw(background.getCell(x, y).getTile().getTextureRegion(),
							x * 16, y * 16, 8, 8, 16, 16, 1, 1, 0);
					
					if (blockers.getCell(x, y) != null) {
						SteveDriver.batch.draw(blockers.getCell(x, y).getTile().getTextureRegion(),
								x * 16, y * 16, 8, 8, 16, 16, 1, 1, 0);
					}
				}
			}
		}
		SteveDriver.batch.end();
		
		SteveDriver.batch.begin();
		for (Pickup p : pickups) {
			if (p.getActive()) {
				p.draw(SteveDriver.batch);
			}
			else {
				pickupsToRemove.add(p);
			}
		}
		while(pickupsToRemove.size() > 0) {
			pickups.remove(pickupsToRemove.remove());
		}
		
		for (Enemy e : enemies) {
			e.draw();
		}
		
		for (Projectile p : projectiles) {
			p.draw();
		}
		SteveDriver.batch.end();
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
		
		TiledMapTileLayer layer = (TiledMapTileLayer)SteveDriver.field.map.getLayers().get(1);	
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
			
				if (cell != null && 
						CollisionHelper.isCollide(new Rectangle(x * SteveDriver.TEXTURE_WIDTH, y * SteveDriver.TEXTURE_LENGTH, SteveDriver.TEXTURE_WIDTH, SteveDriver.TEXTURE_LENGTH), newObject)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public int getFieldRadius(){
		return this.totalRadius;
	}
}