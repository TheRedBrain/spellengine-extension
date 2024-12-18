package com.github.theredbrain.spellengineextension;

import com.github.theredbrain.spellengineextension.config.ClientConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ClientModInitializer;

public class SpellEngineExtensionClient implements ClientModInitializer {
	public static ClientConfig CLIENT_CONFIG = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);
	@Override
	public void onInitializeClient() {
	}
}