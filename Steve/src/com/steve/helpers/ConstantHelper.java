package com.steve.helpers;

import java.util.HashMap;
import java.util.Map;

public class ConstantHelper {
	
	public Map<String, Float> upgradeDict;
	
	public ConstantHelper() {
		upgradeDict = new HashMap<String, Float>();
		initConstants();
	}
	
	public void addToConstants(String key, float value) {
		upgradeDict.put(key, value);
	}
	
	public void removeFromConstant(String key) {
		upgradeDict.remove(key);
	}

	public void modifyConstant(String key, float modValue) {
		float newValue = upgradeDict.get(key);
		newValue += modValue;
		upgradeDict.put(key, newValue);
	}
	
	public float get(String key) {
		return upgradeDict.get(key);
	}
	
	public void initConstants() {
		addToConstants("steve", 1.0f);
		addToConstants("cyborg", 0f);
		addToConstants("robot", 0f);
		addToConstants("hitpoints", 1.0f);
		addToConstants("hungerRate", 1.0f);
		addToConstants("startLength", 0f);
		addToConstants("maxLength", 10f);
		addToConstants("mainGun", 0f);
		addToConstants("fireRate", 1.0f);
		addToConstants("fireRange", 1.0f);
		addToConstants("fireDamage", 1.0f);
		addToConstants("goldModifier", 1.0f);
		addToConstants("priceModifier", 1.0f);
		addToConstants("glueTrail", 0f);
		addToConstants("candyZone", 0f);
		addToConstants("nuke", 0f);
		addToConstants("drill", 0f);
		addToConstants("jetFuel", 0f);
		addToConstants("bulletTime", 0f);
	}
}
