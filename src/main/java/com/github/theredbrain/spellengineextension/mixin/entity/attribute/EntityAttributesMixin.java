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
		SpellEngineExtension.HEALTH_SPELL_COST_MULTIPLIER = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.health_spell_cost_multiplier"), new ClampedEntityAttribute("attribute.name.generic.health_spell_cost_multiplier", 1.0, -1024.0, 1024.0).setTracked(true));
		SpellEngineExtension.MANA_SPELL_COST_MULTIPLIER = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.mana_spell_cost_multiplier"), new ClampedEntityAttribute("attribute.name.generic.mana_spell_cost_multiplier", 1.0, -1024.0, 1024.0).setTracked(true));
		SpellEngineExtension.STAMINA_SPELL_COST_MULTIPLIER = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.stamina_spell_cost_multiplier"), new ClampedEntityAttribute("attribute.name.generic.stamina_spell_cost_multiplier", 1.0, -1024.0, 1024.0).setTracked(true));
		SpellEngineExtension.EXTRA_LAUNCH_COUNT = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.extra_launch_count"), new ClampedEntityAttribute("attribute.name.generic.extra_launch_count", 0.0, 0.0, 1024.0).setTracked(true));
		SpellEngineExtension.EXTRA_RICOCHET = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.extra_ricochet"), new ClampedEntityAttribute("attribute.name.generic.extra_ricochet", 0.0, 0.0, 1024.0).setTracked(true));
		SpellEngineExtension.EXTRA_RICOCHET_RANGE = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.extra_ricochet_range"), new ClampedEntityAttribute("attribute.name.generic.extra_ricochet_range", 0.0, 0.0, 1024.0).setTracked(true));
		SpellEngineExtension.EXTRA_BOUNCE = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.extra_bounce"), new ClampedEntityAttribute("attribute.name.generic.extra_bounce", 0.0, 0.0, 1024.0).setTracked(true));
		SpellEngineExtension.EXTRA_PIERCE = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.extra_pierce"), new ClampedEntityAttribute("attribute.name.generic.extra_pierce", 0.0, 0.0, 1024.0).setTracked(true));
		SpellEngineExtension.EXTRA_CHAIN_REACTION_SIZE = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.extra_chain_reaction_size"), new ClampedEntityAttribute("attribute.name.generic.extra_chain_reaction_size", 0.0, 0.0, 1024.0).setTracked(true));
		SpellEngineExtension.EXTRA_CHAIN_REACTION_TRIGGERS = Registry.registerReference(Registries.ATTRIBUTE, SpellEngineExtension.identifier("generic.extra_chain_reaction_triggers"), new ClampedEntityAttribute("attribute.name.generic.extra_chain_reaction_triggers", 0.0, 0.0, 1024.0).setTracked(true));
	}
}
