package com.github.theredbrain.spellengineextension.config;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

import java.awt.*;

public class ClientConfig extends Config {
	public ClientConfig() {
		super(SpellEngineExtension.identifier("client"));
	}

	public ValidatedBoolean enable_spell_hotbar_background_rendering = new ValidatedBoolean(true);
	public ValidatedBoolean enable_spell_hotbar_cooldown_overlay = new ValidatedBoolean(true);
	public ValidatedBoolean enable_spell_hotkey_icons = new ValidatedBoolean(true);
	public ValidatedBoolean show_items_in_spell_hot_bar = new ValidatedBoolean(true);
	public ValidatedBoolean disable_use_key_spell_hotbar_slot_rendering = new ValidatedBoolean(true);
	public ValidatedBoolean enable_cooldown_icons = new ValidatedBoolean(false);
	public ValidatedBoolean enable_spell_hotbar_cooldown_number = new ValidatedBoolean(false);
	public ValidatedInt spell_cooldown_number_offset_x = new ValidatedInt(10);
	public ValidatedInt spell_cooldown_number_offset_y = new ValidatedInt(-10);
	public ValidatedColor spell_cooldown_number_color = new ValidatedColor(Color.LIGHT_GRAY, false);
}
