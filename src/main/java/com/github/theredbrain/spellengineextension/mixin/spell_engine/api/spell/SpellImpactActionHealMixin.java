package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellImpactActionHealMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Spell.Impact.Action.Heal.class)
public class SpellImpactActionHealMixin implements DuckSpellImpactActionHealMixin {
    @Unique
    private double direct_heal = 0.0;

    @Override
    public double betteradventuremode$getDirectHeal() {
        return direct_heal;
    }
}
