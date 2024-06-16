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
- health_cost (with optional check if player has enough health)
Spending health to cast spells inflicts damage with a custom damage type.

- stamina_cost (with optional check if player has enough stamina)
This only has an effect, when [Stamina Attributes](https://modrinth.com/mod/stamina-attributes) is installed.

- mana_cost (with optional check if player has enough mana)
This only has an effect, when [Mana Attributes](https://modrinth.com/mod/mana-attributes) is installed.

## Proxy Pools

Items can be defined as spell proxies, which means they can cast spells which are added by spell books.

Normally a spell proxy can cast all spells from equipped spell books, but if the 'proxy_pool' field in the spell_assignment for that spell proxy is a valid spell pool identifier, the spell proxy can only cast spells that are both on equipped spell books and in the specified spell pool.