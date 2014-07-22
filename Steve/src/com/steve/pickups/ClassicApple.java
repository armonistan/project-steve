package com.steve.pickups;

import com.badlogic.gdx.Gdx;
import com.steve.Snake;
import com.steve.SteveDriver;

public class ClassicApple extends Apple{
	public ClassicApple(float x, float y){
		super(x,y);
		SteveDriver.numApples++;	
	}
	
	public void update(){
		lifeTimer -= Gdx.app.getGraphics().getDeltaTime();
		if (lifeTimer <= 1) {
			SteveDriver.field.pickupsToRemove.add(this);
			SteveDriver.numApples--;
		}
		
		else {
			int startX = (int)(SteveDriver.camera.position.x - SteveDriver.camera.viewportWidth / 2f)-SteveDriver.TEXTURE_SIZE;
			int endX = (int)(SteveDriver.camera.position.x + SteveDriver.camera.viewportWidth / 2f)+SteveDriver.TEXTURE_SIZE;
			int startY = (int)(SteveDriver.camera.position.y - SteveDriver.camera.viewportHeight / 2f)-SteveDriver.TEXTURE_SIZE;
			int endY = (int)(SteveDriver.camera.position.y + SteveDriver.camera.viewportHeight / 2f)+SteveDriver.TEXTURE_SIZE;
			if((this.getX() < startX) ){
				SteveDriver.field.pickupsToRemove.add(this);
				SteveDriver.numApples--;	
				//System.out.println("kill start x: " + SteveDriver.numApples);
			}
			else if((this.getX() > endX)){
				SteveDriver.field.pickupsToRemove.add(this);
				SteveDriver.numApples--;	
				//System.out.println("kill end x: " + SteveDriver.numApples);
			}
			else if((this.getY() < startY)){
				SteveDriver.field.pickupsToRemove.add(this);
				SteveDriver.numApples--;				
				//System.out.println("kill start y: " + SteveDriver.numApples );
			}
			else if((this.getY() > endY)){
				SteveDriver.field.pickupsToRemove.add(this);
				SteveDriver.numApples--;
				//System.out.println("kill end y: " + SteveDriver.numApples);
			}
			else
				this.alphaMod = (lifeTimer / lifeTime > .5f) ? 1 : (2f*lifeTimer / lifeTime);
		}
	}
	
	@Override
	public void consume(Snake snake){
		SteveDriver.numApples--;
		super.consume(snake);
	}
}
