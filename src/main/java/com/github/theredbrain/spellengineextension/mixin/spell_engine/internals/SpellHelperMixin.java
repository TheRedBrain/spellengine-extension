package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.entity.DuckLivingEntityMixin;
import com.github.theredbrain.spellengineextension.entity.damage.DuckDamageSourcesMixin;
import com.github.theredbrain.spellengineextension.spell_engine.CustomSpellModifiers;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellCostMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellImpactActionDamageMixin;
import com.github.theredbrain.spellengineextension.spell_engine.ExtendedSpellHelper;
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
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.Ammo;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellBatcher;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.container.SpellContainerSource;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_engine.utils.WorldScheduler;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(SpellHelper.class)
@SuppressWarnings("UnreachableCode")
public abstract class SpellHelperMixin {

	/**
	 * @author TheRedBrain
	 * @reason check for custom cost
	 */
	@WrapOperation(
			method = "attemptCasting(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Identifier;Z)Lnet/spell_engine/internals/casting/SpellCast$Attempt;",
			at = @At(value = "INVOKE", target = "Lnet/spell_engine/internals/casting/SpellCast$Attempt;success()Lnet/spell_engine/internals/casting/SpellCast$Attempt;")
	)
	private static SpellCast.Attempt spellengineextension$attemptCasting(Operation<SpellCast.Attempt> original, @Local(argsOnly = true) PlayerEntity player, @Local RegistryEntry.Reference<Spell> spellEntry) {
		return ExtendedSpellHelper.checkForCustomSpellCost(player, spellEntry);
	}

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

	/**
	 * @author TheRedBrain
	 * @reason integrate health cost, mana cost, stamina cost, reducing amplifier of status effect cost instead of removing them, self consuming of casting item
	 */
	@Overwrite
	private static void consumeSpellCost(PlayerEntity player, float progress, SpellContainerSource.SourcedContainer spellSource, Identifier spellId, RegistryEntry<Spell> spellEntry, ItemStack heldItemStack, Ammo.Result ammoResult, boolean scheduled) {
		Spell spell = (Spell) spellEntry.value();
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
			if (spellEngineExtensionConfig.spell_cost_health_allowed.get() && !((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingHealthCost()) {
				float healthCost = CustomSpellModifiers.getModifiedHealthCost(player, spellEntry);
				if (((DuckSpellCostMixin) spell.cost).spellengineextension$healthCostMultiplierApplies()) {
					healthCost = healthCost * ((DuckLivingEntityMixin) player).spellengineextension$getHealthSpellCostMultiplier();
				}
				if (healthCost > 0.0F) {
					player.damage(((DuckDamageSourcesMixin) player.getDamageSources()).spellengineextension$bloodMagicCasting(), healthCost);
				}
			}

			// mana cost
			if (SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get() && !((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingManaCost()) {
				float manaCost = CustomSpellModifiers.getModifiedManaCost(player, spellEntry);
				if (((DuckSpellCostMixin) spell.cost).spellengineextension$manaCostMultiplierApplies()) {
					manaCost = manaCost * ((DuckLivingEntityMixin) player).spellengineextension$getManaSpellCostMultiplier();
				}
				if (manaCost > 0.0F) {
					SpellEngineExtension.addMana(player, -manaCost);
				}
			}

			// stamina cost
			if (SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get() && !((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingStaminaCost()) {
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
