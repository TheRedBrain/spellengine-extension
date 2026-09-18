package com.github.theredbrain.spellengineextension.registry;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.component.type.HasConditionalSpellContainerComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SpellEngineExtensionDataComponents {

	public static ComponentType<HasConditionalSpellContainerComponent> HAS_CONDITIONAL_SPELL_CONTAINER;

	public static void bootstrap() {
	}

	static {
		HAS_CONDITIONAL_SPELL_CONTAINER = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				SpellEngineExtension.identifier("has_conditional_spell_container"),
				ComponentType.<HasConditionalSpellContainerComponent>builder().codec(HasConditionalSpellContainerComponent.CODEC)/*.packetCodec(HasConditionalSpellContainerComponent.PACKET_CODEC)*/.build()
		);
	}
}
