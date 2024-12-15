package com.github.theredbrain.spellengineextension.mixin.entity.attributes;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
	@Shadow
	private static EntityAttribute register(String id, EntityAttribute attribute) {
		throw new AssertionError();
	}

	static {
		SpellEngineExtension.GENERIC_MAGIC_DAMAGE = register(SpellEngineExtension.MOD_ID + ":generic.magic_damage", new ClampedEntityAttribute("attribute.name.generic.magic_damage", 0.0F, 0.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.HEALTH_SPELL_COST_MULTIPLIER = register(SpellEngineExtension.MOD_ID + ":generic.health_spell_cost_multiplier", new ClampedEntityAttribute("attribute.name.generic.health_spell_cost_multiplier", 1.0F, -1024.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.MANA_SPELL_COST_MULTIPLIER = register(SpellEngineExtension.MOD_ID + ":generic.mana_spell_cost_multiplier", new ClampedEntityAttribute("attribute.name.generic.mana_spell_cost_multiplier", 1.0F, -1024.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.STAMINA_SPELL_COST_MULTIPLIER = register(SpellEngineExtension.MOD_ID + ":generic.stamina_spell_cost_multiplier", new ClampedEntityAttribute("attribute.name.generic.stamina_spell_cost_multiplier", 1.0F, -1024.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.EXTRA_LAUNCH_COUNT = register(SpellEngineExtension.MOD_ID + ":generic.extra_launch_count", new ClampedEntityAttribute("attribute.name.generic.extra_launch_count", 0.0F, 0.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.EXTRA_LAUNCH_DELAY = register(SpellEngineExtension.MOD_ID + ":generic.extra_launch_delay", new ClampedEntityAttribute("attribute.name.generic.extra_launch_delay", 0.0F, 0.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.EXTRA_VELOCITY = register(SpellEngineExtension.MOD_ID + ":generic.extra_velocity", new ClampedEntityAttribute("attribute.name.generic.extra_velocity", 0.0F, 0.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.EXTRA_RICOCHET = register(SpellEngineExtension.MOD_ID + ":generic.extra_ricochet", new ClampedEntityAttribute("attribute.name.generic.extra_ricochet", 0.0F, 0.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.EXTRA_RICOCHET_RANGE = register(SpellEngineExtension.MOD_ID + ":generic.extra_ricochet_range", new ClampedEntityAttribute("attribute.name.generic.extra_ricochet_range", 0.0F, 0.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.EXTRA_BOUNCE = register(SpellEngineExtension.MOD_ID + ":generic.extra_bounce", new ClampedEntityAttribute("attribute.name.generic.extra_bounce", 0.0F, 0.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.EXTRA_PIERCE = register(SpellEngineExtension.MOD_ID + ":generic.extra_pierce", new ClampedEntityAttribute("attribute.name.generic.extra_pierce", 0.0F, 0.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.EXTRA_CHAIN_REACTION_SIZE = register(SpellEngineExtension.MOD_ID + ":generic.extra_chain_reaction_size", new ClampedEntityAttribute("attribute.name.generic.extra_chain_reaction_size", 0.0F, 0.0F, 1024.0F).setTracked(true));
		SpellEngineExtension.EXTRA_CHAIN_REACTION_TRIGGERS = register(SpellEngineExtension.MOD_ID + ":generic.extra_chain_reaction_triggers", new ClampedEntityAttribute("attribute.name.generic.extra_chain_reaction_triggers", 0.0F, 0.0F, 1024.0F).setTracked(true));
	}
}
