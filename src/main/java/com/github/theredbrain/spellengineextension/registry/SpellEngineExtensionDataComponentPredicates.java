package com.github.theredbrain.spellengineextension.registry;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.predicate.item.SpellContainersPredicate;
import net.minecraft.predicate.item.ItemSubPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SpellEngineExtensionDataComponentPredicates {

	public static ItemSubPredicate.Type<SpellContainersPredicate> SPELL_CONTAINER_PREDICATE;

	public static void bootstrap() {
	}

	static {
		SPELL_CONTAINER_PREDICATE = Registry.register(Registries.ITEM_SUB_PREDICATE_TYPE, SpellEngineExtension.identifier("spell_container_predicate"), new ItemSubPredicate.Type<>(SpellContainersPredicate.CODEC));
	}
}
