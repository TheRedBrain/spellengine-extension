package com.github.theredbrain.spellengineextension.mixin.spell_engine.client.input;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.SpellEngineExtensionClient;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.container.SpellContainer;
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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Mixin(SpellHotbar.class)
public class SpellHotbarMixin {

	@Shadow public List<SpellHotbar.Slot> slots;

	@Shadow public SpellHotbar.StructuredSlots structuredSlots;

	/**
	 * @author TheRedBrain
	 * @reason allow only tier 1 spells to bind to use hotkey
	 */
	@Overwrite
	public boolean update(ClientPlayerEntity player, GameOptions options) {
		boolean changed = false;
		int initialSlotCount = this.slots.size();
		SpellContainer mergedContainer = SpellContainerSource.activeContainerOf(player);
		ArrayList<SpellHotbar.Slot> slots = new ArrayList();
		ArrayList<SpellHotbar.Slot> otherSlots = new ArrayList();
		SpellHotbar.Slot onUseKey = null;
		List<WrappedKeybinding> allBindings = Keybindings.Wrapped.all();
		InputUtil.Key useKey = ((KeybindingAccessor)options.useKey).getBoundKey();
		WrappedKeybinding useKeyBinding = new WrappedKeybinding(options.useKey, WrappedKeybinding.VanillaAlternative.USE_KEY);
		if (mergedContainer != null && !mergedContainer.spell_ids().isEmpty()) {
			SpellHotbar.ItemUseExpectation itemUseExpectation = SpellHotbar.expectedUseStack(player);
			if (itemUseExpectation != null) {
				onUseKey = new SpellHotbar.Slot((RegistryEntry)null, SpellCast.Mode.ITEM_USE, itemUseExpectation.itemStack(), useKeyBinding, (KeyBinding)null);
			}

			List<String> spellIds = mergedContainer.spell_ids();
			List<RegistryEntry.Reference<Spell>> spellEntryList = spellIds.stream().map((idString) -> {
				Identifier id = Identifier.of(idString);
				return (RegistryEntry.Reference<Spell>) SpellRegistry.from(player.getWorld()).getEntry(id).orElse((RegistryEntry.Reference<Spell>) null);
			}).filter(Objects::nonNull).toList();
			int keyBindingIndex = 0;
			Iterator var16 = spellEntryList.iterator();

			while(var16.hasNext()) {
				RegistryEntry<Spell> spellEntry = (RegistryEntry)var16.next();
				Spell spell = (Spell)spellEntry.value();
				if (spell != null) {
					WrappedKeybinding keyBinding = null;
					if (keyBindingIndex < allBindings.size()) {
						keyBinding = (WrappedKeybinding)allBindings.get(keyBindingIndex);
						++keyBindingIndex;
						if (spell.tier <= SpellEngineExtension.SERVER_CONFIG.max_spell_tier_for_use_key.get() && SpellEngineClient.config.spellHotbarUseKey && onUseKey == null) {
							keyBinding = useKeyBinding;
						}

						SpellHotbar.Slot slot = new SpellHotbar.Slot(spellEntry, SpellCast.Mode.from(spell), (ItemStack)null, keyBinding, (KeyBinding)null);
						if (keyBinding != null) {
							WrappedKeybinding.Unwrapped unwrapped = keyBinding.get(options);
							if (unwrapped != null) {
								InputUtil.Key hotbarKey = ((KeybindingAccessor)unwrapped.keyBinding()).getBoundKey();
								if (hotbarKey.equals(useKey)) {
									onUseKey = slot;
								} else {
									otherSlots.add(slot);
								}
							}
						}

						slots.add(slot);
					}
				}
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
