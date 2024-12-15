# 1.2.0

- backported a 1.21.1 Spell Engine fix for an exploit that allowed using an item while casting a spell
- added server config option to disable Spell Engines auto swap feature
- added 3 new entity attributes which multiply the health/mana/stamina cost of spells. Spells can be defined to not be affected by this
- added 3 new spell schools ("Generic Melee", "Generic Ranged", "Generic Magic")
- added "generic.magic_damage" entity attribute
- added 10 new entity attributes which modify cast spells ("generic.extra_launch_delay", "generic.extra_launch_count", "generic.extra_velocity", "generic.extra_ricochet", "generic.extra_ricochet_range", "generic.extra_bounce", "generic.extra_pierce", "generic.extra_chain_reaction_size", "generic.extra_chain_reaction_triggers")
- removed dependency on Cloth Config
- added dependency on Fzzy Config

Details about the new content can be found in the readme.

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