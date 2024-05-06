package com.github.theredbrain.spellengineextension.registry;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class DamageTypesRegistry {

    public static final RegistryKey<DamageType> BLOOD_MAGIC_CASTING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, SpellEngineExtension.identifier("blood_magic_casting_damage_type"));
}
