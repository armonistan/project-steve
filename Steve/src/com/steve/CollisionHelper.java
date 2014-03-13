package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollisionHelper {
	public static boolean isCollide(Rectangle a, Rectangle b) {
		return Intersector.overlaps(a, b);
	}
	
	
	
	public static boolean isCollide(Vector2 posA, float radiusA, Vector2 posB, float radiusB) {
		float distance = distance(posA, posB);
		
		return distance <= (radiusA + radiusB);
	}
	
	public static float distance(Vector2 position1, Vector2 position2) {
		return position1.dst(position2);
	}
}
