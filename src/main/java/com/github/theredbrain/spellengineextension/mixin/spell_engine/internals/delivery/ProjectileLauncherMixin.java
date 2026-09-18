package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals.delivery;

import com.github.theredbrain.spellengineextension.spell_engine.ExtendedSpellHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.delivery.ProjectileLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileLauncher.class)
public class ProjectileLauncherMixin {

	// shootProjectile: LaunchProperties
	@WrapOperation(
			method = "shootProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellExecution$ImpactContext;I)V",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$LaunchProperties;copy()Lnet/spell_engine/api/spell/Spell$LaunchProperties;", remap = false)
	)
	private static Spell.LaunchProperties spellengineextension$wrap_shootProjectile_mutableLaunchProperties(
			Spell.LaunchProperties instance, Operation<Spell.LaunchProperties> original, @Local(argsOnly = true) LivingEntity caster
	) {
		return ExtendedSpellHelper.applySpellLaunchPropertiesAttributes(original.call(instance), caster);
	}

	// shootProjectile: Perks
	@WrapOperation(
			method = "shootProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellExecution$ImpactContext;I)V",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;copy()Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;", remap = false)
	)
	private static Spell.ProjectileData.Perks spellengineextension$wrap_shootProjectile_mutablePerks(
			Spell.ProjectileData.Perks instance, Operation<Spell.ProjectileData.Perks> original, @Local(argsOnly = true) LivingEntity caster
	) {
		return ExtendedSpellHelper.applySpellProjectileDataPerkAttributes(original.call(instance), caster);
	}

	// fallProjectile: LaunchProperties
	@WrapOperation(
			method = "fallProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellExecution$ImpactContext;I)Z",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$LaunchProperties;copy()Lnet/spell_engine/api/spell/Spell$LaunchProperties;", remap = false)
	)
	private static Spell.LaunchProperties spellengineextension$wrap_fallProjectile_mutableLaunchProperties(
			Spell.LaunchProperties instance, Operation<Spell.LaunchProperties> original, @Local(argsOnly = true) LivingEntity caster
	) {
		return ExtendedSpellHelper.applySpellLaunchPropertiesAttributes(original.call(instance), caster);
	}

	// fallProjectile: Perks
	@WrapOperation(
			method = "fallProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellExecution$ImpactContext;I)Z",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;copy()Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;", remap = false)
	)
	private static Spell.ProjectileData.Perks spellengineextension$wrap_fallProjectile_mutablePerks(
			Spell.ProjectileData.Perks instance, Operation<Spell.ProjectileData.Perks> original, @Local(argsOnly = true) LivingEntity caster
	) {
		return ExtendedSpellHelper.applySpellProjectileDataPerkAttributes(original.call(instance), caster);
	}

}
