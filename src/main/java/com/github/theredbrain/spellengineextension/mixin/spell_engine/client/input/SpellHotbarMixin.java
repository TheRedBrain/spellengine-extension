package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.input;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.SpellEngineExtensionClient;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.client.SpellEngineClient;
import net.spell_engine.client.input.SpellHotbar;
import net.spell_engine.config.ClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SpellHotbar.class)
public class SpellHotbarMixin {

	@WrapOperation(method = "update", at = @At(value = "FIELD", target = "Lnet/spell_engine/config/ClientConfig;spellHotbarUseKey:Z", remap = false))
	private boolean spellengineextension$wrap_spellHotbarUseKey(ClientConfig instance, Operation<Boolean> original, @Local(name = "spellEntry") RegistryEntry<Spell> spellEntry) {
		return original.call(instance) && (!SpellEngineExtension.SERVER_CONFIG.enable_spell_hotbar_use_key_restriction.get() || spellEntry.isIn(SpellEngineExtension.CAN_BE_IN_USE_ITEM_SPELL_HOTBAR_SLOT));
	}

	@ModifyVariable(method = "update", at = @At(value = "INVOKE", target = "Lnet/spell_engine/client/input/WrappedKeybinding;get(Lnet/minecraft/client/option/GameOptions;)Lnet/spell_engine/client/input/WrappedKeybinding$Unwrapped;"/*, shift = At.Shift.AFTER*/, args = ""), name = "keyBindingIndex")
	private int spellengineextension$giveUseKeyADedicatedSpellHotbarSlot(int value, @Local(name = "onUseKey") SpellHotbar.Slot onUseKey) {
		return (!SpellEngineExtensionClient.CLIENT_CONFIG.should_spell_hotbar_use_key_replace_first_number_slot.get() && SpellEngineClient.config.spellHotbarUseKey && onUseKey == null) ? value - 1 : value;
	}

	@WrapMethod(method = "expectedUseStack")
	private static SpellHotbar.ItemUseExpectation spellengineextension$wrap_expectedUseStack(PlayerEntity player, Operation<SpellHotbar.ItemUseExpectation> original) {
		if (SpellEngineExtensionClient.CLIENT_CONFIG.show_items_in_spell_hot_bar.get()) {
			return original.call(player);
		} else {
			return null;
		}
	}
}
