# SpellEngine Extension

This is an extension to the [Spell Engine](https://modrinth.com/mod/spell-engine) mod by Daedelus. On its own it changes nothing in the game, but mod and data pack authors have more possibilities when designing spells.

## Additions to spell.json
Several aspects of spells can be controlled more directly.

Damage Impact
- direct_damage (overrides damage amount)
- damage_type_override (overrides damage type)

Heal Impact
- direct_heal (overrides heal amount)

Spell Cost
- consume_self (consumes the item used to cast the spell)
- decrement_effect_amount (allows better control over what happens with status effects defined as spell cost. When < 0, the effect is removed (the normal behaviour), when > 0 the effects amplifier (also known as effect level) is reduced (0 is the lowest amplifier possible)). When the decrement amount is 0, nothing happens to the effect.

- health_cost (amount of health casting the spell is costing)
- check_health_cost (if casting fails when player has not enough health)
- health_cost_multiplier_applies (whether the health cost should be multiplied with the "generic.health_spell_cost_multiplier" entity attribute)

> Spending health to cast spells inflicts damage with the "spellengineextension:blood_magic_casting_damage_type" damage type.


- stamina_cost (amount of stamina casting the spell is costing)
- check_stamina_cost (if casting fails when player has not enough stamina)
- stamina_cost_multiplier_applies (whether the stamina cost should be multiplied with the "generic.stamina_spell_cost_multiplier" entity attribute)

This only has an effect, when [Stamina Attributes](https://modrinth.com/mod/stamina-attributes) is installed.

- mana_cost (amount of mana casting the spell is costing)
- check_mana_cost (if casting fails when player has not enough mana)
- mana_cost_multiplier_applies (whether the mana cost should be multiplied with the "generic.mana_spell_cost_multiplier" entity attribute)

This only has an effect, when [Mana Attributes](https://modrinth.com/mod/mana-attributes) is installed.

### Example

This is an example spell.json where all added values are present (with their default values)

> Note that this is not a valid spell.json, as several fields added by Spell Engine are not present. It is also normally not possible to have multiple release target fields 

```json
{
	"release": {
		"target": {
			"type": "PROJECTILE",
			"projectile": {
				"projectile": {
					"launch_properties": {
						"respect_extra_launch_count_attribute": true,
						"respect_extra_launch_delay_attribute": true,
						"respect_extra_velocity_attribute": true
					},
					"perks": {
						"respect_extra_ricochet_attribute": true,
						"respect_extra_ricochet_range_attribute": true,
						"respect_extra_bounce_attribute": true,
						"respect_extra_pierce_attribute": true,
						"respect_extra_chain_reaction_size_attribute": true,
						"respect_extra_chain_reaction_triggers_attribute": true
					}
				}
			}
		},
		"target": {
			"type": "SHOOT_ARROW",
			"projectile": {
				"projectile": {
					"launch_properties": {
						"respect_extra_launch_count_attribute": true,
						"respect_extra_launch_delay_attribute": true,
						"respect_extra_velocity_attribute": true
					}
				}
			}
		},
		"target": {
			"type": "METEOR",
			"projectile": {
				"projectile": {
					"launch_properties": {
						"respect_extra_launch_count_attribute": true,
						"respect_extra_launch_delay_attribute": true,
						"respect_extra_velocity_attribute": true
					},
					"perks": {
						"respect_extra_ricochet_attribute": true,
						"respect_extra_ricochet_range_attribute": true,
						"respect_extra_bounce_attribute": true,
						"respect_extra_pierce_attribute": true,
						"respect_extra_chain_reaction_size_attribute": true,
						"respect_extra_chain_reaction_triggers_attribute": true
					}
				}
			}
		}
	},
	"impact": [
		{
			"action": {
				"type": "DAMAGE",
				"damage": {
					"direct_damage": 0.0,
					"damage_type_override": ""
				}
			}
		},
		{
			"action": {
				"type": "HEAL",
				"damage": {
					"direct_heal": 0.0
				}
			}
		}
	],
	"cost": {
		"check_health_cost": false,
		"check_mana_cost": true,
		"check_stamina_cost": false,
		"health_cost_multiplier_applies": true,
		"mana_cost_multiplier_applies": true,
		"stamina_cost_multiplier_applies": true,
		"consume_self": false,
		"decrement_effect_amount": -1,
		"mana_cost": 0.0,
		"health_cost": 0.0,
		"stamina_cost": 0.0
	}
}
```

## Spell schools

These spell schools can be used in the spell.json.

- "GENERIC_MELEE" uses "minecraft:generic.attack_damage" and "minecraft:generic.attack_speed" as their power and haste attributes respectively. Uses the "minecraft:generic" damage type.
- "GENERIC_RANGED" uses "ranged_weapon:damage" and "ranged_weapon:haste" as their power and haste attributes respectively. When the "Ranged Weapon API" mod is not installed, "minecraft:generic.attack_damage" and "minecraft:generic.attack_speed" are used instead. Uses the "minecraft:arrow" damage type.
- "GENERIC_MAGIC" uses "spellengineextension:generic.magic_damage" and "spell_power:generic.haste" as their power and haste attributes respectively. Uses the "minecraft:magic" damage type.

They have no "crit_chance" or "crit_damage" traits and also no entity attribute or status effect defined, to keep them as simple and generic as possible.

## Entity Attributes

- "generic.magic_damage" the damage done by magic attacks. Default value is 0.0.
- "generic.health_spell_cost_multiplier" multiplies the health cost of spells. Default value is 1.0.
- "generic.mana_spell_cost_multiplier" multiplies the mana cost of spells. Default value is 1.0.
- "generic.stamina_spell_cost_multiplier" multiplies the stamina cost of spells. Default value is 1.0.

The following attributes add to the respective values defined in the spell.json. This can be disabled for each spell individually or globally in the server config.
- "generic.extra_launch_count"
- "generic.extra_launch_delay"
- "generic.extra_velocity"
- "generic.extra_ricochet"
- "generic.extra_ricochet_range"
- "generic.extra_bounce"
- "generic.extra_pierce"
- "generic.extra_chain_reaction_size"
- "generic.extra_chain_reaction_triggers"

## Proxy Pools

Items can be defined as spell proxies, which means they can cast spells which are added by spell books.

Normally a spell proxy can cast all spells from equipped spell books, but if the 'proxy_pool' field in the spell_assignment for that spell proxy is a valid spell pool identifier, the spell proxy can only cast spells that are both on equipped spell books and in the specified spell pool.