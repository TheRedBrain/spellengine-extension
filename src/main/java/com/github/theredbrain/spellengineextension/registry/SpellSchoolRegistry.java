package com.github.theredbrain.spellengineextension.registry;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageTypes;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

public class SpellSchoolRegistry {

	public static final SpellSchool GENERIC_MELEE = new SpellSchool(SpellSchool.Archetype.MELEE, SpellEngineExtension.identifier("generic_melee"), 10066363, DamageTypes.GENERIC, null, null);
	public static final SpellSchool GENERIC_RANGED = new SpellSchool(SpellSchool.Archetype.ARCHERY, SpellEngineExtension.identifier("generic_ranged"), 10066363, DamageTypes.ARROW, null, null);
	public static final SpellSchool GENERIC_MAGIC = new SpellSchool(SpellSchool.Archetype.MAGIC, SpellEngineExtension.identifier("generic_magic"), 10066363, DamageTypes.MAGIC, null, null);

	public static void init() {
		GENERIC_MELEE.addSource(SpellSchool.Trait.POWER, new SpellSchool.Source(SpellSchool.Apply.ADD, (query) -> {
			return query.entity().getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
		}));
		GENERIC_MELEE.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, (query) -> {
			return query.entity().getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED);
		}));
		SpellSchools.register(GENERIC_MELEE);

		GENERIC_RANGED.addSource(SpellSchool.Trait.POWER, new SpellSchool.Source(SpellSchool.Apply.ADD, (query) -> {
			return query.entity().getAttributeValue(SpellEngineExtension.getRangedAttackDamageAttribute());
		}));
		GENERIC_RANGED.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, (query) -> {
			return query.entity().getAttributeValue(SpellEngineExtension.getRangedAttackSpeedAttribute());
		}));
		SpellSchools.register(GENERIC_RANGED);

		GENERIC_MAGIC.addSource(SpellSchool.Trait.POWER, new SpellSchool.Source(SpellSchool.Apply.ADD, (query) -> {
			return query.entity().getAttributeValue(SpellEngineExtension.GENERIC_MAGIC_DAMAGE);
		}));
		GENERIC_MAGIC.addSource(SpellSchool.Trait.HASTE, new SpellSchool.Source(SpellSchool.Apply.ADD, (query) -> {
			return query.entity().getAttributeValue(SpellPowerMechanics.HASTE.attributeEntry);
		}));
		SpellSchools.register(GENERIC_MAGIC);
	}
}
