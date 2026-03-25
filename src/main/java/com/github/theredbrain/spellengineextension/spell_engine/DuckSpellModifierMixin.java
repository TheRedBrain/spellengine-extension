package com.github.theredbrain.spellengineextension.spell_engine;

import org.jetbrains.annotations.Nullable;

public interface DuckSpellModifierMixin {

	float spellengineextension$getAdditionalHealthCost();

	void spellengineextension$setAdditionalHealthCost(float additionalHealthCost);

	float spellengineextension$getAdditionalManaCost();

	void spellengineextension$setAdditionalManaCost(float additionalManaCost);

	float spellengineextension$getAdditionalStaminaCost();

	void spellengineextension$setAdditionalStaminaCost(float additionalStaminaCost);

	double spellengineextension$getAdditionalDirectDamage();

	void spellengineextension$setAdditionalDirectDamage(double additionalDirectDamage);

	double spellengineextension$getAdditionalDirectHealing();

	void spellengineextension$setAdditionalDirectHealing(double additionalDirectHealing);

	@Nullable String spellengineextension$getReplacedEffectCostId();

	void spellengineextension$setReplacedEffectCostId(String replacedEffectCostId);

	int spellengineextension$getReplacedDecrementEffectCostAmount();

	void spellengineextension$setReplacedDecrementEffectCostAmount(int replacedDecrementEffectCostAmount);

}
