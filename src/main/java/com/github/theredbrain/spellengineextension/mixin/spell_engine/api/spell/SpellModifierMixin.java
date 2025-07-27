package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellModifierMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Spell.Modifier.class)
public class SpellModifierMixin implements DuckSpellModifierMixin {

	@Unique
	private float additional_health_cost = 0.0F;

	@Unique
	private float additional_mana_cost = 0.0F;

	@Unique
	private float additional_stamina_cost = 0.0F;

	@Unique
	private double additional_direct_damage = 0.0;

	@Unique
	private double additional_direct_healing = 0.0;

	@Override
	public float spellengineextension$getAdditionalHealthCost() {
		return this.additional_health_cost;
	}

	@Override
	public void spellengineextension$setAdditionalHealthCost(float additionalHealthCost) {
		this.additional_health_cost = additionalHealthCost;
	}

	@Override
	public float spellengineextension$getAdditionalManaCost() {
		return this.additional_mana_cost;
	}

	@Override
	public void spellengineextension$setAdditionalManaCost(float additionalManaCost) {
		this.additional_mana_cost = additionalManaCost;
	}

	@Override
	public float spellengineextension$getAdditionalStaminaCost() {
		return this.additional_stamina_cost;
	}

	@Override
	public void spellengineextension$setAdditionalStaminaCost(float additionalStaminaCost) {
		this.additional_stamina_cost = additionalStaminaCost;
	}

	@Override
	public double spellengineextension$getAdditionalDirectDamage() {
		return this.additional_direct_damage;
	}

	@Override
	public void spellengineextension$setAdditionalDirectDamage(double additionalDirectDamage) {
		this.additional_direct_damage = additionalDirectDamage;
	}

	@Override
	public double spellengineextension$getAdditionalDirectHealing() {
		return this.additional_direct_healing;
	}

	@Override
	public void spellengineextension$setAdditionalDirectHealing(double additionalDirectHealing) {
		this.additional_direct_healing = additionalDirectHealing;
	}

}
