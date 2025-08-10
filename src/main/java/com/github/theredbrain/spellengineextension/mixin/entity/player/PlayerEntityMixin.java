package com.github.theredbrain.spellengineextension.mixin.entity.player;

import com.github.theredbrain.spellengineextension.entity.player.PlayerEntityHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow public abstract PlayerInventory getInventory();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void spellengineextension$tick(CallbackInfo ci) {
		if (!this.getWorld().isClient) {
			PlayerEntityHelper.spellengineextension$updateConditionalSpellContainerItems(this.getInventory());
		}
	}

}
