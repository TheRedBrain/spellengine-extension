package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals.container;

import com.github.theredbrain.spellengineextension.spell_engine.ProxyPoolHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.internals.container.SpellContainerSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(SpellContainerSource.class)
public class SpellContainerSourceMixin {

	@WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/spell_engine/internals/container/SpellContainerSource;mergedContainerSources(Ljava/util/List;ZLnet/spell_engine/api/spell/container/SpellContainer$ContentType;Lnet/spell_engine/api/spell/Spell$Type;Lnet/minecraft/world/World;)Lnet/spell_engine/internals/container/SpellContainerSource$MergeResult;"))
	private static SpellContainerSource.MergeResult spellengineextension$update_spellContainer(List<SpellContainerSource.SourcedContainer> sources, boolean proxy, SpellContainer.@Nullable ContentType contentType, Spell.Type type, World world, Operation<SpellContainerSource.MergeResult> original, @Local(argsOnly = true) PlayerEntity owner, @Local ItemStack heldItemStack) {
		return ProxyPoolHelper.mergedContainerSourcesWithProxyPool(sources, proxy, contentType, type, world, owner, heldItemStack);
	}

}
