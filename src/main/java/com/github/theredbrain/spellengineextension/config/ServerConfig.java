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
    public ServerConfig() {

    }
}
