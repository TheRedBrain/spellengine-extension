package com.github.theredbrain.spellengineextension.mixin.client;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.entity.player.DuckPlayerEntityMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.spell_engine.internals.casting.SpellCasterClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mouse.class)
public abstract class MouseMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@WrapOperation(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V", ordinal = 0))
	private void spellengineextension$updateMouse(ClientPlayerEntity instance, double cursorDeltaX, double cursorDeltaY, Operation<Void> original) {
		if (SpellEngineExtension.SERVER_CONFIG.enable_movement_locking_spell_casting.get()) {
			ClientPlayerEntity player = client.player;
			if (player != null) {
				var process = ((SpellCasterClient) player).getSpellCastProcess();
				if (SpellEngineExtension.SERVER_CONFIG.movement_locking_prevents_player_orientation_changes.get() && ((((DuckPlayerEntityMixin) player).spellengineextension$getMovementLockingTicks() > 0) || (process != null && process.spell().value().active.cast != null && process.spell().isIn(SpellEngineExtension.ENABLES_MOVEMENT_LOCKING_DURING_CASTING)))) {
					return;
				}
			}
		}
		original.call(instance, cursorDeltaX, cursorDeltaY);
	}
}