package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellCostMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Spell.Cost.class)
public class SpellCostMixin implements DuckSpellCostMixin {
    @Unique
    private boolean check_health_cost = false;
    @Unique
    private boolean check_mana_cost = true;
    @Unique
    private boolean check_stamina_cost = false;
    @Unique
    private boolean health_cost_multiplier_applies = true;
    @Unique
    private boolean mana_cost_multiplier_applies = true;
    @Unique
    private boolean stamina_cost_multiplier_applies = true;
    @Unique
    private boolean consume_self = false;
    @Unique
    private int decrement_effect_amount = -1;
    @Unique
    private float mana_cost = 0.0F;
    @Unique
    private float health_cost = 0.0F;
    @Unique
    private float stamina_cost = 0.0F;

    @Override
    public boolean spellengineextension$checkHealthCost() {
        return this.check_health_cost;
    }

    @Override
    public void spellengineextension$setCheckHealthCost(boolean checkHealthCost) {
        this.check_health_cost = checkHealthCost;
    }

    @Override
    public boolean spellengineextension$checkManaCost() {
        return this.check_mana_cost;
    }

    @Override
    public void spellengineextension$setCheckManaCost(boolean checkManaCost) {
        this.check_mana_cost = checkManaCost;
    }

    @Override
    public boolean spellengineextension$checkStaminaCost() {
        return this.check_stamina_cost;
    }

    @Override
    public void spellengineextension$setCheckStaminaCost(boolean checkStaminaCost) {
        this.check_stamina_cost = checkStaminaCost;
    }

    @Override
    public boolean spellengineextension$healthCostMultiplierApplies() {
        return this.health_cost_multiplier_applies;
    }

    @Override
    public void spellengineextension$setHealthCostMultiplierApplies(boolean healthCostMultiplierApplies) {
        this.health_cost_multiplier_applies = healthCostMultiplierApplies;
    }

    @Override
    public boolean spellengineextension$manaCostMultiplierApplies() {
        return this.mana_cost_multiplier_applies;
    }

    @Override
    public void spellengineextension$setManaCostMultiplierApplies(boolean manaCostMultiplierApplies) {
        this.mana_cost_multiplier_applies = manaCostMultiplierApplies;
    }

    @Override
    public boolean spellengineextension$staminaCostMultiplierApplies() {
        return this.stamina_cost_multiplier_applies;
    }

    @Override
    public void spellengineextension$setStaminaCostMultiplierApplies(boolean staminaCostMultiplierApplies) {
        this.stamina_cost_multiplier_applies = staminaCostMultiplierApplies;
    }

    @Override
    public float spellengineextension$getManaCost() {
        return this.mana_cost;
    }

    @Override
    public void spellengineextension$setManaCost(float manaCost) {
        this.mana_cost = manaCost;
    }

    @Override
    public float spellengineextension$getHealthCost() {
        return this.health_cost;
    }

    @Override
    public void spellengineextension$setHealthCost(float healthCost) {
        this.health_cost = healthCost;
    }

    @Override
    public float spellengineextension$getStaminaCost() {
        return this.stamina_cost;
    }

    @Override
    public void spellengineextension$setStaminaCost(float staminaCost) {
        this.stamina_cost = staminaCost;
    }

    @Override
    public boolean spellengineextension$consumeSelf() {
        return this.consume_self;
    }

    @Override
    public void spellengineextension$setConsumeSelf(boolean consumeSelf) {
        this.consume_self = consumeSelf;
    }

    @Override
    public int spellengineextension$getDecrementEffectAmount() {
        return this.decrement_effect_amount;
    }

    @Override
    public void spellengineextension$setDecrementEffectAmount(int decrementEffectAmount) {
        this.decrement_effect_amount = decrementEffectAmount;
    }
}
