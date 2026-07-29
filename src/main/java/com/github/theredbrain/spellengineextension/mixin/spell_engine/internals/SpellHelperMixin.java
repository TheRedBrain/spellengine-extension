package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.spell_engine.CustomSpellModifiers;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellImpactActionDamageMixin;
import com.github.theredbrain.spellengineextension.spell_engine.ExtendedSpellHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpellHelper.class)
public abstract class SpellHelperMixin {

	@Inject(method = "performSpell", at = @At(value = "INVOKE", target = "Lnet/spell_engine/internals/SpellHelper;channelValueMultiplier(Lnet/spell_engine/api/spell/Spell;)F", remap = false))
	private static void spellengineextension$performSpell_applyChannelingCost(World world, PlayerEntity player, RegistryEntry<Spell> spellEntry, SpellTarget.SearchResult targetResult, SpellCast.Action action, float progress, CallbackInfo ci) {
		ExtendedSpellHelper.applyChannelingCost(player, spellEntry);
	}

//	/* TODO casting with offhand */
//	@WrapOperation(
//			method = "startCasting",
//			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;")
//	)
//	private static ItemStack spellengineextension$startCasting_wrap_getMainHandStack(PlayerEntity instance, Operation<ItemStack> original, @Local Spell spell) {
//		return ((DuckSpellMixin) spell).spellengineextension$useOffhandForCasting() ? instance.getOffHandStack() : original.call(instance);
//	}
//
//	/* TODO casting with offhand */
//	@WrapOperation(
//			method = "performSpell",
//			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;")
//	)
//	private static ItemStack spellengineextension$performSpell_wrap_getMainHandStack(PlayerEntity instance, Operation<ItemStack> original, @Local Spell spell) {
//		return ((DuckSpellMixin) spell).spellengineextension$useOffhandForCasting() ? instance.getOffHandStack() : original.call(instance);
//	}

	@WrapOperation(
			method = "getRange(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/api/spell/Spell$Modifier;)F",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getEntityInteractionRange()D")
	)
	private static double spellengineextension$getRange(PlayerEntity instance, Operation<Double> original) {
		if (SpellEngineExtension.SERVER_CONFIG.enable_attack_range_attribute_integration.get()) {
			return SpellEngineExtension.getAttackRange(instance);
		} else {
			return original.call(instance);
		}
	}

	// shootProjectile: LaunchProperties
	@WrapOperation(
			method = "shootProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellHelper$ImpactContext;I)V",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$LaunchProperties;copy()Lnet/spell_engine/api/spell/Spell$LaunchProperties;", remap = false)
	)
	private static Spell.LaunchProperties spellengineextension$wrap_shootProjectile_mutableLaunchProperties(
			Spell.LaunchProperties instance, Operation<Spell.LaunchProperties> original, @Local(argsOnly = true) LivingEntity caster
	) {
		return ExtendedSpellHelper.applySpellLaunchPropertiesAttributes(original.call(instance), caster);
	}

	// shootProjectile: Perks
	@WrapOperation(
			method = "shootProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellHelper$ImpactContext;I)V",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;copy()Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;", remap = false)
	)
	private static Spell.ProjectileData.Perks spellengineextension$wrap_shootProjectile_mutablePerks(
			Spell.ProjectileData.Perks instance, Operation<Spell.ProjectileData.Perks> original, @Local(argsOnly = true) LivingEntity caster
	) {
		return ExtendedSpellHelper.applySpellProjectileDataPerkAttributes(original.call(instance), caster);
	}

	// fallProjectile: LaunchProperties
	@WrapOperation(
			method = "fallProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellHelper$ImpactContext;I)Z",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$LaunchProperties;copy()Lnet/spell_engine/api/spell/Spell$LaunchProperties;", remap = false)
	)
	private static Spell.LaunchProperties spellengineextension$wrap_fallProjectile_mutableLaunchProperties(
			Spell.LaunchProperties instance, Operation<Spell.LaunchProperties> original, @Local(argsOnly = true) LivingEntity caster
	) {
		return ExtendedSpellHelper.applySpellLaunchPropertiesAttributes(original.call(instance), caster);
	}

	// fallProjectile: Perks
	@WrapOperation(
			method = "fallProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellHelper$ImpactContext;I)Z",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;copy()Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;", remap = false)
	)
	private static Spell.ProjectileData.Perks spellengineextension$wrap_fallProjectile_mutablePerks(
			Spell.ProjectileData.Perks instance, Operation<Spell.ProjectileData.Perks> original, @Local(argsOnly = true) LivingEntity caster
	) {
		return ExtendedSpellHelper.applySpellProjectileDataPerkAttributes(original.call(instance), caster);
	}

	@WrapOperation(
			method = "performImpact(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/api/spell/Spell$Impact;Lnet/spell_engine/internals/SpellHelper$ImpactContext;Ljava/util/Collection;)Z",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	)
	private static boolean spellengineextension$wrap_damage(Entity instance, DamageSource source, float amount, Operation<Boolean> original, @Local(argsOnly = true) LivingEntity caster, @Local Spell.Impact.Action.Damage damageData, @Local(argsOnly = true) RegistryEntry<Spell> spellEntry) {

		// direct damage
		double directDamageAmount = 0.0;
		if (caster instanceof PlayerEntity playerCaster) {
			directDamageAmount = CustomSpellModifiers.getModifiedDirectDamage(playerCaster, damageData, spellEntry);
		}

		// damage type override
		DamageSource damageSource = null;
		String damageTypeOverride = ((DuckSpellImpactActionDamageMixin) damageData).spellengineextension$getDamageTypeOverride();
		if (!damageTypeOverride.isEmpty()) {
			Identifier damageTypeOverrideId = Identifier.tryParse(damageTypeOverride);
			if (damageTypeOverrideId != null) {
				RegistryKey<DamageType> key = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, damageTypeOverrideId);
				Registry<DamageType> registry = ((DamageSourcesAccessor) caster.getDamageSources()).getRegistry();
				damageSource = new DamageSource(registry.entryOf(key), caster);
			}
		}
		return original.call(instance, damageSource != null ? damageSource : source, directDamageAmount > 0 ? ((float) directDamageAmount) : amount);
	}

	@WrapOperation(
			method = "performImpact(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/api/spell/Spell$Impact;Lnet/spell_engine/internals/SpellHelper$ImpactContext;Ljava/util/Collection;)Z",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;heal(F)V")
	)
	private static void spellengineextension$wrap_heal(LivingEntity instance, float amount, Operation<Void> original, @Local(argsOnly = true) LivingEntity caster, @Local Spell.Impact.Action.Heal healData, @Local(argsOnly = true) RegistryEntry<Spell> spellEntry) {

		// direct heal
		double directHealAmount = 0.0;
		if (caster instanceof PlayerEntity playerCaster) {
			directHealAmount = CustomSpellModifiers.getModifiedDirectHealing(playerCaster, healData, spellEntry);
		}

		original.call(instance, directHealAmount > 0 ? ((float) directHealAmount) : amount);
	}
}
