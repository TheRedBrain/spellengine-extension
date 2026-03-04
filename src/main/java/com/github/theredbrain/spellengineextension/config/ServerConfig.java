package com.github.theredbrain.spellengineextension.config;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;

@ConvertFrom(fileName = "server.json5", folder = "spellengineextension")
public class ServerConfig extends Config {
	public ServerConfig() {
		super(SpellEngineExtension.identifier("server"));
	}

	@Comment("""
			When set to true, changing the players orientation and/or position while casting spells is disabled. Spells have to be in the "" spell tag for this to be active. Those spells can also apply an amount of ticks where movement is locked after casting the spell has ended.
			""")
	public ValidatedBoolean enable_movement_locking_spell_casting = new ValidatedBoolean(true);
	public ValidatedBoolean movement_locking_prevents_player_position_changes = new ValidatedBoolean(true);
	public ValidatedBoolean movement_locking_prevents_player_orientation_changes = new ValidatedBoolean(true);
	public ValidatedBoolean enable_attack_range_attribute_integration = new ValidatedBoolean(true);
	@Comment("When enabled, only spells in the 'spellengineextension:can_be_in_use_item_spell_hotbar_slot' are placed in the use_item hotbar slot.")
	public ValidatedBoolean enable_spell_hotbar_use_key_restriction = new ValidatedBoolean(true);
	@Comment("Disables the client side auto swap feature for all connected clients.")
	public ValidatedBoolean disable_auto_swap = new ValidatedBoolean(true);
	@Comment("Spells should cost health. Set to `false` to remove health cost from all spells.")
	public ValidatedBoolean spell_cost_health_allowed = new ValidatedBoolean(true);
	@Comment("Spells should cost mana. Set to `false` to remove mana cost from all spells.")
	public ValidatedBoolean spell_cost_mana_allowed = new ValidatedBoolean(true);
	@Comment("Spells should cost stamina. Set to `false` to remove stamina cost from all spells.")
	public ValidatedBoolean spell_cost_stamina_allowed = new ValidatedBoolean(true);
	@Comment("Spells should require and optionally remove status effects. This cost is different to the effect cost added by vanilla Spell Engine to maintain compatibility. Set to `false` to remove custom effect cost from all spells.")
	public ValidatedBoolean spell_cost_custom_effects_allowed = new ValidatedBoolean(true);
	@Comment("Spells can be affected by the 'spellengineextension:generic.extra_launch_count' entity attribute")
	public ValidatedBoolean spell_launch_properties_extra_launch_count_attribute_allowed = new ValidatedBoolean(true);
	@Comment("Spells can be affected by the 'spellengineextension:generic.extra_launch_delay' entity attribute")
	public ValidatedBoolean spell_launch_properties_extra_launch_delay_attribute_allowed = new ValidatedBoolean(true);
	@Comment("Spells can be affected by the 'spellengineextension:generic.extra_velocity' entity attribute")
	public ValidatedBoolean spell_launch_properties_extra_velocity_attribute_allowed = new ValidatedBoolean(true);
	@Comment("Spells can be affected by the 'spellengineextension:generic.extra_ricochet' entity attribute")
	public ValidatedBoolean spell_projectile_perk_extra_ricochet_attribute_allowed = new ValidatedBoolean(true);
	@Comment("Spells can be affected by the 'spellengineextension:generic.extra_ricochet_range' entity attribute")
	public ValidatedBoolean spell_projectile_perk_extra_ricochet_range_attribute_allowed = new ValidatedBoolean(true);
	@Comment("Spells can be affected by the 'spellengineextension:generic.extra_bounce' entity attribute")
	public ValidatedBoolean spell_projectile_perk_extra_bounce_attribute_allowed = new ValidatedBoolean(true);
	@Comment("Spells can be affected by the 'spellengineextension:generic.extra_pierce' entity attribute")
	public ValidatedBoolean spell_projectile_perk_extra_pierce_attribute_allowed = new ValidatedBoolean(true);
	@Comment("Spells can be affected by the 'spellengineextension:generic.extra_chain_reaction_size' entity attribute")
	public ValidatedBoolean spell_projectile_perk_extra_chain_reaction_size_attribute_allowed = new ValidatedBoolean(true);
	@Comment("Spells can be affected by the 'spellengineextension:generic.extra_chain_reaction_triggers' entity attribute")
	public ValidatedBoolean spell_projectile_perk_extra_chain_reaction_triggers_attribute_allowed = new ValidatedBoolean(true);
}
