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