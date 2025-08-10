package com.github.theredbrain.spellengineextension.entity.player;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.component.type.HasConditionalSpellContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class PlayerEntityHelper {

	public static void spellengineextension$updateConditionalSpellContainerItems(PlayerEntity playerEntity) {

		PlayerInventory playerInventory = playerEntity.getInventory();

		ItemStack itemStack;
		HasConditionalSpellContainerComponent hasConditionalSpellContainerComponent;
		HasConditionalSpellContainerComponent newHasConditionalSpellContainerComponent;

		itemStack = playerInventory.offHand.get(0);
		hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
		if (hasConditionalSpellContainerComponent != null) {
			if (hasConditionalSpellContainerComponent.is_off_hand_valid() != hasConditionalSpellContainerComponent.is_valid()) {
				newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid());
				itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
				playerInventory.offHand.set(0, itemStack);
			}
		}

		itemStack = playerInventory.main.get(playerInventory.selectedSlot);
		hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
		if (hasConditionalSpellContainerComponent != null) {
			if (hasConditionalSpellContainerComponent.is_main_hand_valid() != hasConditionalSpellContainerComponent.is_valid()) {
				newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), hasConditionalSpellContainerComponent.is_main_hand_valid());
				itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
				playerInventory.main.set(playerInventory.selectedSlot, itemStack);
			}
		}

		SpellEngineExtension.updateConditionalSpellContainerItems(playerEntity);
	}
}
