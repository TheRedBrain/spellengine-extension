package com.github.theredbrain.spellengineextension;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class SpellEngineExtensionMixinCanceller implements MixinCanceller {
	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		return switch (mixinClassName) {
			case "net.spell_engine.mixin.client.ClientPlayerEntityMixin",
				 "net.spell_engine.mixin.client.control.SpellCastingMovement" -> true;
			default -> false;
		};
	}
}
