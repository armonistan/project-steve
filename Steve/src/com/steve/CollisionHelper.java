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
	
	public static Vector2 directionVectorFromAngle(float angle) {
		 return new Vector2((float)Math.cos(angle / 180 * Math.PI), (float)Math.sin(angle / 180 * Math.PI));
	}
	
	public static float angleFromDirectionVector(Vector2 direction) {
		return (float)(Math.atan2(direction.y, direction.x) / Math.PI) * 180 + 90;
	}
}
