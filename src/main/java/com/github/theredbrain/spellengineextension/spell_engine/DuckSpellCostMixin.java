package com.github.theredbrain.spellengineextension.spell_engine;

public interface DuckSpellCostMixin {
    boolean spellengineextension$checkHealthCost();
    boolean spellengineextension$checkManaCost();
    boolean spellengineextension$checkStaminaCost();
    boolean spellengineextension$healthCostMultiplierApplies();
    boolean spellengineextension$manaCostMultiplierApplies();
    boolean spellengineextension$staminaCostMultiplierApplies();
    float spellengineextension$getManaCost();
    float spellengineextension$getHealthCost();
    float spellengineextension$getStaminaCost();
    boolean spellengineextension$consumeSelf();
    int spellengineextension$getDecrementEffectAmount();
}
