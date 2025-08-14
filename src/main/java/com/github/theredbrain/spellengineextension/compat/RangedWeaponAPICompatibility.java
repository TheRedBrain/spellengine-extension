package com.github.theredbrain.spellengineextension.compat;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;

public class RangedWeaponAPICompatibility {

	public static RegistryEntry<EntityAttribute> getRangedAttackDamageAttribute() {
		return EntityAttributes_RangedWeapon.DAMAGE.entry;
	}

	public static RegistryEntry<EntityAttribute> getRangedAttackSpeedAttribute() {
		return EntityAttributes_RangedWeapon.HASTE.entry;
	}

}
