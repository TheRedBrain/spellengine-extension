# 2.8.0

- now works with Spell Engine 1.7.0
- added compatibility with Merged Items
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