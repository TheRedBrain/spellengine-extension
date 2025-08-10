package com.github.theredbrain.spellengineextension.spell_engine;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.container.SpellContainerSource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class ProxyPoolHelper {

	public static List<RegistryEntry<Spell>> mergedContainerSourcesWithProxyPool(@Nullable List<RegistryEntry<Spell>> proxyPool, List<SpellContainerSource.SourcedContainer> sources, @Nullable SpellContainer.@Nullable ContentType contentType, Spell.Type type, World world) {
		if (sources.isEmpty()) {
			return List.of();
		}
		var spells = new ArrayList<RegistryEntry<Spell>>();
		var registry = SpellRegistry.from(world);
		for (var source : sources) {
			var container = source.container();
			if (type == Spell.Type.ACTIVE && source.name().equals("off_hand")) {
				if (!SpellEngineMod.config.spell_container_from_offhand_any) {
					if (!container.slotMatches(EquipmentSlot.OFFHAND.asString())) {
						continue;
					}
				}
			}
			if (container.contentMatches(contentType)) {
				for (var idString : container.spell_ids()) {
					var id = Identifier.of(idString);
					var spell = registry.getEntry(id).orElse(null);
					if (spell != null && spell.value().type == type) {
						spells.add(spell);
					}
				}
			}
		}

		// Remove spells with the same group, and lower tier
		var toRemove = new HashSet<RegistryEntry<Spell>>();
		for (var spellEntry : spells) {
			var spell = spellEntry.value();

			// remove spells not in the proxy pool
			if (proxyPool != null && !proxyPool.contains(spellEntry)) {
				toRemove.add(spellEntry);
				continue;
			}

			var tag = spell.group;
			if (tag != null) {
				for (var other : spells) {
					var spellId = spellEntry.getKey().get().getValue();
					var otherId = other.getKey().get().getValue();
					if (spellId.equals(otherId)) continue;
					if (tag.equals(other.value().group)) {
						if (spellEntry.value().tier == other.value().tier) {
							if (spellEntry.value().sub_tier > other.value().sub_tier) {
								toRemove.add(other);
							}
						}
						if (spellEntry.value().tier > other.value().tier) {
							toRemove.add(other);
						}
					}
				}
			}
		}
		spells.removeAll(toRemove);

		return spells;
	}

	public static SpellContainerSource.MergeResult mergedContainerSourcesWithProxyPool(List<SpellContainerSource.SourcedContainer> sources, boolean proxy, @Nullable SpellContainer.ContentType contentType, Spell.Type type, World world, PlayerEntity playerEntity, ItemStack itemStack) {
		if (sources.isEmpty()) {
			return SpellContainerSource.MergeResult.EMPTY;
		}

		List<RegistryEntry<Spell>> proxyPool = null;
		Identifier proxyPoolIdentifier = itemStack.get(SpellEngineExtension.PROXY_POOL);
		if (proxyPoolIdentifier != null) {
			proxyPool = SpellRegistry.entries(playerEntity.getWorld(), proxyPoolIdentifier);
		}

		var spells = mergedContainerSourcesWithProxyPool(proxyPool, sources, contentType, type, world);

		var spellIds = new LinkedHashSet<String>(); // We need the IDs only, but remove duplicates
		for (var spell : spells) {
			spellIds.add(spell.getKey().get().getValue().toString());
		}

		// System.out.println("Updated for " + type + ", Spell IDs: " + spellIds);

		var finalContentType = contentType != null ? contentType : SpellContainer.ContentType.MAGIC;
		var container = new SpellContainer(finalContentType, proxy, null, 0, new ArrayList<>(spellIds));
		return new SpellContainerSource.MergeResult(container, spells);
	}

}
