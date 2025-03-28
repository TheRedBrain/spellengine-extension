package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals.arrow;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.entity.DuckLivingEntityMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellLaunchPropertiesMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.arrow.ArrowHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArrowHelper.class)
public class ArrowHelperMixin {

	@WrapOperation(
			method = "shootArrow(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellHelper$ImpactContext;I)V",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$LaunchProperties;copy()Lnet/spell_engine/api/spell/Spell$LaunchProperties;", remap = false)
	)
	private static Spell.LaunchProperties spellengineextension$wrap_shootArrow_mutableLaunchProperties(Spell.LaunchProperties instance, Operation<Spell.LaunchProperties> original, @Local(argsOnly = true) LivingEntity shooter) {

		ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;
		if (serverConfig.spell_launch_properties_extra_launch_count_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) instance).spellengineextension$respectExtraLaunchCountAttribute()) {
			instance.extra_launch_count += (int) (((DuckLivingEntityMixin) shooter).spellengineextension$getExtraLaunchCount());
		}

		if (serverConfig.spell_launch_properties_extra_launch_delay_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) instance).spellengineextension$respectExtraLaunchDelayAttribute()) {
			instance.extra_launch_delay += (int) (((DuckLivingEntityMixin) shooter).spellengineextension$getExtraLaunchDelay());
		}

		if (serverConfig.spell_launch_properties_extra_velocity_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) instance).spellengineextension$respectExtraVelocityAttribute()) {
			instance.velocity += (int) (((DuckLivingEntityMixin) shooter).spellengineextension$getExtraVelocity());
		}

		return original.call(instance);
	}
}
