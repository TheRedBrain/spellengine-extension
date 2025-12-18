package com.github.theredbrain.spellengineextension.mixin.entity.effect;

import com.github.theredbrain.spellengineextension.entity.effect.ProvidesSpell;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(StatusEffect.class)
public class StatusEffectMixin implements ProvidesSpell {
	@Unique
	private List<List<String>> spellengineextension$providedSpellsList = null;

	@Override
	public List<List<String>> spellengineextension$providedSpells() {
		return this.spellengineextension$providedSpellsList;
	}

	@Override
	public StatusEffect spellengineextension$setProvidedSpells(List<List<String>> providedSpellsList) {
		this.spellengineextension$providedSpellsList = providedSpellsList;
		return (StatusEffect) ((Object) this);
	}
}
