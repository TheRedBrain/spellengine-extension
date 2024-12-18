package com.github.theredbrain.spellengineextension.config;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;

public class ClientConfig extends Config {
	public ClientConfig() {
		super(SpellEngineExtension.identifier("client"));
	}

	public ValidatedBoolean enable_spell_hotbar_background_rendering = new ValidatedBoolean(true);
}
