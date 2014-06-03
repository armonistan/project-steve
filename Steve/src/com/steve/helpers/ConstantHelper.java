package com.steve.helpers;

import java.util.HashMap;
import java.util.Map;

public class ConstantHelper {
	
	public Map<String, Float> upgradeDict;
	
	public ConstantHelper() {
		upgradeDict = new HashMap<String, Float>();
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
}
