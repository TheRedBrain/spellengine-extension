package com.github.theredbrain.spellengineextension.spell_engine;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.entity.DuckLivingEntityMixin;
import com.github.theredbrain.spellengineextension.entity.damage.DuckDamageSourcesMixin;
import com.github.theredbrain.spellengineextension.entity.player.DuckPlayerEntityMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.casting.SpellCast;

import java.util.Optional;

public class ExtendedSpellHelper {

	public static SpellCast.Attempt checkForCustomSpellCost(PlayerEntity player, RegistryEntry<Spell> spellEntry) {

		ServerConfig spellEngineExtensionConfig = SpellEngineExtension.SERVER_CONFIG;
		Spell spell = spellEntry.value();

		if (!player.isCreative() && spellEngineExtensionConfig.spell_cost_health_allowed.get()) {
			float healthCost = CustomSpellModifiers.getModifiedHealthCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$healthCostMultiplierApplies()) {
				healthCost = healthCost * ((DuckLivingEntityMixin) player).spellengineextension$getHealthSpellCostMultiplier();
			}
			if (healthCost > 0 && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkHealthCost() && healthCost > player.getHealth()) {
				player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_health"), true);
				return SpellCast.Attempt.none();
			}
		}
		if (!player.isCreative() && SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get()) {
			float manaCost = CustomSpellModifiers.getModifiedManaCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$manaCostMultiplierApplies()) {
				manaCost = manaCost * ((DuckLivingEntityMixin) player).spellengineextension$getManaSpellCostMultiplier();
			}
			float currentMana = SpellEngineExtension.getCurrentMana(player);
			if (manaCost > 0 && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkMana() && (currentMana < 0 || (manaCost > currentMana && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkManaCost()))) {
				player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_mana"), true);
				return SpellCast.Attempt.none();
			}
		}
		if (!player.isCreative() && SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get()) {
			float staminaCost = CustomSpellModifiers.getModifiedStaminaCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$staminaCostMultiplierApplies()) {
				staminaCost = staminaCost * ((DuckLivingEntityMixin) player).spellengineextension$getStaminaSpellCostMultiplier();
			}
			float currentStamina = SpellEngineExtension.getCurrentStamina(player);
			if (staminaCost > 0 && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkStamina() && (currentStamina < 0 || (staminaCost > currentStamina && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkStaminaCost()))) {
				player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_stamina"), true);
				return SpellCast.Attempt.none();
			}
		}
		if (spellEngineExtensionConfig.spell_cost_custom_effects_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkEffectCost()) {
			String custom_effect_id = CustomSpellModifiers.getModifiedEffectCostId(player, spellEntry);
			if (!custom_effect_id.isEmpty()) {
				Optional<RegistryEntry.Reference<StatusEffect>> effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(custom_effect_id));
				if (effect.isPresent()) {
					if (!player.hasStatusEffect(effect.get())) {
						player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_status_effect", Text.translatable(effect.get().value().getTranslationKey()).getString()), true);
						return SpellCast.Attempt.none();
					} else {
						StatusEffectInstance statusEffectInstance = player.getStatusEffect(effect.get());
						if (statusEffectInstance != null) {
							int decrementEffectAmount = CustomSpellModifiers.getModifiedDecrementEffectCostAmount(player, spellEntry);
							if (decrementEffectAmount > 0 && statusEffectInstance.getAmplifier() + 1 < decrementEffectAmount) {
								player.sendMessage(Text.translatable("hud.cast_attempt_error.status_effect_amplifier_too_low", Text.translatable(effect.get().value().getTranslationKey()).getString()), true);
								return SpellCast.Attempt.none();
							}
						}
					}
				}
			}
		}
		return SpellCast.Attempt.success();
	}

	public static void consumeCustomSpellCost(PlayerEntity player, RegistryEntry<Spell> spellEntry, ItemStack spellCastingItem) {

		ServerConfig spellEngineExtensionConfig = SpellEngineExtension.SERVER_CONFIG;
		Spell spell = spellEntry.value();

		// health cost
		if (!player.isCreative() && spellEngineExtensionConfig.spell_cost_health_allowed.get() && !((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingHealthCost()) {
			float healthCost = CustomSpellModifiers.getModifiedHealthCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$healthCostMultiplierApplies()) {
				healthCost = healthCost * ((DuckLivingEntityMixin) player).spellengineextension$getHealthSpellCostMultiplier();
			}
			if (healthCost > 0.0F) {
				player.damage(((DuckDamageSourcesMixin) player.getDamageSources()).spellengineextension$bloodMagicCasting(), healthCost);
			}
		}

		// mana cost
		if (!player.isCreative() && SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get() && !((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingManaCost()) {
			float manaCost = CustomSpellModifiers.getModifiedManaCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$manaCostMultiplierApplies()) {
				manaCost = manaCost * ((DuckLivingEntityMixin) player).spellengineextension$getManaSpellCostMultiplier();
			}
			if (manaCost > 0.0F) {
				SpellEngineExtension.addMana(player, -manaCost);
			}
		}

		// stamina cost
		if (!player.isCreative() && SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get() && !((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingStaminaCost()) {
			float staminaCost = CustomSpellModifiers.getModifiedStaminaCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$addItemUseStaminaCostAttributeValue()) {
				staminaCost = staminaCost + SpellEngineExtension.getItemUseStaminaCost(player);
			}
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$staminaCostMultiplierApplies()) {
				staminaCost = staminaCost * ((DuckLivingEntityMixin) player).spellengineextension$getStaminaSpellCostMultiplier();
			}
			if (staminaCost > 0.0F) {
				SpellEngineExtension.addStamina(player, -staminaCost);
			}
		}

		// consume spell casting item
		if (((DuckSpellCostMixin) spell.cost).spellengineextension$consumeSelf()) {
			player.incrementStat(Stats.USED.getOrCreateStat(spellCastingItem.getItem()));
			if (!player.isCreative()) {
				spellCastingItem.decrement(1);
			}
		}

		// consume custom status effect cost
		if (spellEngineExtensionConfig.spell_cost_custom_effects_allowed.get()) {
			String custom_effect_id = CustomSpellModifiers.getModifiedEffectCostId(player, spellEntry);
			if (!custom_effect_id.isEmpty()) {
				Optional<RegistryEntry.Reference<StatusEffect>> optionalStatusEffectReference = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(custom_effect_id));
				if (optionalStatusEffectReference.isPresent()) {
					int decrementEffectAmount = CustomSpellModifiers.getModifiedDecrementEffectCostAmount(player, spellEntry);
					if (decrementEffectAmount < 0) {
						player.removeStatusEffect(optionalStatusEffectReference.get());
					} else if (decrementEffectAmount > 0) {
						int newAmplifier = -1;
						StatusEffectInstance statusEffectInstance = player.getStatusEffect(optionalStatusEffectReference.get());
						if (statusEffectInstance != null) {
							int oldAmplifier = statusEffectInstance.getAmplifier();
							newAmplifier = oldAmplifier - decrementEffectAmount;
						}
						player.removeStatusEffect(optionalStatusEffectReference.get());
						if (newAmplifier >= 0) {
							player.addStatusEffect(new StatusEffectInstance(optionalStatusEffectReference.get(), statusEffectInstance.getDuration(), newAmplifier, statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles(), statusEffectInstance.shouldShowIcon()));
						}
					}
				}
			}
		}

	}

	public static void applyChannelingCost(PlayerEntity player, RegistryEntry<Spell> spellEntry) {

		ServerConfig spellEngineExtensionConfig = SpellEngineExtension.SERVER_CONFIG;
		Spell spell = spellEntry.value();

		// health cost
		if (!player.isCreative() && spellEngineExtensionConfig.spell_cost_health_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingHealthCost()) {
			float healthCost = CustomSpellModifiers.getModifiedHealthCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$healthCostMultiplierApplies()) {
				healthCost = healthCost * ((DuckLivingEntityMixin) player).spellengineextension$getHealthSpellCostMultiplier();
			}
			if (healthCost > 0.0F) {
				player.damage(((DuckDamageSourcesMixin) player.getDamageSources()).spellengineextension$bloodMagicCasting(), healthCost);
			}
		}

		// mana cost
		if (!player.isCreative() && SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingManaCost()) {
			float manaCost = CustomSpellModifiers.getModifiedManaCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$manaCostMultiplierApplies()) {
				manaCost = manaCost * ((DuckLivingEntityMixin) player).spellengineextension$getManaSpellCostMultiplier();
			}
			if (manaCost > 0.0F) {
				SpellEngineExtension.addMana(player, -manaCost);
			}
		}

		// stamina cost
		if (!player.isCreative() && SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingStaminaCost()) {
			float staminaCost = CustomSpellModifiers.getModifiedStaminaCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$addItemUseStaminaCostAttributeValue()) {
				staminaCost = staminaCost + SpellEngineExtension.getItemUseStaminaCost(player);
			}
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$staminaCostMultiplierApplies()) {
				staminaCost = staminaCost * ((DuckLivingEntityMixin) player).spellengineextension$getStaminaSpellCostMultiplier();
			}
			if (staminaCost > 0.0F) {
				SpellEngineExtension.addStamina(player, -staminaCost);
			}
		}
	}

	public static void applyAfterCastingMovementLockingTicks(PlayerEntity player, RegistryEntry<Spell> spellEntry) {

		if (SpellEngineExtension.SERVER_CONFIG.enable_movement_locking_spell_casting.get() && spellEntry.isIn(SpellEngineExtension.ENABLES_MOVEMENT_LOCKING_DURING_CASTING)) {
			((DuckPlayerEntityMixin) player).spellengineextension$setMovementLockingTicks(Math.max(0, ((DuckSpellActiveCastMixin) spellEntry.value().active.cast).spellengineextension$getAfterCastingMovementLockingTicks()));
		}

	}

	public static Spell.LaunchProperties applySpellLaunchPropertiesAttributes(Spell.LaunchProperties launchProperties, LivingEntity caster) {

		ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;

		if (serverConfig.spell_launch_properties_extra_launch_count_attribute_allowed.get()
				&& ((DuckSpellLaunchPropertiesMixin) launchProperties).spellengineextension$respectExtraLaunchCountAttribute()) {
			launchProperties.extra_launch_count += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchCount());
		}
		if (serverConfig.spell_launch_properties_extra_launch_delay_attribute_allowed.get()
				&& ((DuckSpellLaunchPropertiesMixin) launchProperties).spellengineextension$respectExtraLaunchDelayAttribute()) {
			launchProperties.extra_launch_delay += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchDelay());
		}
		if (serverConfig.spell_launch_properties_extra_velocity_attribute_allowed.get()
				&& ((DuckSpellLaunchPropertiesMixin) launchProperties).spellengineextension$respectExtraVelocityAttribute()) {
			launchProperties.velocity += ((DuckLivingEntityMixin) caster).spellengineextension$getExtraVelocity();
		}

		return launchProperties;
	}

	public static Spell.ProjectileData.Perks applySpellProjectileDataPerkAttributes(Spell.ProjectileData.Perks perks, LivingEntity caster) {

		ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;

		if (serverConfig.spell_projectile_perk_extra_ricochet_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$respectExtraRicochetAttribute()) {
			perks.ricochet += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochet());
		}
		if (serverConfig.spell_projectile_perk_extra_ricochet_range_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$respectExtraRicochetRangeAttribute()) {
			perks.ricochet_range += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochetRange());
		}
		if (serverConfig.spell_projectile_perk_extra_bounce_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$respectExtraBounceAttribute()) {
			perks.bounce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraBounce());
		}
		if (serverConfig.spell_projectile_perk_extra_pierce_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$respectExtraPierceAttribute()) {
			perks.pierce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraPierce());
		}
		if (serverConfig.spell_projectile_perk_extra_chain_reaction_size_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$respectExtraChainReactionSizeAttribute()) {
			perks.chain_reaction_size += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionSize());
		}
		if (serverConfig.spell_projectile_perk_extra_chain_reaction_triggers_attribute_allowed.get()
				&& ((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$respectExtraChainReactionTriggersAttribute()) {
			perks.chain_reaction_triggers += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionTriggers());
		}

		return perks;
	}
}
