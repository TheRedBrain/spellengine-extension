package com.github.theredbrain.spellengineextension.mixin.entity.player;

import com.github.theredbrain.spellengineextension.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.spellengineextension.entity.player.PlayerEntityHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckPlayerEntityMixin {

	@Shadow
	public abstract PlayerInventory getInventory();

	@Unique
	private static final TrackedData<Integer> MOVEMENT_LOCKING_TICKS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	protected void spellengineextension$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(MOVEMENT_LOCKING_TICKS, 0);

	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void spellengineextension$tick(CallbackInfo ci) {
		if (!this.getWorld().isClient) {
			PlayerEntityHelper.spellengineextension$updateConditionalSpellContainerItems(this.getInventory());
			if (this.spellengineextension$getMovementLockingTicks() > 0) {
				this.spellengineextension$setMovementLockingTicks(this.spellengineextension$getMovementLockingTicks() - 1);
			}
		}
	}

	@Override
	public int spellengineextension$getMovementLockingTicks() {
		return this.dataTracker.get(MOVEMENT_LOCKING_TICKS);
	}

	@Override
	public void spellengineextension$setMovementLockingTicks(int movementLockingTicks) {
		this.dataTracker.set(MOVEMENT_LOCKING_TICKS, movementLockingTicks);
	}

}
