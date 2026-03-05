package com.github.theredbrain.spellengineextension.mixin.server.network;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.entity.effect.ProvidesSpell;
import com.github.theredbrain.spellengineextension.server.network.DuckServerPlayerEntityMixin;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.internals.container.SpellContainerSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements DuckServerPlayerEntityMixin {

	@Unique
	private final List<String> enchantmentProvidedSpellsList = new ArrayList<>();

	@Unique
	private int oldEnchantmentProvidedSpellsListHash = 0;

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void spellengineextension$tick(CallbackInfo ci) {
		if (this.enchantmentProvidedSpellsList.hashCode() != this.oldEnchantmentProvidedSpellsListHash) {
			List<String> newSpellList = new ArrayList<>(this.enchantmentProvidedSpellsList);

			((SpellContainerSource.Owner) this).serverSideSpellContainers().put("enchantment_provided", new SpellContainer(SpellContainer.ContentType.NONE, "", "", newSpellList.size(), newSpellList));
			((SpellContainerSource.Owner) this).markServerSideSpellContainersDirty();

			this.oldEnchantmentProvidedSpellsListHash = this.enchantmentProvidedSpellsList.hashCode();
		}
		this.enchantmentProvidedSpellsList.clear();
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

	@Override
	public void spellengineextension$addEnchantmentProvidedSpells(List<String> newSpellIds) {
		for (String newSpellId : newSpellIds) {
			if (!this.enchantmentProvidedSpellsList.contains(newSpellId)) {
				this.enchantmentProvidedSpellsList.add(newSpellId);
			}
		}
	}

	@Unique
	private void spellengineextension$clearSpellContainerCache(ServerPlayerEntity player) {
		((SpellContainerSource.Owner) player).serverSideSpellContainers().put("effect_provided", ProvidesSpell.getStatusEffectContainer(player));
		((SpellContainerSource.Owner) player).markServerSideSpellContainersDirty();
	}
}
