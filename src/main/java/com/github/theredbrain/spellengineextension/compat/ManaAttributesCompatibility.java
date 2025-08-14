package com.github.theredbrain.spellengineextension.compat;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.entity.LivingEntity;

public class ManaAttributesCompatibility {

	public static float getCurrentMana(LivingEntity livingEntity) {
		return ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
	}

	public static void addMana(LivingEntity livingEntity, float amount) {
		((ManaUsingEntity) livingEntity).manaattributes$addMana(amount);
	}

}
