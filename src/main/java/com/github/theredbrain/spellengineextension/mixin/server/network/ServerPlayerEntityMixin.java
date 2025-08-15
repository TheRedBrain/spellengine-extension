package com.github.theredbrain.spellengineextension.mixin.server.network;

import com.github.theredbrain.spellengineextension.entity.effect.ProvidesSpell;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.spell_engine.internals.container.SpellContainerSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Override
	protected void onStatusEffectApplied(StatusEffectInstance effect, @Nullable Entity source) {
		super.onStatusEffectApplied(effect, source);
		clearSpellContainerCache(((ServerPlayerEntity) (Object) this));
	}

	@Override
	protected void onStatusEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect, @Nullable Entity source) {
		super.onStatusEffectUpgraded(effect, reapplyEffect, source);
		clearSpellContainerCache(((ServerPlayerEntity) (Object) this));
	}

	@Override
	protected void onStatusEffectRemoved(StatusEffectInstance effect) {
		super.onStatusEffectRemoved(effect);
		clearSpellContainerCache(((ServerPlayerEntity) (Object) this));
	}

	@Unique
	private void clearSpellContainerCache(ServerPlayerEntity player) {
		((SpellContainerSource.Owner) player).serverSideSpellContainers().put("effect_provided", ProvidesSpell.getStatusEffectContainer(player));
		((SpellContainerSource.Owner) player).markServerSideSpellContainersDirty();
	}
}
