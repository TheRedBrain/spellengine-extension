package com.github.theredbrain.spellengineextension.compat;

import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.entity.LivingEntity;

public class StaminaAttributesCompatibility {

	public static float getCurrentStamina(LivingEntity livingEntity) {
		return ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
	}

	public static float getItemUseStaminaCost(LivingEntity livingEntity) {
		return ((StaminaUsingEntity) livingEntity).staminaattributes$getItemUseStaminaCost();
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(amount);
	}

}
