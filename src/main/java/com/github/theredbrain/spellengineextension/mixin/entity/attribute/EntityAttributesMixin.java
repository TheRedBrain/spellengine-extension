package com.github.theredbrain.spellengineextension.mixin.entity.attribute;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
	static {
		SpellEngineExtension.HEALTH_SPELL_COST_MULTIPLIER = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.health_spell_cost_multiplier"), new ClampedEntityAttribute("attribute.name.generic.health_spell_cost_multiplier", 100.0, -1024.0, 1024.0).setTracked(true));
		SpellEngineExtension.MANA_SPELL_COST_MULTIPLIER = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.mana_spell_cost_multiplier"), new ClampedEntityAttribute("attribute.name.generic.mana_spell_cost_multiplier", 100.0, -1024.0, 1024.0).setTracked(true));
		SpellEngineExtension.STAMINA_SPELL_COST_MULTIPLIER = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.stamina_spell_cost_multiplier"), new ClampedEntityAttribute("attribute.name.generic.stamina_spell_cost_multiplier", 100.0, -1024.0, 1024.0).setTracked(true));
	}
}
