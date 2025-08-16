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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "onStatusEffectApplied", at = @At("TAIL"))
	protected void spellengineextension$onStatusEffectApplied(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
		spellengineextension$clearSpellContainerCache(((ServerPlayerEntity) (Object) this));
	}

	@Inject(method = "onStatusEffectUpgraded", at = @At("TAIL"))
	protected void spellengineextension$onStatusEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect, Entity source, CallbackInfo ci) {
		spellengineextension$clearSpellContainerCache(((ServerPlayerEntity) (Object) this));
	}

	@Inject(method = "onStatusEffectRemoved", at = @At("TAIL"))
	protected void spellengineextension$onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
		spellengineextension$clearSpellContainerCache(((ServerPlayerEntity) (Object) this));
	}

	@Unique
	private void spellengineextension$clearSpellContainerCache(ServerPlayerEntity player) {
		((SpellContainerSource.Owner) player).serverSideSpellContainers().put("effect_provided", ProvidesSpell.getStatusEffectContainer(player));
		((SpellContainerSource.Owner) player).markServerSideSpellContainersDirty();
	}
}
