package com.github.theredbrain.spellengineextension;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class SpellEngineExtensionMixinCanceller implements MixinCanceller {
	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		if (mixinClassName.equals("net.spell_engine.mixin.client.control.SpellHotbarMinecraftClient")) {
			return true;
		}
		return false;
	}
}
