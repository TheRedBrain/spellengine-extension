package com.github.theredbrain.spellengineextension.registry;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.component.type.HasConditionalSpellContainerComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemComponentRegistry {
	static {
		SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				SpellEngineExtension.identifier("has_conditional_spell_container"),
				ComponentType.<HasConditionalSpellContainerComponent>builder().codec(HasConditionalSpellContainerComponent.CODEC)/*.packetCodec(HasConditionalSpellContainerComponent.PACKET_CODEC)*/.build()
		);
		SpellEngineExtension.PROXY_POOL = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				SpellEngineExtension.identifier("proxy_pool"),
				ComponentType.<Identifier>builder().codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC).build()
		);
	}

	public static void init() {
	}
}
