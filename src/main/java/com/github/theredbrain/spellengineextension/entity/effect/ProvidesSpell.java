package com.github.theredbrain.spellengineextension.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.api.spell.container.SpellContainer;

import java.util.ArrayList;
import java.util.List;

public interface ProvidesSpell {
	List<List<String>> spellengineextension$providedSpells();

	StatusEffect spellengineextension$setProvidedSpells(List<List<String>> providedSpellsList);

	static void configure(StatusEffect effect, List<List<String>> providedSpellsList) {
		((ProvidesSpell) effect).spellengineextension$setProvidedSpells(providedSpellsList);
	}

	static SpellContainer getStatusEffectContainer(PlayerEntity player) {
		List<String> spellList = new ArrayList<>();
		for (StatusEffectInstance statusEffectInstance : player.getStatusEffects()) {
			List<List<String>> providedSpellsList = ((ProvidesSpell) statusEffectInstance.getEffectType().value()).spellengineextension$providedSpells();

			if (providedSpellsList != null && !providedSpellsList.isEmpty()) {
				List<String> providedSpells = providedSpellsList.get(Math.min(statusEffectInstance.getAmplifier(), providedSpellsList.size() - 1));

				if (providedSpells != null && !providedSpells.isEmpty()) {
					spellList.addAll(providedSpells);
				}
			}
		}
		return new SpellContainer(SpellContainer.ContentType.ANY, false, "", spellList.size(), spellList);
	}
}
