package com.steve;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollisionHelper {
	public static boolean isCollide(Rectangle a, Rectangle b) {
		return Intersector.overlaps(a, b);
	}
	
	public static boolean isCollide(float x1, float y1, float radius1, float x2, float y2, float radius2) {
		float distance = distanceSquared(x1, y1, x2, y2);
		
		return distance <= (radius2 + radius2) * (radius2 + radius2);
	}
	
	public static float distanceSquared(float x1, float y1, float x2, float y2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}
	
	public static float angleFromDirectionVector(float x, float y) {
		return MathUtils.atan2(y, x) * MathUtils.radiansToDegrees;
	}
}
