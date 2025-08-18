package com.github.theredbrain.spellengineextension.spell_engine;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.entity.DuckLivingEntityMixin;
import com.github.theredbrain.spellengineextension.entity.damage.DuckDamageSourcesMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.api.spell.Spell;

public class ExtendedSpellHelper {
	public static void applyChannelingCost(PlayerEntity player, RegistryEntry<Spell> spellEntry) {
		Spell spell = (Spell) spellEntry.value();

		var spellEngineExtensionConfig = SpellEngineExtension.SERVER_CONFIG;

		// health cost
		if (spellEngineExtensionConfig.spell_cost_health_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingHealthCost()) {
			float healthCost = CustomSpellModifiers.getModifiedHealthCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$healthCostMultiplierApplies()) {
				healthCost = healthCost * ((DuckLivingEntityMixin) player).spellengineextension$getHealthSpellCostMultiplier();
			}
			if (healthCost > 0.0F) {
				player.damage(((DuckDamageSourcesMixin) player.getDamageSources()).spellengineextension$bloodMagicCasting(), healthCost);
			}
		}

		// mana cost
		if (SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingManaCost()) {
			float manaCost = CustomSpellModifiers.getModifiedManaCost(player, spellEntry);
			if (((DuckSpellCostMixin) spell.cost).spellengineextension$manaCostMultiplierApplies()) {
				manaCost = manaCost * ((DuckLivingEntityMixin) player).spellengineextension$getManaSpellCostMultiplier();
			}
			if (manaCost > 0.0F) {
				SpellEngineExtension.addMana(player, -manaCost);
			}
		}

		// stamina cost
		if (SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$applyChannelingStaminaCost()) {
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
}
