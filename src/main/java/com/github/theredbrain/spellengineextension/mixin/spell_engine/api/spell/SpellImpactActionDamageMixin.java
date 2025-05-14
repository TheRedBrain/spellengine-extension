package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellImpactActionDamageMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Spell.Impact.Action.Damage.class)
public class SpellImpactActionDamageMixin implements DuckSpellImpactActionDamageMixin {
    @Unique
    private double direct_damage = 0.0;
    @Unique
    private String damage_type_override = "";

    @Override
    public double spellengineextension$getDirectDamage() {
        return direct_damage;
    }

    @Override
    public void spellengineextension$setDirectDamage(double directDamage) {
        this.direct_damage = directDamage;
    }

    @Override
    public String spellengineextension$getDamageTypeOverride() {
        return damage_type_override;
    }

    @Override
    public void spellengineextension$setDamageTypeOverride(String damageTypeOverride) {
        this.damage_type_override = damageTypeOverride;
    }
}
