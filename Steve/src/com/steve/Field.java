package com.steve;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.*;

public class Field {
	int length;
	int width;
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	
	ArrayList<Pickup> pickups;
	
	//TODO:Define "circles"

	public Field(OrthographicCamera camera) {
		length = 500;
		width = 500;
		
		map = new TmxMapLoader().load("data/levels/grass.tmx");
		
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1);
		//mapRenderer.setView(camera);
		
		pickups = new ArrayList<Pickup>();
		pickups.add(new Apple(0, 0));
		pickups.add(new Apple(5, 5));
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