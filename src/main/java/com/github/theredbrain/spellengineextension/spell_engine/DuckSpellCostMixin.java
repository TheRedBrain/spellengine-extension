package com.github.theredbrain.spellengineextension.spell_engine;

public interface DuckSpellCostMixin {
    boolean betteradventuremode$checkHealthCost();
    boolean betteradventuremode$checkManaCost();
    boolean betteradventuremode$checkStaminaCost();
    boolean betteradventuremode$checkEffectCost();
    float betteradventuremode$getManaCost();
    float betteradventuremode$getHealthCost();
    float betteradventuremode$getStaminaCost();
    boolean betteradventuremode$consumeSelf();
    int betteradventuremode$getDecrementEffectAmount();
}
