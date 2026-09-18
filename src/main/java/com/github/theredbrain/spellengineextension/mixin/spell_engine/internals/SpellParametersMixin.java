package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.internals.SpellParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpellParameters.class)
public class SpellParametersMixin {

	@WrapOperation(
			method = "getRangeCurved(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/registry/entry/RegistryEntry;F)F",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getEntityInteractionRange()D")
	)
	private static double spellengineextension$getRange(PlayerEntity instance, Operation<Double> original) {
		if (SpellEngineExtension.SERVER_CONFIG.enable_attack_range_attribute_integration.get()) {
			return SpellEngineExtension.getAttackRange(instance);
		} else {
			return original.call(instance);
		}
	}
}
