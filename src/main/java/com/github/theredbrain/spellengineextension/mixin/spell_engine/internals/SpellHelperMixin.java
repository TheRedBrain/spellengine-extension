package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.entity.DuckLivingEntityMixin;
import com.github.theredbrain.spellengineextension.entity.damage.DuckDamageSourcesMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellCostMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellImpactActionDamageMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellImpactActionHealMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellLaunchPropertiesMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellProjectileDataPerksMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.Ammo;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellBatcher;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.container.SpellContainerSource;
import net.spell_engine.utils.WorldScheduler;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SpellHelper.class)
@SuppressWarnings("UnreachableCode")
public abstract class SpellHelperMixin {

	/**
	 * @author TheRedBrain
	 * @reason check for custom cost
	 */
	@Inject(method = "attemptCasting(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Identifier;Z)Lnet/spell_engine/internals/casting/SpellCast$Attempt;", at = @At(value = "RETURN", ordinal = 3), cancellable = true)
	private static void attemptCasting(PlayerEntity player, ItemStack itemStack, Identifier spellId, boolean checkAmmo, CallbackInfoReturnable<SpellCast.Attempt> cir, @Local RegistryEntry.Reference<Spell> spellEntry) {

		Spell spell = (Spell)spellEntry.value();
		ServerConfig spellEngineExtensionConfig = SpellEngineExtension.SERVER_CONFIG;

		if (spellEngineExtensionConfig.spell_cost_health_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkHealthCost()) {
			float healthCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getHealthCost();
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$healthCostMultiplierApplies()) {
				healthCost = healthCost * ((DuckLivingEntityMixin) player).spellengineextension$getHealthSpellCostMultiplier();
			}
			if (healthCost > 0 && healthCost > player.getHealth()) {
				player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_health"), true);
				cir.setReturnValue(SpellCast.Attempt.none());
				cir.cancel();
			}
		}
		if (SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkManaCost()) {
			float manaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getManaCost();
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$manaCostMultiplierApplies()) {
				manaCost = manaCost * ((DuckLivingEntityMixin) player).spellengineextension$getManaSpellCostMultiplier();
			}
			float currentMana = SpellEngineExtension.getCurrentMana(player);
			if (manaCost > 0 && manaCost > currentMana) {
				player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_mana"), true);
				cir.setReturnValue(SpellCast.Attempt.none());
				cir.cancel();
			}
		}
		if (SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkStaminaCost()) {
			float staminaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getStaminaCost();
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$staminaCostMultiplierApplies()) {
				staminaCost = staminaCost * ((DuckLivingEntityMixin) player).spellengineextension$getStaminaSpellCostMultiplier();
			}
			float currentStamina = SpellEngineExtension.getCurrentStamina(player);
			if (staminaCost > 0 && staminaCost > currentStamina) {
				player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_stamina"), true);
				cir.setReturnValue(SpellCast.Attempt.none());
				cir.cancel();
			}
		}
		if (spellEngineExtensionConfig.spell_cost_effects_allowed.get() && spell.cost.effect_id != null) {
			Optional<RegistryEntry.Reference<StatusEffect>> effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(spell.cost.effect_id));
			if (effect.isPresent()) {
				if (!player.hasStatusEffect(effect.get())) {
					player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_status_effect", Text.translatable(effect.get().value().getTranslationKey()).getString()), true);
					cir.setReturnValue(SpellCast.Attempt.none());
					cir.cancel();
				} else {
					StatusEffectInstance statusEffectInstance = player.getStatusEffect(effect.get());
					if (statusEffectInstance != null) {
						int decrementEffectAmount = ((DuckSpellCostMixin) spell.cost).spellengineextension$getDecrementEffectAmount();
						if (decrementEffectAmount > 0 && statusEffectInstance.getAmplifier() + 1 < decrementEffectAmount) {
							player.sendMessage(Text.translatable("hud.cast_attempt_error.status_effect_amplifier_too_low", Text.translatable(effect.get().value().getTranslationKey()).getString()), true);
							cir.setReturnValue(SpellCast.Attempt.none());
							cir.cancel();
						}
					}
				}
			}
		}
	}

	/**
	 * @author TheRedBrain
	 * @reason integrate health cost, mana cost, stamina cost, reducing amplifier of status effect cost instead of removing them, self consuming of casting item
	 */
	@Overwrite
	private static void consumeSpellCost(PlayerEntity player, float progress, SpellContainerSource.SourcedContainer spellSource, Identifier spellId, RegistryEntry<Spell> spellEntry, ItemStack heldItemStack, Ammo.Result ammoResult, boolean scheduled) {
		Spell spell = (Spell)spellEntry.value();
		boolean batching = spell.cost.batching;
		if (batching && !scheduled) {
			if (!((SpellBatcher) player).hasBatchedCost(spellId)) {
				((WorldScheduler) player.getWorld()).schedule(0, () -> {
					consumeSpellCost(player, progress, spellSource, spellId, spellEntry, heldItemStack, ammoResult, true);
				});
				((SpellBatcher) player).batchCost(spellId, true);
			}
		} else {
			SpellHelper.imposeCooldown(player, spellSource, spellId, spellEntry, progress);
			player.addExhaustion(spell.cost.exhaust * SpellEngineMod.config.spell_cost_exhaust_multiplier);

			var spellEngineExtensionConfig = SpellEngineExtension.SERVER_CONFIG;

			// health cost
			if (spellEngineExtensionConfig.spell_cost_health_allowed.get()) {
				float healthCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getHealthCost();
				if (((DuckSpellCostMixin) spell.cost).spellengineextension$healthCostMultiplierApplies()) {
					healthCost = healthCost * ((DuckLivingEntityMixin) player).spellengineextension$getHealthSpellCostMultiplier();
				}
				if (healthCost > 0.0F) {
					player.damage(((DuckDamageSourcesMixin) player.getDamageSources()).spellengineextension$bloodMagicCasting(), healthCost);
				}
			}

			// mana cost
			if (SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get()) {
				float manaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getManaCost();
				if (((DuckSpellCostMixin) spell.cost).spellengineextension$manaCostMultiplierApplies()) {
					manaCost = manaCost * ((DuckLivingEntityMixin) player).spellengineextension$getManaSpellCostMultiplier();
				}
				if (manaCost > 0.0F) {
					SpellEngineExtension.addMana(player, -manaCost);
				}
			}

			// stamina cost
			if (SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get()) {
				float staminaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getStaminaCost();
				if (((DuckSpellCostMixin) spell.cost).spellengineextension$staminaCostMultiplierApplies()) {
					staminaCost = staminaCost * ((DuckLivingEntityMixin) player).spellengineextension$getStaminaSpellCostMultiplier();
				}
				if (staminaCost > 0.0F) {
					SpellEngineExtension.addStamina(player, -staminaCost);
				}
			}

			// consume spell casting item
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$consumeSelf()) {
				player.incrementStat(Stats.USED.getOrCreateStat(heldItemStack.getItem()));
				if (!player.isCreative()) {
					heldItemStack.decrement(1);
				}
			}

			if (SpellEngineMod.config.spell_cost_durability_allowed && spell.cost.durability > 0) {
				ItemStack stackToDamage = spellSource.itemStack() != null && spellSource.itemStack().isDamageable() ? spellSource.itemStack() : heldItemStack;
				stackToDamage.damage(spell.cost.durability, player, EquipmentSlot.MAINHAND);
			}

			Ammo.consume(ammoResult, player);
			if (spell.cost.effect_id != null) {
				Optional<RegistryEntry.Reference<StatusEffect>> effect = Registries.STATUS_EFFECT.getEntry(Identifier.of(spell.cost.effect_id));
				if (effect.isPresent()) {
					int decrementEffectAmount = ((DuckSpellCostMixin) spell.cost).spellengineextension$getDecrementEffectAmount();
					if (decrementEffectAmount < 0) {
						player.removeStatusEffect(effect.get());
					} else if (decrementEffectAmount > 0) {
						int newAmplifier = -1;
						StatusEffectInstance statusEffectInstance = player.getStatusEffect(effect.get());
						if (statusEffectInstance != null) {
							int oldAmplifier = statusEffectInstance.getAmplifier();
							newAmplifier = oldAmplifier - decrementEffectAmount;
						}
						player.removeStatusEffect(effect.get());
						if (newAmplifier >= 0) {
							player.addStatusEffect(new StatusEffectInstance(effect.get(), statusEffectInstance.getDuration(), newAmplifier, statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles(), statusEffectInstance.shouldShowIcon()));
						}
					}
				}
			}
		}
	}

	// shootProjectile: LaunchProperties
	@WrapOperation(
			method = "shootProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellHelper$ImpactContext;I)V",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$LaunchProperties;copy()Lnet/spell_engine/api/spell/Spell$LaunchProperties;", remap = false)
	)
	private static Spell.LaunchProperties spellengineextension$wrap_shootProjectile_mutableLaunchProperties(
			Spell.LaunchProperties instance, Operation<Spell.LaunchProperties> original, @Local LivingEntity caster
	) {
		Spell.LaunchProperties copy = original.call(instance);
		ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;

		if (serverConfig.spell_launch_properties_extra_launch_count_attribute_allowed.get()
				&& ((DuckSpellLaunchPropertiesMixin) copy).spellengineextension$respectExtraLaunchCountAttribute()) {
			copy.extra_launch_count += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchCount());
		}
		if (serverConfig.spell_launch_properties_extra_launch_delay_attribute_allowed.get()
				&& ((DuckSpellLaunchPropertiesMixin) copy).spellengineextension$respectExtraLaunchDelayAttribute()) {
			copy.extra_launch_delay += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchDelay());
		}
		if (serverConfig.spell_launch_properties_extra_velocity_attribute_allowed.get()
				&& ((DuckSpellLaunchPropertiesMixin) copy).spellengineextension$respectExtraVelocityAttribute()) {
			copy.velocity += ((DuckLivingEntityMixin) caster).spellengineextension$getExtraVelocity();
		}

		return copy;
	}

	// shootProjectile: Perks
	@WrapOperation(
			method = "shootProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellHelper$ImpactContext;I)V",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;copy()Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;", remap = false)
	)
	private static Spell.ProjectileData.Perks spellengineextension$wrap_shootProjectile_mutablePerks(
			Spell.ProjectileData.Perks instance, Operation<Spell.ProjectileData.Perks> original, @Local(argsOnly = true) LivingEntity caster
	) {
		Spell.ProjectileData.Perks copy = original.call(instance);
		ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;

		if (serverConfig.spell_projectile_perk_extra_ricochet_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraRicochetAttribute()) {
			copy.ricochet += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochet());
		}
		if (serverConfig.spell_projectile_perk_extra_ricochet_range_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraRicochetRangeAttribute()) {
			copy.ricochet_range += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochetRange());
		}
		if (serverConfig.spell_projectile_perk_extra_bounce_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraBounceAttribute()) {
			copy.bounce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraBounce());
		}
		if (serverConfig.spell_projectile_perk_extra_pierce_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraPierceAttribute()) {
			copy.pierce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraPierce());
		}
		if (serverConfig.spell_projectile_perk_extra_chain_reaction_size_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraChainReactionSizeAttribute()) {
			copy.chain_reaction_size += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionSize());
		}
		if (serverConfig.spell_projectile_perk_extra_chain_reaction_triggers_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraChainReactionTriggersAttribute()) {
			copy.chain_reaction_triggers += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionTriggers());
		}

		return copy;
	}

	// fallProjectile: LaunchProperties
	@WrapOperation(
			method = "fallProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellHelper$ImpactContext;I)Z",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$LaunchProperties;copy()Lnet/spell_engine/api/spell/Spell$LaunchProperties;", remap = false)
	)
	private static Spell.LaunchProperties spellengineextension$wrap_fallProjectile_mutableLaunchProperties(
			Spell.LaunchProperties instance, Operation<Spell.LaunchProperties> original, @Local LivingEntity caster
	) {
		Spell.LaunchProperties copy = original.call(instance);
		ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;

		if (serverConfig.spell_launch_properties_extra_launch_count_attribute_allowed.get()
				&& ((DuckSpellLaunchPropertiesMixin) copy).spellengineextension$respectExtraLaunchCountAttribute()) {
			copy.extra_launch_count += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchCount());
		}
		if (serverConfig.spell_launch_properties_extra_launch_delay_attribute_allowed.get()
				&& ((DuckSpellLaunchPropertiesMixin) copy).spellengineextension$respectExtraLaunchDelayAttribute()) {
			copy.extra_launch_delay += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchDelay());
		}
		if (serverConfig.spell_launch_properties_extra_velocity_attribute_allowed.get()
				&& ((DuckSpellLaunchPropertiesMixin) copy).spellengineextension$respectExtraVelocityAttribute()) {
			copy.velocity += ((DuckLivingEntityMixin) caster).spellengineextension$getExtraVelocity();
		}

		return copy;
	}

	// fallProjectile: Perks
	@WrapOperation(
			method = "fallProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/internals/SpellHelper$ImpactContext;I)Z",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;copy()Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;", remap = false)
	)
	private static Spell.ProjectileData.Perks spellengineextension$wrap_fallProjectile_mutablePerks(
			Spell.ProjectileData.Perks instance, Operation<Spell.ProjectileData.Perks> original, @Local(argsOnly = true) LivingEntity caster
	) {
		Spell.ProjectileData.Perks copy = original.call(instance);
		ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;

		if (serverConfig.spell_projectile_perk_extra_ricochet_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraRicochetAttribute()) {
			copy.ricochet += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochet());
		}
		if (serverConfig.spell_projectile_perk_extra_ricochet_range_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraRicochetRangeAttribute()) {
			copy.ricochet_range += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochetRange());
		}
		if (serverConfig.spell_projectile_perk_extra_bounce_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraBounceAttribute()) {
			copy.bounce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraBounce());
		}
		if (serverConfig.spell_projectile_perk_extra_pierce_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraPierceAttribute()) {
			copy.pierce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraPierce());
		}
		if (serverConfig.spell_projectile_perk_extra_chain_reaction_size_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraChainReactionSizeAttribute()) {
			copy.chain_reaction_size += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionSize());
		}
		if (serverConfig.spell_projectile_perk_extra_chain_reaction_triggers_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) copy).spellengineextension$respectExtraChainReactionTriggersAttribute()) {
			copy.chain_reaction_triggers += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionTriggers());
		}

		return copy;
	}

	@WrapOperation(
			method = "performImpact(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/api/spell/Spell$Impact;Lnet/spell_engine/internals/SpellHelper$ImpactContext;Ljava/util/Collection;)Z",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	)
	private static boolean spellengineextension$wrap_damage(Entity instance, DamageSource source, float amount, Operation<Boolean> original, @Local(argsOnly = true) LivingEntity caster, @Local Spell.Impact.Action.Damage damageData) {

		// direct damage
		double directDamageAmount = ((DuckSpellImpactActionDamageMixin) damageData).spellengineextension$getDirectDamage();

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
	private static void spellengineextension$wrap_heal(LivingEntity instance, float amount, Operation<Void> original, @Local Spell.Impact.Action.Heal healData) {

		// direct heal
		double directHealAmount = ((DuckSpellImpactActionHealMixin) healData).spellengineextension$getDirectHeal();

		original.call(instance, directHealAmount > 0 ? ((float) directHealAmount) : amount);
	}
}
