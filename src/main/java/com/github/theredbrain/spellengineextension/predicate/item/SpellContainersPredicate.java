package com.github.theredbrain.spellengineextension.predicate.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.ComponentSubPredicate;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;

import java.util.List;
import java.util.Optional;

public record SpellContainersPredicate(
		Optional<SpellContainer.ContentType> content,
		Optional<Boolean> is_proxy,
		Optional<String> pool,
		Optional<String> slot,
		NumberRange.IntRange max_spell_count,
		Optional<Boolean> exact_spell_ids_match,
		Optional<List<String>> spell_ids
) implements ComponentSubPredicate<SpellContainer> {
	public static final Codec<SpellContainersPredicate> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							SpellContainer.ContentType.CODEC.optionalFieldOf("content").forGetter(SpellContainersPredicate::content),
							Codec.BOOL.optionalFieldOf("is_proxy").forGetter(SpellContainersPredicate::is_proxy),
							Codec.STRING.optionalFieldOf("pool").forGetter(SpellContainersPredicate::pool),
							Codec.STRING.optionalFieldOf("slot").forGetter(SpellContainersPredicate::slot),
							NumberRange.IntRange.CODEC
									.optionalFieldOf("max_spell_count", NumberRange.IntRange.ANY)
									.forGetter(SpellContainersPredicate::max_spell_count),
							Codec.BOOL.optionalFieldOf("exact_spell_ids_match").forGetter(SpellContainersPredicate::exact_spell_ids_match),
							Codec.STRING.listOf().optionalFieldOf("spell_ids").forGetter(SpellContainersPredicate::spell_ids)
					)
					.apply(instance, SpellContainersPredicate::new)
	);

	@Override
	public ComponentType<SpellContainer> getComponentType() {
		return SpellDataComponents.SPELL_CONTAINER;
	}

	@Override
	public boolean test(ItemStack itemStack, SpellContainer spellContainer) {
		if (this.content.isPresent() && !this.content.get().equals(spellContainer.content())) {
			return false;
		} else if (this.is_proxy.isPresent() && this.is_proxy.get() != spellContainer.is_proxy()) {
			return false;
		} else if (this.pool.isPresent() && this.pool.get().equals(spellContainer.pool())) {
			return false;
		} else if (this.slot.isPresent() && this.slot.get().equals(spellContainer.slot())) {
			return false;
		} else if (!this.max_spell_count.test(spellContainer.max_spell_count())) {
			return false;
		} else {
			if (this.spell_ids.isEmpty()) {
				return true;
			}
			for (String spellId : this.spell_ids.get()) {
				if (!spellContainer.spell_ids().contains(spellId)) {
					return false;
				}
			}
			if (this.exact_spell_ids_match.isPresent() && this.exact_spell_ids_match.get()) {
				if (this.spell_ids.get().size() != spellContainer.spell_ids().size()) {
					return false;
				}
			}
			return true;
		}
	}
}
