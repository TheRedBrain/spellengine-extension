package com.github.theredbrain.spellengineextension.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.api.spell.container.SpellContainer;

import java.util.ArrayList;
import java.util.List;

public interface ProvidesSpell {
	List<String> spellengineextension$providedSpells();

	StatusEffect spellengineextension$setProvidedSpells(List<String> providedSpells);

	static void configure(StatusEffect effect, List<String> providedSpells) {
		((ProvidesSpell) effect).spellengineextension$setProvidedSpells(providedSpells);
	}

	static SpellContainer getStatusEffectContainer(PlayerEntity player) {
		List<String> spellList = new ArrayList<>();
		for (StatusEffectInstance statusEffectInstance : player.getStatusEffects()) {

			List<String> providedSpells = ((ProvidesSpell) statusEffectInstance.getEffectType().value()).spellengineextension$providedSpells();
			if (providedSpells != null && !providedSpells.isEmpty()) {
				spellList.addAll(providedSpells);
			}
		}
		return new SpellContainer(SpellContainer.ContentType.ANY, false, "", spellList.size(), spellList);
	}
}
