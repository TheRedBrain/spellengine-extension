package com.github.theredbrain.spellengineextension.mixin.entity.damage;

import com.github.theredbrain.spellengineextension.entity.damage.DuckDamageSourcesMixin;
import com.github.theredbrain.spellengineextension.registry.DamageTypesRegistry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin implements DuckDamageSourcesMixin {

    @Shadow public abstract DamageSource create(RegistryKey<DamageType> key);

    @Override
    public DamageSource betteradventuremode$bloodMagicCasting() {
        return this.create(DamageTypesRegistry.BLOOD_MAGIC_CASTING_DAMAGE_TYPE);
    }
}
