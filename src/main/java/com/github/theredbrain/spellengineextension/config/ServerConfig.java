package com.github.theredbrain.spellengineextension.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    @Comment("Spells should cost health. Set `false` to remove health cost from all spells.")
    public boolean spell_cost_health_allowed = true;
    @Comment("Spells should cost mana. Set `false` to remove mana cost from all spells.")
    public boolean spell_cost_mana_allowed = true;
    @Comment("Spells should cost stamina. Set `false` to remove stamina cost from all spells.")
    public boolean spell_cost_stamina_allowed = true;
    @Comment("Spells should require and optionally remove status effects. Set `false` to remove effect cost from all spells.")
    public boolean spell_cost_effects_allowed = true;
    @Comment("Spells can be affected by the 'generic.extra_launch_count' entity attribute")
    public boolean spell_launch_properties_extra_launch_count_attribute_allowed = true;
    @Comment("Spells can be affected by the 'generic.extra_launch_delay' entity attribute")
    public boolean spell_launch_properties_extra_launch_delay_attribute_allowed = true;
    @Comment("Spells can be affected by the 'generic.extra_velocity' entity attribute")
    public boolean spell_launch_properties_extra_velocity_attribute_allowed = true;
    @Comment("Spells can be affected by the 'generic.extra_ricochet' entity attribute")
    public boolean spell_projectile_perk_extra_ricochet_attribute_allowed = true;
    @Comment("Spells can be affected by the 'generic.extra_ricochet_range' entity attribute")
    public boolean spell_projectile_perk_extra_ricochet_range_attribute_allowed = true;
    @Comment("Spells can be affected by the 'generic.extra_bounce' entity attribute")
    public boolean spell_projectile_perk_extra_bounce_attribute_allowed = true;
    @Comment("Spells can be affected by the 'generic.extra_pierce' entity attribute")
    public boolean spell_projectile_perk_extra_pierce_attribute_allowed = true;
    @Comment("Spells can be affected by the 'generic.extra_chain_reaction_size' entity attribute")
    public boolean spell_projectile_perk_extra_chain_reaction_size_attribute_allowed = true;
    @Comment("Spells can be affected by the 'generic.extra_chain_reaction_triggers' entity attribute")
    public boolean spell_projectile_perk_extra_chain_reaction_triggers_attribute_allowed = true;
    @Comment("Disables the client side auto swap feature for all connected clients.")
    public boolean disable_auto_swap = true;
    public ServerConfig() {

    }
}
