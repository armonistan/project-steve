package com.steve.stages;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.base.Pickup;
import com.steve.base.Projectile;
import com.steve.helpers.CollisionHelper;
import com.steve.helpers.Generator;
import com.steve.pickups.BossSummon;

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
	TextureRegion[][] splitTiles;
	TileRegion grass, desert, barren;
	public static TiledMapTileLayer background;
	public static TiledMapTileLayer blockers;
	public static TiledMapTileLayer rubble;
	
	public static LinkedList<Pickup> pickups;
	public static LinkedList<Pickup> pickupsToRemove;
	
	public ArrayList<Enemy> enemies;
	public LinkedList<Enemy> enemiesToRemove;
	public LinkedList<Enemy> enemiesToAdd;
	
	public Cell glue;
	public StaticTiledMapTile glueTile;
	
	protected ArrayList<Projectile> projectiles;
	public LinkedList<Projectile> projectilesToRemove;
	
	public Generator generator;
	public Thread generatingField;
	
	protected Sprite space;
	
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
		this.blockerChains = 200 * scale * scale;
		this.maxBlockerLength = 10;
		
		SteveDriver.cyborgBossActivate = SteveDriver.prefs.getBoolean("cyborgBossActivate", false);
		SteveDriver.robotBossActivate = SteveDriver.prefs.getBoolean("robotBossActivate", false);
		SteveDriver.carrierDefeated = SteveDriver.prefs.getBoolean("carrierDefeated", SteveDriver.carrierDefeated);
		SteveDriver.razorbullDefeated = SteveDriver.prefs.getBoolean("razorbullDefeated", SteveDriver.razorbullDefeated);
		
		
		if (SteveDriver.constants.get("candyZone") == 0f) {
			this.grass = new TileRegion(6, 4, 1, 2);
			this.desert = new TileRegion(6, 7, 1, 2);
			this.barren = new TileRegion(6, 10, 1, 2);
		} else {
			this.grass = new TileRegion(6, 13, 1, 2);
			this.desert = new TileRegion(6, 16, 1, 2);
			this.barren = new TileRegion(6, 19, 1, 2);
		}
		
		splitTiles = TextureRegion.split(SteveDriver.atlas, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		Field.map = new TiledMap();
		
		float x = totalRadius/2 - 10;
		float y = totalRadius/2 + 3;
		
		this.RandomizeField();
	}

	public void cleanupSetup() {
		System.gc();
		
		rubble = (TiledMapTileLayer)map.getLayers().get(2);		
		blockers = (TiledMapTileLayer)map.getLayers().get(1);
		background = (TiledMapTileLayer)map.getLayers().get(0);
		
		glue = new Cell();
		glueTile = new StaticTiledMapTile(this.splitTiles[5][8]); 
		glueTile.setId(100);
		glue.setTile(glueTile);
		
		Field.pickups = new LinkedList<Pickup>();
		Field.pickupsToRemove = new LinkedList<Pickup>();
		
		this.enemies = new ArrayList<Enemy>();
		this.enemiesToRemove = new LinkedList<Enemy>();
		this.enemiesToAdd = new LinkedList<Enemy>();
		
		this.projectiles = new ArrayList<Projectile>();
		this.projectilesToRemove =  new LinkedList<Projectile>();
		this.generator = new Generator();
		
		SteveDriver.camera.position.x = SteveDriver.snake.getHeadPosition().x;
		SteveDriver.camera.position.y = SteveDriver.snake.getHeadPosition().y;
		
		space = new Sprite(new TextureRegion(SteveDriver.space, 0f, 0f, 1f, 1f));
	}
	
	public void destroyBlocker(int xPos, int yPos){
		if (rubble.getCell(xPos, yPos) == null && blockers.getCell(xPos, yPos) != null) {
			blockers.setCell(xPos, yPos, null);
			Cell destroyed = new Cell();
			switch (checkRing(xPos, yPos)) {
				case 0:
					destroyed.setTile(new StaticTiledMapTile(splitTiles[6][8]));
					rubble.setCell(xPos, yPos, destroyed);
					break;
				case 1:
					destroyed.setTile(new StaticTiledMapTile(splitTiles[6][9]));
					rubble.setCell(xPos, yPos, destroyed);
					break;
				case 2:
					destroyed.setTile(new StaticTiledMapTile(splitTiles[6][10]));
					rubble.setCell(xPos, yPos, destroyed);
					break;
			}
		}
	}
	
	public int checkRing(int x, int y) {
		if (x >= this.grassRadius && x < this.totalRadius - this.grassRadius && y >= this.grassRadius && y < this.totalRadius - this.grassRadius) {
			return 0;
		} else if (x >= this.desertRadius && x < this.totalRadius - this.desertRadius && y >= this.desertRadius && y < this.totalRadius - this.desertRadius) {
			return 1;
		} else if (x >= 0 && x < totalRadius && y >= 0 && y < totalRadius) {
			return 2;
		}
		else {
			return 3;
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
		generatingField = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Nothing yet.");
				
				MapLayers layers = Field.map.getLayers();
				TiledMapTileLayer layer = new TiledMapTileLayer(totalRadius, totalRadius, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
				TiledMapTileLayer blockers = new TiledMapTileLayer(totalRadius, totalRadius, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
				TiledMapTileLayer rubble = new TiledMapTileLayer(totalRadius, totalRadius, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
				ArrayList<CellContainer> blockerTiles = new ArrayList<CellContainer>();
				
				if (SteveDriver.constants.get("candyZone") == 0f){
					blockerTiles.add(new CellContainer(0, 4, splitTiles, SteveDriver.random));
					blockerTiles.add(new CellContainer(0, 7, splitTiles, SteveDriver.random));
					blockerTiles.add(new CellContainer(0, 10, splitTiles, SteveDriver.random));
				} else {
					blockerTiles.add(new CellContainer(0, 13, splitTiles, SteveDriver.random));
					blockerTiles.add(new CellContainer(0, 16, splitTiles, SteveDriver.random));
					blockerTiles.add(new CellContainer(0, 19, splitTiles, SteveDriver.random));
				}
				//This is the Background generation	
				//Barren tiles generated
				for (int x = 0; x < totalRadius; x++) {
					for (int y = 0; y < totalRadius; y++) {
						int ty = barren.GetRandomY();
						int tx = barren.GetRandomX();
						Cell cell = new Cell();
						cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
						layer.setCell(x, y, cell);
					}
				}
				
				//Desert tiles generated
				for (int x = desertRadius; x < totalRadius - desertRadius; x++) {
					for (int y = desertRadius; y < totalRadius - desertRadius; y++) {
						placeTile(x, y, splitTiles, layer, 1);
					}
				}
				
				//Grass tiles generated
				for (int x = grassRadius; x < totalRadius - grassRadius; x++) {
					for (int y = grassRadius; y < totalRadius - grassRadius; y++) {
						placeTile(x, y, splitTiles, layer, 0);
					}
				}
				
				int randX, randY;
				//this code sets up the positions for the blockers on the grid
				for (int i = 0; i < blockerChains; ) {
					randX = SteveDriver.random.nextInt(totalRadius);
					randY = SteveDriver.random.nextInt(totalRadius);
					
					Cell cell = new Cell();
					cell.setTile(new StaticTiledMapTile(splitTiles[5][1]));
					
					int dx = 0;
					int dy = 0;
					
					for (int j = 0; j < maxBlockerLength && SteveDriver.random.nextFloat() > .4f; j++) {
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
						
						if(isOccupied(randX*SteveDriver.TEXTURE_SIZE, randY*SteveDriver.TEXTURE_SIZE) && checkRing(randX, randY) == checkRing(randX+2, randY+2) && checkRing(randX, randY) == checkRing(randX-1, randY-1)){
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
				for (int x = 0; x < totalRadius; x++) {
					for (int y = 0; y < totalRadius; y++) {
						if(blockers.getCell(x, y) != null) {
							tileRad = checkRing(x, y);
							left = (blockers.getCell(x-1, y) == null) || (tileRad != checkRing(x - 1, y));
							right = (blockers.getCell(x + 1, y) == null) || (tileRad != checkRing(x + 1, y));
							top = (blockers.getCell(x, y + 1) == null) || (tileRad != checkRing(x, y + 1));
							bottom = (blockers.getCell(x, y - 1) == null) || (tileRad != checkRing(x, y - 1));
							
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
								boolean topLeft = (blockers.getCell(x-1, y + 1) == null) || (tileRad != checkRing(x - 1, y + 1));
								boolean topRight = (blockers.getCell(x + 1, y + 1) == null) || (tileRad != checkRing(x + 1, y + 1));
								boolean bottomLeft = (blockers.getCell(x - 1, y - 1) == null) || (tileRad != checkRing(x - 1, y - 1));
								boolean bottomRight = (blockers.getCell(x + 1, y - 1) == null) || (tileRad != checkRing(x + 1, y - 1));
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
				layers.add(rubble);
			}
		});
		
		generatingField.start();
		
		/*try {
			test.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/	
	}
	
	public void update() {
		if(SteveDriver.cyborgBossActivate){
			BossSummon bs = new BossSummon((int)(SteveDriver.snake.getHeadPosition().x/SteveDriver.TEXTURE_SIZE), (int)(SteveDriver.snake.getHeadPosition().y/SteveDriver.TEXTURE_SIZE)+8, 0);
			pickups.add(bs);
			SteveDriver.cyborgBossActivate = false;
			if (!SteveDriver.prefs.getBoolean("cyborgBossTutorial", false)){
				SteveDriver.prefs.putBoolean("cyborgBossTutorial", true);
				SteveDriver.prefs.flush();
				SteveDriver.tutorialOn = true;
				SteveDriver.tutorial.startCyborgBossTutorial();
			}
		}
		else if(SteveDriver.robotBossActivate){
			BossSummon bs = new BossSummon((int)(SteveDriver.snake.getHeadPosition().x/SteveDriver.TEXTURE_SIZE), (int)(SteveDriver.snake.getHeadPosition().y/SteveDriver.TEXTURE_SIZE)+4, 1);
			pickups.add(bs);
			if (!SteveDriver.prefs.getBoolean("robotBossTutorial", false)){
				SteveDriver.prefs.putBoolean("robotBossTutorial", true);
				SteveDriver.prefs.flush();
				SteveDriver.tutorialOn = true;
				SteveDriver.tutorial.startRobotBossTutorial();
			}
			SteveDriver.robotBossActivate = false;
		}
		
		this.generator.update();
		
		for (Enemy e : enemies) {
			e.update();
		}
		while (enemiesToRemove.size() > 0) {
			enemies.remove(enemiesToRemove.remove());
		}
		
		while (enemiesToAdd.size() > 0) {
			enemies.add(enemiesToAdd.remove());
		}
		
		for (Projectile p : projectiles) {
			p.update();
		}
		while (projectilesToRemove.size() > 0) {
			projectiles.remove(projectilesToRemove.remove());
		}
		
		space.setPosition(SteveDriver.camera.position.x - space.getWidth() / 2f +
				(totalRadius * SteveDriver.TEXTURE_SIZE / 2 - SteveDriver.snake.getHeadPosition().x) / (totalRadius / 10f),
				SteveDriver.camera.position.y - space.getHeight() / 2f +
				(totalRadius * SteveDriver.TEXTURE_SIZE / 2 - SteveDriver.snake.getHeadPosition().y) / (totalRadius / 10f));
	}
	
	public void drawBelowSnake() {
		space.draw(SteveDriver.batch);
		
		int startX = (int)(SteveDriver.camera.position.x - SteveDriver.camera.viewportWidth / 2f) / SteveDriver.TEXTURE_SIZE;
		int endX = (int)(SteveDriver.camera.position.x + SteveDriver.camera.viewportWidth / 2f) / SteveDriver.TEXTURE_SIZE;
		int startY = (int)(SteveDriver.camera.position.y - SteveDriver.camera.viewportHeight / 2f) / SteveDriver.TEXTURE_SIZE;
		int endY = (int)(SteveDriver.camera.position.y + SteveDriver.camera.viewportHeight / 2f) / SteveDriver.TEXTURE_SIZE;
		
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				Cell temp = background.getCell(x, y);
				
				if (temp != null) {
					SteveDriver.batch.draw(temp.getTile().getTextureRegion(), x * SteveDriver.TEXTURE_SIZE,
							y * SteveDriver.TEXTURE_SIZE, 8, 8, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, 1, 1, 0);
					
					if (blockers.getCell(x, y) != null) {
						SteveDriver.batch.draw(blockers.getCell(x, y).getTile().getTextureRegion(),
								x * SteveDriver.TEXTURE_SIZE, y * SteveDriver.TEXTURE_SIZE, 8, 8, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, 1, 1, 0);
					}
					
					if (rubble.getCell(x, y) != null) {
						SteveDriver.batch.draw(rubble.getCell(x, y).getTile().getTextureRegion(),
								x * SteveDriver.TEXTURE_SIZE, y * SteveDriver.TEXTURE_SIZE, 8, 8, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, 1, 1, 0);
					}
				}
			}
		}/**/

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
	}
	
	public void drawAboveSnake(){
		for (Projectile p : projectiles) {
			p.draw();
		}
	}
	
	public boolean isOccupied(Rectangle newObject){
		for(Enemy e : enemies){
			if(CollisionHelper.isCollide(newObject, e.getRectangle())){
				return false;
			}
		}
		
		for(Pickup p : pickups){
			if(CollisionHelper.isCollide(newObject, p.getRectangle())){
				return false;
			}
		}
		
		for(Sprite s : SteveDriver.snake.getSnakeSprites()){
			if(CollisionHelper.isCollide(newObject, s.getBoundingRectangle())){
				return false;
			}
		}
		
		TiledMapTileLayer layer = (TiledMapTileLayer)Field.map.getLayers().get(1);	
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
			
				if (cell != null && 
						CollisionHelper.isCollide(new Rectangle(x * SteveDriver.TEXTURE_SIZE, y * SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE), newObject)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean isOccupied(int x, int y){
		float deltaX = 0;
		float deltaY = 0;
		
		for(Sprite s : SteveDriver.snake.getSnakeSprites()){
				deltaX = s.getX() - x;
				deltaY = s.getY() - y;
				
				if(Math.abs(deltaX) < 100 && Math.abs(deltaY) < 100){
					return false;
				}
		}

		
		return true;
	}
	
	public int getFieldRadius(){
		return this.totalRadius;
	}
	
	public void addProjectile(Projectile p) {
		if (!projectiles.contains(p)) {
			projectiles.add(p);
		}
	}
	
	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}
	
	public void destroyBlockersRadius(int radius, int startX, int startY) {
		for (int y = startY - radius; y < startY + radius; y++) {
			for (int x = startX - radius; x < startX + radius; x++) {
				destroyBlocker(x, y);
			}
		}
	}

	public void createBlockerFormation(int x, int y){
		Cell cell = new Cell();
		cell.setTile(new StaticTiledMapTile(splitTiles[5][1]));
		ArrayList<CellContainer> blockerTiles = new ArrayList<CellContainer>();
		if (SteveDriver.constants.get("candyZone") == 0f){
			blockerTiles.add(new CellContainer(0, 4, splitTiles, SteveDriver.random));
			blockerTiles.add(new CellContainer(0, 7, splitTiles, SteveDriver.random));
			blockerTiles.add(new CellContainer(0, 10, splitTiles, SteveDriver.random));
		} else {
			blockerTiles.add(new CellContainer(0, 13, splitTiles, SteveDriver.random));
			blockerTiles.add(new CellContainer(0, 16, splitTiles, SteveDriver.random));
			blockerTiles.add(new CellContainer(0, 19, splitTiles, SteveDriver.random));
		}
		
		blockers.setCell(x, y, cell);
		blockers.setCell(x+1, y+1, cell);
		blockers.setCell(x+1, y, cell);
		blockers.setCell(x, y+1, cell);
		
		setBlockerFormationImage(blockerTiles, x,y);
		setBlockerFormationImage(blockerTiles, x+1,y+1);
		setBlockerFormationImage(blockerTiles, x+1,y);
		setBlockerFormationImage(blockerTiles, x,y+1);
	}
	
	private void setBlockerFormationImage(ArrayList<CellContainer> blockerTiles, int x, int y){
		boolean left, right, top, bottom;
		int tileRad = 0;
		
		if (rubble.getCell(x, y) != null) {
			blockers.setCell(x, y, new Cell());
			rubble.setCell(x,y, null);
		}
		
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
	
	public void setGlueTile(int x, int y) {
		background.setCell(x, y, glue);
	}
	
	public boolean checkGlueTile(int x, int y) {
		//System.out.println(background.getCell(x, y).getTile().getProperties());
		Cell temp = background.getCell(x, y);
		
		if (temp != null && temp.getTile().getId() == 100) {
			return true;
		}
		return false;
	}
}