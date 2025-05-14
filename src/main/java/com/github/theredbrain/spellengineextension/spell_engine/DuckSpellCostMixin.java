package com.github.theredbrain.spellengineextension.spell_engine;

public interface DuckSpellCostMixin {
	boolean spellengineextension$checkHealthCost();

	void spellengineextension$setCheckHealthCost(boolean checkHealthCost);

	boolean spellengineextension$checkManaCost();

	void spellengineextension$setCheckManaCost(boolean checkManaCost);

	boolean spellengineextension$checkStaminaCost();

	void spellengineextension$setCheckStaminaCost(boolean checkStaminaCost);

	boolean spellengineextension$healthCostMultiplierApplies();

	void spellengineextension$setHealthCostMultiplierApplies(boolean healthCostMultiplierApplies);

	boolean spellengineextension$manaCostMultiplierApplies();

	void spellengineextension$setManaCostMultiplierApplies(boolean manaCostMultiplierApplies);

	boolean spellengineextension$staminaCostMultiplierApplies();

	void spellengineextension$setStaminaCostMultiplierApplies(boolean staminaCostMultiplierApplies);

	float spellengineextension$getManaCost();

	void spellengineextension$setManaCost(float manaCost);

	float spellengineextension$getHealthCost();

	void spellengineextension$setHealthCost(float healthCost);

	float spellengineextension$getStaminaCost();

	void spellengineextension$setStaminaCost(float staminaCost);

	boolean spellengineextension$consumeSelf();

	void spellengineextension$setConsumeSelf(boolean consumeSelf);

	int spellengineextension$getDecrementEffectAmount();

	void spellengineextension$setDecrementEffectAmount(int decrementEffectAmount);
}
