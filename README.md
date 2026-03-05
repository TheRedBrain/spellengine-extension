# SpellEngine Extension (RPG Series Tweaks)

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


- custom_effect_cost is a more customizable status effect cost. The vanilla 'effect_cost' field is unaffected and works as expected.
- decrement_effect_amount (defines what happens with status effects defined as 'custom_effect_cost'. When < 0, the effect is removed (same as the 'effect_cost' behaviour), when > 0 the effects amplifier (also known as effect level) is reduced (0 is the lowest amplifier possible)). When the decrement amount is 0, nothing happens to the effect.
- check_effect_cost (if the caster has to have the effect defined by 'custom_effect_cost' applied)


- health_cost (amount of health casting the spell is costing)
- check_health_cost (if casting fails when player has not enough health)
- health_cost_multiplier_applies (whether the health cost should be multiplied with the "spellengineextension:generic.health_spell_cost_multiplier" entity attribute)

> Spending health to cast spells inflicts damage with the "spellengineextension:blood_magic_casting_damage_type" damage type.

### Stamina Attributes integration

- stamina_cost (amount of stamina casting the spell is costing)
- check_stamina (if casting fails when player has no stamina)
- check_stamina_cost (if casting fails when player has not enough stamina)
- add_item_use_stamina_cost_attribute_value (if the value of the "staminaattributes:generic.item_use_stamina_cost" entity attribute should be added to the spell stamina cost)
- stamina_cost_multiplier_applies (whether the stamina cost should be multiplied with the "spellengineextension:generic.stamina_spell_cost_multiplier" entity attribute)
- apply_channeling_mana_cost (whether the stamina cost should be applied every 'channel_tick')

This only has an effect, when [Stamina Attributes](https://modrinth.com/mod/stamina-attributes) is installed.

### Mana Attributes integration

- mana_cost (amount of mana casting the spell is costing)
- check_mana (if casting fails when player has no mana)
- check_mana_cost (if casting fails when player has not enough mana)
- mana_cost_multiplier_applies (whether the mana cost should be multiplied with the "spellengineextension:generic.mana_spell_cost_multiplier" entity attribute)
- apply_channeling_mana_cost (whether the mana cost should be applied every 'channel_tick')

This only has an effect, when [Mana Attributes](https://modrinth.com/mod/mana-attributes) is installed.

## Movement locking spell casting

While casting spells in the "spellengineextension:enables_movement_locking_during_casting" spell tag, all player movements and rotations are disabled.
The movement locking is extended by x amount of ticks, where x is defined by the "after_casting_movement_locking_ticks" int field, located in the spell.json under 'spell.active.cast'.

By default, movement locking prevents both position and orientation changes. These can be individually disabled in the server config.

Additionally, the entire feature can be disabled in the server config.

## Spell Modifiers

Spell modifiers got more options to modify spells:

- additional_health_cost
- additional_mana_cost
- additional_stamina_cost
- additional_direct_damage
- additional_direct_healing
- replaced_effect_cost_id replaces the 'custom_effect_cost'
- replaced_decrement_effect_cost_amount

## Example

This is an example spell.json where all added values are present (with their default values)

> Note that this is not a valid spell.json, as several fields added by Spell Engine are not present. It is also normally not possible to have multiple target fields

```json
{
  "active": {
    "cast": {
      "after_casting_movement_locking_ticks": 0
    }
  },
  "modifiers": [
    {
      "additional_health_cost": 0.0,
      "additional_mana_cost": 0.0,
      "additional_stamina_cost": 0.0,
      "additional_direct_damage": 0.0,
      "additional_direct_healing": 0.0,
      "replaced_effect_cost_id": null,
      "replaced_decrement_effect_cost_amount": -1
    }
  ],
  "deliver": {
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
  },
  "impacts": [
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
    "check_mana": true,
    "check_mana_cost": true,
    "check_stamina": true,
    "check_stamina_cost": false,
    "health_cost_multiplier_applies": true,
    "mana_cost_multiplier_applies": true,
    "stamina_cost_multiplier_applies": true,
    "add_item_use_stamina_cost_attribute_value": false,
    "consume_self": false,
    "check_effect_cost": true,
    "decrement_effect_amount": -1,
    "mana_cost": 0.0,
    "health_cost": 0.0,
    "stamina_cost": 0.0,
    "apply_channeling_health_cost": false,
    "apply_channeling_mana_cost": false,
    "apply_channeling_stamina_cost": false
  }
}
```

## Spell hotbar customization

The spell hotbar can be customized via several options in the client config.

These include:
- disabling the background
- disabling the cooldown overlay
- disabling the display of items in the spell hot bar
- disabling the hotkey information
- enabling a number that displays the remaining cooldown in seconds
    - x and y offset and the color of this number can be customized
- enabling alternative spell icons when spell is on cooldown
  - the textures are expected to be located under the same namespace and on the same path as the regular spell texture, with "_cooldown" appended to the file name
- disabling the rendering of the use_key spell hot bar slot. This does not prevent casting of spells in that slot.
- disabling the replacement of the first 'number key' spell hotbar slot with the 'use_key' spell hotbar slot. This allows consistent spell hotkeys, regardless of eventual spells/items in the 'use_key' slot.

Additional server side settings:
- only spells in the "spellengineextension:can_be_in_use_item_spell_hotbar_slot" spell tag can be in the use_key spell hot bar slot. This restriction can be disabled in the server config.

## Spell schools

These spell schools can be used in the spell.json.

- "GENERIC_MELEE" uses "minecraft:generic.attack_damage" and "minecraft:generic.attack_speed" as their power and haste attributes respectively. Uses the "minecraft:generic" damage type.
- "GENERIC_RANGED" uses "ranged_weapon:damage" and "ranged_weapon:haste" as their power and haste attributes respectively. When the "Ranged Weapon API" mod is not installed, "minecraft:generic.attack_damage" and "minecraft:generic.attack_speed" are used instead. Uses the "minecraft:arrow" damage type.
- "GENERIC_MAGIC" uses "spellengineextension:generic.magic_damage" and "spell_power:generic.haste" as their power and haste attributes respectively. Uses the "minecraft:magic" damage type.

They have no "crit_chance" or "crit_damage" traits and also no entity attribute or status effect defined, to keep them as simple and generic as possible.

## 'ProvidesSpell' status effect API

This is a small java API that allows status effects to provide a list of spells to players.

This list is selected from a list, where the amplifier of the effect determines which exact list is chosen.

If the amplifier is higher than the amount of configured lists, the last list is used instead.

## "Provide Spells" Enchantment Effect

The "spellengineextension:provide_spells" enchantment effect allows item enchantments to provide spells.

### Example:

```json
{
  "anvil_cost": 1,
  "description": {
    "translate": "enchantment.spellengineextension.test"
  },
  "effects": {
    "minecraft:tick": [
      {
        "effect": {
          "type": "spellengineextension:provide_spells",
          "provided_spells_list": [
            "wizards:fireball"
          ]
        }
      }
    ]
  },
  "max_cost": {
    "base": 25,
    "per_level_above_first": 8
  },
  "max_level": 1,
  "min_cost": {
    "base": 5,
    "per_level_above_first": 8
  },
  "slots": [
    "mainhand"
  ],
  "supported_items": "#minecraft:swords",
  "weight": 5
}
```

## Entity Attributes

- "spellengineextension:generic.magic_damage" the damage done by magic attacks. Default value is 0.0.
- "spellengineextension:generic.health_spell_cost_multiplier" multiplies the health cost of spells. Default value is 1.0.
- "spellengineextension:generic.mana_spell_cost_multiplier" multiplies the mana cost of spells. Default value is 1.0.
- "spellengineextension:generic.stamina_spell_cost_multiplier" multiplies the stamina cost of spells. Default value is 1.0.

The following attributes add to the respective values defined in the spell.json. This can be disabled for each spell individually or globally in the server config.
- "spellengineextension:generic.extra_launch_count"
- "spellengineextension:generic.extra_launch_delay"
- "spellengineextension:generic.extra_velocity"
- "spellengineextension:generic.extra_ricochet"
- "spellengineextension:generic.extra_ricochet_range"
- "spellengineextension:generic.extra_bounce"
- "spellengineextension:generic.extra_pierce"
- "spellengineextension:generic.extra_chain_reaction_size"
- "spellengineextension:generic.extra_chain_reaction_triggers"

## Spell Container Predicate

The "spellengineextension:spell_container_predicate" can be used to check if a item stack contains a specific "spell_engine:spell_container".

All its fields are optional.

> If "exact_spell_ids_match" is true, the spell container has to contain all and only the ids listed in "spell_ids".\
> If "exact_spell_ids_match" is false (default), the spell container has to contain all the ids listed in "spell_ids".

Example:

````json
{
  "access": "ANY",
  "access_param": "",
  "pool": "",
  "slot": "",
  "max_spell_count": 1,
  "exact_spell_ids_match": true,
  "spell_ids": [
    "wizards:fire_blast"
  ]
  
}
````

## Conditional Spell Pools

Items with the "spellengineextension:has_conditional_spell_container" item component can "disable" an existing spell container on the item.
The component has multiple fields that define when the spell container is enabled/disabled:
- is_main_hand_valid: a boolean field, valid when the item is in the main hand
- is_off_hand_valid: a boolean field, valid when the item is in the offhand
- is_two_handed_valid: a boolean field, valid when the item is in the main hand and the offhand is empty
- is_dual_wielding_valid: a boolean field, valid when the item is in the main hand and the offhand contains an item that is in the 'dual_wielding_tag'
- dual_wielding_tag: a string field, describes an identifier for an item tag
- prevent_casting_of_all_spells: a boolean, if true, all casting with that item is disabled. When false, spells supplied by other sources can still be cast.
- fall_back_spell_ids: a string list, spells in this list are used instead of the regular spells, when the regular spell container is disabled
- is_valid, a boolean field, when true the spell container is enabled. If one of the other boolean fields is true and the condition fulfilled, this field is set to true. otherwise to false.

> 'is_two_handed_valid' is checked first. If it's set to false, then 'is_dual_wielding_valid' is checked. If that's also set to false, then 'is_main_hand_valid' is checked.

## Built-in Data Pack

Spell Engine Extension comes ith a built-in data pack that enables SSE features for the RPG Series mods. This includes:

- weapon skills are added to the "spellengineextension:can_be_in_use_item_spell_hotbar_slot" spell tag

## Proxy Pools (1.20.1 only)

Items can be defined as spell proxies, which means they can cast spells which are added by spell sources.

By defining a "proxy pool" for an item, the item can only cast spells that are in that spell pool.

### 1.20.1

In old versions 'Spell Engine Extension' adds the 'proxy_pool' field in the spell_assignment file.

### 1.21.1 +

In modern versions the proxy pool feature is implemented in vanilla Spell Engine via the "spell_engine:spell_container" data component.