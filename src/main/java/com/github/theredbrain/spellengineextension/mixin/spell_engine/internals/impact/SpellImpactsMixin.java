package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals.impact;

import com.github.theredbrain.spellengineextension.spell_engine.CustomSpellModifiers;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellImpactActionDamageMixin;
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
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.impact.SpellImpacts;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpellImpacts.class)
public class SpellImpactsMixin {

	@WrapOperation(
			method = "performImpact(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/api/spell/Spell$Impact;Lnet/spell_engine/internals/SpellExecution$ImpactContext;Ljava/util/Collection;)Z",
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
			method = "performImpact(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/spell_engine/api/spell/Spell$Impact;Lnet/spell_engine/internals/SpellExecution$ImpactContext;Ljava/util/Collection;)Z",
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
