package com.github.theredbrain.spellengineextension.compat;

import com.github.theredbrain.mergeditems.MergedItems;
import com.github.theredbrain.mergeditems.component.type.MergedItemsComponent;
import net.minecraft.item.ItemStack;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;

import java.util.ArrayList;
import java.util.List;

public class MergedItemsCompatibility {

	public static SpellContainer addMergedSpellContainer(SpellContainer spellContainer, ItemStack itemStack) {
		MergedItemsComponent mergedItemsComponent = itemStack.get(MergedItems.MERGED_ITEMS_COMPONENT_TYPE);
		if (mergedItemsComponent != null) {
			List<String> newSpellIds = new ArrayList<>();
			if (!mergedItemsComponent.isEmpty() && mergedItemsComponent.isSpellMergingEnabled()) {
				for (ItemStack itemStack1 : mergedItemsComponent.iterate()) {
					MergedItemsComponent mergedItemsComponent1 = itemStack1.get(MergedItems.MERGED_ITEMS_COMPONENT_TYPE);
					if (mergedItemsComponent1 != null && mergedItemsComponent1.isSpellMergingAllowed()) {
						SpellContainer spellContainer1 = SpellContainerHelper.containerFromItemStack(itemStack1);
						if (spellContainer1 != null) {
							newSpellIds.addAll(spellContainer1.spell_ids());
						}
					}
				}
			}
			if (!newSpellIds.isEmpty()) {
				return spellContainer.withAdditionalSpell(newSpellIds);
			}
		}
		return spellContainer;
	}

}
