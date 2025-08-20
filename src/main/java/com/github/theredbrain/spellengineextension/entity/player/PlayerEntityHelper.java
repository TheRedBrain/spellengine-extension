package com.github.theredbrain.spellengineextension.entity.player;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.component.type.HasConditionalSpellContainerComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class PlayerEntityHelper {

	public static void spellengineextension$updateConditionalSpellContainerItems(PlayerInventory playerInventory) {

		ItemStack itemStack;
		ItemStack otherItemStack;
		HasConditionalSpellContainerComponent hasConditionalSpellContainerComponent;
		HasConditionalSpellContainerComponent newHasConditionalSpellContainerComponent;

		itemStack = playerInventory.offHand.get(0).copy();
		hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
		if (hasConditionalSpellContainerComponent != null) {
			if (hasConditionalSpellContainerComponent.is_off_hand_valid() != hasConditionalSpellContainerComponent.is_valid()) {
				newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), hasConditionalSpellContainerComponent.is_two_handed_valid(), hasConditionalSpellContainerComponent.is_dual_wielding_valid(), hasConditionalSpellContainerComponent.dual_wielding_tag(), hasConditionalSpellContainerComponent.is_off_hand_valid());
				itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
				playerInventory.offHand.set(0, itemStack);
			}
		}

		itemStack = playerInventory.main.get(playerInventory.selectedSlot).copy();
		otherItemStack = playerInventory.offHand.get(0).copy();
		hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
		if (hasConditionalSpellContainerComponent != null) {
			if (hasConditionalSpellContainerComponent.is_two_handed_valid()) {
				if (otherItemStack.isEmpty()) {
					newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), true, hasConditionalSpellContainerComponent.is_dual_wielding_valid(), hasConditionalSpellContainerComponent.dual_wielding_tag(), true);
					itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
					playerInventory.main.set(playerInventory.selectedSlot, itemStack);
				} else {
					newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), true, hasConditionalSpellContainerComponent.is_dual_wielding_valid(), hasConditionalSpellContainerComponent.dual_wielding_tag(), false);
					itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
					playerInventory.main.set(playerInventory.selectedSlot, itemStack);
				}
			} else if (hasConditionalSpellContainerComponent.is_dual_wielding_valid() && !hasConditionalSpellContainerComponent.dual_wielding_tag().isEmpty()) {
				if (!otherItemStack.isEmpty() && otherItemStack.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(hasConditionalSpellContainerComponent.dual_wielding_tag())))) {
					newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), false, true, hasConditionalSpellContainerComponent.dual_wielding_tag(), true);
					itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
					playerInventory.main.set(playerInventory.selectedSlot, itemStack);
				} else {
					newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), false, true, hasConditionalSpellContainerComponent.dual_wielding_tag(), false);
					itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
					playerInventory.main.set(playerInventory.selectedSlot, itemStack);
				}
			} else if (hasConditionalSpellContainerComponent.is_main_hand_valid() != hasConditionalSpellContainerComponent.is_valid()) {
				newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), false, hasConditionalSpellContainerComponent.is_dual_wielding_valid(), hasConditionalSpellContainerComponent.dual_wielding_tag(), hasConditionalSpellContainerComponent.is_main_hand_valid());
				itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
				playerInventory.main.set(playerInventory.selectedSlot, itemStack);
			}
		}

		SpellEngineExtension.updateConditionalSpellContainerItems(playerInventory);
	}
}
