package com.github.theredbrain.spellengineextension.mixin.entity.effect;

import com.github.theredbrain.spellengineextension.entity.effect.ProvidesSpell;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(StatusEffect.class)
public class StatusEffectMixin implements ProvidesSpell {
	@Unique
	private List<String> spellengineextension$providedSpells = null;

	@Override
	public List<String> spellengineextension$providedSpells() {
		return this.spellengineextension$providedSpells;
	}

	@Override
	public StatusEffect spellengineextension$setProvidedSpells(List<String> providedSpells) {
		this.spellengineextension$providedSpells = providedSpells;
		return (StatusEffect) ((Object) this);
	}
}
