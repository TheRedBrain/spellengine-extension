package com.github.theredbrain.spellengineextension.compat;

import com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.config.Config;

public class ShoulderSurfingCompat {

	public static boolean isShoulderSurfingCameraDecoupled() {
		return ShoulderSurfing.getInstance().isShoulderSurfing() && Config.CLIENT.isCameraDecoupled();
	}

}
