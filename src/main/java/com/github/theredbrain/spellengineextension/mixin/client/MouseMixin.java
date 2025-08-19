package com.github.theredbrain.spellengineextension.mixin.client;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.compat.ShoulderSurfingCompat;
import com.github.theredbrain.spellengineextension.entity.player.DuckPlayerEntityMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.spell_engine.internals.casting.SpellCasterClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Mouse.class)
public abstract class MouseMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V", ordinal = 0), cancellable = true)
	private void bettercombatextension$updateMouse(CallbackInfo ci) {
		if (SpellEngineExtension.SERVER_CONFIG.enable_movement_locking_spell_casting.get()) {
			ClientPlayerEntity player = client.player;
			if (player != null) {
				var process = ((SpellCasterClient) player).getSpellCastProcess();
				if ((((DuckPlayerEntityMixin)player).spellengineextension$getMovementLockingTicks() > 0) || (process != null && process.spell().value().active.cast != null && process.spell().isIn(SpellEngineExtension.ENABLES_MOVEMENT_LOCKING_DURING_CASTING))) {
					ci.cancel();
				}
			}
		}
	}
}