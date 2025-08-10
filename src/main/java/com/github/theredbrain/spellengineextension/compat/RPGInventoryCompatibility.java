package com.github.theredbrain.spellengineextension.compat;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.component.type.HasConditionalSpellContainerComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class RPGInventoryCompatibility {

	public static void updateConditionalSpellContainerItems(PlayerInventory playerInventory) {

		ItemStack itemStack;
		HasConditionalSpellContainerComponent hasConditionalSpellContainerComponent;
		HasConditionalSpellContainerComponent newHasConditionalSpellContainerComponent;

		itemStack = ((DuckPlayerInventoryMixin) playerInventory).rpginventory$getHand().copy();
		hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
		if (hasConditionalSpellContainerComponent != null) {
			if (hasConditionalSpellContainerComponent.is_main_hand_valid() != hasConditionalSpellContainerComponent.is_valid()) {
				newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), hasConditionalSpellContainerComponent.is_main_hand_valid());
				itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
				((DuckPlayerInventoryMixin) playerInventory).rpginventory$setHand(itemStack);
			}
		}

		itemStack = ((DuckPlayerInventoryMixin) playerInventory).rpginventory$getSheathedHand().copy();
		hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
		if (hasConditionalSpellContainerComponent != null) {
			if (hasConditionalSpellContainerComponent.is_main_hand_valid() != hasConditionalSpellContainerComponent.is_valid()) {
				newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), hasConditionalSpellContainerComponent.is_main_hand_valid());
				itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
				((DuckPlayerInventoryMixin) playerInventory).rpginventory$setSheathedHand(itemStack);
			}
		}

		itemStack = ((DuckPlayerInventoryMixin) playerInventory).rpginventory$getSheathedOffhand().copy();
		hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
		if (hasConditionalSpellContainerComponent != null) {
			if (hasConditionalSpellContainerComponent.is_off_hand_valid() != hasConditionalSpellContainerComponent.is_valid()) {
				newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid());
				itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
				((DuckPlayerInventoryMixin) playerInventory).rpginventory$setSheathedOffhand(itemStack);
			}
		}

		itemStack = ((DuckPlayerInventoryMixin) playerInventory).rpginventory$getAlternativeHand().copy();
		hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
		if (hasConditionalSpellContainerComponent != null) {
			if (hasConditionalSpellContainerComponent.is_main_hand_valid() != hasConditionalSpellContainerComponent.is_valid()) {
				newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), hasConditionalSpellContainerComponent.is_main_hand_valid());
				itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
				((DuckPlayerInventoryMixin) playerInventory).rpginventory$setAlternativeHand(itemStack);
			}
		}

		itemStack = ((DuckPlayerInventoryMixin) playerInventory).rpginventory$getAlternativeOffhand().copy();
		hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
		if (hasConditionalSpellContainerComponent != null) {
			if (hasConditionalSpellContainerComponent.is_off_hand_valid() != hasConditionalSpellContainerComponent.is_valid()) {
				newHasConditionalSpellContainerComponent = new HasConditionalSpellContainerComponent(hasConditionalSpellContainerComponent.is_main_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid(), hasConditionalSpellContainerComponent.is_off_hand_valid());
				itemStack.set(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER, newHasConditionalSpellContainerComponent);
				((DuckPlayerInventoryMixin) playerInventory).rpginventory$setAlternativeOffhand(itemStack);
			}
		}

	}

}
