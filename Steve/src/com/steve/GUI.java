package com.steve;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.steve.SteveDriver.STAGE_TYPE;
import com.steve.bosses.Carrier;
import com.steve.bosses.Razorbull;
import com.steve.commands.ChangeStage;
import com.steve.helpers.GUIHelper.BoxColors;

public class GUI {
	
	private float height;
	private ArrayList<Sprite> guiTextures;
	private Vector2 leftEndPosition;
	private Vector2 rightEndPosition;
	private int healthWidth = 4;
	private int healthColor = 2;
	private float currentHealthPercent;
	
	private float spriteWidth = 3 * SteveDriver.TEXTURE_SIZE;
	
	private Razorbull razor;
	private Carrier carrier;
	
	private float razorHP;
	private float carrierHP;
	
	private boolean razorAlive = false;
	private boolean carrierAlive = false;
	
	private Sprite endHP;
	
	private Sprite[] arrows;
	
	public GUI() {
		guiTextures = new ArrayList<Sprite>();
		for (int i = 0; i < 6; i++) {
			this.guiTextures.add(new Sprite(new TextureRegion(SteveDriver.atlas, 15 * SteveDriver.TEXTURE_SIZE -
					(3 * SteveDriver.TEXTURE_SIZE * i), 23 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE)));
		}
		height = SteveDriver.constants.get("screenHeight");
		leftEndPosition = new Vector2(3  * SteveDriver.TEXTURE_SIZE * -3, height / 2 - 150);
		rightEndPosition = new Vector2(3  * SteveDriver.TEXTURE_SIZE * 2, height / 2 - 150);
		guiTextures.get(5).setPosition(leftEndPosition.x, leftEndPosition.y);
		guiTextures.get(0).setPosition(rightEndPosition.x, rightEndPosition.y);
		
		endHP = new Sprite(new TextureRegion(SteveDriver.atlas, 0, 23 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE));
	
		arrows = new Sprite[4];
		arrows[0] = new Sprite(new TextureRegion(SteveDriver.atlas, 3 * SteveDriver.TEXTURE_SIZE, 30 * SteveDriver.TEXTURE_SIZE,
			3 * SteveDriver.TEXTURE_SIZE, 5 * SteveDriver.TEXTURE_SIZE));
		arrows[1] = new Sprite(new TextureRegion(SteveDriver.atlas, 6 * SteveDriver.TEXTURE_SIZE, 30 * SteveDriver.TEXTURE_SIZE,
				3 * SteveDriver.TEXTURE_SIZE, 5 * SteveDriver.TEXTURE_SIZE));
		arrows[2] = new Sprite(new TextureRegion(SteveDriver.atlas, 9 * SteveDriver.TEXTURE_SIZE, 30 * SteveDriver.TEXTURE_SIZE,
				5 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE));
		arrows[3] = new Sprite(new TextureRegion(SteveDriver.atlas, 14 * SteveDriver.TEXTURE_SIZE, 32 * SteveDriver.TEXTURE_SIZE,
				5 * SteveDriver.TEXTURE_SIZE, 3 * SteveDriver.TEXTURE_SIZE));
		
		arrows[0].setPosition(SteveDriver.guiCamera.viewportWidth / 4 - arrows[0].getWidth() / 2, -1 * arrows[0].getHeight() / 2);
		arrows[1].setPosition(-1 * SteveDriver.guiCamera.viewportWidth / 4 - arrows[1].getWidth() / 2, -1 * arrows[1].getHeight() / 2);
		arrows[2].setPosition(-1 * arrows[2].getWidth() / 2, -1 * arrows[2].getHeight() / 2 + SteveDriver.guiCamera.viewportHeight / 4);
		arrows[3].setPosition(-1 * arrows[3].getWidth() / 2, -1 * arrows[3].getHeight() / 2 - SteveDriver.guiCamera.viewportHeight / 4);
		
		for (Sprite s : arrows) {
			s.setColor(1f, 1f, 1f, 0.25f);
		}
	}
	
	public void render() {
		currentHealthPercent = (SteveDriver.snake.getCurrentHealth() / SteveDriver.snake.getMaxHealth());
		
		for (Sprite s : guiTextures) {
			s.setRegion(s.getRegionX(), (23 + (SteveDriver.snake.getLastDamageTimer() > 0 ? 3 : 0)) * SteveDriver.TEXTURE_SIZE,
					s.getRegionWidth(), s.getRegionHeight());
		}

		Sprite temp = guiTextures.get(5);
		if (SteveDriver.snake.segments.size() == 2) {
			if (temp.getRegionY() < (23 + 6) * SteveDriver.TEXTURE_SIZE) {
			temp.setRegion(temp.getRegionX(), temp.getRegionY() + (6) * SteveDriver.TEXTURE_SIZE,
				temp.getRegionWidth(), temp.getRegionHeight());
			}
		}
		else {
			if (temp.getRegionY() >= (23 + 6) * SteveDriver.TEXTURE_SIZE) {
				temp.setRegion(temp.getRegionX(), temp.getRegionY() - (6) * SteveDriver.TEXTURE_SIZE,
						temp.getRegionWidth(), temp.getRegionHeight());
			}
		}
		
		SteveDriver.batch.begin();
		guiTextures.get(0).draw(SteveDriver.batch);
		guiTextures.get(5).draw(SteveDriver.batch);
		for (int i = 0; i < healthWidth; i++) {
			guiTextures.get(1).setPosition(3 * SteveDriver.TEXTURE_SIZE * (i - 2), height / 2 - 150);
			guiTextures.get(1).draw(SteveDriver.batch);
		}
		
		int finalPixel = (int) (healthWidth * currentHealthPercent);
		
		for (int i = 0; i < finalPixel + 1; i++) {
			if (currentHealthPercent > .5f) {
				healthColor = 2;
			} else if (currentHealthPercent < .5f && currentHealthPercent > .25f) {
				healthColor = 3;
			} else if (currentHealthPercent < .25f) {
				healthColor = 4;
			}
			
			if (i == finalPixel) {
				int spriteSubSectionWidth = (int) (spriteWidth * ((currentHealthPercent * 100) % 25) / 25);
				endHP.setRegionX(15 * SteveDriver.TEXTURE_SIZE - (3  * SteveDriver.TEXTURE_SIZE * healthColor));
				endHP.setRegionWidth(spriteSubSectionWidth);
				endHP.setBounds(3f * SteveDriver.TEXTURE_SIZE * (i - 2f), height / 2f - 150f, spriteSubSectionWidth, 3f * SteveDriver.TEXTURE_SIZE);
				endHP.draw(SteveDriver.batch);
			} else {
				guiTextures.get(healthColor).setPosition(3f * SteveDriver.TEXTURE_SIZE * (i - 2), height/2 - 150);
				guiTextures.get(healthColor).draw(SteveDriver.batch);
			}
		}
		
		for (Sprite s : arrows) {
			s.draw(SteveDriver.batch);
		}
		
		if (razorAlive && carrierAlive) {
			drawBossHP(-1 * SteveDriver.constants.get("screenWidth")/4, -2 * SteveDriver.constants.get("screenHeight") / 5, "Razorbull", razor.getHealth(), razorHP);
			drawBossHP(SteveDriver.constants.get("screenWidth") / 4, -2 * SteveDriver.constants.get("screenHeight") / 5, "Carrier", carrier.getHealth(), carrierHP);
		} else if (razorAlive) {
			drawBossHP(0, -2 * SteveDriver.constants.get("screenHeight") / 5, "Razorbull", razor.getHealth(), razorHP);
		} else if (carrierAlive) {
			drawBossHP(0, -2 * SteveDriver.constants.get("screenHeight") / 5, "Carrier", carrier.getHealth(), carrierHP);
		}
		
		SteveDriver.batch.end();
	}
	
	public void razorbullAlive(Razorbull r) {
		razorAlive = true;
		razor = r;
		razorHP = razor.getHealth();
	}
	
	public void razorbullDead() {
		razorAlive = false;
		razorHP = 0;
	}
	
	public void carrierAlive(Carrier c) {
		carrierAlive = true;
		carrier = c;
		carrierHP = carrier.getHealth();
	}
	
	public void carrierDead() {
		carrierAlive = false;
		carrierHP = 0;
	}
	
	public void drawBossHP(float x, float y, String bossname, float hp, float totalHP) {
		SteveDriver.guiHelper.drawBox(x - (5f * SteveDriver.TEXTURE_SIZE), y + (SteveDriver.TEXTURE_SIZE), 10, 4, BoxColors.BLACK, Color.WHITE);
		SteveDriver.guiHelper.drawTextCentered(bossname, x, y + SteveDriver.TEXTURE_SIZE / 2, Color.BLACK);
		if (hp / totalHP > .66) {
			SteveDriver.guiHelper.drawTextCentered(String.format("%10.0f", hp), x - (2.5f * SteveDriver.TEXTURE_SIZE), y - SteveDriver.TEXTURE_SIZE, Color.GREEN);
		} else if (hp / totalHP > .33) {
			SteveDriver.guiHelper.drawTextCentered(String.format("%10.0f", hp), x - (2.5f * SteveDriver.TEXTURE_SIZE), y - SteveDriver.TEXTURE_SIZE, Color.YELLOW);
		} else {
			SteveDriver.guiHelper.drawTextCentered(String.format("%10.0f", hp), x - (2.5f * SteveDriver.TEXTURE_SIZE), y - SteveDriver.TEXTURE_SIZE, Color.RED);
		}
	}
}
