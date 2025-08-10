package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell.container;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.component.type.HasConditionalSpellContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_engine.internals.container.SpellAssignments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SpellContainerHelper.class)
public class SpellContainerHelperMixin {

	/**
	 * @author TheRedBrain
	 * @reason integrate merged items compatibility and conditional spell containers
	 */
	@Overwrite
	public static SpellContainer containerFromItemStack(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return null;
		} else {
			HasConditionalSpellContainerComponent hasConditionalSpellContainerComponent = itemStack.get(SpellEngineExtension.HAS_CONDITIONAL_SPELL_CONTAINER);
			if (hasConditionalSpellContainerComponent != null && !hasConditionalSpellContainerComponent.is_valid()) {
				return null;
			}
			SpellContainer container = (SpellContainer)itemStack.get(SpellDataComponents.SPELL_CONTAINER);
			if (container == null) {
				Identifier id = ((RegistryKey)itemStack.getItem().getRegistryEntry().getKey().get()).getValue();
				container = SpellAssignments.containerForItem(id);
			}
			if (container != null) {
				container = SpellEngineExtension.addMergedSpellContainer(container, itemStack);
			}
			return container;
		}
	}

}
