package com.steve.stages;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.steve.SteveDriver;
import com.steve.base.Enemy;
import com.steve.base.Pickup;
import com.steve.base.Projectile;
import com.steve.bosses.Carrier;
import com.steve.enemies.Slug;
import com.steve.helpers.Generator;

public class MenuField extends Field {
	private final float SPAWN_TIME = 2;
	private float spawnTimer;

	public MenuField() {
		super(SteveDriver.camera, 1);
		
		this.maxBlockerLength = 10;
		
		splitTiles = TextureRegion.split(SteveDriver.atlas, SteveDriver.TEXTURE_SIZE, SteveDriver.TEXTURE_SIZE);
		map = new TmxMapLoader().load("menu.tmx");
		
		System.gc();
		
		blockers = new TiledMapTileLayer(0, 0, 0, 0);
		background = (TiledMapTileLayer)map.getLayers().get(0);
		rubble = new TiledMapTileLayer(0, 0, 0, 0);
		
		Field.pickups = new LinkedList<Pickup>();
		Field.pickupsToRemove = new LinkedList<Pickup>();
		
		this.enemies = new ArrayList<Enemy>();
		this.enemiesToRemove = new LinkedList<Enemy>();
		this.enemiesToAdd = new LinkedList<Enemy>();
		
		this.projectiles = new ArrayList<Projectile>();
		this.projectilesToRemove =  new LinkedList<Projectile>();
		this.generator = new Generator();
		
		spawnTimer = 0;
	}
	
	@Override
	public void update() {
		if (spawnTimer > SPAWN_TIME) {
			if (enemies.size() < 10) {
				Slug temp = new Slug((SteveDriver.menu.snake.getHeadPosition().x + SteveDriver.camera.viewportWidth / 2 *
						((SteveDriver.menu.snake.getRotationIndex() == SteveDriver.RIGHT_ID) ? 1 : -1)) / SteveDriver.TEXTURE_SIZE, SteveDriver.menu.snake.getHeadPosition().y / SteveDriver.TEXTURE_SIZE);
				enemiesToAdd.add(temp);
				spawnTimer = 0f;
			}
		}
		else {
			spawnTimer += Gdx.graphics.getRawDeltaTime();
		}
		
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
	}
}
