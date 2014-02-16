package com.steve;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;

public class Field {
	int length;
	int width;
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	
	//TODO:Define "circles"

	public Field(OrthographicCamera camera) {
		length = 500;
		width = 500;
		
		map = new TmxMapLoader().load("data/levels/grass.tmx");
		
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1);
		//mapRenderer.setView(camera);
	}
	
	public void Update() {
		
	}
	
	public void render(OrthographicCamera camera) {
		mapRenderer.setView(camera);
		mapRenderer.render();
	}
}