# SpellEngine Extension

This is an extension to the 'SpellEngine' mod. It adds support for mana and stamina provided by the mods 'Mana Attributes' and 'Stamina Attributes'.

## Additions to spell.json
Several aspects of spells can be controlled more directly.

Damage Impact
- direct_damage (overrides damage amount)
- damage_type_override (overrides damage type)

Heal Impact
- direct_heal (overrides heal amount)

Spell Cost
- mana_cost (with optional check if player has enough mana)
- health_cost (with optional check if player has enough health)
- stamina_cost (with optional check if player has enough stamina)
- consume_self (consumes the item used to cast the spell)
- decrement_effect_amount (allows better control over what happens with status effects defined as spell cost. When < 0, the effect is removed (the normal behaviour), when > 0 the effects amplifier is reduced (0 is the lowest amplifier possible))
- check_effect_cost (toggles a check for defined status effect costs when attempting to cast the spell)

Spending health to cast spells inflicts damage with a custom damage type.

## Proxy Pools (Java API only)

Items can be defined as spell proxies, which means they can cast spells which are added by spell books. This applies to all currently equipped spells.
SpellEngine Extension allows modders to set a proxy pool, which limits the spells a spell proxy can cast to a defined spell pool.