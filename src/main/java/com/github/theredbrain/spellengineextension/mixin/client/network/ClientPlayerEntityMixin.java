package com.github.theredbrain.spellengineextension.mixin.client.network;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.entity.player.DuckPlayerEntityMixin;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.spell_engine.internals.casting.SpellCasterClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

	@Shadow
	public Input input;

	@Shadow
	protected int ticksLeftToDoubleTapSprint;

	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;tick(ZF)V", shift = At.Shift.AFTER))
	private void spellengineextension$tickMovement_applyMovementLockingSpellCasting(CallbackInfo ci) {
		var player = (ClientPlayerEntity) (Object) this;
		var caster = (SpellCasterClient) player;
		var process = caster.getSpellCastProcess();
		if (SpellEngineExtension.SERVER_CONFIG.enable_movement_locking_spell_casting.get()) {
			if (SpellEngineExtension.SERVER_CONFIG.movement_locking_prevents_player_position_changes.get() && ((((DuckPlayerEntityMixin) player).spellengineextension$getMovementLockingTicks() > 0) || (process != null && process.spell().isIn(SpellEngineExtension.ENABLES_MOVEMENT_LOCKING_DURING_CASTING) && process.spell().value().active.cast != null)) && !player.hasVehicle()) {
				Input var10000 = this.input;
				var10000.movementForward = 0.0F;
				var10000 = this.input;
				var10000.movementSideways = 0.0F;
				this.ticksLeftToDoubleTapSprint = 0;
			}
		}
	}
}
