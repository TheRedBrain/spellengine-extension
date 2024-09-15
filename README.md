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

Spending health to cast spells inflicts damage with the "spellengineextension:blood_magic_casting_damage_type" damage type.

- stamina_cost (amount of stamina casting the spell is costing)
- check_stamina_cost (if casting fails when player has not enough stamina)
- stamina_cost_multiplier_applies (whether the stamina cost should be multiplied with the "generic.stamina_spell_cost_multiplier" entity attribute)

This only has an effect, when [Stamina Attributes](https://modrinth.com/mod/stamina-attributes) is installed.

- mana_cost (amount of mana casting the spell is costing)
- check_mana_cost (if casting fails when player has not enough mana)
- mana_cost_multiplier_applies (whether the mana cost should be multiplied with the "generic.mana_spell_cost_multiplier" entity attribute)

This only has an effect, when [Mana Attributes](https://modrinth.com/mod/mana-attributes) is installed.

## Entity Attributes
"generic.health_spell_cost_multiplier" multiplies the health cost of spells. Default value is 1.0.

"generic.mana_spell_cost_multiplier" multiplies the mana cost of spells. Default value is 1.0.

"generic.stamina_spell_cost_multiplier" multiplies the stamina cost of spells. Default value is 1.0.

## Proxy Pools (1.20.1 only)

### Note: This feature is not available on the 1.21.1 version due to technical reasons

Items can be defined as spell proxies, which means they can cast spells which are added by spell books.

Normally a spell proxy can cast all spells from equipped spell books, but if the 'proxy_pool' field in the spell_assignment for that spell proxy is a valid spell pool identifier, the spell proxy can only cast spells that are both on equipped spell books and in the specified spell pool.