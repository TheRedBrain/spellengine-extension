package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.input;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.SpellEngineExtensionClient;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.client.SpellEngineClient;
import net.spell_engine.client.input.Keybindings;
import net.spell_engine.client.input.SpellHotbar;
import net.spell_engine.client.input.WrappedKeybinding;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.container.SpellContainerSource;
import net.spell_engine.mixin.client.control.KeybindingAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(SpellHotbar.class)
public class SpellHotbarMixin {

	@Shadow(remap = false)
	public List<SpellHotbar.Slot> slots;

	@Shadow(remap = false)
	public SpellHotbar.StructuredSlots structuredSlots;

	/**
	 * @author TheRedBrain
	 * @reason allow only tier 1 spells to bind to use hotkey
	 */
	@Overwrite
	public boolean update(ClientPlayerEntity player, GameOptions options) {
		var changed = false;
		var initialSlotCount = slots.size();
		var mergedContainer = SpellContainerSource.activeContainerOf(player);
		//SpellContainerHelper.getAvailable(player);

		var slots = new ArrayList<SpellHotbar.Slot>();
		var otherSlots = new ArrayList<SpellHotbar.Slot>();
		SpellHotbar.Slot onUseKey = null;

		var allBindings = Keybindings.Wrapped.all();
		var useKey = ((KeybindingAccessor) options.useKey).getBoundKey();
		var useKeyBinding = new WrappedKeybinding(options.useKey, WrappedKeybinding.VanillaAlternative.USE_KEY);

		if (mergedContainer != null
				&& !mergedContainer.spell_ids().isEmpty()) {
			var itemUseExpectation = SpellHotbar.expectedUseStack(player);
			if (itemUseExpectation != null) {
				onUseKey = new SpellHotbar.Slot(null, SpellCast.Mode.ITEM_USE, itemUseExpectation.itemStack(), useKeyBinding, null);
			}

			var spellIds = mergedContainer.spell_ids();
			var spellEntryList = spellIds.stream()
					.map(idString -> {
						var id = Identifier.of(idString);
						return SpellRegistry.from(player.getWorld()).getEntry(id).orElse(null);
					})
					.filter(Objects::nonNull)
					.toList();

			int keyBindingIndex = 0;
			for (RegistryEntry<Spell> spellEntry : spellEntryList) {
				var spell = spellEntry.value();
				if (spell == null) {
					continue;
				}

				WrappedKeybinding keyBinding = null;
				if (keyBindingIndex < allBindings.size()) {
					keyBinding = allBindings.get(keyBindingIndex);
					keyBindingIndex += 1;
				} else {
					continue;
				}

				// Override keybinding with UseKey if available
				if (spell.tier <= SpellEngineExtension.SERVER_CONFIG.max_spell_tier_for_use_key.get() && SpellEngineClient.config.spellHotbarUseKey) {
					if (onUseKey == null) {
						keyBinding = useKeyBinding;
					}
				}

				// Create slot
				var slot = new SpellHotbar.Slot(spellEntry, SpellCast.Mode.from(spell), null, keyBinding, null);

				// Try to categorize slot based on keybinding
				if (keyBinding != null) {
					var unwrapped = keyBinding.get(options);
					if (unwrapped != null) {
						var hotbarKey = ((KeybindingAccessor) unwrapped.keyBinding()).getBoundKey();

						if (hotbarKey.equals(useKey)) {
							onUseKey = slot;
						} else {
							otherSlots.add(slot);
						}
					}
				}

				// Save to all slots
				slots.add(slot);
			}

			if (itemUseExpectation != null) {
				if (itemUseExpectation.isMainHand()) {
					slots.addFirst(onUseKey);
				} else {
					slots.addLast(onUseKey);
				}
			}
		}

		changed = initialSlotCount != slots.size();
		this.structuredSlots = new SpellHotbar.StructuredSlots(onUseKey, otherSlots);
		this.slots = slots;
		return changed;
	}

	@WrapMethod(method = "expectedUseStack")
	private static SpellHotbar.ItemUseExpectation spellengineextension$expectedUseStack(PlayerEntity player, Operation<SpellHotbar.ItemUseExpectation> original) {
		if (SpellEngineExtensionClient.CLIENT_CONFIG.show_items_in_spell_hot_bar.get()) {
			return original.call(player);
		} else {
			return null;
		}
	}
}
