package com.github.theredbrain.spellengineextension.registry;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.enchantment.ProvideSpellsEnchantmentEntityEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SpellEngineExtensionEnchantmentEntityEffects {

	public static MapCodec<ProvideSpellsEnchantmentEntityEffect> PROVIDE_SPELLS;

	public static void bootstrap() {
	}

	private static <T extends EnchantmentEntityEffect> MapCodec<T> register(Identifier id, MapCodec<T> codec) {
		return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, id, codec);
	}

	static {
		PROVIDE_SPELLS = register(SpellEngineExtension.identifier("provide_spells"), ProvideSpellsEnchantmentEntityEffect.CODEC);
	}

}
