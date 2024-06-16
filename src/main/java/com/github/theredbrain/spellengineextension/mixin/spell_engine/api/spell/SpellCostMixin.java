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
    public boolean betteradventuremode$checkHealthCost() {
        return this.check_health_cost;
    }

    @Override
    public boolean betteradventuremode$checkManaCost() {
        return this.check_mana_cost;
    }

    @Override
    public boolean betteradventuremode$checkStaminaCost() {
        return this.check_stamina_cost;
    }

    @Override
    public float betteradventuremode$getManaCost() {
        return this.mana_cost;
    }

    @Override
    public float betteradventuremode$getHealthCost() {
        return this.health_cost;
    }

    @Override
    public float betteradventuremode$getStaminaCost() {
        return this.stamina_cost;
    }

    @Override
    public boolean betteradventuremode$consumeSelf() {
        return this.consume_self;
    }

    @Override
    public int betteradventuremode$getDecrementEffectAmount() {
        return this.decrement_effect_amount;
    }
}
