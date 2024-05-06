package com.github.theredbrain.spellengineextension;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellContainerMixin;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.internals.SpellRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpellEngineExtension implements ModInitializer {
	public static final String MOD_ID = "spellengineextension";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("SpellEngine was extended!");
	}

    public static void setSpellContainerProxyPool(Identifier spellContainerIdentifier, String proxyPool) {
        SpellContainer spellContainer = SpellRegistry.book_containers.get(spellContainerIdentifier);
        ((DuckSpellContainerMixin)spellContainer).betteradventuremode$setProxyPool(proxyPool);
        SpellRegistry.book_containers.put(spellContainerIdentifier, spellContainer);
    }

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}
}