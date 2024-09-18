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