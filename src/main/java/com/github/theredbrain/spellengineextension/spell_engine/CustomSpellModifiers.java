package com.github.theredbrain.spellengineextension.spell_engine;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellModifiers;

import java.util.Iterator;
import java.util.List;

public class CustomSpellModifiers {

	public static float getModifiedHealthCost(PlayerEntity player, RegistryEntry<Spell> spellEntry) {
		List<Spell.Modifier> modifiers = SpellModifiers.of(player, spellEntry);
		Spell spell = spellEntry.value();
		float healthCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getHealthCost();
		Spell.Modifier modifier;
		for (Iterator<Spell.Modifier> var4 = modifiers.iterator(); var4.hasNext(); healthCost += ((DuckSpellModifierMixin) modifier).spellengineextension$getAdditionalHealthCost()) {
			modifier = (Spell.Modifier) var4.next();
		}
		return healthCost;
	}

	public static float getModifiedManaCost(PlayerEntity player, RegistryEntry<Spell> spellEntry) {
		List<Spell.Modifier> modifiers = SpellModifiers.of(player, spellEntry);
		Spell spell = spellEntry.value();
		float manaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getManaCost();
		Spell.Modifier modifier;
		for (Iterator<Spell.Modifier> var4 = modifiers.iterator(); var4.hasNext(); manaCost += ((DuckSpellModifierMixin) modifier).spellengineextension$getAdditionalManaCost()) {
			modifier = (Spell.Modifier) var4.next();
		}
		return manaCost;
	}

	public static float getModifiedStaminaCost(PlayerEntity player, RegistryEntry<Spell> spellEntry) {
		List<Spell.Modifier> modifiers = SpellModifiers.of(player, spellEntry);
		Spell spell = spellEntry.value();
		float staminaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getStaminaCost();
		Spell.Modifier modifier;
		for (Iterator<Spell.Modifier> var4 = modifiers.iterator(); var4.hasNext(); staminaCost += ((DuckSpellModifierMixin) modifier).spellengineextension$getAdditionalStaminaCost()) {
			modifier = (Spell.Modifier) var4.next();
		}
		return staminaCost;
	}

	public static double getModifiedDirectDamage(PlayerEntity player, Spell.Impact.Action.Damage damageData, RegistryEntry<Spell> spellEntry) {
		List<Spell.Modifier> modifiers = SpellModifiers.of(player, spellEntry);
		Spell spell = spellEntry.value();
		double directDamage = ((DuckSpellImpactActionDamageMixin) damageData).spellengineextension$getDirectDamage();
		Spell.Modifier modifier;
		for (Iterator<Spell.Modifier> var4 = modifiers.iterator(); var4.hasNext(); directDamage += ((DuckSpellModifierMixin) modifier).spellengineextension$getAdditionalDirectDamage()) {
			modifier = (Spell.Modifier) var4.next();
		}
		return directDamage;
	}

	public static double getModifiedDirectHealing(PlayerEntity player, Spell.Impact.Action.Heal healData, RegistryEntry<Spell> spellEntry) {
		List<Spell.Modifier> modifiers = SpellModifiers.of(player, spellEntry);
		Spell spell = spellEntry.value();
		double directHeal = ((DuckSpellImpactActionHealMixin) healData).spellengineextension$getDirectHeal();
		Spell.Modifier modifier;
		for (Iterator<Spell.Modifier> var4 = modifiers.iterator(); var4.hasNext(); directHeal += ((DuckSpellModifierMixin) modifier).spellengineextension$getAdditionalDirectHealing()) {
			modifier = (Spell.Modifier) var4.next();
		}
		return directHeal;
	}

	public static String getModifiedEffectCostId(PlayerEntity player, RegistryEntry<Spell> spellEntry) {
		List<Spell.Modifier> modifiers = SpellModifiers.of(player, spellEntry);
		Spell spell = spellEntry.value();
		String effectId = spell.cost.effect_id;
		for (Spell.Modifier modifier : modifiers) {
			effectId = ((DuckSpellModifierMixin) modifier).spellengineextension$getReplacedEffectCostId();
		}
		return effectId;
	}

	public static int getModifiedDecrementEffectCostAmount(PlayerEntity player, RegistryEntry<Spell> spellEntry) {
		List<Spell.Modifier> modifiers = SpellModifiers.of(player, spellEntry);
		Spell spell = spellEntry.value();
		int decrementEffectAmount = ((DuckSpellCostMixin) spell.cost).spellengineextension$getDecrementEffectAmount();
		for (Spell.Modifier modifier : modifiers) {
			decrementEffectAmount = ((DuckSpellModifierMixin) modifier).spellengineextension$getReplacedDecrementEffectCostAmount();
		}
		return decrementEffectAmount;
	}
}
