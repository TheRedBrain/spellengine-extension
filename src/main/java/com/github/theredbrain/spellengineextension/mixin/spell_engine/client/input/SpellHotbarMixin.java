package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.input;

import com.github.theredbrain.spellengineextension.SpellEngineExtensionClient;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.client.input.SpellHotbar;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpellHotbar.class)
public class SpellHotbarMixin {

	@WrapMethod(method = "expectedUseStack")
	private static SpellHotbar.ItemUseExpectation spellengineextension$expectedUseStack(PlayerEntity player, Operation<SpellHotbar.ItemUseExpectation> original) {
		if (SpellEngineExtensionClient.CLIENT_CONFIG.show_items_in_spell_hot_bar.get()) {
			return original.call(player);
		} else {
			return null;
		}
	}
}
