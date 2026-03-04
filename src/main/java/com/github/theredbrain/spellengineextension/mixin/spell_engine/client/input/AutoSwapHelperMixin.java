package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.input;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.spell_engine.client.input.AutoSwapHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AutoSwapHelper.class)
public class AutoSwapHelperMixin {

	@WrapMethod(method = "autoSwapForAttack", remap = false)
	private static boolean spellengineextension$wrap_autoSwapForAttack(Operation<Boolean> original) {
		if (SpellEngineExtension.SERVER_CONFIG.disable_auto_swap.get()) {
			return false;
		}
		return original.call();
	}

	@WrapMethod(method = "autoSwapForSpells", remap = false)
	private static boolean spellengineextension$wrap_autoSwapForSpells(Operation<Boolean> original) {
		if (SpellEngineExtension.SERVER_CONFIG.disable_auto_swap.get()) {
			return false;
		}
		return original.call();
	}
}
