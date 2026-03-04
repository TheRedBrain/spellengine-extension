# 2.14.0

- now works with Spell Engine 1.9.4

## Additions

- added server config options to individually toggle position and orientation locking for movement locking spell casting.

## Technical

- made huge progress in making the mod easier to maintain. Thanks to new events added by Spell Engine, several mixins could be removed. Other mixins were modernized to be much more robust.

# 2.13.0

- now works with Spell Engine 1.9.0

## Additions

- added a built-in data pack that enables SSE mechanics for the RPG Series content mods

## Changes

- removed the "spellengineextension:proxy_pool" data component. This mechanic is now supported by vanilla Spell Engine via the reworked "spell_engine:spell_container" data component.
- changed default values of some config settings

# 2.12.0

- now works with Spell Engine 1.8.19

## Additions

- added "spellengineextension:spell_container_predicate" data component predicate

## Changes

- reworked the ProvidesSpell status effect API. It now optionally provides spells based on the effect amplifier.

# 2.11.2

- now works with Spell Engine 1.8.16

# 2.11.1

- now works with Spell Engine 1.8.10

# 2.11.0

- now works with Spell Engine 1.8.2
- added "prevent_casting_of_all_spells" boolean field to the "spellengineextension:has_conditional_spell_container" item component
- added "fall_back_spell_ids" list to the "spellengineextension:has_conditional_spell_container" item component
- added client config options to disable the "show details" tooltip hint for spell containers
- added optional integration for "Attack Range Attribute"
- health/mana/stamina spell costs are no longer checked/applied for players in creative mode
- bumped Mixin Squared version, hopefully fixing crashes when used with Sinytra Connector
- fixed custom spell cost tooltips

# 2.10.2

- now works with Spell Engine 1.7.3
- fixed "spellengineextension:has_conditional_spell_container" integration with RPG Inventory

# 2.10.1

- fixed mana spell cost

# 2.10.0

- now works with Spell Engine 1.7.1
- added 'ProvidesSpell' status effect API
- added movement locking spell casting, enabled via a server config option and the "spellengineextension:enables_movement_locking_during_casting" spell tag.
- added "add_item_use_stamina_cost_attribute_value" boolean field to 'spell.cost'
- added channeling health/mana/stamina costs, using the existing amounts and new boolean fields added to 'spell.cost'
- added "check_mana" and "check_stamina" boolean fields to 'spell.cost', which allow spell casting when the corresponding resource is above 0 (the existing checks only allow casting when the resource is above the cost)
- added "check_effect_cost" boolean field to 'spell.cost'
- added "after_casting_movement_locking_ticks" int field to 'spell.active.cast', the movement locking for this spell is extended by this amount of ticks
- added "is_two_handed_valid" condition to the "spellengineextension:has_conditional_spell_container" item component, which is true when the item is in the main hand and the offhand is empty
- added "is_dual_wielding_valid" condition to the "spellengineextension:has_conditional_spell_container" item component, which is true when the item is in the main hand and the offhand contains an item that is in the tag defined by the "dual_wielding_tag" field
- added "replaced_effect_cost_id" and "replaced_decrement_effect_cost_amount" fields to 'spell.modifier'
- added client config option to hide the use_key spell hotbar slot. This does not prevent casting the spell in that slot.
- added server config option to disable the use_key spell hotbar slot restriction
- removed the server config option added in the last update, this functionality is now controlled by a spell tag ("spellengineextension:can_be_in_use_item_spell_hotbar_slot")
- improved optional mod integrations, fixing several issues where the mods were still required

# 2.9.0

- added "spellengineextension:has_conditional_spell_container" item component, which can be used to disable the spell container of an item, depending on the hand it's held in (supports RPG Inventory)
- added "spellengineextension:proxy_pool" item component, which can be used to limit what spells a spell casting item can cast
- added config option to limit the types of spells that can be in the 'use key' spell hotbar slot (using spell tier)

# 2.8.0

- now works with Spell Engine 1.7.0
- added compatibility with Merged Items
  - merging items can optionally also merge their spell containers
- added custom spell modifiers
  - includes health/mana/stamina cost, direct damage/healing

# 2.7.1

- fixed spell modifier attributes persistence (Thanks @KevinCz)

# 2.7.0

- now works with Spell Engine 1.6.12
- added client config option to disable rendering of items in the spell hot bar

# 2.6.0

- now works with Spell Engine 1.6.10
- added several client config options to customize the spell hotbar
- added setter methods for fields added to the spell.json. These should allow data generation of those fields.

# 2.5.5

- now works with Spell Engine 1.6.2
- fixed a crash that could occur when rendering tooltips

# 2.5.4

- now works with Spell Engine 1.6.1

# 2.5.3

- now works with Spell Engine 1.6.0
- rewrote several mixins to make the mod easier to maintain.

# 2.5.2

- now works with Spell Engine 1.5.2

# 2.5.1

- now works with Spell Engine 1.5.1

# 2.5.0

- now works with Spell Engine 1.5.0
- re-enabled "consume_self" Spell.Cost option, as it is not redundant to the capabilities of Spell Engine

# 2.4.6

- now works with Spell Engine 1.4.5+1.21.1

# 2.4.5

- now works with Spell Engine 1.4.4+1.21.1

# 2.4.4

- now works with Spell Engine 1.3.2+1.21.1

# 2.4.3

- now works with Spell Engine 1.3.1+1.21.1

# 2.4.2

- now works with Spell Engine 1.2.2+1.21.1

# 2.4.1

- now works with Spell Engine 1.2.0+1.21.1

# 2.4.0

- now works with Spell Engine 1.1.2+1.21.1
- added client config option to disable rendering of the spell hotbar background

# 2.3.0

- removed dependency on Cloth Config
- added dependency on Fzzy Config
- fixed an issue where casting a meteor spell while targeting an entity was not possible
- "generic.extra_launch_count", "generic.extra_launch_delay", "generic.extra_velocity" now also affect spells with the "SHOOT_ARROW" release target type 

# 2.2.0

- added 3 new spell schools ("Generic Melee", "Generic Ranged", "Generic Magic")
- added 9 new entity attributes ("generic.magic_damage", "generic.extra_launch_delay", "generic.extra_velocity", "generic.extra_ricochet", "generic.extra_ricochet_range", "generic.extra_bounce", "generic.extra_pierce", "generic.extra_chain_reaction_size", "generic.extra_chain_reaction_triggers")

Details about the new content can be found in the readme.

# 2.1.0

- now supports latest Spell Engine version

# 2.0.0

- update to 1.21.1
- removed feature "proxy pools" due to technical reasons, if you know how to add a field to a record via mixin, please let me know :)
- added 3 new entity attributes which multiply the health/mana/stamina cost of spells. Spells can be defined to not be affected by this

# 1.1.2

- fixed a crash when 'Mana Attributes' or 'Stamina Attributes' were not installed

# 1.1.1

- 'Mana Attributes' and 'Stamina Attributes' are no longer required dependencies
- health, mana and stamina cost and required status effects are now displayed in the spell tooltip
- proxy pools are now data-driven
- marked API method 'setSpellContainerProxyPool' as deprecated, it will be removed in the next update
- now supports latest version of Spell Engine
- fixed an issue where attempting to cast a spell without having enough health/mana/stamina would crash the game

# 1.0.0

First release!

#