package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.input;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import net.spell_engine.client.input.AutoSwapHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AutoSwapHelper.class, remap = false)
public class AutoSwapHelperMixin {

	@Inject(method = "autoSwapForAttack", at = @At("HEAD"), cancellable = true)
	private static void autoSwapForAttack(CallbackInfoReturnable<Boolean> cir) {
		if (SpellEngineExtension.serverConfig.disable_auto_swap) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}

	@Inject(method = "autoSwapForSpells", at = @At("HEAD"), cancellable = true)
	private static void autoSwapForSpells(CallbackInfoReturnable<Boolean> cir) {
		if (SpellEngineExtension.serverConfig.disable_auto_swap) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}
